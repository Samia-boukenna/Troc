/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samia.dsctroc.controllers;

import com.samia.dsctroc.core.ParserXml;
import com.samia.dsctroc.core.Xml;
import com.samia.dsctroc.models.Auth;
import com.samia.dsctroc.models.Fichier;
import com.samia.dsctroc.models.Message;
import com.samia.dsctroc.repositories.AuthRepo;
import com.samia.dsctroc.repositories.FichierRepo;
import com.samia.dsctroc.repositories.MessageRepo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class MesDmdController {
      @Autowired
    private AuthRepo authRepo;

    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private Xml xml;
     @Autowired
    private FichierRepo fichierRepo;
    @RequestMapping(value = "/mes_dmd", method = RequestMethod.GET)
    public String afficherPageDmd(Model model, HttpServletRequest request) throws IOException {
        
        ClassPathResource classPathResource = new ClassPathResource("/static/xmlexport/dmd/");
        File repert = classPathResource.getFile();
        File[] listFic = repert.listFiles();
        ParserXml doc;
        List<File> listeFicValid=new ArrayList();
        List<File> listeFicInvalid=new ArrayList();
         List<File> listeFicValidExpired=new ArrayList();
          List<String> acceptedList=new ArrayList();
            Fichier fich=new Fichier();
        for(int i=0;i<listFic.length;i++){
            if(!listFic[i].isDirectory()){
            doc=new ParserXml(listFic[i]);
           
            if(doc.getDocument()!=null && doc.isDmd()){
                fich=fichierRepo.findById(Long.parseLong(listFic[i].getName().replaceAll("[^0-9]", ""))).get();
                if(fich.getMessages().get(0).getAuth()!=null)
                    acceptedList.add(listFic[i].getName());
                if(doc.isOnTime())
                 listeFicValid.add(listFic[i]);
                else
                 listeFicValidExpired.add(listFic[i]);
            }
            else
               listeFicInvalid.add(listFic[i]);
            }
            
        }
           
          model.addAttribute("acceptedlist", acceptedList);
        model.addAttribute("listFicValid", listeFicValid);
        model.addAttribute("listFicValidExpired", listeFicValidExpired);
        model.addAttribute("listFicInvalid", listeFicInvalid);
        
        return "mes_dmd";
    }   
    @GetMapping("/dmd-recues")
    public String dmdRecus(Model model) {
        /**
         * todo: on va demander a lutilisateur de saisir son mail au debut de l application comme ca on saura qui est l'emetteur
         */
        List<Fichier> fichiers = fichierRepo.findAllByMessagesDmdRecevedTrue();
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
