package com.lab.server.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lab.lib.repository.BaseRepository;
import com.lab.server.entities.Department;

@Repository
public interface DepartmentRepository extends BaseRepository<Department, Integer> {

	@Query(value = "SELECT COUNT(1) FROM public.departments d "
			+ " WHERE ( :search IS NULL OR :search = '' OR d.department_name = :search ) ", nativeQuery = true)
	long countAllDepartmentWithConditions(@Param("search") String search);
	
	@Query(value = "SELECT d.department_id, d.department_name, d.description FROM public.departments d "
			+ "WHERE ( :search IS NULL OR :search = '' OR d.department_name = :search ) "
			+ "LIMIT :limit OFFSET :offset ", nativeQuery = true)
	List<Department> findAllDepartmentWithConditions(@Param("offset") int offset,
													 @Param("limit") int limit,
													 @Param("search") String search);
}
