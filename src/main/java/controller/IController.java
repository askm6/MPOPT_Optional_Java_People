package controller;

import model.entity.Person;
import java.util.ArrayList;

/**
 * Interface that defines the type of operations that the controller and 
 * therefore the application can perform.
 * @author Francesc Perez
 * @version 1.1.0
 */
public interface IController {
    public abstract Person read(Person p);
    public abstract ArrayList<Person> readAll();
    public abstract void insert(Person p) throws Exception;
    public abstract void update(Person p);
    public abstract void delete(Person p);
    public abstract void start();
    public abstract void deleteAll();
}
