package co.inventorsoft.academy.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import co.inventorsoft.academy.spring.models.NotificationType;
import co.inventorsoft.academy.spring.models.User;
import co.inventorsoft.academy.spring.models.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

	@LocalServerPort
	private int port;

	private String baseUrl = "http://localhost:";

	private static RestTemplate restTemplate;

	@Autowired
	private UserTestRepository userTestRepository;

	@Value("${jwt.token.secret}")
	private String secret;

	@Value("${jwt.token.expired}")
	private int expirationTime;

	@BeforeAll
	public static void init() {
		restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void setUp() {
		baseUrl = baseUrl.concat(port + "");
		userTestRepository.deleteAll();
	}

	public HttpHeaders getHeaders(String email, UserRole role){
		Claims claims = Jwts.claims().setSubject(email);
		claims.put("name", "John Doe");
		claims.put("role", role);
		Date now = new Date();
		Date validity = new Date(now.getTime() + expirationTime);
		String token = Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(SignatureAlgorithm.HS256, secret)
			.compact();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		return headers;
	}

	@Test
	void testGetMeSuccess() {
		User user = User.builder()
			.userRole(UserRole.USER)
			.id(-1L)
			.slackId("-1")
			.username("name")
			.password("password")
			.email("user@mail.com")
			.notificationType(NotificationType.EMAIL)
			.build();
		userTestRepository.save(user);

		Long userId = userTestRepository.findByEmail(user.getEmail()).get().getId();

		HttpHeaders headers = getHeaders("user@mail.com", UserRole.USER);

		HttpEntity<User> entity = new HttpEntity<>(
			userTestRepository.findById(userId).get(), headers);

		ResponseEntity<User> response = restTemplate.exchange(
			baseUrl + "/users/me", HttpMethod.GET, entity,
			User.class);

		assertEquals(UserRole.USER, userTestRepository.findByEmail("user@mail.com").get().getUserRole());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testGetUserException() {
		User user = User.builder()
			.userRole(UserRole.USER)
			.id(-1L)
			.slackId("-1")
			.username("name")
			.password("password")
			.email("user@mail.com")
			.notificationType(NotificationType.EMAIL)
			.build();
		userTestRepository.save(user);

		User otherUser = User.builder()
			.userRole(UserRole.USER)
			.id(-2L)
			.slackId("-2")
			.username("name1")
			.password("password1")
			.email("found@mail.com")
			.notificationType(NotificationType.EMAIL)
			.build();

		userTestRepository.save(user);
		userTestRepository.save(otherUser);

		HttpHeaders headers = getHeaders("user@mail.com", UserRole.USER);

		HttpEntity<User> entity = new HttpEntity<>(
			userTestRepository.findById(otherUser.getId()).get(), headers);

		assertThrows(HttpClientErrorException.Forbidden.class,
			() -> restTemplate.exchange(
				baseUrl + "/users/admin/info/" + otherUser.getId(), HttpMethod.GET, entity,
				User.class));
	}

	@Test
	void testGetUserSuccess() {
		User user = User.builder()
			.userRole(UserRole.ADMIN)
			.id(-1L)
			.slackId("-1")
			.username("name")
			.password("password")
			.email("admin@mail.com")
			.notificationType(NotificationType.EMAIL)
			.build();
		userTestRepository.save(user);

		User otherUser = User.builder()
			.userRole(UserRole.USER)
			.id(-2L)
			.slackId("-2")
			.username("name1")
			.password("password1")
			.email("found@mail.com")
			.notificationType(NotificationType.EMAIL)
			.build();

		userTestRepository.save(user);
		userTestRepository.save(otherUser);


		HttpHeaders headers = getHeaders("admin@mail.com", UserRole.ADMIN);

		HttpEntity<User> entity = new HttpEntity<>(user, headers);

		ResponseEntity<User> response = restTemplate.exchange(
			baseUrl + "/users/admin/info/" + otherUser.getId(), HttpMethod.GET, entity,
			User.class);

		assertEquals(UserRole.USER, userTestRepository.findByEmail("found@mail.com").get().getUserRole());
		assertEquals(HttpStatus.OK, response.getStatusCode());

	}

}
