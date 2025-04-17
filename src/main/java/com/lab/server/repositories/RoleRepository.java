package com.lab.server.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lab.lib.repository.BaseRepository;
import com.lab.server.entities.Role;
import com.lab.server.payload.rolepermission.RolePermissionResponse;

@Repository
public interface RoleRepository extends BaseRepository<Role, Integer> {

	@Query(value = "SELECT COUNT(1) FROM public.roles r WHERE r.role_name = :search ", nativeQuery = true)
	long countAllBySearch(@Param("search") String search);
	
	@Query(value = "SELECT r.role_id, r.role_name, r.description FROM public.roles r "
			+ "WHERE ( :search IS NULL OR :search = '' OR r.role_name = :search ) "
			+ "LIMIT :limit OFFSET :offset ", nativeQuery = true)
	List<Role> findAllRolesWithConditions(@Param("offset") int offset, 
			@Param("limit") int limit, 
			@Param("search") String search);
	
	@Query("SELECT new com.lab.server.payload.rolepermission.RolePermissionResponse(r.roleName, p.permissionName) " +
           "FROM Role r JOIN r.permissions p")
    List<RolePermissionResponse> findAllRolePermissions();

    @Query("SELECT new com.lab.server.payload.rolepermission.RolePermissionResponse(r.roleName, p.permissionName) " +
           "FROM Role r JOIN r.permissions p " +
           "WHERE r.roleId = :roleId AND p.permissionId = :permissionId")
    Optional<RolePermissionResponse> findRolePermission(@Param("roleId") int roleId,
                                                   @Param("permissionId") int permissionId);
}
