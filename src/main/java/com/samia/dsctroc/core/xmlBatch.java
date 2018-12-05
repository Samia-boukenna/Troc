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
            if (folder.isDirectory()) {
                for (final File fileEntry : folder.listFiles()) {
                    System.out.println("fichier: "+fileEntry.getName());
                    if(xml.verifierFichierTraite(fileEntry.getPath())) {
                        System.out.println("traitement en cours");
                        xml.xmlLireDmd(fileEntry.getPath());
                    }else{
                        System.out.println("fichier deja traité");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

