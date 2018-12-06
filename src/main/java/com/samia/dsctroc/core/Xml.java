package com.samia.dsctroc.core;

import com.samia.dsctroc.models.*;
import com.samia.dsctroc.repositories.AuthRepo;
import com.samia.dsctroc.repositories.DemandeRepo;
import com.samia.dsctroc.repositories.DescriptionRepo;
import com.samia.dsctroc.repositories.DmdRepo;
import com.samia.dsctroc.repositories.FichierRepo;
import com.samia.dsctroc.repositories.MessageRepo;
import com.samia.dsctroc.repositories.ObjetRepo;
import com.samia.dsctroc.repositories.OffreRepo;
import com.samia.dsctroc.repositories.ParametreRepo;
import com.samia.dsctroc.repositories.PropRepo;
import com.samia.dsctroc.repositories.UtilisateurRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Calendar.getInstance;
import java.util.HashSet;
import java.util.Set;
import org.w3c.dom.NodeList;

@Service
@Configurable
public class Xml {


    @Autowired
    private UtilisateurRepo utilisateurRepo;
 @Autowired
    private AuthRepo authRepo;

    @Autowired
    private ObjetRepo objetRepo;
    @Autowired
    private PropRepo propRepo;
    @Autowired
    private DmdRepo dmdRepo;
    @Autowired
    private ParametreRepo paramRepo;
    @Autowired
    private OffreRepo offreRepo;
    @Autowired
    private DescriptionRepo descRepo;
    @Autowired
    private DemandeRepo demandeRepo;

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private FichierRepo fichierRepo;

