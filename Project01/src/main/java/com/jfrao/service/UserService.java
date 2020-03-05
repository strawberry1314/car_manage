package com.jfrao.service;

import com.jfrao.domain.User;

import java.util.List;

public interface UserService {


    User login(String username, String password);

    List<User> getAll();

    User getOneById(String id);

    void insert(User user);

    void delete(String id);

    void update(User user);
}
