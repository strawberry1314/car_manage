package com.jfrao.mapper;

import com.jfrao.domain.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {

    public User login(@Param(value = "username") String username, @Param(value = "password") String password);

}
