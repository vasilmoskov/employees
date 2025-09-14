package com.example.xml;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;

import java.util.List;

@XmlRootElement(name = "Employees")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeesWrapper {

    @XmlElement(name = "Employee")
    private List<EmployeeXml> employees;

    public List<EmployeeXml> getEmployees() {
        return employees;
    }

    public EmployeesWrapper setEmployees(List<EmployeeXml> employees) {
        this.employees = employees;
        return this;
    }
}
