package com.lab.server.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lab.lib.api.PaginationResponse;
import com.lab.lib.enumerated.SystemRole;
import com.lab.lib.repository.BaseRepository;
import com.lab.lib.service.BaseService;
import com.lab.lib.utils.PagingUtil;
import com.lab.server.entities.Role;
import com.lab.server.payload.role.RoleRequest;
import com.lab.server.payload.role.RoleResponse;
import com.lab.server.repositories.RoleRepository;


@Service
@Transactional
public class RoleService extends BaseService<Role, Integer> {

    private final RoleRepository repository;    

    protected RoleService(BaseRepository<Role, Integer> repository) {
        super(repository);
        this.repository = (RoleRepository) repository;
    }

	public PaginationResponse<RoleResponse> findRoleWithConditions(int page, int perpage, String search) {
		long totalRecord = repository.countAllBySearch(search);
		int offset = PagingUtil.getOffset(page, perpage);
		int totalPage = PagingUtil.getTotalPage(totalRecord, perpage);
		List<Role> roleList = repository.findAllRolesWithConditions(offset, perpage, search);
		List<RoleResponse> resultList = new ArrayList<>();
		if(roleList!=null) {
			for(Role role: roleList) {
				resultList.add(RoleResponse.builder()
						.id(role.getRoleId())
						.name(role.getRoleName().name())
						.description(role.getDescription())
						.build());
			}
		}
		return PaginationResponse.<RoleResponse>builder()
				.page(page)
				.perPage(perpage)
				.data(resultList)
				.totalRecord(totalRecord)
				.totalPage(totalPage)	
				.build();
	}
	
	@Transactional(readOnly = true)
	@SuppressWarnings("null")
	public List<RoleResponse> findAll(){
		List<Role> roleList= repository.findAll();
		List<RoleResponse> roleResponses = new ArrayList<>();
		roleList.forEach(r -> roleResponses.add(new RoleResponse(r.getRoleId(),r.getRoleName().toString(), r.getDescription())));
		return roleResponses;
	}
	
	@Transactional(readOnly = true)
	public RoleResponse findRoleById(int id) {
		Role role= repository.findById(id).orElse(null);
		return new RoleResponse(role.getRoleId(),role.getRoleName().toString(), role.getDescription());
	}
	
	public RoleResponse createRole(RoleRequest request) {
		Role role = new Role();
		role.setRoleName(SystemRole.fromStringToEnum(request.getName()));
		role.setDescription(request.getDescription());
		repository.save(role);
		return new RoleResponse(role.getRoleId(),role.getRoleName().toString(), role.getDescription());
	}
	
	public RoleResponse updateRoleById(int id, RoleRequest request) {
		Role role= repository.findById(id).orElse(null);
		if(!request.getName().isEmpty())
		role.setRoleName(SystemRole.fromStringToEnum(request.getName()));
		if(!request.getDescription().isEmpty())
		role.setDescription(request.getDescription());
		repository.save(role);
		return new RoleResponse(role.getRoleId(),role.getRoleName().toString(), role.getDescription());
	}
	
	public String deleteRoleById(int id) {
		Role role= repository.findById(id).orElse(null);
		repository.delete(role);
		return "Delete role "+id+" successfully!";
	}
	
}
