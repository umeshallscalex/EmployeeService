package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class EmpService {
    @Autowired
    EmployeeRepository empRepository;
    private final List<Employee> employees = new ArrayList<>();

  /*  public EmpService() {
        Employee emp = new Employee();
        emp.setEmpId(1);
        emp.setEmpName("John Doe");
        employees.add(emp);
    }*/

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>(empRepository.findAll());
        return employees;
    }

    public List<Employee> saveEmployee(Employee employee) {
       /* for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmpId() == employee.getEmpId()) {
                employees.set(i, employee);
                return employees;
            }
        }

        employees.add(employee);
        List<Employee> sortedList = new ArrayList<>(employees);
       //sortedList.sort((e1, e2) -> e1.getEmpId()>e2.getEmpId()? 1 : e1.getEmpId()<e2.getEmpId()? -1 : 0);
       // sortedList.sort((e1, e2) -> e1.getEmpName().compareTo(e2.getEmpName()));
        employees.clear();
        sortedList = sortedList.stream().sorted(Comparator.comparingInt(Employee::getEmpId)).toList();
        employees.addAll(sortedList);

        */
        empRepository.save(employee);
        return getAllEmployees();
    }
    public List<Employee> deleteEmpDetails(@PathVariable int empId) {
        empRepository.deleteById(empId);
        return getAllEmployees();
    }


}


