package com.amazon.documentdb.repository;

import org.springframework.data.repository.CrudRepository;

import com.amazon.documentdb.model.MessageValidator;

public interface AuroraRepository extends CrudRepository<MessageValidator, Long> {

}
