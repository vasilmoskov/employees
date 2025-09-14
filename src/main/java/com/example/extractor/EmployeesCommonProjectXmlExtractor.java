package com.example.extractor;

import com.example.model.Employee;
import com.example.xml.EmployeeXml;
import com.example.xml.EmployeesWrapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeesCommonProjectXmlExtractor extends EmployeesCommonProjectExtractor{
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeesCommonProjectXmlExtractor.class);

    @Override
    protected Map<Long, List<Employee>> extractProjectIdsToEmployeesFromFile(String fileName) {
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

                populateProjectsToEmployess(dateFromAsString, dateToAsString, employeeId, projectId, projectIDsToEmployees);
            }

        } catch (JAXBException e) {
            LOGGER.error("The provided XML file is invalid.", e);
        } catch (FileNotFoundException e) {
            LOGGER.error("No such file.", e);
        }

        return projectIDsToEmployees;
    }
}
