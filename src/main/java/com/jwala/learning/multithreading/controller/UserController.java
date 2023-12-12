package com.jwala.learning.multithreading.controller;

import com.jwala.learning.multithreading.entity.User;
import com.jwala.learning.multithreading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping(value = "/users", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity saveUsers(@RequestParam(value = "files") MultipartFile[] files) throws Exception {
        for (MultipartFile file : files) {
            service.saveUsers(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/users", produces = "application/json")
    public CompletableFuture<ResponseEntity> findAllUsers() {
        return  service.findAllUsers().thenApply(ResponseEntity::ok);
    }

    //Calling 3 different threads
    //allOf shall wait until all 3 threads are executed and then joins
    @GetMapping(value = "/getUsersByThread", produces = "application/json")
    public  ResponseEntity getUsers(){
        CompletableFuture<List<User>> user_1=service.findAllUsers();
        CompletableFuture<List<User>> user_2=service.findAllUsers();
        CompletableFuture<List<User>> user_3=service.findAllUsers();
        CompletableFuture.allOf(user_1,user_2,user_3).join();

        return  ResponseEntity.status(HttpStatus.OK).build();
    }
}

