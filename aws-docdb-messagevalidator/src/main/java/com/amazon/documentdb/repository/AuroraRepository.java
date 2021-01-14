package com.amazon.documentdb.repository;

import org.springframework.data.repository.CrudRepository;

import com.amazon.documentdb.model.MessageLog;

public interface AuroraRepository extends CrudRepository<MessageLog, Long> {

}
