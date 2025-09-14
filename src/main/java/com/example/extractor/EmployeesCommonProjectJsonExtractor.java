package com.example.extractor;

import com.example.model.Employee;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeesCommonProjectJsonExtractor extends EmployeesCommonProjectExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeesCommonProjectJsonExtractor.class);
    private static final Gson GSON = new Gson();

    @Override
    protected Map<Long, List<Employee>> extractProjectIdsToEmployeesFromFile(String fileName) {
        Map<Long, List<Employee>> projectIDsToEmployees = new HashMap<>();

        try (FileReader reader = new FileReader(fileName)) {

            JsonArray jsonArray = GSON.fromJson(reader, JsonArray.class);

            for (JsonElement jsonElement : jsonArray) {
                if (!jsonElement.isJsonObject()) {
                    continue;
                }

                JsonObject jsonObject = jsonElement.getAsJsonObject();

                Long employeeId = jsonObject.get("EmpID").getAsLong();
                Long projectId = jsonObject.get("ProjectID").getAsLong();
                String dateFromAsString = jsonObject.get("DateFrom").getAsString();
                String dateToAsString = jsonObject.get("DateTo").getAsString();

                populateProjectsToEmployess(dateFromAsString, dateToAsString, employeeId, projectId, projectIDsToEmployees);
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("No such file.", e);
        } catch (Exception e) {
            LOGGER.error("An error has occurred during JSON file processing.", e);
        }

        return projectIDsToEmployees;
    }
}
