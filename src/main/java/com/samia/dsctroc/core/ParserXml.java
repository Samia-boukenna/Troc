/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samia.dsctroc.core;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import static java.util.Calendar.getInstance;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import lombok.Data;
import org.springframework.core.io.ClassPathResource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
/**
 *
 * @author fdr
 */
@Data
public class ParserXml {

private Document document;
private static Element root;
private static NodeList nodes;
   public ParserXml(File fileXML) {
     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
factory.setIgnoringElementContentWhitespace(true);

      try {
         
         //Ces trois lignes servent à informer que la validation se fait via un fichier XSD
         SchemaFactory sfactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
         
         //On créé notre schéma XSD
         //Ici, c'est un schéma interne, pour un schéma externe il faut mettre l'URI
       
         Schema schema = sfactory.newSchema(new ClassPathResource("/static/xsd/xmlFiles.xsd").getFile());
         //On l'affecte à notre factory afin que le document prenne en compte le fichier XSD
         factory.setSchema(schema);    
         
         DocumentBuilder builder = factory.newDocumentBuilder();
         
         //création de notre objet d'erreurs
         ErrorHandler errHandler = new SimpleErrorHandler();
         //Affectation de notre objet au document pour interception des erreurs éventuelles
         builder.setErrorHandler(errHandler);
         
         
         //On rajoute un bloc de capture
         //pour intercepter les erreurs au cas où il y en ait
         try {
            document= builder.parse(fileXML);
         root = document.getDocumentElement();
            nodes = root.getChildNodes();
            System.out.println(root.getNodeName());
         } catch (SAXParseException e) {} 
           
      } catch (ParserConfigurationException e) {
         e.printStackTrace();
      } catch (SAXException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }  
 }
   
   
   
   public static boolean isDmd(){
      
         if(root.getLastChild().getFirstChild().getFirstChild().getLastChild().getNodeName().equals("Dmd"))
             return true;
         return false;
}
   public static boolean isProp(){
      
         if(root.getLastChild().getFirstChild().getFirstChild().getLastChild().getNodeName().equals("Prop"))
             return true;
         return false;
} 
   public static boolean isOnTime(){
       Node message=root.getLastChild().getFirstChild().getFirstChild();
       NodeList list = message.getChildNodes();
       
      System.out.println(message.getNodeName());
      String dateString=list.item(0).getTextContent();
          System.out.print(list.item(0).getNodeName());
          System.out.println("  "+list.item(0).getTextContent());
          System.out.print(list.item(1).getNodeName());
          System.out.println("  "+list.item(1).getTextContent());
      int dureeValid=Integer.parseInt(list.item(1).getTextContent());
              Date dateDebut;
    try {
       SimpleDateFormat sdf= new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",Locale.ENGLISH);
        dateDebut = sdf.parse(dateString);
          Calendar c = getInstance();
         c.setTime(dateDebut);
        c.add(Calendar.DATE, dureeValid);
        Date dateFin=c.getTime();
        Date dateActuelle=new Date();
         if(dateActuelle.before(dateFin))
             return true;
    } catch (ParseException ex) {
        Logger.getLogger(ParserXml.class.getName()).log(Level.SEVERE, null, ex);
    }
             
       
         return false;
}
}