package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmpService {
    List<Employee> employees = new ArrayList<>();
        public List<Employee> getAllEmployees() {
            Employee emp1 = new Employee();
            emp1.setEmpId(1);
            emp1.setEmpName("John Doe");
            employees.add(emp1);

            Employee emp2 = new Employee();
            emp2.setEmpId(2);
            emp2.setEmpName("Jane Smith");
            employees.add(emp2);

            return employees;
        }

    public List<Employee> addEmployee(Employee employee) {
        employees.add(employee);
        return employees;
    }
}



