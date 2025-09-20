package in.th.main.service;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JWTService {

	String encodedkey;
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	@Autowired
	private BlackListService service;
	
	public String tokengen(String email) {
		Map<String, Object> claims = new HashMap<>();
		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(email)
				.issuedAt(new Date(System.currentTimeMillis()))
				.and()
				.signWith(retrieveKey())
				.compact();
	}
	
	@PostConstruct
	public void init() {
		try {
			KeyGenerator generator = KeyGenerator.getInstance("HmacSHA256");
			SecretKey secretKey = generator.generateKey();
			encodedkey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private SecretKey retrieveKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(encodedkey));
	}

	public String passwordEncoder(String password) {
		return encoder.encode(password);
	}
	
	private Claims extraxtAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(retrieveKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	private <T> T extractClaims(String token, Function<Claims, T> resolver) {
		Claims claims = extraxtAllClaims(token);
		return resolver.apply(claims);
	}
	
	public String extractUsername(String token) {
		return extractClaims(token, Claims::getSubject);
	}
	
	private boolean isBlackListed(String token) {
		return service.isBlacklisted(token);
	}
	
	public boolean validate(String token, UserDetails details) {
		String extractedEmail = extractUsername(token);
		return (details.getUsername().equals(extractedEmail) && isBlackListed(token)!=true);
	}
	
}