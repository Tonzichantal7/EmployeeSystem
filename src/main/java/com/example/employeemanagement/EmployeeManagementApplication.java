package com.example.employeemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmployeeManagementApplication {

	public static void main(String[] args) {
		String dbUrl = System.getenv("DB_URL");
		if (dbUrl != null && !dbUrl.startsWith("jdbc:")) {
			if (dbUrl.startsWith("postgres://")) {
				dbUrl = dbUrl.replace("postgres://", "jdbc:postgresql://");
			} else if (dbUrl.startsWith("postgresql://")) {
				dbUrl = "jdbc:" + dbUrl;
			}
			if (dbUrl.contains("@dpg-") && !dbUrl.contains(".onrender.com")) {
				dbUrl = dbUrl.replaceAll("@(dpg-[a-z0-9-]+)", "@$1.oregon-postgres.render.com");
			}
			System.setProperty("DB_URL", dbUrl);
		}
		SpringApplication.run(EmployeeManagementApplication.class, args);
	}

}
