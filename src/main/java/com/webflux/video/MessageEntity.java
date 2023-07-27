package com.webflux.video;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "tbl_message")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {


    @Id
    private String id;


    private String roomNumber;

    private String content;


    private String sessionId;

    private String username;


    private LocalDateTime createDate;


    private LocalDateTime updateDate;

    private LocalDateTime deleteDate;

    private String isDeleted;

}