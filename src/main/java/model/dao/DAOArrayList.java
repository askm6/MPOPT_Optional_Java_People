package model.dao;

import model.entity.Person;
import java.util.ArrayList;

/**
 * This class implements the IDAO interface and completes the code blocks of 
 * the functions so that they can operate with an ArrayList structure. Thanks 
 * to the overriding of the "equals" method in the Person class, the ArrayList 
 * will not be able to contain objects with the same NIF.
 * @author Francesc Perez 
 * @version 1.1.0
 */
public class DAOArrayList implements IDAO{
    
    ArrayList <Person> people = new ArrayList<>();

    @Override
    public Person read(Person p){
        return people.contains(p) ? people.get(people.indexOf(p)) : null;
    }
    
    @Override
    public void insert(Person p) {
        people.add(p);
    }
    
    @Override
    public void delete(Person p){
        people.remove(p);
    }
    
    @Override
    public void deleteAll(){
        people.clear();
    }
    
    @Override
    public void update(Person p){
        people.set(people.indexOf(p), p);
    }
    
    @Override
    public ArrayList<Person> readAll(){
        return people;
    }
   
}
