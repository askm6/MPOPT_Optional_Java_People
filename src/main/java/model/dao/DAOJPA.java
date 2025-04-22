package model.dao;

import model.entity.Person;
import start.Routes;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.swing.ImageIcon;

/**
 * This class implements the IDAO interface and completes the function code
 * blocks so that they can operate with object DDBB. The NIF is used as the key.
 *
 * @author Francesc Perez
 * @version 1.1.0
 */
public class DAOJPA implements IDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory(Routes.DBO.getDbServerAddress());

    private byte[] imageIconToBytes(ImageIcon icon) {
        Image image = icon.getImage();
        BufferedImage bufferedImage = new BufferedImage(
                image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB
        );
        bufferedImage.getGraphics().drawImage(image, 0, 0, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", baos);
        } catch (IOException e) {
            return null;
        }
        return baos.toByteArray();
    }

    private ImageIcon bytesToImageIcon(byte[] imageBytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(bais);
        } catch (IOException e) {
            return null;
        }
        if (bufferedImage != null) {
            return new ImageIcon(bufferedImage);
        } else {
            return null;
        }
    }

    @Override
    public Person read(Person p) throws Exception {
        Person personToReturn = null;
        EntityManager em = emf.createEntityManager();
        String query = "SELECT p FROM Person p WHERE p.nif = :value";
        List<Person> people = em.createQuery(query, Person.class).setParameter("value", p.getNif()).getResultList();
        if (!people.isEmpty()) {
            personToReturn = people.get(0);
            if (personToReturn.getPhotoOnlyJPA() != null) {
                personToReturn.setPhoto(bytesToImageIcon(personToReturn.getPhotoOnlyJPA()));
            }
        }
        em.close();
        return personToReturn;
    }

    @Override
    public ArrayList<Person> readAll() throws Exception {
        EntityManager em = emf.createEntityManager();
        String query = "SELECT p FROM Person p";
        List<Person> people = em.createQuery(query, Person.class).getResultList();
        for (Person p : people) {
            if (p.getPhotoOnlyJPA() != null) {
                p.setPhoto(bytesToImageIcon(p.getPhotoOnlyJPA()));
            }
        }
        em.close();
        return (ArrayList) people;
    }

    @Override
    public void insert(Person p) throws Exception {
        EntityManager em = emf.createEntityManager();
        if (p.getPhoto() != null) {
            p.setPhotoOnlyJPA(imageIconToBytes(p.getPhoto()));
        }
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void update(Person p) throws Exception {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Person pC = em.find(Person.class, p.getNif());
        if (pC != null) {
            pC.setName(p.getName());
            pC.setDateOfBirth(p.getDateOfBirth());
            if(p.getPhoto() != null)
                pC.setPhotoOnlyJPA(imageIconToBytes(p.getPhoto()));
            else
                pC.setPhotoOnlyJPA(null);
            em.getTransaction().commit();
        }
        em.close();
    }

    @Override
    public void delete(Person p) throws Exception {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE nif=: nifP", Person.class);
        query.setParameter("nifP", p.getNif());
        List<Person> personas = query.getResultList();
        em.getTransaction().begin();
        for (Person pR : personas) {
            em.remove(pR);
        }
        em.getTransaction().commit();
    }

    @Override
    public void deleteAll() throws Exception {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
        List<Person> personas = query.getResultList();
        em.getTransaction().begin();
        for (Person pR : personas) {
            em.remove(pR);
        }
        em.getTransaction().commit();
    }

}
