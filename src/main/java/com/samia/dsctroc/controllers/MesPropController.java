/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samia.dsctroc.controllers;

import com.samia.dsctroc.core.ParserXml;
import com.samia.dsctroc.models.Fichier;
import com.samia.dsctroc.repositories.FichierRepo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class MesPropController {
     @Autowired
    private FichierRepo fichierRepo;
    @RequestMapping(value = "/mes_prop", method = RequestMethod.GET)
    public String afficherPageDmd(Model model, HttpServletRequest request) throws IOException {
        
        ClassPathResource classPathResource = new ClassPathResource("/static/xmlexport/prop/");
       File repert = classPathResource.getFile();
        File[] listFic = repert.listFiles();
         ParserXml doc;
        List<File> listeFicValid=new ArrayList();
        List<File> listeFicInvalid=new ArrayList();
         List<File> listeFicValidExpired=new ArrayList();
        for(int i=0;i<listFic.length;i++){
            if(!listFic[i].isDirectory()){
            doc=new ParserXml(listFic[i]);
            if(doc.getDocument()!=null && doc.isProp()){
                if(doc.isOnTime())
                 listeFicValid.add(listFic[i]);
                else
                 listeFicValidExpired.add(listFic[i]);
            }
            else
               listeFicInvalid.add(listFic[i]);
            }
            
        }
           
         
        model.addAttribute("listFicValid", listeFicValid);
        model.addAttribute("listFicValidExpired", listeFicValidExpired);
        model.addAttribute("listFicInvalid", listeFicInvalid);
        return "mes_prop";
    }   
    @GetMapping("/prop-recues")
    public String dmdRecus(Model model) {
        /**
         * todo: on va demander a lutilisateur de saisir son mail au debut de l application comme ca on saura qui est l'emetteur
         */
        List<Fichier> fichiers = fichierRepo.findAllByMessagesPropRecevedTrue();
        System.out.println(fichiers.size());
        for (int i = 0; i < fichiers.size(); i++) {
            System.out.println(fichiers.get(i).getIE().getNom());
            System.out.println(fichiers.get(i).getIR().getNom());
        }
        model.addAttribute("fichiers", fichiers);
        return "prop-recues";
    }

}
