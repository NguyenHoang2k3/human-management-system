package com.lab.server.payload.rolepermission;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RolePermissionRequest {
	@NotBlank
	private int roleId;
	@NotBlank
	private int permissionId;
}
