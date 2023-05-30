package com.ptit.spotify.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusError {
    private int return_code;
    private String return_message;
    private int sub_return_code;
    private String sub_return_message;
}