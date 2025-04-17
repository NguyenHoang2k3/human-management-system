package com.lab.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lab.lib.repository.BaseRepository;
import com.lab.server.entities.User;
import com.lab.server.repositories.model.UserModel;

@Repository
public interface UserRepository extends BaseRepository<User, Integer> {


	@Query(value = "SELECT u.user_id as userId, u.username, u.email, r.role_name as roleName FROM users u \r\n"
			+ "LEFT JOIN roles r ON r.role_id = u.role_id\r\n"
			+ "LEFT JOIN employees e ON e.user_id = u.user_id\r\n"
			+ "LEFT JOIN departments d ON d.department_id = e.department_id\r\n"
			+ "WHERE ( :search IS NULL OR :search = '' OR to_tsvector('english', username) @@ to_tsquery('english', :search))\r\n"
			+ "LIMIT :limit OFFSET :offset", nativeQuery = true)
	List<UserModel> findAllUsersWithConditionsForManager(@Param("offset") int offset, 
			@Param("limit") int limit, 
			@Param("search") String search);

	@Query(value = "SELECT COUNT(1) FROM users u \r\n"
			+ "LEFT JOIN roles r ON r.role_id = u.role_id\r\n"
			+ "LEFT JOIN employees e ON e.user_id = u.user_id\r\n"
			+ "LEFT JOIN departments d ON d.department_id = e.department_id\r\n"
			+ "WHERE ( :search IS NULL OR :search = '' OR to_tsvector('english', username) @@ to_tsquery('english', :search))", nativeQuery = true)
	long countAllUsersWithConditionsForManager(@Param("search") String search);
	
	@Query(value = "SELECT u.user_id as userId, u.username, u.email, r.role_name as roleName FROM users u \r\n"
			+ "LEFT JOIN roles r ON r.role_id = u.role_id\r\n"
			+ "WHERE ( :search IS NULL OR :search = '' OR to_tsvector('english', username) @@ to_tsquery('english', :search))\r\n"
			+ "LIMIT :limit OFFSET :offset", nativeQuery = true)
	List<UserModel> findAllUsersWithConditionsForAdmin(@Param("offset") int offset, 
			@Param("limit") int limit, 
			@Param("search") String search);

	@Query(value = "SELECT COUNT(1) FROM users u \r\n"
			+ "LEFT JOIN roles r ON r.role_id = u.role_id\r\n"
			+ "WHERE ( :search IS NULL OR :search = '' OR to_tsvector('english', username) @@ to_tsquery('english', :search))", nativeQuery = true)
	long countAllUsersWithConditionsForAdmin(@Param("search") String search);

	@Query(value = "SELECT u.user_id as userId, u.username, u.email, r.role_name as roleName FROM users u \r\n"
			+ "LEFT JOIN roles r ON r.role_id = u.role_id\r\n"
			+ "WHERE u.user_id = :id ", nativeQuery = true)
	UserModel findUserById(@Param("id") int id);
	
	@Query(value = "SELECT u.user_id as userId, u.username, u.email, r.role_name as roleName FROM users u \r\n"
			+ "LEFT JOIN roles r ON r.role_id = u.role_id\r\n"
			+ "LEFT JOIN employees e ON e.user_id = u.user_id\r\n"
			+ "LEFT JOIN departments d ON d.department_id = e.department_id\r\n"
			+ "WHERE u.user_id = :id ", nativeQuery = true)
	UserModel findUserByIdForManager(@Param("id") int id);
}
