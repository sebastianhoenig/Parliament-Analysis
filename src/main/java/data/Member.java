package data;

/**
 * @author Sebastian Hönig
 * interface of members
 */

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Date;

public interface Member {
    /**
     * sets name
     * @param Mdb
     * @return
     */
    String setName(Node Mdb);

    /**
     * sets surname
     * @param Mdb
     * @return
     */
    String setSurname(Node Mdb);

    /**
     * sets Party
     * @param Mdb
     * @return
     */
    String setParty(Node Mdb);

    /**
     * sets Date
     * @param Mdb
     * @return
     */
    Date setBirthDate(Node Mdb);

    /**
     * sets Id
     * @param Mdb
     * @return
     */
    String setId(Node Mdb);

    /**
     * sets Place of Birth
     * @param Mdb
     * @return
     */
    String setPlaceOfBirth(Node Mdb);

    /**
     * sets Country of Birth
     * @param Mdb
     * @return
     */
    String setCountryOfBirth(Node Mdb);

    /**
     * sets Death date if applicable
     * @param Mdb
     * @return
     */
    Date setDateOfDeath(Node Mdb);

    /**
     * sets Gender
     * @param Mdb
     * @return
     */
    String setGender(Node Mdb);

    /**
     * sets Martial status
     * @param Mdb
     * @return
     */
    String setMartialStatus(Node Mdb);

    /**
     * sets Religion
     * @param Mdb
     * @return
     */
    String setReligion(Node Mdb);

    /**
     * sets Occupation
     * @param Mdb
     * @return
     */
    String setOccupation(Node Mdb);

    /**
     * sets Picture and picture title
     * @param name
     * @param surname
     * @return
     */
    String[] setMetaData(String name, String surname);

    /**
     * gets Name
     * @return
     */
    public String getName();

    /**
     * gets Surname
     * @return
     */
    public String getSurname();

    /**
     * gets Party
     * @return
     */
    public String getParty();

    /**
     * gets Birthdate
     * @return
     */
    public Date getBirthDate();

    /**
     * gets Id
     * @return
     */
    public String getId();

    /**
     * gets Place of Birth
     * @return
     */
    public String getPlaceOfBirth();

    /**
     * gets Country of Birth
     * @return
     */
    public String getCountryOfBirth();

    /**
     * gets Date of death
     * @return
     */
    public Date getDateOfDeath();

    /**
     * gets gender
     * @return
     */
    public String getGender();

    /**
     * gets martial status
     * @return
     */
    public String getMaritalStatus();

    /**
     * gets occupation
     * @return
     */
    public String getOccupation();

    /**
     * gets religion
     * @return
     */
    public String getReligion();

    /**
     * gets the metadata
     * @return
     */
    public String[] getMetaData();

    /**
     * adds Speech
     * @param speechID
     */
    public void addSpeech(String speechID);

    /**
     * gets all Speeches
     * @return
     */
    public ArrayList<String> getallSpeeches();
}
