package com.jwala.learning.multithreading.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwala.learning.multithreading.entity.Employee;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/*
* Use case is to read from a json file and store data in database. No need to return anything.
* */
public class RunAsyncDemo {

    //runAsync method uses Runnable as input and returns Void.
    public Void saveEmployees(File jsonFile) throws ExecutionException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        CompletableFuture<Void> runAsyncFuture = CompletableFuture.runAsync(new Runnable() {

            @Override
            public void run() {
                try {
                    List<Employee> employees = mapper
                            .readValue(jsonFile, new TypeReference<List<Employee>>() {
                            });
                    //write logic t save list of employee to database
                    //repository.saveAll(employees);
                    System.out.println("Thread : " + Thread.currentThread().getName());
                    System.out.println(employees.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return runAsyncFuture.get();
    }

//Using runAsync overloaded method (Runnable, Executor) method.
    public Void saveEmployeesWithCustomExecutor(File jsonFile) throws ExecutionException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        Executor executor = Executors.newFixedThreadPool(5);
        CompletableFuture<Void> runAsyncFuture = CompletableFuture.runAsync(
                () -> {
                    try {
                        List<Employee> employees = mapper
                                .readValue(jsonFile, new TypeReference<List<Employee>>() {
                                });
                        //write logic t save list of employee to database
                        //repository.saveAll(employees);
                        System.out.println("Thread : " + Thread.currentThread().getName());
                        System.out.println(employees.size());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, executor);

        return runAsyncFuture.get();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RunAsyncDemo runAsyncDemo = new RunAsyncDemo();
        runAsyncDemo.saveEmployees(new File("src/main/resources/Employees.json"));
        runAsyncDemo.saveEmployeesWithCustomExecutor(new File("src/main/resources/Employees.json"));

    }
}