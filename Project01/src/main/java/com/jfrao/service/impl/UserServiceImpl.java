package com.jfrao.service.impl;

import com.jfrao.domain.User;
import com.jfrao.mapper.UserMapper;
import com.jfrao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getAll() {
        User user = new User();
        user.setRole("user");
        List<User> users = userMapper.select(user);
        return users;
    }

    @Override
    public User getOneById(String id){
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void insert(User user) {
        userMapper.insert(user);
    }

    @Override
    public void delete(String id) {
        userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(User user){
        userMapper.updateByPrimaryKey(user);
    }
}
