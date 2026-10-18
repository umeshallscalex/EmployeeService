package com.example.demo;

import java.util.Objects;

public class Employee {

    int empId;
    String empName;
    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return empId == employee.empId && Objects.equals(empName, employee.empName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId, empName);
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }
}
