package model.dao;

import model.entity.Person;
import java.util.ArrayList;

/**
 * This interface defines the operations that will have to be performed on the 
 * data storage system chosen by the user. Depending on the system, the 
 * implementation varies.
 * @author Francesc Perez 
 * @version 1.1.0
 */
public interface IDAO { 
    /**
     * This function returns, if it exists, the registered person whose NIF 
     * matches the NIF assigned to the person entered as an input argument and 
     * if it does not exist generates a PersonException object that has to be 
     * manage by the controller. 
     * @param p Person
     * @return Person or null
     * @throws java.lang.Exception
     */
    public abstract Person read (Person p) throws Exception;
    
    /**
     * This function returns an ArrayList<> of all registered persons.If 
     * there're not registered person returns a PersonException object that has 
     * to be manage by the controller.
     * @return ArrayList<>
     * @throws java.lang.Exception
     */
    public abstract ArrayList<Person> readAll() throws Exception;
    
    /**
     * This function receives the person who must enter the storage system and 
     * store it into the ArrayList, if it is not possible generates a 
     * PersonException object that has to be manage by the controller. 
     * @param p Person
     * @throws java.lang.Exception
     */
    public abstract void insert(Person p) throws Exception;
    
    /**
     * This function receives the person with the updated data and enters it 
     * into the storage system, if it does not exist generates a 
     * PersonException object that has to be manage by the controller.
     * @param p Person
     * @throws java.lang.Exception
     */
    public abstract void update (Person p) throws Exception;   
    
    /**
     * This function receives the person (NIF) and deletes it from the storage 
     * system, if it does not exist generates a PersonException object that has 
     * to be manage by the Controller.
     * @param p Person
     * @throws java.lang.Exception
     */
    public abstract void delete (Person p) throws Exception;
    
     /**
     * This function deletes all people registered in the application.
     * @throws java.lang.Exception
     */
    public abstract void deleteAll () throws Exception;
}
