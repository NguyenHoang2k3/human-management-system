package com.lab.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lab.lib.repository.BaseRepository;
import com.lab.server.entities.Permission;

@Repository
public interface PermissionRepository extends BaseRepository<Permission, Integer> {
	@Query(value = "SELECT COUNT(1) FROM public.permissions p WHERE p.permission_name = :search ", nativeQuery = true)
	long countAllBySearch(@Param("search") String search);
	
	@Query(value = "SELECT p.permission_id, p.permission_name, p.description FROM public.permissions p "
			+ "WHERE ( :search IS NULL OR :search = '' OR p.permission_name = :search ) "
			+ "LIMIT :limit OFFSET :offset ", nativeQuery = true)
	List<Permission> findAllPermissionsWithConditions(@Param("offset") int offset, 
			@Param("limit") int limit, 
			@Param("search") String search);
}
