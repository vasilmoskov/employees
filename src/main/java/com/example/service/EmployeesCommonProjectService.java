package com.example.service;

import com.example.extractor.EmployeesCommonProjectCsvExtractor;
import com.example.extractor.EmployeesCommonProjectXmlExtractor;
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
    public List<EmployeesCommonProject> processFile(byte[] fileBytes, String fileType) throws IOException {

        File tempFile = File.createTempFile("uploaded_employees.", fileType);

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(fileBytes);
        }

        if ("xml".equalsIgnoreCase(fileType)) {
            return new EmployeesCommonProjectXmlExtractor().extractFromFile(tempFile.getAbsolutePath());
        } else {
            return new EmployeesCommonProjectCsvExtractor().extractFromFile(tempFile.getAbsolutePath());
        }
    }
}
