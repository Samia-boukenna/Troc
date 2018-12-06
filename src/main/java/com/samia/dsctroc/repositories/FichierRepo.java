package com.samia.dsctroc.repositories;

import com.samia.dsctroc.models.Fichier;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FichierRepo extends CrudRepository<Fichier,Long> {
 List<Fichier> findAllByMessagesDmdRecevedTrue();
    @Query("SELECT COUNT(fic) FROM Fichier fic WHERE fic.ficid=:ficid")
    Integer estTraite(@Param("ficid") String ficid);

    public Optional<Fichier> findByFicid(String id);

    public List<Fichier> findAllByMessagesPropRecevedTrue();
}
