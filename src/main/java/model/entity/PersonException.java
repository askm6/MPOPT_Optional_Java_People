package model.entity;

/**
 * Class that defines its own exception, PersonException. Every time there is a 
 * problem when doing the CRUD, not related to access to the storage device, 
 * an exception of this type will be thrown.
 * @author Francesc Perez
 * @version 1.1.0
 */
public class PersonException extends Exception{

    public PersonException(String message) {
        super(message);
    }

}
