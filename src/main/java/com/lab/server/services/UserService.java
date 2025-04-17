package com.lab.server.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lab.lib.api.ApiResponse;
import com.lab.lib.api.PaginationResponse;
import com.lab.lib.enumerated.SystemRole;
import com.lab.lib.exceptions.BadRequestException;
import com.lab.lib.exceptions.UnAuthorizationException;
import com.lab.lib.repository.BaseRepository;
import com.lab.lib.service.BaseService;
import com.lab.lib.utils.PagingUtil;
import com.lab.server.configs.language.MessageSourceHelper;
import com.lab.server.configs.security.SecurityHelper;
import com.lab.server.entities.Employee;
import com.lab.server.entities.Role;
import com.lab.server.entities.User;
import com.lab.server.payload.role.RoleResponse;
import com.lab.server.payload.user.UserRequest;
import com.lab.server.payload.user.UserResponse;
import com.lab.server.repositories.DepartmentRepository;
import com.lab.server.repositories.EmployeeRepository;
import com.lab.server.repositories.PositionRepository;
import com.lab.server.repositories.RoleRepository;
import com.lab.server.repositories.UserRepository;
import com.lab.server.repositories.model.EmployeeModel;
import com.lab.server.repositories.model.UserModel;

@Service
@Transactional
public class UserService extends BaseService<User, Integer> {

    private final UserRepository repository;
    private final SecurityHelper securityHelper;
    private final MessageSourceHelper messageSourceHelper;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final RoleRepository roleRepository;
    

    protected UserService(BaseRepository<User, Integer> repository, SecurityHelper securityHelper,
    		MessageSourceHelper messageSourceHelper, 
    		EmployeeRepository employeeRepository, DepartmentRepository departmentRepository,
    		PositionRepository positionRepository, RoleRepository roleRepository) {
        super(repository);
        this.repository = (UserRepository) repository;
        this.securityHelper = securityHelper;
        this.messageSourceHelper = messageSourceHelper;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.roleRepository = roleRepository;
    }
    
    @Transactional(readOnly = true)
	public PaginationResponse<UserResponse> getAllUsersWithConditions(int page, int perPage, String search) {
    	User currentUserLogin = findByFields(Map.of("username", securityHelper.getCurrentUserLogin()));
    	String currentUserRole = currentUserLogin.getRoleId().getRoleName().name();
    	
    	long totalRecord;
    	int offset, totalPage;
    	List<UserModel> userList;
    	
    	switch(currentUserRole) {
    	case "ADMIN":
    		totalRecord = repository.countAllUsersWithConditionsForAdmin(search);
    		offset = PagingUtil.getOffset(page, perPage);
    		totalPage = PagingUtil.getTotalPage(totalRecord, perPage);
    		userList = repository.findAllUsersWithConditionsForAdmin(offset, perPage, search);
    		break;
    	case "MANAGER":
    		totalRecord = repository.countAllUsersWithConditionsForManager(search);
    		offset = PagingUtil.getOffset(page, perPage);
    		totalPage = PagingUtil.getTotalPage(totalRecord, perPage);
    		userList = repository.findAllUsersWithConditionsForManager(offset, perPage, search);
    		break;
    	case "EMPLOYEE":
    		throw new BadRequestException(messageSourceHelper.getMessage("warning.accessDenied"));
    	default:
    		throw new BadRequestException(messageSourceHelper.getMessage("warning.accessDenied"));
    	}
    	
		List<UserResponse> response = new ArrayList<>();
		if(userList != null) {
			response = userList.stream().map(user -> responseBuilder(user)).toList();
		}

		return PaginationResponse.<UserResponse>builder()
				.page(page)
				.perPage(perPage)
				.data(response)
				.totalPage(totalPage)
				.totalRecord(totalRecord)
				.build();
	}

