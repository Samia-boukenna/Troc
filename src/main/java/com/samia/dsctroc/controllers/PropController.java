package com.samia.dsctroc.controllers;
import com.samia.dsctroc.core.Xml;
import com.samia.dsctroc.models.Fichier;
import com.samia.dsctroc.models.Message;
import com.samia.dsctroc.repositories.DmdRepo;
import com.samia.dsctroc.repositories.FichierRepo;
import com.samia.dsctroc.repositories.MessageRepo;
import com.samia.dsctroc.repositories.UtilisateurRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

public class PropController {
    @Autowired
    private UtilisateurRepo utilisateurRepo;

    @Autowired
    private DmdRepo dmdRepo;

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private FichierRepo fichierRepo;
    @Autowired
    private Xml xml;
    @RequestMapping(value = "/nouvelle_prop", method = RequestMethod.GET)
    public String afficherPageDmd(Model model, HttpServletRequest request) {
        Fichier fichier = new Fichier();
        Message message = new Message();
        model.addAttribute("fichier", fichier);
        model.addAttribute("message", message);
        return "nouvelle_prop";
    }


}
