package com.jfrao.domain;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "ds_user")
public class User {

    @Id
    private String userid;
    private String username;
    private String password;
    private String role;
    private Integer status;
}
