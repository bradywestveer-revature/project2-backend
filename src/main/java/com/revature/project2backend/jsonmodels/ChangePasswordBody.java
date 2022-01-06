package com.revature.project2backend.jsonmodels;

import lombok.Data;

@Data
public class ChangePasswordBody {   // Change the password using our generated token based on Forgot Password screen
    String token;
    String password;
}
