package com.samia.dsctroc.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class xmlBatch {

    @Autowired
    private Xml xml;

    @Scheduled(fixedRate = 9000)
    public void process() {
        try {
            ClassPathResource cpr = new ClassPathResource("/static/xmlimport/");
            final File folder = cpr.getFile();
            ParserXml atester;
            if (folder.isDirectory()) {
                for (final File fileEntry : folder.listFiles()) {
                    if(fileEntry.length()>5000)
                        return;
                   atester =new ParserXml(fileEntry);
                   
                    if(atester.getDocument()!=null){
                        if(atester.isDmd()){
                    System.out.println("fichier: "+fileEntry.getName());
                    if(xml.verifierFichierTraite(fileEntry.getPath(),"dmd")) {
                        System.out.println("traitement en cours");
                        xml.xmlLireDmd(fileEntry.getPath());
                    }else{
                        System.out.println("fichier deja traité");
                    }
                    
                }else if(atester.isProp()){
                    System.out.println("fichier: "+fileEntry.getName());
                   if(xml.verifierFichierTraite(fileEntry.getPath(),"prop")) {
                        System.out.println("traitement en cours");
                        xml.xmlLireProp(fileEntry.getPath());
                    }else{
                        System.out.println("fichier deja traité");
                    }
                }else if(atester.isAuth()){
                    System.out.println("fichier: "+fileEntry.getName());
                   if(xml.verifierFichierTraite(fileEntry.getPath(),"auth")) {
                        System.out.println("traitement en cours");
                        xml.xmlLireAuth(fileEntry.getPath());
                    }else{
                        System.out.println("fichier deja traité");
                    }
                }
                }else{
                 System.out.println("fichier Invalide");
                }
                        
                        
                        
                        //A faire
                        }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

