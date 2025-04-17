package com.lab.server.payload.role;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleRequest {
	@NotBlank
	private String name;
	@NonNull
	private String description;
}
