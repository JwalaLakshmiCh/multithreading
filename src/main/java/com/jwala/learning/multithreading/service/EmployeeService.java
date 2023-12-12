package com.jwala.learning.multithreading.service;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import com.jwala.learning.multithreading.entity.Employee;

/*
*The below is a use case to send a reminder emails to all new employees who need to complete their trainings
* Thread - 1 : to fetch all employees
* Thread - 2 : to fetch New Joined Employees
* Thread - 3 : to filter the employees who did not complete their trainings
* Thread - 4 : to get their email IDs
* Thread - 5 : To send them the emails.
 */
public class EmployeeService {

    //To understand thenApply(Function), thenApplyAsync(Function), thenApplyAsync(Function, Executor)
    //thenAccept(Consumer), thenAcceptAsync(Consumer), thenAcceptAsync(Consumer, Executor)
    //thenRun(Runnable)
    public  CompletableFuture<Void> sendReminderToEmployee() {

        Executor executor=Executors.newFixedThreadPool(5);

        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("GetEmployeeList : " + Thread.currentThread().getName());
            return EmployeeDatabase.fetchEmployees();
        },executor).thenApplyAsync((employees) -> {
            System.out.println("filter out new joiner employee  : " + Thread.currentThread().getName());
            return employees.stream()
                    .filter(employee -> "TRUE".equals(employee.getNewJoiner()))
                    .collect(Collectors.toList());
        },executor).thenApplyAsync((employees) -> {
            System.out.println("filter employees who did not complete their trainings  : " + Thread.currentThread().getName());
            return employees.stream()
                    .filter(employee -> "TRUE".equals(employee.getLearningPending()))
                    .collect(Collectors.toList());
        },executor).thenApplyAsync((employees) -> {
            System.out.println("Fetch the list of employee emails  : " + Thread.currentThread().getName());
            return employees.stream().map(Employee::getEmail).collect(Collectors.toList());
        },executor).thenAcceptAsync((emails) -> {
            System.out.println("send email to : " + Thread.currentThread().getName());
            emails.forEach(EmployeeService::sendEmail);
        },executor);
        return voidCompletableFuture;
    }


    public static void sendEmail(String email) {
        System.out.println("Sending training reminder mail to : " + email);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        EmployeeService service=new EmployeeService();
        service.sendReminderToEmployee().get();
    }
}
