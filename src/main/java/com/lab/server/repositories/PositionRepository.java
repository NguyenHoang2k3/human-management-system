package com.lab.server.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lab.lib.repository.BaseRepository;
import com.lab.server.entities.Position;

@Repository
public interface PositionRepository extends BaseRepository<Position, Integer> {

	@Query(value = "SELECT COUNT(1) FROM public.positions p "
			+ "WHERE ( :search IS NULL OR :search = '' OR p.position_name = :search ) ", nativeQuery = true)
	long countAllPositionWithConditions(@Param("search") String search);

	@Query(value = "SELECT p.position_id, p.position_name FROM public.positions p "
			+ "WHERE ( :search IS NULL OR :search = '' OR p.position_name = :search ) "
			+ "LIMIT :limit OFFSET :offset ", nativeQuery = true)
	List<Position> findAllPositionWithConditions(@Param("offset") int offset, 
												@Param("limit") int limit, 
												@Param("search") String search);
}
