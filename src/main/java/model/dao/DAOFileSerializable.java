package model.dao;

import model.entity.Person;
import start.Routes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * This class implements the IDAO interface and completes the code of the
 * functions so that they can work with files to store objects. User data is
 * saved in the "dataFileS.ser" file and the associated photos, if any, are
 * saved with the name NIF.png in the "Photos" folder.
 *
 * @author Francesc Perez
 * @version 1.1.0
 */
public class DAOFileSerializable implements IDAO {

    @Override
    public Person read(Person p) throws IOException, ClassNotFoundException {
        Person personToRead = null;
        FileInputStream fIS = null;
        ObjectInputStream o = null;
        try {
            fIS = new FileInputStream(Routes.FILES.getDataFile());
            o = new ObjectInputStream(fIS);
            Person pr;
            while ((pr = (Person) o.readObject()) != null) {
                if (pr.getNif().equals(p.getNif())) {
                    personToRead = pr;
                    break;
                }
            }
            o.close();
        } catch (java.io.EOFException ex) {
            //Do nothing
//            System.out.println("El archivo está vacío y no se puede crear"
//                    + "el objeto ObjectInputStream");
        } finally {
            if (o != null) {
                o.close();
            }
            if (fIS != null) {
                fIS.close();
            }
        }
        return personToRead;
    }

    @Override
    public ArrayList<Person> readAll() throws IOException, ClassNotFoundException {
        ArrayList<Person> people = new ArrayList<>();
        ObjectInputStream ois = null;
        FileInputStream fIS = null;
        try {
            
            fIS = new FileInputStream(Routes.FILES.getDataFile());
            ois = new ObjectInputStream(fIS);
            Person pr;
            while ((pr = (Person) ois.readObject()) != null) {
                people.add(pr);
            }
            ois.close();
        } catch (java.io.EOFException ex) {
            //Do nothing
//            System.out.println("El archivo está vacío y no se puede crear"
//                    + "el objeto ObjectInputStream");
        } finally {
            if (ois != null) {
                ois.close();
            }
            if (fIS != null) {
                fIS.close();
            }
        }
        
        return people;
    }

    @Override
    public void delete(Person p) throws IOException, ClassNotFoundException {
        ArrayList<Person> peopleRead = new ArrayList<>();
        FileInputStream fIS = null;
        ObjectInputStream ois = null;
        try {
            fIS = new FileInputStream(Routes.FILES.getDataFile());
            ois = new ObjectInputStream(fIS);
            Person pr;
            while ((pr = (Person) ois.readObject()) != null) {
                if (!pr.getNif().equals(p.getNif())) {
                    peopleRead.add(pr);
                }
            }
        } catch (java.io.EOFException ex) {
            //Do nothing
//            System.out.println("El archivo está vacío y no se puede crear"
//                    + "el objeto ObjectInputStream");
        }finally {
            if (ois != null) {
                ois.close();
            }
            if (fIS != null) {
                fIS.close();
            }
        }
        ObjectOutputStream oos;
        FileOutputStream fOS;
        fOS = new FileOutputStream(Routes.FILES.getDataFile());
        oos = new ObjectOutputStream(fOS);
        for (Person pf : peopleRead) {
            oos.writeObject(pf);
        }
        oos.flush();
        oos.close();
    }

    @Override
    public void deleteAll() throws IOException, ClassNotFoundException {
        File file = new File(Routes.FILES.getDataFile());
        file.delete();
        file.createNewFile();
    }

    @Override
    public void insert(Person p) throws IOException, ClassNotFoundException {
        ArrayList<Person> personRead = new ArrayList<>();
        FileInputStream fIS = null;
        ObjectInputStream ois = null;
        try {
            fIS = new FileInputStream(Routes.FILES.getDataFile());
            ois = new ObjectInputStream(fIS);
            Person pr;
            while ((pr = (Person) ois.readObject()) != null) {
                personRead.add(pr);
            }
        } catch (java.io.EOFException ex) {
            //Do nothing
//            System.out.println("El archivo está vacío y no se puede crear"
//                   + "el objeto ObjectInputStream");
        } finally {
            if (ois != null) {
                ois.close();
            }
            if (fIS != null) {
                fIS.close();
            }
        }
        ObjectOutputStream oos;
        FileOutputStream fOS;
        fOS = new FileOutputStream(Routes.FILES.getDataFile());
        oos = new ObjectOutputStream(fOS);
        personRead.add(p);
        for (Person pf : personRead) {
            oos.writeObject(pf);
        }
        fOS.flush();
        oos.flush();
        oos.close();
        fOS.close();
    }

    @Override
    public void update(Person p) throws FileNotFoundException, IOException, ClassNotFoundException{
        delete(p);
        insert(p);
    }

}
