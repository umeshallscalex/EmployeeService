package com.example.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmpServiceTest {

    @Test
    void saveEmployeeUpdatesExistingEmployeeWithSameId() {
        EmpService service = new EmpService();

        Employee first = new Employee();
        first.setEmpId(2);
        first.setEmpName("Rajesh");
        service.saveEmployee(first);

        Employee updated = new Employee();
        updated.setEmpId(2);
        updated.setEmpName("Navin");
        var employees = service.saveEmployee(updated);

        assertEquals(2, employees.size());
        assertEquals("Navin", employees.stream()
                .filter(employee -> employee.getEmpId() == 2)
                .findFirst()
                .orElseThrow()
                .getEmpName());
    }
}
