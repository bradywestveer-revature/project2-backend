package com.revature.project2backend.jsonmodels;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CreateSessionDTO {
    String identifier;
    String password;
}
