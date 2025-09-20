package in.th.main.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.th.main.entities.BlackList;
import in.th.main.entities.User;
import in.th.main.enums.Role;
import in.th.main.service.BlackListService;
import in.th.main.service.JWTService;
import in.th.main.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class LoginControl {

	@Autowired
	private UserService userService;
	@Autowired
	private JWTService jwtService;
	@Autowired
	private BlackListService blackListService;
	Logger logger = Logger.getLogger(LoginControl.class.getName());
	
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody User user){
		User login = userService.login(user.getEmail(), user.getPassword());
		if (login!=null) {
			logger.log(Level.INFO, "User Recieved at controller");
			Map<String, Object> response = new HashMap<>();
			response.put("username", login.getName());
			response.put("token","Bearer "+jwtService.tokengen(user.getEmail()));
			logger.log(Level.INFO, "Response Send");
			return ResponseEntity.ok(response);
			
		} else {
			logger.log(Level.SEVERE, "User Is Not Found In DataBase");
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity<Object> register(@RequestPart User user,@RequestPart MultipartFile image){
		user.setPassword(jwtService.passwordEncoder(user.getPassword()));
		user.setRole(Role.USER);
		boolean status = userService.register(user,image);
		if (status) {
			logger.log(Level.INFO, "Registration Complete");
			return ResponseEntity.ok("User Registered Successfully");
		} else {
			logger.log(Level.SEVERE, "User Already Exists In DataBase");
			return ResponseEntity.status(HttpStatus.CONFLICT).body("User Already Exists");
		}
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Object> logout(@RequestBody BlackList bl,HttpServletRequest req){
		HttpSession session = req.getSession();
		if (session!=null) {
			logger.log(Level.INFO, "Session found");
			boolean status = blackListService.blacklisttoken(bl);
			if(status){
				logger.log(Level.INFO,"Confirmation received.");
				session.invalidate();
				return ResponseEntity.ok(null);
			}else {
				logger.log(Level.SEVERE, "Some error occured while blacklisting");
				return ResponseEntity.internalServerError().build();
			}
		} else {
			logger.log(Level.WARNING, "Session not found");
			return ResponseEntity.notFound().build();
		}
	}	
}