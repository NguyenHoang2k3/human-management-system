package com.lab.server.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lab.lib.api.ApiResponse;
import com.lab.lib.api.PaginationResponse;
import com.lab.lib.utils.PagingUtil;
import com.lab.server.payload.department.DepartmentRequest;
import com.lab.server.payload.department.DepartmentResponse;
import com.lab.server.services.DepartmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/departments")
public class DepartmentController {
	
	private final DepartmentService departmentService;
	
	@GetMapping("/{id}")
	public ApiResponse<DepartmentResponse> getDepartmentById(@PathVariable("id") int id){
		DepartmentResponse response = departmentService.getDepartmentById(id);
		return new ApiResponse<DepartmentResponse>(true, response);
	}
	
	@GetMapping
	public PaginationResponse<DepartmentResponse> getAllDepartmentWithConditions(
			@RequestParam(required = false, defaultValue = PagingUtil.DEFAULT_PAGE) int page,
			@RequestParam(required = false, defaultValue = PagingUtil.DEFAULT_SIZE) int perpage, 
			@RequestParam(required = false, defaultValue = StringUtils.EMPTY) String search){
		return departmentService.getAllDepartmentWithConditions(page, perpage, search);
	}
	
	@PostMapping
	public ApiResponse<DepartmentResponse> createDepartment(@Valid @RequestBody DepartmentRequest request){
		DepartmentResponse response = departmentService.createDepartment(request);
		return new ApiResponse<DepartmentResponse>(true, response);
	}
	
	@PutMapping("/{id}")
	public ApiResponse<DepartmentResponse> createDepartment(@PathVariable("id") int id, @Valid @RequestBody DepartmentRequest request) throws Exception {
		DepartmentResponse response = departmentService.saveDepartment(id, request);
		return new ApiResponse<DepartmentResponse>(true, response);
	}
	
	@DeleteMapping("/{id}")
	public ApiResponse<String> deleteDepartment(@PathVariable("id") int id) throws Exception {
		departmentService.deleteDepartment(id);
		return new ApiResponse<String>(true);
	}
	
}
