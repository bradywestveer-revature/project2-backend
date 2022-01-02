package com.revature.project2backend.jsonmodels;

import lombok.Data;

@Data
public class CreateSessionBody {
    String identifier;
    String password;
}
