package com.bptn.feedApp.controller;

import com.bptn.feedApp.jpa.User;
import com.bptn.feedApp.repository.UserRepository;
import com.bptn.feedApp.security.JwtService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
// NEW PATTERN: Explicitly import JacksonAutoConfiguration to fix the ObjectMapper issue
@Import(JacksonAutoConfiguration.class) 
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class UserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	    
	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserRepository userRepository;
	    
	@Autowired
	private PasswordEncoder passwordEncoder;

	private User user;
	
	 String otherUsername = "janedoe";
	 String otherPassword = "letmein";
	
	@BeforeEach
	public void setup() {
		userRepository.deleteAll();

		this.user = new User();
		this.user.setFirstName("John");
		this.user.setLastName("Doe");
		this.user.setUsername("johndoe");
		this.user.setPassword("mypassword");
		this.user.setPhone("987654321");
		this.user.setEmailId("johndoe@example.com");
		
	}
	@Test
	@Order(1)
	public void signupIntegrationTest() throws Exception {

//	  ObjectMapper objectMapper = JsonMapper.builder().disable(MapperFeature.USE_ANNOTATIONS).build();

	  /* Check the Rest End Point Response */
	  this.mockMvc.perform(MockMvcRequestBuilders.post("/user/signup")
	          .contentType(MediaType.APPLICATION_JSON)
	      .content(objectMapper.writeValueAsString(user)))
	        .andExpect(status().isOk()).andExpect(jsonPath("$.firstName", is(this.user.getFirstName())))
	        .andExpect(jsonPath("$.lastName", is(this.user.getLastName())))
	        .andExpect(jsonPath("$.username", is(this.user.getUsername())))
	        .andExpect(jsonPath("$.phone", is(this.user.getPhone())))
	        .andExpect(jsonPath("$.emailId", is(this.user.getEmailId())));

	    /* Check the DB */
	  Optional<User> opt = this.userRepository.findByUsername(this.user.getUsername());

	  assertTrue(opt.isPresent(), "User Should Exist");

	  assertEquals(1, opt.get().getUserId());
	  assertEquals(this.user.getFirstName(), opt.get().getFirstName());
	  assertEquals(this.user.getLastName(), opt.get().getLastName());
	  assertEquals(this.user.getUsername(), opt.get().getUsername());
	  assertEquals(this.user.getPhone(), opt.get().getPhone());
	  assertEquals(this.user.getEmailId(), opt.get().getEmailId());
	  assertEquals(false, opt.get().getEmailVerified());

	  assertTrue(this.passwordEncoder.matches(this.user.getPassword(), opt.get().getPassword()));
	}
	
	@Test
	@Order(2)
	public void signupUsernameExistsIntegrationTest() throws Exception {

	  /* Check the Rest End Point Response */
	  this.mockMvc.perform(MockMvcRequestBuilders.post("/user/signup") 
	          .contentType(MediaType.APPLICATION_JSON)
	      .content(objectMapper.writeValueAsString(this.user)))
	      .andExpect(status().is4xxClientError())
	          .andExpect(jsonPath("$.httpStatusCode", is(400)))
	      .andExpect(jsonPath("$.httpStatus", is("BAD_REQUEST")))
	      .andExpect(jsonPath("$.reason", is("BAD REQUEST")))
	          .andExpect(jsonPath("$.message",
	      is(String.format("Username already exists, %s", this.user.getUsername()))));
	}
	
	@Test
	@Order(3)
	public void signupEmailExistsIntegrationTest() throws Exception {
	      
	      
	    this.user.setUsername(this.otherUsername);
	      
	    /* Check the Rest End Point Response */
	    this.mockMvc.perform(MockMvcRequestBuilders.post("/user/signup")
	          .contentType(MediaType.APPLICATION_JSON)
	          .content(objectMapper.writeValueAsString(this.user)))
	          .andExpect(status().is4xxClientError())
	          .andExpect(jsonPath("$.httpStatusCode", is(400)))
	          .andExpect(jsonPath("$.httpStatus", is("BAD_REQUEST")))
	          .andExpect(jsonPath("$.reason", is("BAD REQUEST")))
	          .andExpect(jsonPath("$.message", is(String.format("Email already exists, %s", this.user.getEmailId()))));
	}
	
	
}