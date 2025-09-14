package com.example.extractor;

import com.example.model.Employee;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeesCommonProjectCsvExtractor extends EmployeesCommonProjectExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeesCommonProjectCsvExtractor.class);

    @Override
    protected Map<Long, List<Employee>> extractProjectIdsToEmployeesFromFile(String fileName) {
        Map<Long, List<Employee>> projectIDsToEmployees = new HashMap<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            // skip first line as it is header
            csvReader.readNext();

            String[] line = csvReader.readNext();

            while (line != null) {
                Long employeeId = Long.parseLong(line[0].trim());
                Long projectId = Long.parseLong(line[1].trim());
                String dateFromAsString = line[2].trim();
                String dateToAsString = line[3].trim();

                populateProjectsToEmployess(dateFromAsString, dateToAsString, employeeId, projectId, projectIDsToEmployees);

                line = csvReader.readNext();
            }

        } catch (FileNotFoundException e) {
            LOGGER.error("No such file.", e);
        } catch (IOException e) {
            LOGGER.error("An error has occurred during file processing.", e);
        } catch (CsvValidationException e) {
            LOGGER.error("The provided CSV file is invalid.", e);
        }

        return projectIDsToEmployees;
    }

}
