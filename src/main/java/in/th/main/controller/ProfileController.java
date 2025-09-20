package in.th.main.controller;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.th.main.entities.User;
import in.th.main.service.JWTService;
import in.th.main.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/profile")
public class ProfileController {

	@Autowired
	private JWTService jwtService;
	@Autowired
	private UserService userService;
	Logger logger = Logger.getLogger(ProfileController.class.getName());
	
	@PostMapping("/details")
	public ResponseEntity<?> details(@RequestBody String username){
		User user = userService.details(username);
		if (user!=null) {
			logger.log(Level.INFO, "User details recieved at the controller");
			user.setImageData(null);
			user.setImageName("");
			user.setImageType("");
			logger.log(Level.INFO, "User details send");
			return ResponseEntity.ok(user);
		} else {
			logger.log(Level.SEVERE, "User details not found");
			return ResponseEntity.notFound().build();
		}
		
	}
	
	@PostMapping("/image")
	public ResponseEntity<?> image(@RequestBody String username){
		User user = userService.details(username);
		if (user!=null && user.getImageData().length!=0) {
			logger.log(Level.INFO, "User image recieved at the controller");
			byte[] image = user.getImageData();
			logger.log(Level.INFO, "User image send");
			return ResponseEntity
					.ok()
					.contentType(MediaType.valueOf(user.getImageType()))
					.body(image);
		} else {
			logger.log(Level.SEVERE, "User image not found");
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/change")
	public ResponseEntity<String> change(@RequestBody Map<String,String> obj){
		User user = userService.details(obj.get("username"));
		user.setPassword(jwtService.passwordEncoder(obj.get("password")));
		boolean status = userService.editDetails(user);
		if (status) {
			logger.log(Level.INFO, "Password updated succesfully");
			return ResponseEntity.ok("Password updated succesfully");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user found by such name");
		}
		
	}
}
