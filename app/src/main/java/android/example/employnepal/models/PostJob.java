package android.example.employnepal.models;

public class PostJob {
    String jobPostDate;
    String jobTitle;
    String jobDescription;
    String numberOfEmployees;
    String jobLocation;
    String jobCategory;
    String approximateWorkingHours;
    String jobSalary;
    String applyBefore;
    String contactInformation;

    public PostJob(String jobPostDate, String jobTitle, String jobDescription, String numberOfEmployees, String jobLocation, String jobCategory, String approximateWorkingHours, String jobSalary, String applyBefore, String contactInformation) {
        this.jobPostDate = jobPostDate;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.numberOfEmployees = numberOfEmployees;
        this.jobLocation = jobLocation;
        this.jobCategory = jobCategory;
        this.approximateWorkingHours = approximateWorkingHours;
        this.jobSalary = jobSalary;
        this.applyBefore = applyBefore;
        this.contactInformation = contactInformation;
    }

    public PostJob() {

    }

    public String getJobPostDate() {
        return jobPostDate;
    }

    public void setJobPostDate(String jobPostDate) {
        this.jobPostDate = jobPostDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(String numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
    }

    public String getApproximateWorkingHours() {
        return approximateWorkingHours;
    }

    public void setApproximateWorkingHours(String approximateWorkingHours) {
        this.approximateWorkingHours = approximateWorkingHours;
    }

    public String getJobSalary() {
        return jobSalary;
    }

    public void setJobSalary(String jobSalary) {
        this.jobSalary = jobSalary;
    }

    public String getApplyBefore() {
        return applyBefore;
    }

    public void setApplyBefore(String applyBefore) {
        this.applyBefore = applyBefore;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }
}