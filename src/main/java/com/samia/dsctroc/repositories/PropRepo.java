package com.samia.dsctroc.repositories;

import com.samia.dsctroc.models.Prop;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface PropRepo extends CrudRepository<Prop, Integer> {
     List<Prop> findAllByMineTrue();
}
