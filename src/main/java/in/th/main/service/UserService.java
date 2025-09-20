package in.th.main.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.th.main.entities.User;
import in.th.main.repository.UserRepo;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	Logger logger = Logger.getLogger(UserService.class.getName());
	
	public boolean register(User user,MultipartFile image) {
		try {
			if (!userRepo.existsByEmail(user.getEmail())) {
				user.setImageName(image.getOriginalFilename());
				user.setImageType(image.getContentType());
				user.setImageData(image.getBytes());
				userRepo.save(user);
				logger.log(Level.INFO, "New User Created");
				return true;
			} else {
				logger.log(Level.SEVERE, "User Already Exists");
				return false;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Some error occured");
			return false;
		}
		
	}
	
	public User login(String email,String password) {
		if (userRepo.existsByEmail(email)) {
			User user = userRepo.findByEmail(email);
			logger.log(Level.INFO,"User Found, Now Validating");
			if (encoder.matches(password, user.getPassword())) {
				logger.log(Level.INFO, "User Authenticated, Sending Confirmation to Controller");
				return user;
			} else {
				logger.log(Level.SEVERE, "Password is Incorrect");
				return null;
			}
		} else {
			logger.log(Level.SEVERE,"No user found");
			return null;
		}
	}
	
	public User details(String username) {
		if (userRepo.existsByName(username)) {
			logger.log(Level.INFO, "User detials found, sending to controller");
			return userRepo.findByName(username);
		} else {
			logger.log(Level.SEVERE, "No user found by such name");
			return null;
		}
	}
	
	public boolean editDetails(User user) {
		if (userRepo.existsByName(user.getName())) {
			logger.log(Level.INFO, "User detials found, saving the new details");
			userRepo.save(user);
			return true;
		} else {
			logger.log(Level.SEVERE, "No user found by such name");
			return false;
			
		}
	}
}
