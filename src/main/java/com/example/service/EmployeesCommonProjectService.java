package com.example.service;

import com.example.model.EmployeesCommonProject;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class EmployeesCommonProjectService {
    public List<EmployeesCommonProject> processFile(byte[] fileBytes) throws IOException {
        File tempFile = File.createTempFile("uploaded_employees", ".csv");

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(fileBytes);
        }

        return EmployeesCommonProjectCsvExtractor.extractFromFile(tempFile.getAbsolutePath());
    }
}
