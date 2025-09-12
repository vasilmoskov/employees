package com.example.filereader;

public class EmployeesCommonProject {
    private Long emp1Id;
    private Long emp2Id;
    private Long projectId;
    private Long daysWorkedTogether;

    public EmployeesCommonProject(Long emp1Id, Long emp2Id, Long projectId, Long daysWorkedTogether) {
        this.emp1Id = emp1Id;
        this.emp2Id = emp2Id;
        this.projectId = projectId;
        this.daysWorkedTogether = daysWorkedTogether;
    }

    public Long getEmp1Id() {
        return emp1Id;
    }

    public EmployeesCommonProject setEmp1Id(Long emp1Id) {
        this.emp1Id = emp1Id;
        return this;
    }

    public Long getEmp2Id() {
        return emp2Id;
    }

    public EmployeesCommonProject setEmp2Id(Long emp2Id) {
        this.emp2Id = emp2Id;
        return this;
    }

    public Long getProjectId() {
        return projectId;
    }

    public EmployeesCommonProject setProjectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public Long getDaysWorkedTogether() {
        return daysWorkedTogether;
    }

    public EmployeesCommonProject setDaysWorkedTogether(Long daysWorkedTogether) {
        this.daysWorkedTogether = daysWorkedTogether;
        return this;
    }
}
