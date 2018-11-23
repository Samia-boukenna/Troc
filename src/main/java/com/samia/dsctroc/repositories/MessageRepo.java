package com.samia.dsctroc.repositories;

import com.samia.dsctroc.models.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepo extends CrudRepository<Message, Integer> {
}
