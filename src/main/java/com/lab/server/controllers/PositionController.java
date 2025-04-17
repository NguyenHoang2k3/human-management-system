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
import com.lab.server.payload.position.PositionRequest;
import com.lab.server.payload.position.PositionResponse;
import com.lab.server.services.PositionService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/positions")
public class PositionController {

	private final PositionService positionService;
	
	@GetMapping("/{id}")
	public ApiResponse<PositionResponse> getPositionById(@PathVariable("id") int id){
		PositionResponse response = positionService.getPositionById(id);
		return new ApiResponse<PositionResponse>(true, response);
	}
	
	@GetMapping
	public PaginationResponse<PositionResponse> getAllPositionWithConditions(
			@RequestParam(required = false, defaultValue = PagingUtil.DEFAULT_PAGE) int page,
			@RequestParam(required = false, defaultValue = PagingUtil.DEFAULT_SIZE) int perpage, 
			@RequestParam(required = false, defaultValue = StringUtils.EMPTY) String search){
		return positionService.getPositionWithConditions(page, perpage, search);
	}
	
	@PostMapping
	public ApiResponse<PositionResponse> createPosition(@RequestBody PositionRequest request){
//		request.setPositionName("Developer");
		System.out.println(request.getPositionName());
		PositionResponse response = positionService.createPosition(request);
		return new ApiResponse<PositionResponse>(true, response);
	}
	
	@PutMapping("/{id}")
	public ApiResponse<PositionResponse> updatePosition(@PathVariable("id") int id, @Valid @RequestBody PositionRequest request) throws Exception {
		PositionResponse response = positionService.savePosition(id, request);
		return new ApiResponse<PositionResponse>(true, response);
	}
	
	@DeleteMapping("/{id}")
	public ApiResponse<String> deletePosition(@PathVariable("id") int id) throws Exception {
		positionService.deletePosition(id);
		return new ApiResponse<String>(true);
	}
}
