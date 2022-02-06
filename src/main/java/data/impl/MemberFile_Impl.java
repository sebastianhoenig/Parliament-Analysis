package data.impl;

import DownloadData.ScrapePictures;
import data.Member;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MemberFile_Impl implements Member {
    String name;
    String surname;
    String party;
    Date birthDate;
    String[] metaData;
    String id;
    String placeOfBirth;
    String countryOfBirth;
    Date dateOfDeath;
    String gender;
    String maritalStatus;
    String religion;
    String occupation;
    ArrayList<String> allSpeeches;


    public MemberFile_Impl(Node Mdb) {
        this.name = setName(Mdb);
        this.surname = setSurname(Mdb);
        this.party = setParty(Mdb);
        this.birthDate = setBirthDate(Mdb);
        this.id = setId(Mdb);
        this.placeOfBirth = setPlaceOfBirth(Mdb);
        this.countryOfBirth = setCountryOfBirth(Mdb);
        this.dateOfDeath = setDateOfDeath(Mdb);
        this.gender = setGender(Mdb);
        this.maritalStatus = setMartialStatus(Mdb);
        this.religion = setReligion(Mdb);
        this.occupation = setOccupation(Mdb);
        this.metaData = setMetaData(name, surname);
    }

    public String setName(Node Mdb) {
        NodeList memberData = Mdb.getChildNodes();
        for (int i=0; i<memberData.getLength(); i++) {
            Node data = memberData.item(i);
            if (data.getNodeType() == Node.ELEMENT_NODE) {
                Element dataElement = (Element) data;
                if (dataElement.getTagName().equalsIgnoreCase("NAMEN")) {
                    NodeList nameAttributes = dataElement.getChildNodes();
                    for (int j=0; j<nameAttributes.getLength(); j++) {
                        Node nameAttribute = nameAttributes.item(j);
                        if (nameAttribute.getNodeType() == Node.ELEMENT_NODE) {
                            Element nameElement = (Element) nameAttribute;
                            if (nameElement.getTagName().equalsIgnoreCase("NAME")) {
                                NodeList specificNameAttributes = nameElement.getChildNodes();
                                for (int k=0; j< specificNameAttributes.getLength(); k++) {
                                    Node specificNameAttribute = specificNameAttributes.item(k);
                                    if (specificNameAttribute.getNodeType() == Node.ELEMENT_NODE) {
                                        Element specificNameElement = (Element) specificNameAttribute;
                                        if (specificNameElement.getTagName().equalsIgnoreCase("VORNAME")) {
                                            return specificNameElement.getTextContent();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return "No name found";
    }

    public String setSurname(Node Mdb) {
        NodeList memberData = Mdb.getChildNodes();
        for (int i=0; i<memberData.getLength(); i++) {
            Node data = memberData.item(i);
            if (data.getNodeType() == Node.ELEMENT_NODE) {
                Element dataElement = (Element) data;
                if (dataElement.getTagName().equalsIgnoreCase("NAMEN")) {
                    NodeList nameAttributes = dataElement.getChildNodes();
                    for (int j=0; j<nameAttributes.getLength(); j++) {
                        Node nameAttribute = nameAttributes.item(j);
                        if (nameAttribute.getNodeType() == Node.ELEMENT_NODE) {
                            Element nameElement = (Element) nameAttribute;
                            if (nameElement.getTagName().equalsIgnoreCase("NAME")) {
                                NodeList specificNameAttributes = nameElement.getChildNodes();
                                for (int k=0; j< specificNameAttributes.getLength(); k++) {
                                    Node specificNameAttribute = specificNameAttributes.item(k);
                                    if (specificNameAttribute.getNodeType() == Node.ELEMENT_NODE) {
                                        Element specificNameElement = (Element) specificNameAttribute;
                                        if (specificNameElement.getTagName().equalsIgnoreCase("NACHNAME")) {
                                            return specificNameElement.getTextContent();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return "No surname found";
    }

    public String setParty(Node Mdb) {
        NodeList memberData = Mdb.getChildNodes();
        for (int i=0; i<memberData.getLength(); i++) {
            Node data = memberData.item(i);
            if (data.getNodeType() == Node.ELEMENT_NODE) {
                Element dataElement = (Element) data;
                if (dataElement.getTagName().equalsIgnoreCase("BIOGRAFISCHE_ANGABEN")) {
                    NodeList biographyAttributes = dataElement.getChildNodes();
                    for (int j=0; j<biographyAttributes.getLength(); j++) {
                        Node biographyAttribute = biographyAttributes.item(j);
                        if (biographyAttribute.getNodeType() == Node.ELEMENT_NODE) {
                            Element biographyElement = (Element) biographyAttribute;
                            if (biographyElement.getTagName().equalsIgnoreCase("PARTEI_KURZ")) {
                                return biographyElement.getTextContent();
                            }
                        }
                    }
                }
            }
        }
        return "No party found";
    }

    public Date setBirthDate(Node Mdb) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        NodeList memberData = Mdb.getChildNodes();
        for (int i=0; i<memberData.getLength(); i++) {
            Node data = memberData.item(i);
            if (data.getNodeType() == Node.ELEMENT_NODE) {
                Element dataElement = (Element) data;
                if (dataElement.getTagName().equalsIgnoreCase("BIOGRAFISCHE_ANGABEN")) {
                    NodeList biographyAttributes = dataElement.getChildNodes();
                    for (int j=0; j<biographyAttributes.getLength(); j++) {
                        Node biographyAttribute = biographyAttributes.item(j);
                        if (biographyAttribute.getNodeType() == Node.ELEMENT_NODE) {
                            Element biographyElement = (Element) biographyAttribute;
                            if (biographyElement.getTagName().equalsIgnoreCase("GEBURTSDATUM")) {
                                try {
                                    Date date = formatter.parse(biographyElement.getTextContent());
                                    return date;
                                }
                                catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public String setId(Node Mdb) {
        NodeList memberData = Mdb.getChildNodes();
        for (int i=0; i<memberData.getLength(); i++) {
            Node data = memberData.item(i);
            if (data.getNodeType() == Node.ELEMENT_NODE) {
                Element dataElement = (Element) data;
                if (dataElement.getTagName().equalsIgnoreCase("ID")) {
                    return dataElement.getTextContent();
                }
            }
        }
        return "No Id found";
    }

    public String setPlaceOfBirth(Node Mdb) {
        NodeList memberData = Mdb.getChildNodes();
        for (int i=0; i<memberData.getLength(); i++) {
            Node data = memberData.item(i);
            if (data.getNodeType() == Node.ELEMENT_NODE) {
                Element dataElement = (Element) data;
                if (dataElement.getTagName().equalsIgnoreCase("BIOGRAFISCHE_ANGABEN")) {
                    NodeList biographyAttributes = dataElement.getChildNodes();
                    for (int j=0; j<biographyAttributes.getLength(); j++) {
                        Node biographyAttribute = biographyAttributes.item(j);
                        if (biographyAttribute.getNodeType() == Node.ELEMENT_NODE) {
                            Element biographyElement = (Element) biographyAttribute;
                            if (biographyElement.getTagName().equalsIgnoreCase("GEBURTSORT")) {
                                return biographyElement.getTextContent();
                            }
                        }
                    }
                }
            }
        }
        return "No place of birth found";
    }

    public String setCountryOfBirth(Node Mdb) {
        NodeList memberData = Mdb.getChildNodes();
        for (int i=0; i<memberData.getLength(); i++) {
            Node data = memberData.item(i);
            if (data.getNodeType() == Node.ELEMENT_NODE) {
                Element dataElement = (Element) data;
                if (dataElement.getTagName().equalsIgnoreCase("BIOGRAFISCHE_ANGABEN")) {
                    NodeList biographyAttributes = dataElement.getChildNodes();
                    for (int j=0; j<biographyAttributes.getLength(); j++) {
                        Node biographyAttribute = biographyAttributes.item(j);
                        if (biographyAttribute.getNodeType() == Node.ELEMENT_NODE) {
                            Element biographyElement = (Element) biographyAttribute;
                            if (biographyElement.getTagName().equalsIgnoreCase("GEBURTSLAND")) {
                                return biographyElement.getTextContent();
                            }
                        }
                    }
                }
            }
        }
        return "No country of birth found";
    }

    public Date setDateOfDeath(Node Mdb) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        NodeList memberData = Mdb.getChildNodes();
        for (int i=0; i<memberData.getLength(); i++) {
            Node data = memberData.item(i);
            if (data.getNodeType() == Node.ELEMENT_NODE) {
                Element dataElement = (Element) data;
                if (dataElement.getTagName().equalsIgnoreCase("BIOGRAFISCHE_ANGABEN")) {
                    NodeList biographyAttributes = dataElement.getChildNodes();
                    for (int j=0; j<biographyAttributes.getLength(); j++) {
                        Node biographyAttribute = biographyAttributes.item(j);
                        if (biographyAttribute.getNodeType() == Node.ELEMENT_NODE) {
                            Element biographyElement = (Element) biographyAttribute;
                            if (biographyElement.getTagName().equalsIgnoreCase("STERBEDATUM")) {
                                try {
                                    return formatter.parse(biographyElement.getTextContent());
                                }
                                catch (ParseException e) {
//                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public String setGender(Node Mdb) {
        NodeList memberData = Mdb.getChildNodes();
        for (int i=0; i<memberData.getLength(); i++) {
            Node data = memberData.item(i);
            if (data.getNodeType() == Node.ELEMENT_NODE) {
                Element dataElement = (Element) data;
                if (dataElement.getTagName().equalsIgnoreCase("BIOGRAFISCHE_ANGABEN")) {
                    NodeList biographyAttributes = dataElement.getChildNodes();
                    for (int j=0; j<biographyAttributes.getLength(); j++) {
                        Node biographyAttribute = biographyAttributes.item(j);
                        if (biographyAttribute.getNodeType() == Node.ELEMENT_NODE) {
                            Element biographyElement = (Element) biographyAttribute;
                            if (biographyElement.getTagName().equalsIgnoreCase("GESCHLECHT")) {
                                return biographyElement.getTextContent();
                            }
                        }
                    }
                }
            }
        }
        return "No gender found";
    }

    public String setMartialStatus(Node Mdb) {
        NodeList memberData = Mdb.getChildNodes();
        for (int i=0; i<memberData.getLength(); i++) {
            Node data = memberData.item(i);
            if (data.getNodeType() == Node.ELEMENT_NODE) {
                Element dataElement = (Element) data;
                if (dataElement.getTagName().equalsIgnoreCase("BIOGRAFISCHE_ANGABEN")) {
                    NodeList biographyAttributes = dataElement.getChildNodes();
                    for (int j=0; j<biographyAttributes.getLength(); j++) {
                        Node biographyAttribute = biographyAttributes.item(j);
                        if (biographyAttribute.getNodeType() == Node.ELEMENT_NODE) {
                            Element biographyElement = (Element) biographyAttribute;
                            if (biographyElement.getTagName().equalsIgnoreCase("FAMILIENSTAND")) {
                                return biographyElement.getTextContent();
                            }
                        }
                    }
                }
            }
        }
        return "No martial status found";
    }

    public String setReligion(Node Mdb) {
        NodeList memberData = Mdb.getChildNodes();
        for (int i=0; i<memberData.getLength(); i++) {
            Node data = memberData.item(i);
            if (data.getNodeType() == Node.ELEMENT_NODE) {
                Element dataElement = (Element) data;
                if (dataElement.getTagName().equalsIgnoreCase("BIOGRAFISCHE_ANGABEN")) {
                    NodeList biographyAttributes = dataElement.getChildNodes();
                    for (int j=0; j<biographyAttributes.getLength(); j++) {
                        Node biographyAttribute = biographyAttributes.item(j);
                        if (biographyAttribute.getNodeType() == Node.ELEMENT_NODE) {
                            Element biographyElement = (Element) biographyAttribute;
                            if (biographyElement.getTagName().equalsIgnoreCase("RELIGION")) {
                                return biographyElement.getTextContent();
                            }
                        }
                    }
                }
            }
        }
        return "No religion found";
    }

    public String setOccupation(Node Mdb) {
        NodeList memberData = Mdb.getChildNodes();
        for (int i=0; i<memberData.getLength(); i++) {
            Node data = memberData.item(i);
            if (data.getNodeType() == Node.ELEMENT_NODE) {
                Element dataElement = (Element) data;
                if (dataElement.getTagName().equalsIgnoreCase("BIOGRAFISCHE_ANGABEN")) {
                    NodeList biographyAttributes = dataElement.getChildNodes();
                    for (int j=0; j<biographyAttributes.getLength(); j++) {
                        Node biographyAttribute = biographyAttributes.item(j);
                        if (biographyAttribute.getNodeType() == Node.ELEMENT_NODE) {
                            Element biographyElement = (Element) biographyAttribute;
                            if (biographyElement.getTagName().equalsIgnoreCase("BERUF")) {
                                return biographyElement.getTextContent();
                            }
                        }
                    }
                }
            }
        }
        return "No occupation found";
    }

    public String[] setMetaData(String name, String surname) {
        ScrapePictures scraper = new ScrapePictures(name, surname);
        return scraper.getMetaData();
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getParty() {
        return this.party;
    }

    public Date getBirthDate() {
        return this.birthDate;
    }

    public String getId() {return this.id; }

    public String getPlaceOfBirth() {return this.placeOfBirth;}

    public String getCountryOfBirth() {return this.countryOfBirth;}

    public Date getDateOfDeath() {return this.dateOfDeath;}

    public String getGender() {return this.gender;};

    public String getMaritalStatus() {return this.maritalStatus;}

    public String getOccupation() {return this.occupation;}

    public String getReligion() {return this.religion;}

    public String[] getMetaData() {return this.metaData;}

    public String getFullInfoForTesting() {
        String name = getName();
        String surname = getSurname();
        String party = getParty();
        Date birthDate = getBirthDate();
        String id = getId();
        String placeOfBirth = getPlaceOfBirth();
        String countryOfBirth = getCountryOfBirth();
        Date dateOfDeath = getDateOfDeath();
        String gender = getGender();
        String maritalStatus = getMaritalStatus();
        String religion = getReligion();
        String occupation = getOccupation();
        String[] metaData = getMetaData();
        String final_ = String.format("Full Name: %1s %2s, Party: %3s, Birthdate: %4s, ID: %5s, Place/Country: %6s, %7s, " +
                "Deathdate: %8s, Gender: %9s, martial:%10s, religion:%11s, occupation: %12s, picture: %13s, text: %14s",
                name, surname, party, birthDate, id, placeOfBirth, countryOfBirth, dateOfDeath, gender, maritalStatus, religion, occupation, metaData[0], metaData[1]);
        return final_;
    }

    public void addSpeech(String speechID) {
        this.allSpeeches.add(speechID);
    }

    public ArrayList<String> getallSpeeches() {
        return this.allSpeeches;
    }

}
