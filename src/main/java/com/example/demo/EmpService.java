package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Comparator;
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
        List<Employee> sortedList = new ArrayList<>(employees);
        //sortedList.sort((e1, e2) -> e1.getEmpName()>e2.empId? 1 : e1.empId<e2.empId? -1 : 0);
        sortedList.sort((e1, e2) -> e1.getEmpName().compareTo(e2.getEmpName()));
        employees.clear();
        employees.addAll(sortedList);
        return employees;
    }


    public List<Employee> deleteEmployee(int id) {
        employees.removeIf(employee -> employee.getEmpId() == id);
        return employees;
    }
}



