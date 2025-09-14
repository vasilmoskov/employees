package com.example.xml;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "Employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeeXml {

    @XmlElement(name = "EmpID")
    private Long id;

    @XmlElement(name = "ProjectID")
    private Long projectId;

    @XmlElement(name = "DateFrom")
    private String dateFrom;

    @XmlElement(name = "DateTo")
    private String dateTo;

    public Long getId() {
        return id;
    }

    public EmployeeXml setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getProjectId() {
        return projectId;
    }

    public EmployeeXml setProjectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public EmployeeXml setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public String getDateTo() {
        return dateTo;
    }

    public EmployeeXml setDateTo(String dateTo) {
        this.dateTo = dateTo;
        return this;
    }
}
