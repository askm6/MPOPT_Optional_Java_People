package controller;

import model.entity.Person;
import model.entity.PersonException;
import model.dao.DAOArrayList;
import model.dao.DAOFile;
import model.dao.DAOFileSerializable;
import model.dao.DAOHashMap;
import model.dao.DAOJPA;
import model.dao.DAOSQL;
import model.dao.IDAO;
import start.Routes;
import view.DataStorageSelection;
import view.Delete;
import view.Insert;
import view.Menu;
import view.Read;
import view.ReadAll;
import view.Update;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.persistence.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.DateModel;

/**
 * This class starts the visual part of the application and programs and manages
 * all the events that it can receive from it. For each event received the
 * controller performs an action.
 *
 * @author Francesc Perez
 * @version 1.1.0
 */
public class ControllerImplementation implements IController, ActionListener {

    //Instance variables used so that both the visual and model parts can be 
    //accessed from the Controller.
    private final DataStorageSelection dSS;
    private IDAO dao;
    private Menu menu;
    private Insert insert;
    private Read read;
    private Delete delete;
    private Update update;
    private ReadAll readAll;

    /**
     * This constructor allows the controller to know which data storage option
     * the user has chosen.Schedule an event to deploy when the user has made
     * the selection.
     *
     * @param dSS
     */
    public ControllerImplementation(DataStorageSelection dSS) {
        this.dSS = dSS;
        ((JButton) (dSS.getAccept()[0])).addActionListener(this);
    }

    /**
     * With this method, the application is started, asking the user for the
     * chosen storage system.
     */
    @Override
    public void start() {
        dSS.setVisible(true);
    }

