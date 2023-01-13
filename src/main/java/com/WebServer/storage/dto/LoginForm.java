package com.WebServer.storage.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginForm {
    private String username;
    private String password;
}
