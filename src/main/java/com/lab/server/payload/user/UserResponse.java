package com.lab.server.payload.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserResponse {
	private int userId;
    private String username;	
    private String email;
    private String roleName; 
}