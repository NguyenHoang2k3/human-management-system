package com.lab.server.payload.department;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequest {
	
	@NotNull
	private String departmentName;
	
	@NotNull
	private String description;
	
}
