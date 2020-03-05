package com.jfrao.service;

import com.jfrao.domain.User;
import com.jfrao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        User user = userMapper.login(username, password);
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = userMapper.selectAll();
        return users;
    }

    @Override
    public void insert(User user) {
        userMapper.insert(user);
    }

    @Override
    public void delete(String id) {
        userMapper.deleteByPrimaryKey(id);
    }
}
