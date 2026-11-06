package com.bptn.feedApp.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.bptn.feedApp.repository.FeedRepository;
import com.bptn.feedApp.repository.UserRepository;
import java.sql.Timestamp;
import java.time.Instant;
import org.springframework.security.core.context.SecurityContextHolder;
import com.bptn.feedApp.exception.domain.UserNotFoundException;
import com.bptn.feedApp.jpa.Feed;
import com.bptn.feedApp.jpa.User;

@Service
public class FeedService {
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	UserRepository userRepository;

	@Autowired
	FeedRepository feedRepository;
	
	public Feed createFeed(Feed feed) {

	    String username = SecurityContextHolder.getContext().getAuthentication().getName();

	    User user = this.userRepository.findByUsername(username)
	                .orElseThrow(() -> new UserNotFoundException(String.format("Username doesn't exist, %s", username)));

	    feed.setUser(user);
	    feed.setCreatedOn(Timestamp.from(Instant.now()));

	    return this.feedRepository.save(feed);
	}
}
