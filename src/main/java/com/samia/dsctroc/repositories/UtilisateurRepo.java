package com.samia.dsctroc.repositories;

import com.samia.dsctroc.models.Utilisateur;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UtilisateurRepo extends CrudRepository<Utilisateur, Integer> {

    @Query("SELECT (u) FROM Utilisateur u WHERE u.mail=:mail")
    Utilisateur findByMail(@Param("mail") String mail);

}
