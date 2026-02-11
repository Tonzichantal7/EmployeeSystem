package com.example.employeemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmployeeManagementApplication {

	public static void main(String[] args) {
		String dbUrl = System.getenv("DATABASE_URL");
		if (dbUrl != null && !dbUrl.startsWith("jdbc:")) {
			if (dbUrl.startsWith("postgres://")) {
				dbUrl = dbUrl.replace("postgres://", "jdbc:postgresql://");
			} else if (dbUrl.startsWith("postgresql://")) {
				dbUrl = "jdbc:" + dbUrl;
			}
			System.setProperty("DB_URL", dbUrl);
		}
		SpringApplication.run(EmployeeManagementApplication.class, args);
	}

}
