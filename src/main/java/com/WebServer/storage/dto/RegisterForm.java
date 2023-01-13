package com.WebServer.storage.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RegisterForm {
    private String name;
    private String email;
    private String password;
    private String gender;
    private String address;
    private Boolean married;
    private String profession;
}
