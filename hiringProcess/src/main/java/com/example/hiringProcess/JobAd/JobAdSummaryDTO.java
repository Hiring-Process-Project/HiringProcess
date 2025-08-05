package com.example.hiringProcess.JobAd;

public class JobAdSummaryDTO {
    private String jobTitle;
    private String occupationName;
    private String departmentName;

    public JobAdSummaryDTO(String jobTitle, String occupationName, String departmentName) {
        this.jobTitle = jobTitle;
        this.occupationName = occupationName;
        this.departmentName = departmentName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getOccupationName() {
        return occupationName;
    }

    public String getDepartmentName() {
        return departmentName;
    }
}
