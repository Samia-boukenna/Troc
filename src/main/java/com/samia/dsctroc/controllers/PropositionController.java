package com.samia.dsctroc.controllers;


import com.samia.dsctroc.models.Demande;
import com.samia.dsctroc.models.Objet;
import com.samia.dsctroc.models.Offre;
import com.samia.dsctroc.models.Parametre;
import com.samia.dsctroc.models.Prop;
import com.samia.dsctroc.repositories.DemandeRepo;
import com.samia.dsctroc.repositories.DescriptionRepo;
import com.samia.dsctroc.repositories.ObjetRepo;
import com.samia.dsctroc.repositories.OffreRepo;
import com.samia.dsctroc.repositories.ParametreRepo;
import com.samia.dsctroc.repositories.PropRepo;
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
import java.util.HashSet;
import java.util.Set;
import org.springframework.core.io.ClassPathResource;


@Controller
public class PropositionController {
@Autowired
    private OffreRepo offreRepo;
@Autowired
    private DemandeRepo demandeRepo;
    @Autowired
    private ObjetRepo objetRepo;
    @Autowired
    private PropRepo propRepo;
 
    @RequestMapping(value = "/mes_propositions", method = RequestMethod.GET)
    public String afficherPropositions(Model model, HttpServletRequest request) throws IOException {
      List<Prop> propositions=(List<Prop>) propRepo.findAll();
        model.addAttribute("propositions", propositions);
        
        model.addAttribute("objets", objetRepo.findAll());
        model.addAttribute("newProp", new Prop());
        return "mes_propositions";
    }
    @RequestMapping("/ajouter_proposition")
    public String ajoutProposition(Model model,Prop  newProp,int offreObjet, int demandeObjet) {
        Set<Objet> offreList=new HashSet();
         Set<Objet> dmdList=new HashSet();
         offreList.add(objetRepo.findById(offreObjet).get());
         dmdList.add(objetRepo.findById(demandeObjet).get());
         Offre offre=new Offre();
         Demande demande = new Demande();
         demande.setObjets(dmdList);
         offre.setObjets(offreList);
         offreRepo.save(offre);
         demandeRepo.save(demande);
        newProp.setDemande(demande);
        newProp.setOffre(offre);
     propRepo.save(newProp);
        return "redirect:/mes_propositions";
    }

 @RequestMapping("/voir_proposition")
    public String modifierProposition(int idProp,Model model) {
        Prop prop=propRepo.findById(idProp).get();
        model.addAttribute("objets", objetRepo.findAll());
        model.addAttribute("proposition",prop);
        return "modif_proposition";
    }
    @RequestMapping("/modif_proposition")
    public String modifierObjet(int idObjet,int idProp,String action ,Model model) {
        Objet objet=objetRepo.findById(idObjet).get();
        Prop prop = propRepo.findById(idProp).get();
        if(action.equals("offre")){
            prop.getOffre().getObjets().add(objet);
            offreRepo.save(prop.getOffre());
        }
            else{
            prop.getDemande().getObjets().add(objet);
            demandeRepo.save(prop.getDemande());
                    }
       propRepo.save(prop); 
            
        return "redirect:/voir_proposition?idProp=" + idProp;
    }


}