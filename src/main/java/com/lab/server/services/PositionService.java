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
import com.lab.server.entities.Position;
import com.lab.server.payload.position.PositionRequest;
import com.lab.server.payload.position.PositionResponse;
import com.lab.server.repositories.PositionRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PositionService extends BaseService<Position, Integer>{
	
	private final PositionRepository repository;
	private final MessageSourceHelper messageHelper;
	
	protected PositionService(BaseRepository<Position, Integer> repository, MessageSourceHelper messageHelper) {
		super(repository);
		this.repository = (PositionRepository) repository;
		this.messageHelper = messageHelper;
	}
	
	@Transactional(readOnly = true)
	public PositionResponse getPositionById(int id) {
		Position position = findByFields(Map.of("positionId", id));
		if(position == null) return new PositionResponse();
		return responseBuilder(position);
	}
	
	@Transactional(readOnly = true)
	public PaginationResponse<PositionResponse> getPositionWithConditions(int page, int perpage, String search){
		long totalRecord = repository.countAllPositionWithConditions(search);
		int offset = PagingUtil.getOffset(page, perpage);
		int totalPage = PagingUtil.getTotalPage(totalRecord, perpage);
		List<Position> positionList = repository.findAllPositionWithConditions(offset, perpage, search);
		List<PositionResponse> response = new ArrayList<>();
		if(positionList != null) {
			response = positionList.stream().map(position -> responseBuilder(position)).toList();
		}
		
		return PaginationResponse.<PositionResponse>builder()
				.page(page)
				.perPage(perpage) 
				.data(response)
				.totalPage(totalPage)
				.totalRecord(totalRecord)
				.build();
	}
	
	@Transactional(rollbackFor = Exception.class)
	public PositionResponse createPosition(PositionRequest request) {
		Position position = entityBuilder(request);
		position = save(position);
		return responseBuilder(position);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public PositionResponse savePosition(Integer id, PositionRequest request) throws Exception {
		Position position = findByFields(Map.of("positionId", id));
		position.setPositionName(request.getPositionName());
		position = save(position);
		return responseBuilder(position);
	}
	
	public void deletePosition(int id) throws Exception {
		try { 
			Position position = findByFields(Map.of("positionId", id));
			repository.delete(position);
		} catch(Exception e) {
			log.error(messageHelper.getMessage("error.data.notfound"));
			throw new Exception("error.data.notfound");
		}
	}
	
	private Position entityBuilder(PositionRequest position) {
		return Position.builder()
				.positionName(position.getPositionName())
				.build();
	}
	
	private PositionResponse responseBuilder(Position position) {
		return PositionResponse.builder()
				.positionId(position.getPositionId())
				.positionName(position.getPositionName())
				.build();
	}
}
