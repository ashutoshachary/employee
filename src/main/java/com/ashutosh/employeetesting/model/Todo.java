package com.ashutosh.employeetesting.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


//@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Document(collection = "todos")
public class Todo {
	

	@Id
    private String id;
    
    @NotBlank(message = "Task name is required")
    private String taskName;
    
    @NotNull(message = "Date is required")
    private LocalDate date;
    
    private boolean completed;
    
    @NotBlank(message = "Employee ID is required")
    private String employeeId;
    
    public Todo()
	{	
		
	}
    public Todo(String id, @NotBlank(message = "Task name is required") String taskName,
			@NotNull(message = "Date is required") LocalDate date, boolean completed,
			@NotBlank(message = "Employee ID is required") String employeeId) {
		super();
		this.id = id;
		this.taskName = taskName;
		this.date = date;
		this.completed = completed;
		this.employeeId = employeeId;
	}
    
    

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	@Override
	public String toString() {
		return "Todo [id=" + id + ", taskName=" + taskName + ", date=" + date + ", completed=" + completed
				+ ", employeeId=" + employeeId + "]";
	}
    
}
