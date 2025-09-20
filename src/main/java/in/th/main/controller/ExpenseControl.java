package in.th.main.controller;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.th.main.entities.Expense;
import in.th.main.service.ExpenseService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/expense")
public class ExpenseControl {
	
	@Autowired
	private ExpenseService expenseService;
	
	Logger logger = Logger.getLogger(ExpenseControl.class.getName());
	
	@GetMapping("/")
	public ResponseEntity<Object> getAll(){
		List<Expense> list = expenseService.getAll();
		if (!list.isEmpty()) {
			logger.log(Level.INFO, "Recieved Expense List at controller");
			logger.log(Level.INFO, "Expense List Send");
			return ResponseEntity.ok(list);
		} else {
			logger.log(Level.SEVERE, "No records received");
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getOnlyOne(@PathVariable long id){
		Expense expense = expenseService.getOne(id);
		if (expense!=null) {
			logger.log(Level.INFO, "Entry Recived at controller");
			logger.log(Level.INFO, "Entry Send");
			return ResponseEntity.ok(expense);
		} else {
			logger.log(Level.SEVERE, "Entry Not Found");
			return ResponseEntity.notFound().build();
		}
	}
	@GetMapping("/id")
	public ResponseEntity<Object> getLastId(){
		try {
			long lastid = expenseService.getLastId();
			logger.log(Level.INFO, "Recieved Last Id at controller.");
			logger.log(Level.INFO, "Last Id send");
			return ResponseEntity.ok(Map.of("lastid",lastid));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Some error occured", e);
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@PostMapping("/")
	public ResponseEntity<Object> add(@RequestBody Expense expense) {
		boolean status = expenseService.add(expense);
		if (status) {
			return ResponseEntity.status(HttpStatus.CREATED).body("Record Added");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Some Error Occurred");
		}
	}
	
	@PutMapping("/")
	public ResponseEntity<Object> edit(@RequestBody Expense expense){
		boolean status = expenseService.edit(expense);
		if (status) {
			return ResponseEntity.ok(Map.of("message", "Record edited"));
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Some Error Occurred");
		}
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable long id){
		boolean status = expenseService.delete(id);
		if (status) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Record Deleted");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Some Error Occurred");
		}
	}
}
