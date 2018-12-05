package com.samia.dsctroc.controllers;

import com.samia.dsctroc.models.*;
import com.samia.dsctroc.repositories.*;
import com.samia.dsctroc.core.Xml;
import java.io.File;
import java.io.IOException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.util.Calendar.getInstance;
import org.springframework.core.io.ClassPathResource;


@Controller
public class DmdController {

    @Autowired
    private UtilisateurRepo utilisateurRepo;

    @Autowired
    private DmdRepo dmdRepo;

    @Autowired
    private AuthRepo authRepo;

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private FichierRepo fichierRepo;

    @Autowired
    private Xml xml;
    @RequestMapping(value = "/nouvelle_dmd", method = RequestMethod.GET)
    public String afficherPageDmd(Model model, HttpServletRequest request) throws IOException {
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
        Calendar c=getInstance();
        c.setTime(dateActuelle);
        c.add(Calendar.DATE, message.getDureeValide());
        Date dateFin=c.getTime();
        Dmd dmd = message.getDmd();
        dmd.setDateDebut(dateActuelle);
        dmd.setDateFin(dateFin);
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


    @GetMapping("/dmd-recues")
    public String dmdRecus(Model model) {
        /**
         * todo: on va demander a lutilisateur de saisir son mail au debut de l application comme ca on saura qui est l'emetteur
         */
        String emetteur = "samia@gmail.com";
        List<Fichier> fichiers = dmdRepo.dmdUtilisateur(emetteur);
        System.out.println(fichiers.size());
        for (int i = 0; i < fichiers.size(); i++) {
            System.out.println(fichiers.get(i).getIE().getNom());
            System.out.println(fichiers.get(i).getIR().getNom());
        }
        model.addAttribute("fichiers", fichiers);
        return "dmd-recues";
    }

    @PostMapping("/traiter-dmd")
    public @ResponseBody
    String createContreProposition(@RequestBody String propoString) {
        System.out.println(propoString);
        JSONObject jsonObject = new JSONObject(propoString);
        Optional<Message> optionalMessage = messageRepo.findById(Integer.parseInt(jsonObject.get("msgId").toString()));
        if(jsonObject.get("type").toString().equals("accepter")){
            if(optionalMessage.isPresent()){
                Message message = optionalMessage.get();
                Auth auth = new Auth();
                auth.setAccepte(true);
                auth.setNumAuth((UUID.randomUUID().toString()));
                authRepo.save(auth);
                message.setAuth(auth);
                messageRepo.save(message);
                xml.xmlCreerAuth(message.getFichier());
                return "[\"accep\"]";

            }

        }else if(jsonObject.get("type").toString().equals("refuser")){
            if(optionalMessage.isPresent()){
                Message message = optionalMessage.get();
                Auth auth = new Auth();
                auth.setAccepte(false);
                auth.setNumAuth((UUID.randomUUID().toString()));
                authRepo.save(auth);
                message.setAuth(auth);
                messageRepo.save(message);
                xml.xmlCreerAuth(message.getFichier());
                return "[\"ref\"]";
            }
        }
        return "[\"erreur\"]";
    }



}