    // creer le contenu xml
    public void xmlCreerDmd(Fichier fichier) {
        Calendar c = getInstance();
        c.setTime(fichier.getMessages().get(0).getDmd().getDateDebut());
        c.add(Calendar.DATE, fichier.getMessages().get(0).getDureeValide());
        Date dateFin = c.getTime();

        try {
            StringWriter stringWriter = new StringWriter();
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xMLStreamWriter =
                    xMLOutputFactory.createXMLStreamWriter(stringWriter);


            xMLStreamWriter.writeStartDocument();
            xMLStreamWriter.writeProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"xsl/dmd.xsl\"");
            xMLStreamWriter.writeStartElement("Fichier");
            xMLStreamWriter.writeStartElement("Header");
            xMLStreamWriter.writeStartElement("FicID");
            xMLStreamWriter.writeCharacters(fichier.getFicid());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("NmIE");
            xMLStreamWriter.writeCharacters(fichier.getIE().getNom());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("NmIR");
            xMLStreamWriter.writeCharacters(fichier.getIR().getNom());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("MailDest");
            xMLStreamWriter.writeCharacters(fichier.getIR().getMail());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("MailExp");
            xMLStreamWriter.writeCharacters(fichier.getIE().getMail());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("Body");
            xMLStreamWriter.writeStartElement("CollMess");
            xMLStreamWriter.writeAttribute("NbOfTxs", "1");
            xMLStreamWriter.writeStartElement("Message");
            xMLStreamWriter.writeAttribute("MsgId", fichier.getMessages().get(0).getMsgId());
            xMLStreamWriter.writeStartElement("Dte");
            xMLStreamWriter.writeCharacters(fichier.getMessages().get(0).getDte().toString());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("DureeValideMsg");
            xMLStreamWriter.writeCharacters(Integer.toString(fichier.getMessages().get(0).getDureeValide()));
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("Dmd");
            xMLStreamWriter.writeStartElement("DescDmd");
            xMLStreamWriter.writeCharacters(fichier.getMessages().get(0).getDmd().getDescription());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("DateDebut");
            xMLStreamWriter.writeCharacters(fichier.getMessages().get(0).getDmd().getDateDebut().toString());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("DateFin");
            xMLStreamWriter.writeCharacters(dateFin.toString());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndDocument();


            xMLStreamWriter.flush();
            xMLStreamWriter.close();
            // creer le fichier xml
            String xmlString = stringWriter.getBuffer().toString();

            ClassPathResource classPathResource = new ClassPathResource("/static/xmlexport/dmd/");
            String chemin = classPathResource.getFile().getPath() + "/dmd" + fichier.getId() + ".xml";

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File file = new File(chemin);
            if (file.createNewFile()) {
                StreamResult result = new StreamResult(file);
                transformer.transform(source, result);
            }
            stringWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // lecture de la  demande ça lit la demande et enregistration dans la bdd ( lors de la reception)
    public void xmlLireProp(String chemin) {
        try {
            File file = new File(chemin);
          ParserXml mondoc=new ParserXml(file);
          Document document=mondoc.getDocument();
            if (document.getElementsByTagName("Prop").getLength() != 0) {
                Fichier fic = new Fichier();
                Message message = new Message();
                Date dateActuelle = new Date();
                List<Message> messages = new ArrayList<>();
                List<Fichier> fichiers = new ArrayList<>();
                Utilisateur uE = utilisateurRepo.findByMail(document.getElementsByTagName("MailExp").item(0).getTextContent());
                Utilisateur uR = utilisateurRepo.findByMail(document.getElementsByTagName("MailDest").item(0).getTextContent());
                fic.setFicid(document.getElementsByTagName("FicID").item(0).getTextContent());
                if (uE == null) {
                    uE = new Utilisateur();
                    uE.setNom(document.getElementsByTagName("NmIE").item(0).getTextContent());
                    uE.setMail(document.getElementsByTagName("MailExp").item(0).getTextContent());
                    utilisateurRepo.save(uE);

                }
                if (uR == null) {
                    uR = new Utilisateur();
                    uR.setNom(document.getElementsByTagName("NmIR").item(0).getTextContent());
                    uR.setMail(document.getElementsByTagName("MailDest").item(0).getTextContent());
                    utilisateurRepo.save(uR);
                }
                fic.setIE(uE);
                fic.setIR(uR);
                fichierRepo.save(fic);

                Prop prop = new Prop();
                prop.setReceved(true);
                
                prop.setTitre(document.getElementsByTagName("TitreP").item(0).getTextContent());
                Offre offre=new Offre();
                Objet objet;
                Description description;
                Parametre param;
                List<Parametre> listeparam=new ArrayList();
                Set<Objet> listeobj=new HashSet();
                NodeList objetsListOffre=document.getElementsByTagName("Offre").item(0).getChildNodes();
                NodeList objetParam;
                    
                for(int i=0;i<objetsListOffre.getLength() ;i++){
                    objet=new Objet();
                    description=new Description();
                    listeparam=new ArrayList();
                    listeobj=new HashSet();
                    objetParam=objetsListOffre.item(i).getChildNodes().item(1).getChildNodes();
                    objet.setType(objetsListOffre.item(i).getFirstChild().getTextContent());
                    for(int k=0;k<objetParam.getLength();k++){
                        param=new Parametre();
                        param.setNom(objetParam.item(k).getFirstChild().getTextContent());
                        param.setValeur(objetParam.item(k).getLastChild().getTextContent());
                       listeparam.add(param);
                    }
                    description.setParametres(listeparam);
                    paramRepo.saveAll(listeparam);
                    descRepo.save(description);
                    objet.setDescription(description);
                    objetRepo.save(objet);
                    listeobj.add(objet);
                    
                }
                 offre.setObjets(listeobj);
                prop.setOffre(offre);
                offreRepo.save(offre);
                  NodeList objetsListDemande=document.getElementsByTagName("Demande").item(0).getChildNodes();
              Demande demande=new Demande();
                 for(int i=0;i<objetsListDemande.getLength() ;i++){
                    objet=new Objet();
                    description=new Description();
                    listeparam=new ArrayList();
                    listeobj=new HashSet();
                    objetParam=objetsListDemande.item(i).getChildNodes().item(1).getChildNodes();
                    objet.setType(objetsListDemande.item(i).getFirstChild().getTextContent());
                    for(int k=0;k<objetParam.getLength();k++){
                        param=new Parametre();
                        param.setNom(objetParam.item(k).getFirstChild().getTextContent());
                        param.setValeur(objetParam.item(k).getLastChild().getTextContent());
                       listeparam.add(param);
                    }
                    description.setParametres(listeparam);
                    paramRepo.saveAll(listeparam);
                    descRepo.save(description);
                    objet.setDescription(description);
                    objetRepo.save(objet);
                    listeobj.add(objet);
                }
                 demande.setObjets(listeobj);
                 prop.setDemande(demande);
                demandeRepo.save(demande);
                propRepo.save(prop);
                message.setProp(prop);
                message.setDureeValide(Integer.parseInt(document.getElementsByTagName("DureeValideMsg").item(0).getTextContent()));
                message.setDte(dateActuelle);
                message.setFichier(fic);

                messageRepo.save(message);
                messages.add(message);
                fic.setMessages(messages);
                fichierRepo.save(fic);
                fichiers.add(fic);
                uE.setFichiersEm(fichiers);
                utilisateurRepo.save(uE);

                uR.setFichiersRec(fichiers);
                utilisateurRepo.save(uR);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
     public void xmlLireDmd(String chemin) {
        try {
            File file = new File(chemin);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            if (document.getElementsByTagName("Dmd").getLength() != 0) {
                Fichier fic = new Fichier();
                Message message = new Message();
                Date dateActuelle = new Date();
                List<Message> messages = new ArrayList<>();
                List<Fichier> fichiers = new ArrayList<>();
                Utilisateur uE = utilisateurRepo.findByMail(document.getElementsByTagName("MailExp").item(0).getTextContent());
                Utilisateur uR = utilisateurRepo.findByMail(document.getElementsByTagName("MailDest").item(0).getTextContent());
                fic.setFicid(document.getElementsByTagName("FicID").item(0).getTextContent());
                if (uE == null) {
                    uE = new Utilisateur();
                    uE.setNom(document.getElementsByTagName("NmIE").item(0).getTextContent());
                    uE.setMail(document.getElementsByTagName("MailExp").item(0).getTextContent());
                    utilisateurRepo.save(uE);

                }
                if (uR == null) {
                    uR = new Utilisateur();
                    uR.setNom(document.getElementsByTagName("NmIR").item(0).getTextContent());
                    uR.setMail(document.getElementsByTagName("MailDest").item(0).getTextContent());
                    utilisateurRepo.save(uR);
                }
                fic.setIE(uE);
                fic.setIR(uR);
                fichierRepo.save(fic);

                Dmd dmd = new Dmd();
                dmd.setReceved(true);
                dmd.setDateDebut(dateActuelle);
                dmd.setDateFin(dateActuelle);
                dmd.setDescription(document.getElementsByTagName("DescDmd").item(0).getTextContent());
                dmdRepo.save(dmd);
                message.setDmd(dmd);
                message.setDureeValide(Integer.parseInt(document.getElementsByTagName("DureeValideMsg").item(0).getTextContent()));
                message.setDte(dateActuelle);
                message.setFichier(fic);

                messageRepo.save(message);
                messages.add(message);
                fic.setMessages(messages);
                fichierRepo.save(fic);
                fichiers.add(fic);
                uE.setFichiersEm(fichiers);
                utilisateurRepo.save(uE);

                uR.setFichiersRec(fichiers);
                utilisateurRepo.save(uR);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
public void xmlLireAuth(String chemin) {
        try {
            File file = new File(chemin);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            if (document.getElementsByTagName("Auth").getLength() != 0) {
                Fichier fic = fichierRepo.findByFicid(document.getElementsByTagName("FicID").item(0).getTextContent()).get();
               
                
                Auth auth = new Auth();
                if(document.getElementsByTagName("AccAuth").getLength() != 0)
                    auth.setAccepte(true);
                else
                    auth.setAccepte(false);
                
                auth.setNumAuth(document.getElementsByTagName("NumAuto").item(0).getTextContent());
                authRepo.save(auth);
                fic.getMessages().get(0).setAuth(auth);
                messageRepo.save(fic.getMessages().get(0));
                fichierRepo.save(fic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void xmlCreateProp(Fichier fichier) {

        try {
            StringWriter stringWriter = new StringWriter();
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xMLStreamWriter =
                    xMLOutputFactory.createXMLStreamWriter(stringWriter);


            xMLStreamWriter.writeStartDocument();
            xMLStreamWriter.writeProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"xsl/prop.xsl\"");
            xMLStreamWriter.writeStartElement("Fichier");
            xMLStreamWriter.writeStartElement("Header");
            xMLStreamWriter.writeStartElement("FicID");
            xMLStreamWriter.writeCharacters(fichier.getFicid());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("NmIE");
            xMLStreamWriter.writeCharacters(fichier.getIE().getNom());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("NmIR");
            xMLStreamWriter.writeCharacters(fichier.getIR().getNom());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("MailDest");
            xMLStreamWriter.writeCharacters(fichier.getIR().getMail());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("MailExp");
            xMLStreamWriter.writeCharacters(fichier.getIE().getMail());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("Body");
            xMLStreamWriter.writeStartElement("CollMess");
            xMLStreamWriter.writeAttribute("NbOfTxs", "1");
            xMLStreamWriter.writeStartElement("Message");
            xMLStreamWriter.writeAttribute("MsgId", fichier.getMessages().get(0).getMsgId());
            xMLStreamWriter.writeStartElement("Dte");
            xMLStreamWriter.writeCharacters(fichier.getMessages().get(0).getDte().toString());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("DureeValideMsg");
            xMLStreamWriter.writeCharacters(Integer.toString(fichier.getMessages().get(0).getDureeValide()));
            xMLStreamWriter.writeEndElement();

            xMLStreamWriter.writeStartElement("Prop");
            xMLStreamWriter.writeStartElement("TitreP");
            xMLStreamWriter.writeCharacters(fichier.getMessages().get(0).getProp().getTitre());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("Offre");
            Objet[] objetsOffre=fichier.getMessages().get(0).getProp().getOffre().getObjets().toArray(new Objet[fichier.getMessages().get(0).getProp().getOffre().getObjets().size()]);
            for (int j=0;j<fichier.getMessages().get(0).getProp().getOffre().getObjets().size();j++){
            xMLStreamWriter.writeStartElement("Objet");
            xMLStreamWriter.writeStartElement("Type");
            xMLStreamWriter.writeCharacters(objetsOffre[j].getType());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("Description");
            for (int k=0;k<objetsOffre[j].getDescription().getParametres().size();k++){
            xMLStreamWriter.writeStartElement("Parametre");
            xMLStreamWriter.writeStartElement("Nom");
            xMLStreamWriter.writeCharacters(objetsOffre[j].getDescription().getParametres().get(k).getNom());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("Valeur");
            xMLStreamWriter.writeCharacters(objetsOffre[j].getDescription().getParametres().get(k).getValeur());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            }
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            }
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("Demande");
             Objet[] objetsDemande=fichier.getMessages().get(0).getProp().getDemande().getObjets().toArray(new Objet[fichier.getMessages().get(0).getProp().getDemande().getObjets().size()]);
           for (int j=0;j<fichier.getMessages().get(0).getProp().getDemande().getObjets().size();j++){
            xMLStreamWriter.writeStartElement("Objet");
            xMLStreamWriter.writeStartElement("Type");
            xMLStreamWriter.writeCharacters(objetsDemande[j].getType());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("Description");
            for (int k=0;k<objetsDemande[j].getDescription().getParametres().size();k++){
            xMLStreamWriter.writeStartElement("Parametre");
            xMLStreamWriter.writeStartElement("Nom");
            xMLStreamWriter.writeCharacters(objetsDemande[j].getDescription().getParametres().get(k).getNom());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("Valeur");
            xMLStreamWriter.writeCharacters(objetsDemande[j].getDescription().getParametres().get(k).getValeur());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            }
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            }
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();

            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndDocument();


            xMLStreamWriter.flush();
            xMLStreamWriter.close();
            // creer le fichier xml
            String xmlString = stringWriter.getBuffer().toString();

            ClassPathResource classPathResource = new ClassPathResource("/static/xmlexport/prop/");
            String chemin = classPathResource.getFile().getPath() + "/prop" + fichier.getId() + ".xml";

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File file = new File(chemin);
            if (file.createNewFile()) {
                StreamResult result = new StreamResult(file);
                transformer.transform(source, result);
            }
            stringWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }{
}
    public void xmlCreerAuth(Fichier fichier) {
        try {
            Auth auth = fichier.getMessages().get(0).getAuth();
            StringWriter stringWriter = new StringWriter();
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xMLStreamWriter =
                    xMLOutputFactory.createXMLStreamWriter(stringWriter);

            xMLStreamWriter.writeStartDocument();
            xMLStreamWriter.writeStartElement("Fichier");
            xMLStreamWriter.writeStartElement("Header");
            xMLStreamWriter.writeStartElement("FicID");
            xMLStreamWriter.writeCharacters(fichier.getFicid());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("NmIE");
            xMLStreamWriter.writeCharacters(fichier.getIE().getNom());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("NmIR");
            xMLStreamWriter.writeCharacters(fichier.getIR().getNom());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("NumAuto");
            xMLStreamWriter.writeCharacters(auth.getNumAuth());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("MailDest");
            xMLStreamWriter.writeCharacters(fichier.getIR().getMail());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("MailExp");
            xMLStreamWriter.writeCharacters(fichier.getIE().getMail());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();            //start of body
            xMLStreamWriter.writeStartElement("Body");
            //start of colMess
            xMLStreamWriter.writeStartElement("CollMess");
            xMLStreamWriter.writeAttribute("NbOfTxs", "1");
            xMLStreamWriter.writeStartElement("Message");
            xMLStreamWriter.writeAttribute("MsgId", fichier.getMessages().get(0).getMsgId());
            xMLStreamWriter.writeStartElement("Dte");
            xMLStreamWriter.writeCharacters(fichier.getMessages().get(0).getDte().toString());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("DureeValideMsg");
            xMLStreamWriter.writeCharacters(Integer.toString(fichier.getMessages().get(0).getDureeValide()));
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("Auth");
            xMLStreamWriter.writeStartElement("Rep");
            if (auth.isAccepte()) {
                xMLStreamWriter.writeStartElement("AccAuth");
                xMLStreamWriter.writeCharacters(auth.getMessageAuth());
                xMLStreamWriter.writeEndElement();
            } else {
                xMLStreamWriter.writeStartElement("RefAuth");
                xMLStreamWriter.writeCharacters(auth.getMessageAuth());
                xMLStreamWriter.writeEndElement();
            }
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            //end of colMess
            xMLStreamWriter.writeEndElement();
            //end of body
            xMLStreamWriter.writeEndElement();
            //end of fichier
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndDocument();


            xMLStreamWriter.flush();
            xMLStreamWriter.close();

            String xmlString = stringWriter.getBuffer().toString();
            stringWriter.close();
            ClassPathResource classPathResource = new ClassPathResource("/static/xmlexport/");
            String chemin = classPathResource.getFile().getPath() + "/auth" + fichier.getId() + ".xml";

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File file = new File(chemin);
            if (file.createNewFile()) {
                StreamResult result = new StreamResult(file);
                transformer.transform(source, result);
            }
            stringWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean verifierFichierTraite(String chemin, String type){

        try {
            File file = new File(chemin);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            if(document.getElementsByTagName("FicID").getLength() != 1)
                return false;
           
            String ficId = document.getElementsByTagName("FicID").item(0).getTextContent();
            if(type.equals("auth")){
                 if(fichierRepo.estTraite(ficId) != 1)
                     return false;
             Fichier fic = fichierRepo.findByFicid(document.getElementsByTagName("FicID").item(0).getTextContent()).get();
             if(type.equals("prop") && fic.getMessages().get(0).getAuth()==null)
                 return false;
                    if(type.equals("prop") && fic.getMessages().get(0).getAuth()!=null && type.equals("prop") && !fic.getMessages().get(0).getAuth().isAccepte())
                 return false;
                 
               if(fic.getMessages().get(0).getAuth()!=null)
                   return false;
            }
            if(type.equals("prop")){
                 if(fichierRepo.estTraite(ficId) != 1)
                     return false;
             Fichier fic = fichierRepo.findByFicid(document.getElementsByTagName("FicID").item(0).getTextContent()).get();
               if(fic.getMessages().get(0).getAuth()==null)
                   return false;
            }
            if(fichierRepo.estTraite(ficId) != 0 && !type.equals("auth") && !type.equals("prop"))
                return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
