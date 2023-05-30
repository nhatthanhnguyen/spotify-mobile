package com.ptit.spotify.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private int user_id;
    private String username;
    private String email;
    private String code;
    private String role;
    private String status;
}