package com.example.filereader;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class EmployeeCollaborationService {
    public List<EmployeesCommonProject> processCsv(byte[] fileBytes) throws IOException {
        File tempFile = File.createTempFile("uploaded_employees", ".csv");

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(fileBytes);
        }

        return CSVFileReader.processFile(tempFile.getAbsolutePath());
    }
}
