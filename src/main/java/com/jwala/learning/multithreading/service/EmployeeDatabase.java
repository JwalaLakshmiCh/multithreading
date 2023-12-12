package com.jwala.learning.multithreading.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwala.learning.multithreading.entity.Employee;

import java.io.File;
import java.io.IOException;
import java.util.List;

/*
* Added this class instead of fetching the records from Database.
 */
public class EmployeeDatabase {

        public static List<Employee> fetchEmployees() {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper
                        .readValue(new File("src/main/resources/Employees.json"), new TypeReference<List<Employee>>() {
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
}

