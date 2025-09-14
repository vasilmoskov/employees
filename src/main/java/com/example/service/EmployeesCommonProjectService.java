package com.example.service;

import com.example.model.EmployeesCommonProject;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class EmployeesCommonProjectService {
    public List<EmployeesCommonProject> processFile(byte[] fileBytes, String fileType) throws IOException {

        String extension = ".csv";

        if (fileType != null && fileType.equalsIgnoreCase("xml")) {
            extension = ".xml";
        }

        File tempFile = File.createTempFile("uploaded_employees", extension);

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(fileBytes);
        }

        if ("xml".equalsIgnoreCase(fileType)) {
            return EmployeesCommonProjectXmlExtractor.extractFromFile(tempFile.getAbsolutePath());
        } else {
            return EmployeesCommonProjectCsvExtractor.extractFromFile(tempFile.getAbsolutePath());
        }
    }
}
