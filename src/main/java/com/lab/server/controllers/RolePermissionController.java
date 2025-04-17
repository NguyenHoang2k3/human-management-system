package com.lab.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lab.server.payload.rolepermission.RolePermissionRequest;
import com.lab.server.services.RolePermissionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
@Tag(name = "role-permission")
public class RolePermissionController {
	private final RolePermissionService rolePermissionService;
	
	@Operation(summary = "API get all role-permissions")
	@GetMapping("/role-permissions")
	public ResponseEntity<?> getAllRolePermissions() {
		return ResponseEntity.ok(rolePermissionService.getAllRolePermission());
	}
	
	@Operation(summary = "API find role-permissions by roleId, permissionId")
	@GetMapping("/role-permissions/{roleId}/{permissionId}")
	public ResponseEntity<?> findRolePermission(@PathVariable int roleId, @PathVariable int permissionId) {
		return ResponseEntity.ok(rolePermissionService.geRolePermission(new RolePermissionRequest(roleId, permissionId)));
	}
	
	@Operation(summary = "API find permissions by roleId")
	@GetMapping("/roles/{roleId}/permissions")
	public ResponseEntity<?> findPermissionsByRoleId(@PathVariable int roleId) {
		return ResponseEntity.ok(rolePermissionService.getPermissionsByRoleId(roleId));
	}
	
	@Operation(summary = "API find roles by permissions")
	@GetMapping("/permissions/{permissionId}/roles")
	public ResponseEntity<?> findRolesByPermissionId(@PathVariable int permissionId) {
		return ResponseEntity.ok(rolePermissionService.getRolesByPermissionId(permissionId));
	}
	
	@Operation(summary = "API create permission for role")
	@PostMapping("/role-permissions")
	public ResponseEntity<?> createRolePermission(@RequestBody RolePermissionRequest request) {
		return ResponseEntity.ok(rolePermissionService.createRolePermission(request));
	}
	
	@Operation(summary = "API delete permission for role")
	@DeleteMapping("/role-permissions/{roleId}/{permissionId}")
	public ResponseEntity<?> deleteRolePermission(@PathVariable int roleId, @PathVariable int permissionId) {
		rolePermissionService.deleteRolePermission(new RolePermissionRequest(roleId, permissionId));
		return ResponseEntity.ok("RolePermission deleted successfully!");
	}
}
