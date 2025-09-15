package com.example.extractor;

import com.example.model.Employee;
import com.example.model.EmployeesCommonProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class EmployeesCommonProjectExtractor {
    private static final List<DateTimeFormatter> SUPPORTED_DATE_FORMATS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd")
    );

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeesCommonProjectExtractor.class);

    public List<EmployeesCommonProject> extractFromFile(String fileName) {
        Map<Long, List<Employee>> projectIDsToEmployees = extractProjectIdsToEmployeesFromFile(fileName);

        Long emp1Id = null;
        Long emp2Id = null;
        long longestPeriodOfWorkingTogether = Long.MIN_VALUE;

        List<EmployeesCommonProject> commonProjects = new ArrayList<>();

        for (Map.Entry<Long, List<Employee>> entry : projectIDsToEmployees.entrySet()) {
            List<Employee> employeesInProject = entry.getValue();

            if (employeesInProject.size() > 1) {
                for (int i = 0; i < employeesInProject.size() - 1; i++) {
                    for (int j = i + 1; j < employeesInProject.size(); j++) {
                        Long projectId = entry.getKey();

                        Employee employee1 = employeesInProject.get(i);
                        Employee employee2 = employeesInProject.get(j);

                        Long daysWorkedTogether = calculateDaysWorkedTogether(
                                employee1.dateFrom(), employee1.dateTo(),
                                employee2.dateFrom(), employee2.dateTo()
                        );

                        if (daysWorkedTogether != null) {
                            // make interval inclusive
                            daysWorkedTogether++;

                            if (daysWorkedTogether > longestPeriodOfWorkingTogether) {
                                longestPeriodOfWorkingTogether = daysWorkedTogether;
                                emp1Id = employee1.id();
                                emp2Id = employee2.id();
                            }

                            EmployeesCommonProject commonProject = new EmployeesCommonProject(employee1.id(), employee2.id(), projectId, daysWorkedTogether);
                            commonProjects.add(commonProject);
                        }
                    }
                }
            }
        }

        if (emp1Id != null && emp2Id != null) {
            LOGGER.info("Longest period working together:");
            LOGGER.info("{}, {}, {}", emp1Id, emp2Id, longestPeriodOfWorkingTogether);
        }

        return commonProjects;
    }

    protected abstract Map<Long, List<Employee>> extractProjectIdsToEmployeesFromFile(String fileName);

    protected void populateProjectsToEmployess(String dateFromAsString, String dateToAsString, Long employeeId, Long projectId,
                                               Map<Long, List<Employee>> projectIDsToEmployees) {
        LocalDate dateFrom = parseDate(dateFromAsString);
        LocalDate dateTo = dateToAsString.equals("NULL") ?
                LocalDate.now() :
                parseDate(dateToAsString);

        projectIDsToEmployees.putIfAbsent(projectId, new ArrayList<>());

        // this check ensures that Date From is not after Date To which would be invalid case
        if (!dateFrom.isAfter(dateTo)) {
            projectIDsToEmployees.get(projectId).add(new Employee(employeeId, dateFrom, dateTo));
        }
    }

    private LocalDate parseDate(String dateString) {
        for (DateTimeFormatter formatter : SUPPORTED_DATE_FORMATS) {
            try {
                return LocalDate.parse(dateString, formatter);
            } catch (Exception ignored) {
                // No-op
            }
        }

        throw new IllegalArgumentException("Unsupported date format: " + dateString);
    }

    private Long calculateDaysWorkedTogether(LocalDate employee1DateFrom, LocalDate employee1DateTo,
                                                    LocalDate employee2DateFrom, LocalDate employee2DateTo) {
        Long daysWorkedTogether = null;
        if (employee1DateFrom.isBefore(employee2DateFrom)) {
            if (employee2DateFrom.isAfter(employee1DateTo)) {
                return null;
            }
            if (employee1DateTo.isBefore(employee2DateTo)) {
                daysWorkedTogether = ChronoUnit.DAYS.between(employee2DateFrom, employee1DateTo);
            } else {
                daysWorkedTogether = ChronoUnit.DAYS.between(employee2DateFrom, employee2DateTo);
            }
        } else if (employee1DateFrom.isAfter(employee2DateFrom)) {
            if (employee1DateFrom.isAfter(employee2DateTo)) {
                return null;
            }
            if (employee1DateTo.isBefore(employee2DateTo)) {
                daysWorkedTogether = ChronoUnit.DAYS.between(employee1DateFrom, employee1DateTo);
            } else {
                daysWorkedTogether = ChronoUnit.DAYS.between(employee1DateFrom, employee2DateTo);
            }
        } else if (employee1DateFrom.isEqual(employee2DateFrom)) {
            if (employee1DateTo.isBefore(employee2DateTo)) {
                daysWorkedTogether = ChronoUnit.DAYS.between(employee1DateFrom, employee1DateTo);
            } else {
                daysWorkedTogether = ChronoUnit.DAYS.between(employee1DateFrom, employee2DateTo);
            }
        }

        return daysWorkedTogether;
    }
}
