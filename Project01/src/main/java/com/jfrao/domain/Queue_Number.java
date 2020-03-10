package com.jfrao.domain;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "queue_number")
@Data
public class Queue_Number {

    @Id
    private String num_id;
    private String num;
    private Date create_time;
    private Date call_time;
    private String isovertime;
    private Date start_time;

}
