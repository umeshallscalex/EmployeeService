package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmpService {
    private final List<Employee> employees = new ArrayList<>();

    public EmpService() {
        Employee emp = new Employee();
        emp.setEmpId(1);
        emp.setEmpName("John Doe");
        employees.add(emp);
    }

    public List<Employee> getAllEmployees() {
        return employees;
    }

    public List<Employee> saveEmployee(Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmpId() == employee.getEmpId()) {
                employees.set(i, employee);
                return employees;
            }
        }

        employees.add(employee);
        return employees;
    }
    public List<Employee> deleteEmpDetails(@PathVariable int empId) {
        employees.removeIf(emp -> emp.getEmpId() == empId);
        return employees;
    }


}


