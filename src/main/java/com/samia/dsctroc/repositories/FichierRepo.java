package com.samia.dsctroc.repositories;

import com.samia.dsctroc.models.Fichier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FichierRepo extends CrudRepository<Fichier, Integer> {

    @Query("SELECT COUNT(fic) FROM Fichier fic WHERE fic.ficId=:ficId")
    Integer estTraite(@Param("ficId") String ficId);
}
