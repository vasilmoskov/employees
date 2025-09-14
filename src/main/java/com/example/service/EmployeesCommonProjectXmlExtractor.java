package com.example.service;

import com.example.model.Employee;
import com.example.model.EmployeesCommonProject;
import com.example.xml.EmployeeXml;
import com.example.xml.EmployeesWrapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeesCommonProjectXmlExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeesCommonProjectXmlExtractor.class);

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static List<EmployeesCommonProject> extractFromFile(String fileName) {
        Map<Long, List<Employee>> projectIDsToEmployees = extractProjectIdsToEmployeesFromFile(fileName);

        return EmployeesCommonProjectCalculator.calculate(projectIDsToEmployees);
    }

    private static Map<Long, List<Employee>> extractProjectIdsToEmployeesFromFile(String fileName) {
        Map<Long, List<Employee>> projectIDsToEmployees = new HashMap<>();

        try {
            JAXBContext context = JAXBContext.newInstance(EmployeesWrapper.class);

            EmployeesWrapper employeesWrapper = (EmployeesWrapper) context.createUnmarshaller()
                    .unmarshal(new FileReader(fileName));

            List<EmployeeXml> employees = employeesWrapper.getEmployees();

            for (EmployeeXml employee : employees) {
                String dateFromAsString = employee.getDateFrom();
                String dateToAsString = employee.getDateTo();
                Long projectId = employee.getProjectId();
                Long employeeId = employee.getId();

                LocalDate dateFrom = LocalDate.parse(dateFromAsString, DateTimeFormatter.ofPattern(DATE_FORMAT));

                LocalDate dateTo = dateToAsString.equals("NULL") ?
                        LocalDate.now() :
                        LocalDate.parse(dateToAsString, DateTimeFormatter.ofPattern(DATE_FORMAT));

                projectIDsToEmployees.putIfAbsent(projectId, new ArrayList<>());

                // this check ensures that Date From is not after Date To which would be invalid case
                if (!dateFrom.isAfter(dateTo)) {
                    projectIDsToEmployees.get(projectId).add(new Employee(employeeId, dateFrom, dateTo));
                }
            }

        } catch (JAXBException e) {
            LOGGER.error("The provided XML file is invalid.", e);
        } catch (FileNotFoundException e) {
            LOGGER.error("No such file.", e);
        }

        return projectIDsToEmployees;
    }
}
