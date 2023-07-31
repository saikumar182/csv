package com.example.fileupload.Controller;
import com.example.fileupload.entity.Employee;
import com.example.fileupload.repository.EmployeeRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.fileupload.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


@RestController
public class EmployeeController {
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private EmployeeService employeeService;

    public EmployeeController(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }


    @PostMapping("/fileupload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile)  {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));
            List<Employee> employees = new ArrayList<>();
            String line;
            boolean isFirstLine = true;
            while ((line = bufferedReader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                System.out.println(Arrays.toString(data));
                if (data.length == 4) {
                    Employee employee = new Employee();
                    employee.setId(Integer.parseInt(data[0]));
                    employee.setFirstName(data[1]);
                    employee.setLastName(data[2]);
                    employee.setEmail(data[3]);
                    employees.add(employee);
                }
            }
            employeeRepo.saveAll(employees);
            return ResponseEntity.status(HttpStatus.OK).body("fileUploaded successfully");
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error uploading ");
        }
    }

        @GetMapping("/employees")
         public ResponseEntity<Page<Employee>> getEmployees(
                               @RequestParam("page") int page,
                                @RequestParam("size") int size,
                                @RequestParam("sortAttribute") String sortAttribute,
                               @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder) {

            Sort.Direction direction = Sort.Direction.ASC;

            if (sortOrder.equalsIgnoreCase("desc")) {

                direction = Sort.Direction.DESC;
            }
            Sort sort = Sort.by(direction, sortAttribute);
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Employee> employeePage = employeeService.getEmployees(pageable);
            return ResponseEntity.ok(employeePage);
        }

    @GetMapping("/employees/search")
    public List<Employee> searchEmployees(
            @RequestParam("attribute") String attribute,
            @RequestParam("value") String value)
     {
        switch (attribute) {
            case "id":
                return employeeRepo.findById(Integer.parseInt(value))
                        .map(Collections::singletonList)
                        .orElse(Collections.emptyList());
            case "firstname":
                return employeeRepo.findByFirstName(value);
            case "lastname":
                return employeeRepo.findByLastName(value);
            case "email":
                return employeeRepo.findByEmail(value);
            default:
                return Collections.emptyList();
            }
       }
   }



