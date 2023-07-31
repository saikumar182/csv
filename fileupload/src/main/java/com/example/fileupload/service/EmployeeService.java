package com.example.fileupload.service;

import com.example.fileupload.entity.Employee;
import com.example.fileupload.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    public Page<Employee> getEmployees(int page, int size, String sortAttribute, String sortOrder) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortOrder.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Sort sort = Sort.by(direction, sortAttribute);
        Pageable pageable = PageRequest.of(page, size, sort);

        return employeeRepo.findAll(pageable);
    }
    public Page<Employee> getEmployees(Pageable pageable) {
        return employeeRepo.findAll(pageable);
    }

}
