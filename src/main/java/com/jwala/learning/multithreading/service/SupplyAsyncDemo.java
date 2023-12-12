package com.jwala.learning.multithreading.service;

import com.jwala.learning.multithreading.entity.Employee;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
/*
*Here the use case developed is to fetch the records from Employee Database and display it.
* */
public class SupplyAsyncDemo {

    //Visualized the changes threads used from fork join pool and custom executor pool.
    //supplyAsync - takes Supplier as parameter and returns something
    //it has another overloaded method (Supplier,  executor) - which is the use case below
    public List<Employee> getEmployees() throws ExecutionException, InterruptedException {
        Executor executor = Executors.newCachedThreadPool();
        CompletableFuture<List<Employee>> listCompletableFuture = CompletableFuture
                .supplyAsync(() -> {
                    System.out.println("Executed by : " + Thread.currentThread().getName());
                    return EmployeeDatabase.fetchEmployees();
                },executor);
        return listCompletableFuture.get();
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SupplyAsyncDemo supplyAsyncDemo = new SupplyAsyncDemo();
        List<Employee> employees = supplyAsyncDemo.getEmployees();
        employees.stream().forEach(System.out::println);
    }
}
