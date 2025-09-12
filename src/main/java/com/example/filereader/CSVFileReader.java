package com.example.filereader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CSVFileReader {
    public static void main(String[] args) {
        Map<Long, List<Employee>> projectIDsToEmployees = new TreeMap<>();
        String format = "yyyy-MM-dd";

        try (CSVReader csvReader = new CSVReader(new FileReader("C:\\Users\\User\\Downloads\\employees.csv"))) {
            String[] headers = csvReader.readNext();

            String[] line = csvReader.readNext();

            while (line != null) {
                Long employeeId = Long.parseLong(line[0].trim());
                Long projectId = Long.parseLong(line[1].trim());
                String dateFromAsString = line[2].trim();
                String dateToAsString = line[3].trim();

                LocalDate dateFrom = LocalDate.parse(dateFromAsString, DateTimeFormatter.ofPattern(format));

                LocalDate dateTo = dateToAsString.equals("NULL") ?
                        LocalDate.now() :
                        LocalDate.parse(dateToAsString, DateTimeFormatter.ofPattern(format));

                projectIDsToEmployees.putIfAbsent(projectId, new ArrayList<>());

                // this check ensures that Date From is not after Date To which would be invalid case
                if (!dateFrom.isAfter(dateTo)) {
                    projectIDsToEmployees.get(projectId).add(new Employee(employeeId, dateFrom, dateTo));
                }

                line = csvReader.readNext();
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        Long emp1Id = null;
        Long emp2Id = null;
        Long longestPeriodOfWorkingTogether = Long.MIN_VALUE;

        // return: Employee ID 1, Employee ID 2, Project ID, Days Worked
        List<EmployeesCommonProject> commonProjects = new ArrayList<>();

        for (Map.Entry<Long, List<Employee>> entry : projectIDsToEmployees.entrySet()) {

            List<Employee> employeesInProject = entry.getValue();

            if (employeesInProject.size() > 1) {

                for (int i = 0; i < employeesInProject.size() - 1; i++) { // todo: check for indexoutofboundsEx
                    for (int j = i + 1; j < employeesInProject.size(); j++) { // todo: check for indexoutofboundsEx
                        Long projectId = entry.getKey();

                        Employee employee1 = employeesInProject.get(i);
                        Employee employee2 = employeesInProject.get(j);

                        LocalDate employee1DateFrom = employee1.getDateFrom();
                        LocalDate employee1DateTo = employee1.getDateTo();

                        LocalDate employee2DateFrom = employee2.getDateFrom();
                        LocalDate employee2DateTo = employee2.getDateTo();

                        Long daysWorkedTogether = null;

                        if (employee1DateFrom.isBefore(employee2DateFrom)) {
                            if (employee2DateFrom.isAfter(employee1DateTo)) {
                                continue;
                            }

                            if (employee2DateFrom.isBefore(employee1DateTo) || employee2DateFrom.isEqual(employee1DateTo)) {
                                if (employee1DateTo.isBefore(employee2DateTo)) {
                                    daysWorkedTogether = ChronoUnit.DAYS.between(employee2DateFrom, employee1DateTo);
                                } else {
                                    daysWorkedTogether = ChronoUnit.DAYS.between(employee2DateFrom, employee2DateTo);
                                }
                            }

                        } else if (employee1DateFrom.isAfter(employee2DateFrom)) {
                            if (employee1DateFrom.isAfter(employee2DateTo)) {
                                continue;
                            }

                            if (employee1DateFrom.isBefore(employee2DateTo) || employee2DateFrom.isEqual(employee1DateTo)) {
                                if (employee1DateTo.isBefore(employee2DateTo)) {
                                    daysWorkedTogether = ChronoUnit.DAYS.between(employee1DateFrom, employee1DateTo);
                                } else {
                                    daysWorkedTogether = ChronoUnit.DAYS.between(employee1DateFrom, employee2DateTo);
                                }
                            }

                        } else if (employee1DateFrom.isEqual(employee2DateFrom)) {
                            if (employee1DateTo.isBefore(employee2DateTo)) {
                                daysWorkedTogether = ChronoUnit.DAYS.between(employee1DateFrom, employee1DateTo);
                            } else {
                                daysWorkedTogether = ChronoUnit.DAYS.between(employee1DateFrom, employee2DateTo); // todo: check if they are equal
                            }
                        }

                        if (daysWorkedTogether != null) {
                            daysWorkedTogether++; // todo: explain
                            if (daysWorkedTogether > longestPeriodOfWorkingTogether) {
                                longestPeriodOfWorkingTogether = daysWorkedTogether;
                                emp1Id = employee1.getId();
                                emp2Id = employee2.getId();
                            }

                            EmployeesCommonProject commonProject = new EmployeesCommonProject(employee1.getId(), employee2.getId(), projectId, daysWorkedTogether);
                            commonProjects.add(commonProject);
                        }
                    }
                }

            }

        }

        if (emp1Id != null && emp2Id != null) {
            System.out.printf("Longest Period working together: %d, %d, %d%n", emp1Id, emp2Id, longestPeriodOfWorkingTogether);
        }

        System.out.println("Employee ID #1, Employee ID #2, Project ID, Days worked");

        for (EmployeesCommonProject commonProject : commonProjects) {
            System.out.printf("%d, %d, %d, %d%n", commonProject.getEmp1Id(), commonProject.getEmp2Id(), commonProject.getProjectId(), commonProject.getDaysWorkedTogether());
        }
    }

}