    /**
     * This receives method handles the events of the visual part. Each event
     * has an associated action.
     *
     * @param e The event generated in the visual part
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == dSS.getAccept()[0]) {
            handleDataStorageSelection();
        } else if (e.getSource() == menu.getInsert()) {
            handleInsertAction();
        } else if (insert != null && e.getSource() == insert.getInsert()) {
            handleInsertPerson();
        } else if (e.getSource() == menu.getRead()) {
            handleReadAction();
        } else if (read != null && e.getSource() == read.getRead()) {
            handleReadPerson();
        } else if (e.getSource() == menu.getDelete()) {
            handleDeleteAction();
        } else if (delete != null && e.getSource() == delete.getDelete()) {
            handleDeletePerson();
        } else if (e.getSource() == menu.getUpdate()) {
            handleUpdateAction();
        } else if (update != null && e.getSource() == update.getRead()) {
            handleReadForUpdate();
        } else if (update != null && e.getSource() == update.getUpdate()) {
            handleUpdatePerson();
        } else if (e.getSource() == menu.getReadAll()) {
            handleReadAll();
        } else if (e.getSource() == menu.getDeleteAll()) {
            handleDeleteAll();
        }
    }

    private void handleDataStorageSelection() {
        String daoSelected = ((javax.swing.JCheckBox) (dSS.getAccept()[1])).getText();
        dSS.dispose();
        switch (daoSelected) {
            case "ArrayList":
                dao = new DAOArrayList();
                break;
            case "HashMap":
                dao = new DAOHashMap();
                break;
            case "File":
                setupFileStorage();
                break;
            case "File (Serialization)":
                setupFileSerialization();
                break;
            case "SQL - Database":
                setupSQLDatabase();
                break;
            case "JPA - Database":
                setupJPADatabase();
                break;
        }
        setupMenu();
    }

    private void setupFileStorage() {
        File folderPath = new File(Routes.FILE.getFolderPath());
        File folderPhotos = new File(Routes.FILE.getFolderPhotos());
        File dataFile = new File(Routes.FILE.getDataFile());
        folderPath.mkdir();
        folderPhotos.mkdir();
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(dSS, "File structure not created. Closing application.", "File - People v1.1.0", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
        dao = new DAOFile();
    }

    private void setupFileSerialization() {
        File folderPath = new File(Routes.FILES.getFolderPath());
        File dataFile = new File(Routes.FILES.getDataFile());
        folderPath.mkdir();
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(dSS, "File structure not created. Closing application.", "FileSer - People v1.1.0", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
        dao = new DAOFileSerializable();
    }

    private void setupSQLDatabase() {
        try {
            Connection conn = DriverManager.getConnection(Routes.DB.getDbServerAddress() + Routes.DB.getDbServerComOpt(),
                    Routes.DB.getDbServerUser(), Routes.DB.getDbServerPassword());
            if (conn != null) {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("create database if not exists " + Routes.DB.getDbServerDB() + ";");
                stmt.executeUpdate("create table if not exists " + Routes.DB.getDbServerDB() + "." + Routes.DB.getDbServerTABLE() + "("
                        + "nif varchar(9) primary key not null, "
                        + "name varchar(50), "
                        + "dateOfBirth DATE, "
                        + "photo varchar(200) );");
                stmt.close();
                conn.close();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(dSS, "SQL-DDBB structure not created. Closing application.", "SQL_DDBB - People v1.1.0", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        dao = new DAOSQL();
    }

    private void setupJPADatabase() {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(Routes.DBO.getDbServerAddress());
            EntityManager em = emf.createEntityManager();
            em.close();
            emf.close();
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(dSS, "JPA_DDBB not created. Closing application.", "JPA_DDBB - People v1.1.0", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        dao = new DAOJPA();
    }

    private void setupMenu() {
        menu = new Menu();
        menu.setVisible(true);
        menu.getInsert().addActionListener(this);
        menu.getRead().addActionListener(this);
        menu.getUpdate().addActionListener(this);
        menu.getDelete().addActionListener(this);
        menu.getReadAll().addActionListener(this);
        menu.getDeleteAll().addActionListener(this);
    }

    private void handleInsertAction() {
        insert = new Insert(menu, true);
        insert.getInsert().addActionListener(this);
        insert.setVisible(true);
    }

    private void handleInsertPerson() {
        Person p = new Person(insert.getNam().getText(), insert.getNif().getText());
        if (insert.getDateOfBirth().getModel().getValue() != null) {
            p.setDateOfBirth(((GregorianCalendar) insert.getDateOfBirth().getModel().getValue()).getTime());
        }
        if (insert.getPhoto().getIcon() != null) {
            p.setPhoto((ImageIcon) insert.getPhoto().getIcon());
        }
        insert(p);
        insert.getReset().doClick();
    }

    private void handleReadAction() {
        read = new Read(menu, true);
        read.getRead().addActionListener(this);
        read.setVisible(true);
    }

    private void handleReadPerson() {
        Person p = new Person(read.getNif().getText());
        Person pNew = read(p);
        if (pNew != null) {
            read.getNam().setText(pNew.getName());
            if (pNew.getDateOfBirth() != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(pNew.getDateOfBirth());
                DateModel<Calendar> dateModel = (DateModel<Calendar>) read.getDateOfBirth().getModel();
                dateModel.setValue(calendar);
            }
            //To avoid charging former images
            if (pNew.getPhoto() != null) {
                pNew.getPhoto().getImage().flush();
                read.getPhoto().setIcon(pNew.getPhoto());
            }
        } else {
            JOptionPane.showMessageDialog(read, p.getNif() + " doesn't exist.", read.getTitle(), JOptionPane.WARNING_MESSAGE);
            read.getReset().doClick();
        }
    }

    public void handleDeleteAction() {
        delete = new Delete(menu, true);
        delete.getDelete().addActionListener(this);
        delete.setVisible(true);
    }

    public void handleDeletePerson() {
        if (delete != null) {
            Person p = new Person(delete.getNif().getText());
            delete(p);
            delete.getReset().doClick();
        }
    }

    public void handleUpdateAction() {
        update = new Update(menu, true);
        update.getUpdate().addActionListener(this);
        update.getRead().addActionListener(this);
        update.setVisible(true);
    }

    public void handleReadForUpdate() {
        if (update != null) {
            Person p = new Person(update.getNif().getText());
            Person pNew = read(p);
            if (pNew != null) {
                update.getNam().setEnabled(true);
                update.getDateOfBirth().setEnabled(true);
                update.getPhoto().setEnabled(true);
                update.getUpdate().setEnabled(true);
                update.getNam().setText(pNew.getName());
                if (pNew.getDateOfBirth() != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(pNew.getDateOfBirth());
                    DateModel<Calendar> dateModel = (DateModel<Calendar>) update.getDateOfBirth().getModel();
                    dateModel.setValue(calendar);
                }
                if (pNew.getPhoto() != null) {
                    pNew.getPhoto().getImage().flush();
                    update.getPhoto().setIcon(pNew.getPhoto());
                    update.getUpdate().setEnabled(true);
                }
            } else {
                JOptionPane.showMessageDialog(update, p.getNif() + " doesn't exist.", update.getTitle(), JOptionPane.WARNING_MESSAGE);
                update.getReset().doClick();
            }
        }
    }

    public void handleUpdatePerson() {
        if (update != null) {
            Person p = new Person(update.getNam().getText(), update.getNif().getText());
            if ((update.getDateOfBirth().getModel().getValue()) != null) {
                p.setDateOfBirth(((GregorianCalendar) update.getDateOfBirth().getModel().getValue()).getTime());
            }
            if ((ImageIcon) (update.getPhoto().getIcon()) != null) {
                p.setPhoto((ImageIcon) update.getPhoto().getIcon());
            }
            update(p);
            update.getReset().doClick();
        }
    }

    public void handleReadAll() {
        ArrayList<Person> s = readAll();
        if (s.isEmpty()) {
            JOptionPane.showMessageDialog(menu, "There are not people registered yet.", "Read All - People v1.1.0", JOptionPane.WARNING_MESSAGE);
        } else {
            readAll = new ReadAll(menu, true);
            DefaultTableModel model = (DefaultTableModel) readAll.getTable().getModel();
            for (int i = 0; i < s.size(); i++) {
                model.addRow(new Object[i]);
                model.setValueAt(s.get(i).getNif(), i, 0);
                model.setValueAt(s.get(i).getName(), i, 1);
                if (s.get(i).getDateOfBirth() != null) {
                    model.setValueAt(s.get(i).getDateOfBirth().toString(), i, 2);
                } else {
                    model.setValueAt("", i, 2);
                }
                if (s.get(i).getPhoto() != null) {
                    model.setValueAt("yes", i, 3);
                } else {
                    model.setValueAt("no", i, 3);
                }
            }
            readAll.setVisible(true);
        }
    }

    public void handleDeleteAll() {
        Object[] options = {"Yes", "No"};
        //int answer = JOptionPane.showConfirmDialog(menu, "Are you sure to delete all people registered?", "Delete All - People v1.1.0", 0, 0);
        int answer = JOptionPane.showOptionDialog(
        menu,
        "Are you sure you want to delete all registered people?", 
        "Delete All - People v1.1.0",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE,
        null,
        options,
        options[1] // Default selection is "No"
    );

        if (answer == 0) {
            deleteAll();
        }
    }
    
    /**
     * This function inserts the Person object with the requested NIF, if it
     * doesn't exist. If there is any access problem with the storage device,
     * the program stops.
     *
     * @param p Person to insert
     */
    @Override
    public void insert(Person p) {
        try {
            if (dao.read(p) == null) {
                dao.insert(p);
            } else {
                throw new PersonException(p.getNif() + " is registered and can not "
                        + "be INSERTED.");
            }
        } catch (Exception ex) {
            //Exceptions generated by file read/write access. If something goes 
            // wrong the application closes.
            if (ex instanceof FileNotFoundException || ex instanceof IOException
                    || ex instanceof ParseException || ex instanceof ClassNotFoundException
                    || ex instanceof SQLException || ex instanceof PersistenceException) {
                JOptionPane.showMessageDialog(insert, ex.getMessage() + ex.getClass() + " Closing application.", insert.getTitle(), JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            if (ex instanceof PersonException) {
                JOptionPane.showMessageDialog(insert, ex.getMessage(), insert.getTitle(), JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * This function updates the Person object with the requested NIF, if it
     * doesn't exist. NIF can not be aupdated. If there is any access problem
     * with the storage device, the program stops.
     *
     * @param p Person to update
     */
    @Override
    public void update(Person p) {
        try {
            dao.update(p);
        } catch (Exception ex) {
            //Exceptions generated by file read/write access. If something goes 
            // wrong the application closes.
            if (ex instanceof FileNotFoundException || ex instanceof IOException
                    || ex instanceof ParseException || ex instanceof ClassNotFoundException
                    || ex instanceof SQLException || ex instanceof PersistenceException) {
                JOptionPane.showMessageDialog(update, ex.getMessage() + ex.getClass() + " Closing application.", update.getTitle(), JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
    }

    /**
     * This function deletes the Person object with the requested NIF, if it
     * exists. If there is any access problem with the storage device, the
     * program stops.
     *
     * @param p Person to read
     */
    @Override
    public void delete(Person p) {
        try {
            if (dao.read(p) != null) {
                dao.delete(p);
            } else {
                throw new PersonException(p.getNif() + " is not registered and can not "
                        + "be DELETED");
            }
        } catch (Exception ex) {
            //Exceptions generated by file, DDBB read/write access. If something  
            //goes wrong the application closes.
            if (ex instanceof FileNotFoundException || ex instanceof IOException
                    || ex instanceof ParseException || ex instanceof ClassNotFoundException
                    || ex instanceof SQLException || ex instanceof PersistenceException) {
                JOptionPane.showMessageDialog(read, ex.getMessage() + ex.getClass() + " Closing application.", "Insert - People v1.1.0", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            if (ex instanceof PersonException) {
                JOptionPane.showMessageDialog(read, ex.getMessage(), "Delete - People v1.1.0", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * This function returns the Person object with the requested NIF, if it
     * exists. Otherwise it returns null. If there is any access problem with
     * the storage device, the program stops.
     *
     * @param p Person to read
     * @return Person or null
     */
    @Override
    public Person read(Person p) {
        try {
            Person pTR = dao.read(p);
            if (pTR != null) {
                return pTR;
            }
        } catch (Exception ex) {

            //Exceptions generated by file read access. If something goes wrong 
            //reading the file, the application closes.
            if (ex instanceof FileNotFoundException || ex instanceof IOException
                    || ex instanceof ParseException || ex instanceof ClassNotFoundException
                    || ex instanceof SQLException || ex instanceof PersistenceException) {
                JOptionPane.showMessageDialog(read, ex.getMessage() + " Closing application.", read.getTitle(), JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
        return null;
    }

    /**
     * This function returns the people registered. If there is any access
     * problem with the storage device, the program stops.
     *
     * @return ArrayList
     */
    @Override
    public ArrayList<Person> readAll() {
        ArrayList<Person> people = new ArrayList<>();
        try {
            people = dao.readAll();
        } catch (Exception ex) {
            if (ex instanceof FileNotFoundException || ex instanceof IOException
                    || ex instanceof ParseException || ex instanceof ClassNotFoundException
                    || ex instanceof SQLException || ex instanceof PersistenceException) {
                JOptionPane.showMessageDialog(readAll, ex.getMessage() + " Closing application.", readAll.getTitle(), JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
        return people;
    }

    /**
     * This function deletes all the people registered. If there is any access
     * problem with the storage device, the program stops.
     */
    @Override
    public void deleteAll() {
        try {
            dao.deleteAll();
        } catch (Exception ex) {
            if (ex instanceof FileNotFoundException || ex instanceof IOException
                    || ex instanceof ParseException || ex instanceof ClassNotFoundException
                    || ex instanceof SQLException || ex instanceof PersistenceException) {
                JOptionPane.showMessageDialog(menu, ex.getMessage() + " Closing application.", "Delete All - People v1.1.0", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
    }

}
