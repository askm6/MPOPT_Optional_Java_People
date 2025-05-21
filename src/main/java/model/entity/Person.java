package model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.regex.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.swing.ImageIcon;
import utils.DataValidation;

/**
 * Encapsulated class that defines the type of entity that will manage the
 * application.
 *
 * @author Fran Perez
 * @version 1.1.0
 */
@Entity
public class Person implements Serializable {

    @Id
    private String nif;
    private String name;
    private String email;
    private Date dateOfBirth;
    private String phoneNumber;
    @Transient
    private ImageIcon photo;
    @Lob
    private byte[] photoOnlyJPA;

    public Person() {

    }

    /**
     * Constructor to validate new person. Two persons cannot have the same NIF
     *
     * @param nif
     */
    public Person(String nif) {
        this.nif = nif;
    }

    /**
     * Constructor with mandatory data.
     *
     * @author Fran Perez
     * @version 1.0
     */
    public Person(String name, String nif) {
        this.name = name;
        this.nif = nif;
    }

    /**
     * Constructor with all data
     *
     * @author Fran Perez
     * @version 1.0
     * @param name
     * @param nif
     * @param email
     * @param dateOfBirth
     * @param photo
     */
    public Person(String name, String nif, String email, Date dateOfBirth, String phoneNumber, ImageIcon photo) throws Exception {
        this.name = name;
        this.nif = nif;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        setPhoneNumber(phoneNumber);
        this.photo = photo;
    }

    //Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public ImageIcon getPhoto() {
        return photo;
    }

    public void setPhoto(ImageIcon photo) {
        this.photo = photo;
    }

    public byte[] getPhotoOnlyJPA() {
        return photoOnlyJPA;
    }

    public void setPhotoOnlyJPA(byte[] photoOnlyJPA) {
        this.photoOnlyJPA = photoOnlyJPA;
    }

   public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) throws PersonException {
        if (phoneNumber != null && !phoneNumber.isEmpty() && !DataValidation.isValidPhoneNumber(phoneNumber)) {
            throw new PersonException("Invalid phone number format");
        }
        this.phoneNumber = phoneNumber;
    }
        
    /**
     * Function used to compare two Personas. There cannot be two or more people
     * with the same ID. Actually it isn't used in this project.
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.nif);
        return hash;
    }

    /**
     * Function used to compare two Personas in ArrayList and HashMap
     * structures. There cannot be two or more people with the same ID.
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        return Objects.equals(this.hashCode(), other.hashCode());
    }

    /**
     * Function sed to show person's inform by console. Only for debugging
     * pourposes.
     *
     * @return
     */
    @Override
    public String toString() {
        return "Person {" + "Name = " + name + ", NIF = " + nif
                + ", Email = " + email + ", DateOfBirth = " + dateOfBirth + ", PhoneNumber = " + phoneNumber + ", Photo = " + (photo != null) + "}";

    }

}
