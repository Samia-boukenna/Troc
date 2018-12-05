package com.samia.dsctroc.controllers;


import com.samia.dsctroc.models.Objet;
import com.samia.dsctroc.models.Parametre;
import com.samia.dsctroc.repositories.DescriptionRepo;
import com.samia.dsctroc.repositories.ObjetRepo;
import com.samia.dsctroc.repositories.ParametreRepo;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Calendar.getInstance;
import org.springframework.core.io.ClassPathResource;


@Controller
public class ObjetController {

    @Autowired
    private ObjetRepo objetRepo;
 @Autowired
    private DescriptionRepo descRepo;
  @Autowired
    private ParametreRepo paramRepo;
  
    @RequestMapping(value = "/mes_objets", method = RequestMethod.GET)
    public String afficherObjets(Model model, HttpServletRequest request) throws IOException {
      List<Objet> objets=(List<Objet>) objetRepo.findAllByMineTrue();
        model.addAttribute("objets", objets);
        
        model.addAttribute("newObjet", new Objet());
        return "mes_objets";
    }
    @RequestMapping("/ajouter_objet")
    public String ajoutObjet(Model model,Objet  newObjet) {
        newObjet.setMine(true);
        paramRepo.saveAll(newObjet.getDescription().getParametres());
        descRepo.save(newObjet.getDescription());
     objetRepo.save(newObjet);
        return "redirect:/mes_objets";
    }

 @RequestMapping("/voir_objet")
    public String modifierObjet(int idObjet,Model model) {
        Objet objet=objetRepo.findById(idObjet).get();
        model.addAttribute("parametre", new Parametre());
        model.addAttribute("objet",objet);
        return "modif_objet";
    }
    @RequestMapping("/modif_objet")
    public String modifierObjet(int idObjet,Parametre parametre ,Model model) {
        Objet objet=objetRepo.findById(idObjet).get();
        objet.getDescription().getParametres().add(parametre);
        paramRepo.save(parametre);
        descRepo.save(objet.getDescription());
        objetRepo.save(objet);
        model.addAttribute("objet",objet);
        return "redirect:/voir_objet?idObjet=" + idObjet;
    }


}