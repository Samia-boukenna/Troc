package com.samia.dsctroc.repositories;

import com.samia.dsctroc.models.Objet;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ObjetRepo extends CrudRepository<Objet, Integer> {
     List<Objet> findAllByMineTrue();
}
