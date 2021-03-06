package com.piggy.PIGGY.service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@Override
	public Map<String, Object> uploadImage(MultipartFile fileStream, String folderName) throws IOException, ServletException {
		
		log.trace("FileService - uploadImage");
		Resource resource = resourceLoader.getResource("classpath:/piggy-credential.json");
		Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(resource.getFile()));
		Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
		String bucketName = "piggy02301";
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd-HHmmssSSS-");
		ZonedDateTime dt = ZonedDateTime.now();
		String dtString = dt.format(dtf);
		final String fileName = folderName + "/" + dtString + fileStream.getOriginalFilename();
		
		if (fileName != null && !fileName.isEmpty() && fileName.contains(".")) {
			final String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
			String[] allowedExt = {"jpg", "jpeg", "png", "gif"};
			for (String s : allowedExt) {
				if (extension.equals(s)) {
					InputStream is = fileStream.getInputStream();
				    ByteArrayOutputStream os = new ByteArrayOutputStream();
				    byte[] readBuf = new byte[4096];
				    while (is.available() > 0) {
				        int bytesRead = is.read(readBuf);
				        os.write(readBuf, 0, bytesRead);
				    }
				    
				    BlobInfo blobInfo =
				        storage.create(
				                    BlobInfo
				                    .newBuilder(bucketName, fileName)
				                    .setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER))))
				                    .build(),
				                os.toByteArray());
				    Map<String, Object> imageResponse = new HashMap<>();
				    String url = blobInfo.getMediaLink();
				    String blobName = blobInfo.getName();
				    System.out.println(blobName);
				    imageResponse.put("image", url);
				    imageResponse.put("imageName", blobName);
			        return imageResponse;
	            }
	        }
	        throw new ServletException("이미지 파일만 업로드가 가능합니다.");
	    }
		return null;
	}
	
	@Override
	public int deleteImage(String imageName) throws IOException, ServletException {
		Resource resource = resourceLoader.getResource("classpath:/piggy-credential.json");
		Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(resource.getFile()));
		Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
		String bucketName = "piggy02301";
		
		BlobId blobId = BlobId.of(bucketName, imageName);
	    boolean deleted = storage.delete(blobId);
	    if (deleted) {
	    	return 1;
	    } else {
	    	return 0;
	    }
	}

}
