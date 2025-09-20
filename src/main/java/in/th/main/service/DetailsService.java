package in.th.main.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.th.main.entities.User;
import in.th.main.principals.UserPrincipal;
import in.th.main.repository.UserRepo;

@Service
public class DetailsService implements UserDetailsService{

	@Autowired
	private UserRepo userRepo;
	Logger logger = Logger.getLogger(DetailsService.class.getName());
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(email);
		if (user!=null) {
			logger.log(Level.INFO, "User Found, Sending user to service");
			return new UserPrincipal(user);
		} else {
			logger.log(Level.SEVERE, "User couldn't be found.");
			return null;
		}
	}
}
