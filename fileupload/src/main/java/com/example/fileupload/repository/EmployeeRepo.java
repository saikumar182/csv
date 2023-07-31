package com.example.fileupload.repository;
import com.example.fileupload.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface EmployeeRepo extends JpaRepository<Employee,Integer> {
    List<Employee> findByFirstName(String value);
    List<Employee> findByLastName(String value);
    List<Employee> findByEmail(String value);
}