    @Transactional(readOnly = true)
    public UserResponse findUserById(int id) {
    	User currentUserLogin = findByFields(Map.of("username", securityHelper.getCurrentUserLogin()));
    	String currentUserRole = currentUserLogin.getRoleId().getRoleName().name();
    	UserModel user;
    	
    	switch(currentUserRole) {
    	case "ADMIN":
    		user = repository.findUserById(id);
    		break;
    	case "MANAGER":
    		user = repository.findUserByIdForManager(id);
    		break;
    	case "EMPLOYEE":
    		if(currentUserLogin.getUserId()!=id) throw new BadRequestException("warning.accessDenied");
    		user = repository.findUserById(id);
    	default:
    		throw new BadRequestException("warning.accessDenied");
    	}
    	
        return new UserResponse(user.getUserId(),user.getUsername(), user.getEmail(), user.getRoleName());
    }

    @Transactional(rollbackFor = Exception.class)
    public UserResponse createUser(UserRequest request) {
    	User currentUserLogin = findByFields(Map.of("username", securityHelper.getCurrentUserLogin()));
    	String currentUserRole = currentUserLogin.getRoleId().getRoleName().name();
    	EmployeeModel currentUserEmployee = employeeRepository.findEmployeeByUserId(currentUserLogin.getUserId());
    	
    	if(currentUserRole.equals(SystemRole.EMPLOYEE.name())) {
    		throw new BadRequestException("warning.accessDenied");
    	}
    	
    	Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new BadRequestException("error.notFound"));
        User user = new User();
        user.setUsername(request.getUsername());
        
        
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRoleId(role);

        user = repository.save(user);
        
        if(currentUserRole.equals(SystemRole.EMPLOYEE.name())) {
        	employeeRepository.save(Employee.builder()
        			.firstName(request.getFirstName())
        			.lastName(request.getLastName())
        			.dateOfBirth(request.getDateOfBirth())
        			.departmentId(departmentRepository.findById(currentUserEmployee.getDepartmentId()).orElse(null))
        			.positionId(positionRepository.findById(request.getPositionId()).orElse(null))
        			.build());
        }
        
        return new UserResponse(
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getRoleId().getRoleName().name()
        );
    }

    public UserResponse updateUserById(int id, UserRequest request) {
    	User currentUserLogin = findByFields(Map.of("username", securityHelper.getCurrentUserLogin()));
    	String currentUserRole = currentUserLogin.getRoleId().getRoleName().name();
    	
    	switch(currentUserRole) {
    	case "ADMIN":
    		break;
    	case "MANAGER":
    		EmployeeModel currentUserEmployee = employeeRepository.findEmployeeByUserId(currentUserLogin.getUserId());
    		EmployeeModel updatingEmployee = employeeRepository.findEmployeeByUserId(id);
    		if(currentUserEmployee.getDepartmentId() != updatingEmployee.getDepartmentId()) throw new BadRequestException("warning.accessDenied");
    		break;
    	case "EMPLOYEE":
    		throw new BadRequestException("warning.accessDenied");
    	default:
    		throw new BadRequestException("warning.accessDenied");
    	}
    	
    	
    	Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new BadRequestException("error.notFound"));
        User user = repository.findById(id).orElse(null);
        if (user == null) throw new BadRequestException("error.notFound");

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); 
        user.setEmail(request.getEmail());
        user.setRoleId(role);

        user = repository.save(user);
        return new UserResponse(
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getRoleId().getRoleName().name()
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public String deleteUserById(int id) {
    	User currentUserLogin = findByFields(Map.of("username", securityHelper.getCurrentUserLogin()));
    	if(!currentUserLogin.getRoleId().getRoleName().equals(SystemRole.ADMIN)) throw new BadRequestException("warning.accessDenied");
        User user = repository.findById(id).orElse(null);
        if (user == null) return "User not found!";

        repository.delete(user);
        return "Delete user " + id + " successfully!";
    }
    
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser() {
		String username = securityHelper.getCurrentUserLogin();
		User user = findByFields(Map.of("username", username));
		
		 return UserResponse.builder()
	                .userId(user.getUserId())
	                .username(user.getUsername())
	                .email(user.getEmail())
	                .roleName(user.getRoleId().getRoleName().name())
	                .build();
	}

	private UserResponse responseBuilder(UserModel user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roleName(user.getRoleName())
                .build();
    }

	
}
