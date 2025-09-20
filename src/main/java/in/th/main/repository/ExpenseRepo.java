package in.th.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.th.main.entities.Expense;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Long> {

}
