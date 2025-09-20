package in.th.main.service;

import in.th.main.entities.Expense;
import in.th.main.repository.ExpenseRepo;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

	@Autowired
	private ExpenseRepo expenseRepo;
	
	Logger logger = Logger.getLogger(ExpenseService.class.getName());
	
	public List<Expense> getAll(){
		try {
			logger.log(Level.INFO, "Sending Expense List to Controller");
			return expenseRepo.findAll();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Couldn't get expense list", e);
			return List.of();
		}
	}
	
	public Expense getOne(long id) {
		logger.log(Level.INFO, "Sending Entry to controller");
		Optional<Expense> entry = expenseRepo.findById(id);
		return entry.orElse(null);		
	}
	
	public long getLastId() {
		logger.log(Level.INFO, "Sending last id to controller");
		return (expenseRepo.count()+1L);
	}
	
	public boolean add(Expense expense) {
		try {
			expenseRepo.save(expense);
			logger.log(Level.INFO, "Expense Added Successfully");
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Expense Couldn't be added",e);
			return false;
		}
	}
	public boolean edit(Expense expense) {
	    try {
	        if (!expenseRepo.existsById(expense.getId())) {
	            logger.log(Level.WARNING, "Expense with ID " + expense.getId() + " does not exist");
	            return false;
	        }
	        expenseRepo.save(expense);
	        logger.log(Level.INFO, "Expense Edited Successfully");
	        return true;
	    } catch (Exception e) {
	        logger.log(Level.SEVERE, "Expense Couldn't be Edited", e);
	        return false;
	    }
	}
	public boolean delete(long id) {
		try {
			expenseRepo.delete(getOne(id));
			logger.log(Level.INFO, "Record Deleted Successfully");
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Record Couldn't be Deleted",e);
			return false;
		}
	}
	
}
