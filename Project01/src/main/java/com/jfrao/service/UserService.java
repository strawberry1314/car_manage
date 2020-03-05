package com.jfrao.service;

import com.jfrao.domain.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getOneById(String id);

    void insert(User user);

    void delete(String id);

    void update(User user);
}
