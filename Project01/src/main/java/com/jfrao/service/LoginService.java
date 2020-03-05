package com.jfrao.service;

import com.jfrao.domain.User;

public interface LoginService {
    User login(String username, String password);
}
