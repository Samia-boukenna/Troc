package com.samia.dsctroc.core;

import com.samia.dsctroc.models.Dmd;
import com.samia.dsctroc.models.Fichier;
import com.samia.dsctroc.models.Message;
import com.samia.dsctroc.models.Utilisateur;
import com.samia.dsctroc.repositories.DmdRepo;
import com.samia.dsctroc.repositories.FichierRepo;
import com.samia.dsctroc.repositories.MessageRepo;
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

@Service
@Configurable
public class Xml {


    @Autowired
    private UtilisateurRepo utilisateurRepo;

    @Autowired
    private DmdRepo dmdRepo;

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
            xMLStreamWriter.writeCharacters(fichier.getFicId());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("nmIE");
            xMLStreamWriter.writeCharacters(fichier.getIE().getNom());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("nmIR");
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

            ClassPathResource classPathResource = new ClassPathResource("/static/xmlexport/");
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
                if (uE == null) {
                    uE = new Utilisateur();
                    uE.setNom(document.getElementsByTagName("nmIE").item(0).getTextContent());
                    uE.setMail(document.getElementsByTagName("MailExp").item(0).getTextContent());
                    utilisateurRepo.save(uE);

                }
                if (uR == null) {
                    uR = new Utilisateur();
                    uR.setNom(document.getElementsByTagName("nmIR").item(0).getTextContent());
                    uR.setMail(document.getElementsByTagName("MailDest").item(0).getTextContent());
                    utilisateurRepo.save(uR);
                }
                fic.setIE(uE);
                fic.setIR(uR);
                fichierRepo.save(fic);

                Dmd dmd = new Dmd();
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

    public void xmlCreateProp(Fichier fichier) {

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
            xMLStreamWriter.writeCharacters(fichier.getFicId());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("nmIE");
            xMLStreamWriter.writeCharacters(fichier.getIE().getNom());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("nmIR");
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
            xMLStreamWriter.writeStartElement("Objet");
            xMLStreamWriter.writeStartElement("Type");
            xMLStreamWriter.writeCharacters(fichier.getMessages().get(0).getProp().getOffre().getObjets().get(0).getType());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("Description");
            xMLStreamWriter.writeStartElement("Parametre");
            xMLStreamWriter.writeStartElement("Nom");
            xMLStreamWriter.writeCharacters(fichier.getMessages().get(0).getProp().getOffre().getObjets().get(0).getDescription().getParametres().get(0).getNom());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("Valeur");
            xMLStreamWriter.writeCharacters(fichier.getMessages().get(0).getProp().getOffre().getObjets().get(0).getDescription().getParametres().get(0).getValeur());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("Demande");
            xMLStreamWriter.writeStartElement("Objet");
            xMLStreamWriter.writeStartElement("Type");
            xMLStreamWriter.writeCharacters(fichier.getMessages().get(0).getProp().getDemande().getObjets().get(0).getType());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("Description");
            xMLStreamWriter.writeStartElement("Parametre");
            xMLStreamWriter.writeStartElement("Nom");
            xMLStreamWriter.writeCharacters(fichier.getMessages().get(0).getProp().getDemande().getObjets().get(0).getDescription().getParametres().get(0).getNom());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeStartElement("Valeur");
            xMLStreamWriter.writeCharacters(fichier.getMessages().get(0).getProp().getDemande().getObjets().get(0).getDescription().getParametres().get(0).getValeur());
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndElement();
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

            ClassPathResource classPathResource = new ClassPathResource("/static/xmlexport/");
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
    }
}
