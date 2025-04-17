package com.lab.lib.api;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int page;
	private int perPage;
	private List<T> data; 
	private long totalRecord;
	private long totalPage;

}
