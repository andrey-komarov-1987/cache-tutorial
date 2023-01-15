package com.example.cachetutorial.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LockStatDto {
    long wait;
    long duration;
    boolean interrupted;
}
