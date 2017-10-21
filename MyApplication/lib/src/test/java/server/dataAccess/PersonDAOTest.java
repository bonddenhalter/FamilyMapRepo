package server.dataAccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.models.Person;

import static org.junit.Assert.*;

/**
 * Created by bondd on 10/20/2017.
 */
public class PersonDAOTest {

    private PersonDAO personDAO;

    @Before
    public void setUp() throws Exception {
        personDAO = new PersonDAO();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testPersonDAO() throws Exception
    {
        createTable();
        addPerson();
        getPerson();
        addPerson2();
  //      getPeople();
    }

    @Test
    public void createTable() throws Exception {
        personDAO.createTable();
    }

    private String personID = "fake person id";
    private String descendant = "fake descendant";
    private String firstName = "fake first name";
    private String lastName = "fake last name";
    private String gender = "fake gender";
    private String father = "fake father";
    private String mother = "fake mother";
    private String spouse = "fake spouse";

    private String personID2 = "fake person id 2";
    private String firstName2 = "fake first name 2";
    private String lastName2 = "fake last name 2";
    private String gender2 = "fake gender 2";
    private String father2 = "fake father 2";
    private String mother2 = "fake mother 2";
    private String spouse2 = "fake spouse 2";


    public void addPerson() throws Exception {
        Person p = new Person(personID, descendant, firstName, lastName, gender, father, mother, spouse);
        personDAO.addPerson(p);
    }

    public void addPerson2() throws Exception
    {
        //descendant is the same so both people will be returned when I call getPeople()
        Person p = new Person(personID2, descendant, firstName2, lastName2, gender2, father2, mother2, spouse2);
        personDAO.addPerson(p);
    }


    public void getPerson() throws Exception
    {
        Person p = personDAO.getPerson(personID);
        assertNotNull(p);
        assertEquals(p.getPersonID(), personID);
        assertEquals(p.getDescendant(), descendant);
        assertEquals(p.getFirstName(), firstName);
        assertEquals(p.getLastName(), lastName);
        assertEquals(p.getGender(), gender);
        assertEquals(p.getFather(), father);
        assertEquals(p.getMother(), mother);
        assertEquals(p.getSpouse(), spouse);

        //test nonexistent person
        Person p2 = personDAO.getPerson("nonexistent ID");
        assertNull(p2);
    }


    public void getPeople() throws Exception {

    }

}