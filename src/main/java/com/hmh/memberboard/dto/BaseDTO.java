package com.hmh.memberboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseDTO {
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

