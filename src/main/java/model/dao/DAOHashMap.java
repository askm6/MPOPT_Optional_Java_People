package model.dao;

import model.entity.Person;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements the IDAO interface and completes the function code
 * blocks so that they can operate with a HashMap structure. The NIF is used as
 * the key.
 *
 * @author Francesc Perez
 * @version 1.1.0
 */
public class DAOHashMap implements IDAO {

    HashMap<String, Person> people = new HashMap();

    @Override
    public Person read(Person p){
        return people.containsKey(p.getNif()) ? people.get(p.getNif()) : null;
    }
    
    @Override
    public void insert(Person p) {
        people.put(p.getNif(), p);
    }
    
    @Override
    public void delete(Person p){
        people.remove(p.getNif());
    }
    
    @Override
    public void deleteAll(){
        people.clear();
    }
    
    @Override
    public void update(Person p) {
        people.replace(p.getNif(), p);
    }

    @Override
    public ArrayList<Person> readAll() {
        return new ArrayList<>(people.values());
    }

}
