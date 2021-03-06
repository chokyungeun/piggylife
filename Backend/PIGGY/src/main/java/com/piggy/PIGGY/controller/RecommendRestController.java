package com.piggy.PIGGY.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.piggy.PIGGY.dto.AreaRecommendDto;
import com.piggy.PIGGY.dto.MatchDto;
import com.piggy.PIGGY.dto.StoreOutputDto;
import com.piggy.PIGGY.entity.Post;
import com.piggy.PIGGY.entity.Store;
import com.piggy.PIGGY.entity.UserRecommend;
import com.piggy.PIGGY.service.PostService;
import com.piggy.PIGGY.service.RecommendService;
import com.piggy.PIGGY.util.MapperUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = { "Recommend" }, description = "추천 정보 REST API")
@RequestMapping(value = "/recommend")
@CrossOrigin(origins = "*")
public class RecommendRestController {

	@Autowired
	private RecommendService rService;
	
	@Autowired
	private PostService pService;
	
	@ApiOperation(value = "모든 추천 불러오기 (테스트용)")
	@GetMapping("/findAllRecommend")
	public ResponseEntity<Object> findAllRecommend(){
		try {
			List<UserRecommend> recos = rService.findAllRecommend();
			return new ResponseEntity<Object>(recos, HttpStatus.OK);
		} catch (Exception e) {
			throw e;
		}
	}

	@ApiOperation(value = "해당 유저 추천 불러오기")
	@GetMapping("/findUserRecommend")
	public ResponseEntity<Object> findUserRecommend(@RequestParam Long uId) {
		try {
			List<Post> list = pService.findByUser(uId);
			if(list.size()<5) {
				uId = (long) 13;
			}
			List<Store> stores = rService.findUserRecommend(uId);
			List<StoreOutputDto> output = MapperUtils.mapAll(stores, StoreOutputDto.class);
			return new ResponseEntity<Object>(output, HttpStatus.OK);
		} catch (Exception e) {

			throw e;
		}
	}

	@ApiOperation(value = "해당 유저 추천 불러오기")
	@GetMapping("/findAreaRecommend")
	public ResponseEntity<Object> findAreaRecommend(@RequestParam Long uId) {
		try {
			List<Post> list = pService.findByUser(uId);
			if(list.size()<5) {
				uId = (long) 13;
			}
			AreaRecommendDto output = rService.findAreaRecommend(uId);
			return new ResponseEntity<Object>(output, HttpStatus.OK);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@ApiOperation(value = "두 유저 매칭 불러오기")
	@GetMapping("/findMatch")
	public ResponseEntity<Object> findMatch(@RequestParam String selfEmail, @RequestParam String friendEmail) {
		try {
			MatchDto output = rService.findMatch(selfEmail, friendEmail);
			return new ResponseEntity<Object>(output, HttpStatus.OK);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@ApiOperation(value = "인기 지역 맛집 불러오기")
	@GetMapping("/findPopularAreaRecommend")
	public ResponseEntity<Object> findPopularAreaRecommend() {
		try {
			AreaRecommendDto output = rService.findPopularAreaRecommend();
			return new ResponseEntity<Object>(output, HttpStatus.OK);
		} catch (Exception e) {
			throw e;
		}
	}

}
