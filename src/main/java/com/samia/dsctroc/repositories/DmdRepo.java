package com.samia.dsctroc.repositories;

import com.samia.dsctroc.models.Dmd;
import com.samia.dsctroc.models.Fichier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DmdRepo extends CrudRepository<Dmd, Integer> {

    @Query("select distinct f from Dmd d join Message m on m.dmd=d join Fichier f on m.fichier = f join Utilisateur u on f.iE = u where u.mail=:mail")
    List<Fichier> dmdUtilisateur(@Param("mail") String mail);
}
