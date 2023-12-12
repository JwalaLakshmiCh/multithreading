package com.jwala.learning.multithreading.service;

import com.jwala.learning.multithreading.entity.User;
import com.jwala.learning.multithreading.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    Logger log = LoggerFactory.getLogger(UserService.class);
    Object target;

    @Autowired
    private UserRepository repository;

    @Async
    public CompletableFuture<List<User>> saveUsers(MultipartFile file) throws Exception {
        long start = System.currentTimeMillis();
        List<User> users = parseCSV(file);
        log.info("Save the users from the file of size {}", users.size(), "" + Thread.currentThread().getName());
        users = repository.saveAll(users);
        long end = System.currentTimeMillis();
        log.info("Total amount of time to execute {}", (end - start));

        return CompletableFuture.completedFuture(users);
    }

    @Async
    public CompletableFuture<List<User>> findAllUsers(){
        log.info("Fetch the users thread "+Thread.currentThread().getName());
        List<User> users=repository.findAll();

        return CompletableFuture.completedFuture(users);
    }


    private List<User> parseCSV(final MultipartFile file) throws Exception {
        final List<User> users = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split(",");
                    final User user = new User();
                    user.setName(data[0]);
                    user.setEmail(data[1]);
                    user.setGender(data[2]);
                    users.add(user);
                }
                return users;
            }
        } catch (final IOException e) {
            log.error("Failed to parse CSV file {}", e);
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }

}
