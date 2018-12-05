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
                   atester =new ParserXml(fileEntry);
                   
                    if(atester.getDocument()!=null){
                        if(atester.isDmd()){
                    System.out.println("fichier: "+fileEntry.getName());
                    if(xml.verifierFichierTraite(fileEntry.getPath())) {
                        System.out.println("traitement en cours");
                        xml.xmlLireDmd(fileEntry.getPath());
                    }else{
                        System.out.println("fichier deja traité");
                    }
                    
                }else if(atester.isProp()){
                   //a faire 
                }//else if(atester.isAuth()){
                   //a faire 
              //  }
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

