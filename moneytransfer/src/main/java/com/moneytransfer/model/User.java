package com.moneytransfer.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@lombok.Builder
public class User {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
}