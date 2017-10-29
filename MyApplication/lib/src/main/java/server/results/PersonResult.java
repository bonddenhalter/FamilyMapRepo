package server.results;

import server.models.Person;

/**
 * The result of the person request
 */

public class PersonResult {

    private String descendant;
    private String personID;
    private String firstName;
    private String lastName;
    private String gender;
    private String father; //optional
    private String mother; //optional
    private String spouse; //optional
    private String errorMsg;

    public static String SQLFailureMsg = "There was a SQL error while trying to retrieve the person.";
    public static String invalidTokenMsg = "Error: invalid auth token.";
    public static String invalidPersonIDMsg = "Error: invalid PersonID.";
    public static String userPermissiongMsg = "Error: the requested person does not belong to the user.";

    public PersonResult(String descendant, String personID, String firstName, String lastName, String gender, String father, String mother, String spouse) {
        this.descendant = descendant;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;
    }

    public PersonResult(Person p)
    {
        if (p != null)
        {
            descendant = p.getDescendant();
            personID = p.getPersonID();
            firstName = p.getFirstName();
            lastName = p.getLastName();
            gender = p.getGender();
            father = p.getFather();
            mother = p.getMother();
            spouse = p.getSpouse();
        }
    }


    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


}
