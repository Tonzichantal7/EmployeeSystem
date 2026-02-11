package com.example.employeemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmployeeManagementApplication {

	public static void main(String[] args) {
		String dbUrl = System.getenv("DB_URL");
		if (dbUrl != null && dbUrl.startsWith("postgresql://")) {
			System.setProperty("DB_URL", "jdbc:" + dbUrl);
		}
		SpringApplication.run(EmployeeManagementApplication.class, args);
	}

}
