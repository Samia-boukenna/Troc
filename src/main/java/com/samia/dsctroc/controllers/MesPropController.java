/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samia.dsctroc.controllers;

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class MesPropController {
    @RequestMapping(value = "/mes_prop", method = RequestMethod.GET)
    public String afficherPageDmd(Model model, HttpServletRequest request) throws IOException {
        
        ClassPathResource classPathResource = new ClassPathResource("/static/xmlexport/prop/");
        File repert = classPathResource.getFile();
        File[] listFic = repert.listFiles();
        
        model.addAttribute("listFic", listFic);
        
        return "mes_prop";
    }   
}
