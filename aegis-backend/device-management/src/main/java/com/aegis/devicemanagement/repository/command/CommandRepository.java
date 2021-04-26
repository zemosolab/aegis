package com.aegis.devicemanagement.repository.command;

import com.aegis.devicemanagement.model.command.Command;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandRepository extends MongoRepository<Command, String> {
}
