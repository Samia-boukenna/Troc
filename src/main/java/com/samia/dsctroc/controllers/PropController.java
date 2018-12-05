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

import com.samia.dsctroc.core.Xml;
import com.samia.dsctroc.models.Dmd;
import com.samia.dsctroc.models.Fichier;
import com.samia.dsctroc.models.Message;
import com.samia.dsctroc.models.Prop;
import com.samia.dsctroc.models.Utilisateur;
import com.samia.dsctroc.repositories.DemandeRepo;
import com.samia.dsctroc.repositories.DescriptionRepo;
import com.samia.dsctroc.repositories.DmdRepo;
import com.samia.dsctroc.repositories.FichierRepo;
import com.samia.dsctroc.repositories.MessageRepo;
import com.samia.dsctroc.repositories.ObjetRepo;
import com.samia.dsctroc.repositories.OffreRepo;
import com.samia.dsctroc.repositories.ParametreRepo;
import com.samia.dsctroc.repositories.PropRepo;
import com.samia.dsctroc.repositories.UtilisateurRepo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import static java.util.Calendar.getInstance;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PropController {

    @Autowired
    private UtilisateurRepo utilisateurRepo;

    @Autowired
    private PropRepo propRepo;
    @Autowired
    private DemandeRepo demandeRepo;
    @Autowired
    private OffreRepo offreRepo;
    @Autowired
    private ParametreRepo paramRepo;
    
    @Autowired
    private DescriptionRepo descRepo;
    
    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private FichierRepo fichierRepo;
    
    @Autowired
    private ObjetRepo objetRepo;

    @Autowired
    private Xml xml;
    @RequestMapping(value = "/nouvelle_prop", method = RequestMethod.GET)
    public String afficherPageProp(Model model, HttpServletRequest request) throws IOException {
        Fichier fichier = new Fichier();
        Message message = new Message();
       
        model.addAttribute("propositions", propRepo.findAll());
        model.addAttribute("fichier", fichier);
        model.addAttribute("message", message);
        return "nouvelle_prop";
    }

    @PostMapping("/nouvelle_prop")
    public String creerDmd(int idProposition,@ModelAttribute("fichier") Fichier f,@ModelAttribute("message") Message message, Model model) {
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
       
message.setProp(propRepo.findById(idProposition).get());
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
        xml.xmlCreateProp(fic);


        return "nouvelle_prop";
    }

    
}
