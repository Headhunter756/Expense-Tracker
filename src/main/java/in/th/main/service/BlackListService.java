package in.th.main.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.th.main.entities.BlackList;
import in.th.main.repository.BlackListRepo;

@Service
public class BlackListService {

	@Autowired
	private BlackListRepo repo;
	Logger logger = Logger.getLogger(BlackListService.class.getName());
	
	public boolean blacklisttoken(BlackList bl) {
		try {
			long totaltoken =  repo.count()+1;
			bl.setId(totaltoken);
			repo.save(bl);
			logger.log(Level.INFO, "Token Blacklisted");
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Token couldn't be blacklisted",e);
			return false;
		}
	}
	public boolean isBlacklisted(String token) {
		return repo.existsByToken(token);
	}
}
