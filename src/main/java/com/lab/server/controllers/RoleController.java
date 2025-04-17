package com.lab.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lab.server.configs.language.MessageSourceHelper;
import com.lab.server.payload.role.RoleRequest;
import com.lab.server.services.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
@Tag(name = "role")
public class RoleController {

	private final RoleService roleService;
	private final MessageSourceHelper helper;
	
	@Operation(summary = "API get all roles")
	@PreAuthorize("hasAuthority('read_role')")
	@GetMapping("")
	public ResponseEntity<?> getAllRoles() {
		return ResponseEntity.ok(roleService.findAll());
	}
	
	@Operation(summary = "API get role by id")
	@GetMapping("/{id}")
	public ResponseEntity<?> getRoleById(@PathVariable int id) {
		return ResponseEntity.ok(roleService.findRoleById(id));
	}
	
	@Operation(summary = "API create new role")
	@PostMapping("")
	public ResponseEntity<?> createRole(@RequestBody RoleRequest request) {
		return ResponseEntity.ok(roleService.createRole(request));
	}
	
	@Operation(summary = "API update role by id")
	@PutMapping("/{id}")
	public ResponseEntity<?> updateRoleById(@PathVariable int id, @RequestBody RoleRequest request) {
		return ResponseEntity.ok(roleService.updateRoleById(id,request));
	}
	
	@Operation(summary = "API delete role by id")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteRoleById(@PathVariable int id) {
		return ResponseEntity.ok(roleService.deleteRoleById(id));
	}
	
}
