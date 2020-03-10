package com.jfrao.domain;


import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name="SEQS_IN_QUEUE")
public class User_In_Queue {
    private String username;
    private String ip;
    private String operation;
    private String operate_time;
    private String task_seq;
}
