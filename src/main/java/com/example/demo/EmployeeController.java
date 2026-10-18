package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    EmpService empService;

    @RequestMapping("/HelloWorld")
    public String helloWorld() {
        return "Hello, World!";
    }

    @GetMapping("/getEmpDetails")
    public List<Employee> getEmpDetails() {

        return empService.getAllEmployees();
    }

    @PostMapping("/addEmpDetails")
    public List<Employee> addEmpDetails(@RequestBody Employee employee) {

        return empService.addEmployee(employee); // Return the added employee details
    }
}
