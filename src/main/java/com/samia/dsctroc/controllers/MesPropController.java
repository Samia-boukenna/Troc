/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samia.dsctroc.controllers;

import com.samia.dsctroc.core.ParserXml;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author gentile
 */
@Controller
public class MesPropController {
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
}
