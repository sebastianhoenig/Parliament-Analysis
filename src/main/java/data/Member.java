package data;


import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Date;
//TODO: Add Javadoc
public interface Member {
    String setName(Node Mdb);

    String setSurname(Node Mdb);

    String setParty(Node Mdb);

    Date setBirthDate(Node Mdb);

    String setId(Node Mdb);

    String setPlaceOfBirth(Node Mdb);

    String setCountryOfBirth(Node Mdb);

    Date setDateOfDeath(Node Mdb);

    String setGender(Node Mdb);

    String setMartialStatus(Node Mdb);

    String setReligion(Node Mdb);

    String setOccupation(Node Mdb);

    String[] setMetaData(String name, String surname);

    public String getName();

    public String getSurname();

    public String getParty();

    public Date getBirthDate();

    public String getId();

    public String getPlaceOfBirth();

    public String getCountryOfBirth();

    public Date getDateOfDeath();

    public String getGender();

    public String getMaritalStatus();

    public String getOccupation();

    public String getReligion();

    public String[] getMetaData();

    public String getFullInfoForTesting();

    public void addSpeech(String speechID);

    public ArrayList<String> getallSpeeches();
}
