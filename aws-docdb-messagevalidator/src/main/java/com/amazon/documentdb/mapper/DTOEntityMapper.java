package com.amazon.documentdb.mapper;

public interface DTOEntityMapper<E, D> {
	
	D toDto(E inputType);
	
	E toEntity(D dtoType);
}
