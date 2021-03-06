package com.piggy.PIGGY.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.piggy.PIGGY.entity.Post;
import com.piggy.PIGGY.entity.Store;
import com.piggy.PIGGY.entity.User;


public interface PostRepository extends JpaRepository<Post, Long>{
	
	Optional<Post> findByUserAndStore(User user, Store store);
	
	List<Post> findByUser(User user);

	List<Post> findByUserAndVisited(User user, boolean visited);
	
	List<Post> findByUserOrderByCreatedTimeAtDesc(User user);
}
