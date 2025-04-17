package com.lab.server.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lab.lib.repository.BaseRepository;
import com.lab.server.entities.Employee;
import com.lab.server.repositories.model.EmployeeModel;

@Repository
public interface EmployeeRepository extends BaseRepository<Employee, Integer> {
	
	@Query(value = "SELECT e.employee_id as employeeId, CONCAT(e.first_name || ' ' || e.last_name) as fullName, \r\n"
			+ "	e.department_id as departmentId, e.position_id as positionId FROM public.employees e\r\n"
			+ "	WHERE e.user_id = :userId ", nativeQuery = true)
	public EmployeeModel findEmployeeByUserId(@Param("userId") int userId);
}
