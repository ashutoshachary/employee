package com.ashutosh.employeetesting.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "employees")
public class Employee {
    @Id
    private String id;

    @NotBlank(message = "Employee name is required")
    private String employeeName;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    @Indexed(unique = true)
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    private String photoUrl;

    @NotNull(message = "Department is required")
    private Department department;

    private String resumeUrl;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Gender is required")
    private Boolean isMale;

    @NotBlank(message = "Password is required")
    private String password;

    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "Date of birth is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private List<String> checkBoxOptions = new ArrayList<>();

    // Default constructor
    public Employee() {
    }

    // All-args constructor
    public Employee(String id, 
                   String employeeName,
                   String email, 
                   String phoneNumber,
                   String photoUrl,
                   Department department,
                   String resumeUrl,
                   String address,
                   Boolean isMale,
                   String password,
                   LocalDate dateOfBirth,
                   List<String> checkBoxOptions) {
        this.id = id;
        this.employeeName = employeeName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.photoUrl = photoUrl;
        this.department = department;
        this.resumeUrl = resumeUrl;
        this.address = address;
        this.isMale = isMale;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.checkBoxOptions = checkBoxOptions != null ? checkBoxOptions : new ArrayList<>();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsMale() {
        return isMale;
    }

    public void setIsMale(Boolean isMale) {
        this.isMale = isMale;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<String> getCheckBoxOptions() {
        return checkBoxOptions;
    }

    public void setCheckBoxOptions(List<String> checkBoxOptions) {
        this.checkBoxOptions = checkBoxOptions != null ? checkBoxOptions : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", department=" + department +
                ", resumeUrl='" + resumeUrl + '\'' +
                ", address='" + address + '\'' +
                ", isMale=" + isMale +
                ", password='[PROTECTED]'" + // Don't expose password in toString
                ", dateOfBirth=" + dateOfBirth +
                ", checkBoxOptions=" + checkBoxOptions +
                '}';
    }
}