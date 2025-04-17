package com.lab.server.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lab.lib.api.PaginationResponse;
import com.lab.lib.repository.BaseRepository;
import com.lab.lib.service.BaseService;
import com.lab.lib.utils.PagingUtil;
import com.lab.server.configs.language.MessageSourceHelper;
import com.lab.server.entities.Department;
import com.lab.server.payload.department.DepartmentRequest;
import com.lab.server.payload.department.DepartmentResponse;
import com.lab.server.repositories.DepartmentRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class DepartmentService extends BaseService<Department, Integer>{
	
	private final DepartmentRepository repository;
	private final MessageSourceHelper messageHelper;

	protected DepartmentService(BaseRepository<Department, Integer> repository, MessageSourceHelper messageHelper) {
		super(repository);
		this.repository = (DepartmentRepository) repository;
		this.messageHelper = messageHelper;
	}
	
	@Transactional(readOnly = true)
	public PaginationResponse<DepartmentResponse> getAllDepartmentWithConditions(int page, int perpage, String search){
		long totalRecord = repository.countAllDepartmentWithConditions(search);
		int offset = PagingUtil.getOffset(page, perpage);
		int totalPage = PagingUtil.getTotalPage(totalRecord, perpage);
		List<Department> departmentList = repository.findAllDepartmentWithConditions(offset, perpage, search);
		List<DepartmentResponse> response = new ArrayList<>();
		if(departmentList != null) {
			response = departmentList.stream().map(department -> responseBuilder(department)).toList();
		}
		return PaginationResponse.<DepartmentResponse>builder()
				.page(page)
				.perPage(perpage) 
				.data(response)
				.totalPage(totalPage)
				.totalRecord(totalRecord)
				.build();
	}
	
	@Transactional(readOnly = true)
	public DepartmentResponse getDepartmentById(int id) {
		Department department = findByFields(Map.of("departmentId", id));
		if(department == null) return new DepartmentResponse();
		return responseBuilder(department);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public DepartmentResponse createDepartment(DepartmentRequest request) {
		Department department = entityBuilder(request);
		department = save(department);
		return responseBuilder(department);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public DepartmentResponse saveDepartment(int id, DepartmentRequest request) throws Exception {
		Department department = findByFields(Map.of("departmentId", id));
		department.setDepartmentName(request.getDepartmentName());
		department.setDescription(request.getDescription());
		department = save(department);
		return responseBuilder(department);
	}
	
	public void deleteDepartment(int id) throws Exception {
		try {
			Department department = findByFields(Map.of("departmentId", id));
			repository.delete(department);
		} catch(Exception e) {
			log.error(messageHelper.getMessage("error.data.notfound"));
			throw new Exception("error.data.notfound");
		}
	}
	
	private final Department entityBuilder(DepartmentRequest request) {
		return Department.builder()
				.departmentName(request.getDepartmentName())
				.description(request.getDescription())
				.build();
	}
	
	private DepartmentResponse responseBuilder(Department department) {
		return DepartmentResponse.builder()
				.departmentId(department.getDepartmentId())
				.departmentName(department.getDepartmentName())
				.description(department.getDescription())
				.build();
	}

}
