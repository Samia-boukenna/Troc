package com.samia.dsctroc.controllers;

import com.samia.dsctroc.models.Dmd;
import com.samia.dsctroc.models.Fichier;
import com.samia.dsctroc.models.Message;
import com.samia.dsctroc.models.Utilisateur;
import com.samia.dsctroc.repositories.DmdRepo;
import com.samia.dsctroc.repositories.FichierRepo;
import com.samia.dsctroc.repositories.MessageRepo;
import com.samia.dsctroc.repositories.UtilisateurRepo;
import com.samia.dsctroc.core.Xml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class DmdController {

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
    @RequestMapping(value = "/nouvelle_dmd", method = RequestMethod.GET)
    public String afficherPageDmd(Model model, HttpServletRequest request) {
        Fichier fichier = new Fichier();
        Message message = new Message();
        model.addAttribute("fichier", fichier);
        model.addAttribute("message", message);
        return "nouvelle_dmd";
    }

    @PostMapping("/nouvelle_dmd")
    public String creerDmd(@ModelAttribute("fichier") Fichier f,@ModelAttribute("message") Message message, Model model) {
        Fichier fic = new Fichier();
        Date dateActuelle = new Date();
        List<Message> messages = new ArrayList<>();
        List<Fichier> fichiers = new ArrayList<>();
        Utilisateur uE = f.getIE();
        Utilisateur uR = f.getIR();
        utilisateurRepo.save(uE);
        utilisateurRepo.save(uR);

        fic.setIE(uE);
        fic.setIR(uR);
        fichierRepo.save(fic);

        Dmd dmd = message.getDmd();
        dmd.setDateDebut(dateActuelle);
        dmdRepo.save(dmd);

        message.setDte(dateActuelle);
        message.setFichier(fic);

        messageRepo.save(message);
        messages.add(message);
        fic.setMessages(messages);
        fichierRepo.save(fic);
        fichiers.add(fic);
        uE.setFichiersEm(fichiers);
        utilisateurRepo.save(uE);

        uR.setFichiersRec(fichiers);
        utilisateurRepo.save(uR);
        model.addAttribute("msgConf","Ajout reussi");
        xml.xmlCreerDmd(fic);


        return "nouvelle_dmd";
    }


}