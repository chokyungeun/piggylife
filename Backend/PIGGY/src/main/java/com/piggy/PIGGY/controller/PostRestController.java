package com.piggy.PIGGY.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.piggy.PIGGY.dto.PostAreaStatisticDto;
import com.piggy.PIGGY.dto.PostImageDto;
import com.piggy.PIGGY.dto.PostInputDto;
import com.piggy.PIGGY.dto.PostOutputDto;
import com.piggy.PIGGY.dto.ResultDto;
import com.piggy.PIGGY.entity.Post;
import com.piggy.PIGGY.service.FileService;
import com.piggy.PIGGY.service.PostService;
import com.piggy.PIGGY.util.MapperUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(tags = { "Post" }, description = "Post 정보 REST API")
@RequestMapping(value = "/post")
@CrossOrigin(origins = "*")
@Slf4j
public class PostRestController {

	@Autowired
	private PostService pService;
	
	@Autowired
	private FileService fileService;
	
	@ApiOperation(value = "Post 생성")
	@PostMapping("/create/{uId}")
	public ResponseEntity<Object> create(@PathVariable Long uId, @RequestBody PostInputDto dto){
		try {
			log.trace("PostRestController - create", dto);
			Map<String, Object> output = pService.create(uId, dto);
			return new ResponseEntity<Object>(output, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@ApiOperation(value = "모든 Post 불러오기")
	@GetMapping("/findAll")
	public ResponseEntity<Object> findAll(){
		try {
			log.trace("PostRestController - findAll");
			List<Post> posts = pService.findAll();
			List<PostOutputDto> output = MapperUtils.mapAll(posts, PostOutputDto.class);
			return new ResponseEntity<Object>(output, HttpStatus.OK);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@ApiOperation(value = "해당 Post 불러오기")
	@GetMapping("/findById/{pId}")
	public ResponseEntity<Object> findById(@PathVariable Long pId){
		try {
			log.trace("PostRestController - findById", pId);
			Post post = pService.findById(pId);
			PostOutputDto output = MapperUtils.map(post, PostOutputDto.class);
			return new ResponseEntity<Object>(output, HttpStatus.OK);
		} catch (Exception e) {
			
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@ApiOperation(value = "해당 유저의 모든 Post 불러오기")
	@GetMapping("/findByUser/{uId}")
	public ResponseEntity<Object> findByUser(@PathVariable Long uId){
		try {
			log.trace("PostRestController - findByUser", uId);
			List<Post> posts = pService.findByUser(uId);
			List<PostOutputDto> output = MapperUtils.mapAll(posts, PostOutputDto.class);
			return new ResponseEntity<Object>(output, HttpStatus.OK);
		} catch (Exception e) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("data", e.getMessage());
			resultMap.put("status", false);
			return new ResponseEntity<Object>(resultMap, HttpStatus.OK);
		}
	}
	
	@ApiOperation(value = "해당 피드 업데이트")
	@PutMapping("/update/{pId}")
	public ResponseEntity<Object> update(@PathVariable Long pId, @RequestBody PostInputDto dto){
		try {
			log.trace("PostRestController - update", dto);
			Post post = pService.findById(pId);
			post = pService.update(pId, dto);
			PostOutputDto output = MapperUtils.map(post, PostOutputDto.class);
			return new ResponseEntity<Object>(output, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@ApiOperation(value = "해당  Post 삭제")
	@DeleteMapping("/delete/{pId}")
	public ResponseEntity<Object> delete(@PathVariable Long pId){
		try {
			log.trace("PostRestController - delete", pId);
			Post post = pService.findById(pId);
			fileService.deleteImage(post.getImageName());
			pService.delete(pId);
			return new ResponseEntity<Object>(new ResultDto(true, 1, "삭제되었습니다."), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@ApiOperation(value = "해당 유저의 지역통계 불러오기")
	@GetMapping("/getAreaStatistic/{uId}")
	public ResponseEntity<Object> getAreaStatistic(@PathVariable Long uId){
		try {
			log.trace("PostRestController - getAreaStatistic", uId);
			List<PostAreaStatisticDto> output = pService.getAreaStatistic(uId);
			return new ResponseEntity<Object>(output, HttpStatus.OK);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@ApiOperation(value = "해당 유저의 카테코리 통계 불러오기")
	@GetMapping("/getCategoryStatistic/{uId}")
	public ResponseEntity<Object> getCategoryStatistic(@PathVariable Long uId){
		try {
			log.trace("PostRestController - getCategoryStatistic", uId);
			Map<String, Integer> output = pService.getCategoryStatistic(uId);
			return new ResponseEntity<Object>(output, HttpStatus.OK);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@ApiOperation(value = "해당 유저의 먹킷리스트 불러오기")
	@GetMapping("/getMukitlist/{uId}")
	public ResponseEntity<Object> getMukitlist(@PathVariable Long uId){
		try {
			log.trace("PostRestController - getMukitlist", uId);
			List<Post> posts = pService.findByUserAndVisited(uId, false);
			List<PostOutputDto> output = MapperUtils.mapAll(posts, PostOutputDto.class);
			return new ResponseEntity<Object>(output, HttpStatus.OK);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PostMapping("/uploadImage/{uId}")
	public ResponseEntity<Object> uploadImage(@PathVariable Long uId, @RequestParam("file") MultipartFile file){
		try {
			log.trace("PostRestController - uploadImage", file);
			Map<String, Object> responseImage = fileService.uploadImage(file, "post");
			return new ResponseEntity<Object>(responseImage, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@DeleteMapping("/deleteImage/{uId}")
	public ResponseEntity<Object> deleteImage(@RequestParam("imageName") String imageName){
		try {
			int status = fileService.deleteImage(imageName);
			return new ResponseEntity<Object>(status, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@PostMapping("/postImage/{pId}")
	public ResponseEntity<Object> postImage(@PathVariable Long pId , @RequestParam("file") MultipartFile file) {
		try {
			Map<String, Object> responseImage = fileService.uploadImage(file, "post");
			PostImageDto dto = new PostImageDto(pId, responseImage.get("image").toString(), responseImage.get("imageName").toString());
			pService.updateImage(pId, dto);
			return new ResponseEntity<Object>(1, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.OK);
		}
	}
	
	
}

