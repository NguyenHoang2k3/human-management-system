package com.lab.server.payload.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RoleResponse {
	private int id;
	private String name;
	private String description;
}
