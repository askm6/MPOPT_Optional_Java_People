package model.dao;

import model.entity.Person;
import start.Routes;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * This class implements the IDAO interface and completes the code of the
 * functions so that they can work with files. User data is saved in the
 * "dataFile.txt" file and the associated photos, if any, are saved with the
 * name NIF.png in the "Photos" folder.
 * @author Francesc Perez
 * @version 1.1.0
 */
public class DAOFile implements IDAO {

    @Override
    public Person read(Person p) throws Exception {
        Person personToRead = null;
        FileReader fr;
        BufferedReader br;
        fr = new FileReader(Routes.FILE.getDataFile());
        br = new BufferedReader(fr);
        String line;
        line = br.readLine();
        while (line != null) {
            String data[] = line.split("\t");
            if (data[1].equals(p.getNif())) {
                Date date = null;
                if (!data[2].equals("null")) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    date = dateFormat.parse(data[2]);
                }
                ImageIcon photo = null;
                if (!data[3].equals("null")) {
                    photo = new ImageIcon(data[3]);
                }
                personToRead = new Person(data[0], data[1], date, photo);
                break;
            }
            line = br.readLine();
        }
        br.close();
        return personToRead;
    }
    
    @Override
    public ArrayList<Person> readAll() throws FileNotFoundException, IOException, ParseException {
        ArrayList<Person> people = new ArrayList<>();
        FileReader fr;
        BufferedReader br;
        fr = new FileReader(Routes.FILE.getDataFile());
        br = new BufferedReader(fr);
        String line;
        line = br.readLine();
        while (line != null) {
            String data[] = line.split("\t");
            Date date = null;
            if (!data[2].equals("null")) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                date = dateFormat.parse(data[2]);
            }
            ImageIcon photo = null;
            if (!data[3].equals("null")) {
                photo = new ImageIcon(data[3]);
            }
            people.add(new Person(data[0], data[1], date, photo));
            line = br.readLine();
        }
        br.close();
        return people;
    }

    @Override
    public void insert(Person p) throws IOException {
        String sep = File.separator;
        FileWriter fw;
        BufferedWriter bw;
        fw = new FileWriter(Routes.FILE.getDataFile(), true);
        bw = new BufferedWriter(fw);
        if (p.getDateOfBirth() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyy/MM/dd");
            String dateAsString = dateFormat.format(p.getDateOfBirth());
            bw.write(p.getName() + "\t" + p.getNif() + "\t" + dateAsString + "\t");
        } else {
            bw.write(p.getName() + "\t" + p.getNif() + "\t" + "null" + "\t");
        }
        if (p.getPhoto() != null) {
            FileOutputStream out;
            BufferedOutputStream outB;
            String fileName = Routes.FILE.getFolderPhotos() + sep + p.getNif() + ".png";         
            out = new FileOutputStream(fileName);
            outB = new BufferedOutputStream(out);
            BufferedImage bi = new BufferedImage(p.getPhoto().getImage().getWidth(null),
                    p.getPhoto().getImage().getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);
            bi.getGraphics().drawImage(p.getPhoto().getImage(), 0, 0, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);
            baos.flush();
            byte[] img = baos.toByteArray();
            baos.close();
            for (int i = 0; i < img.length; i++) {
                outB.write(img[i]);
            }
            outB.flush();
            outB.close();
            bw.write(fileName + "\n");
        } else {
            bw.write("null" + "\n");
        }
        bw.flush();
        bw.close();
    }

    @Override
    public void delete(Person p) throws IOException {
        String sep = File.separator;
        RandomAccessFile rafRW;
        rafRW = new RandomAccessFile(Routes.FILE.getDataFile(), "rw");
        String textoNuevo = "";
        while (rafRW.getFilePointer() < rafRW.length()) {
            String l = rafRW.readLine();
            String d[] = l.split("\t");
            if (p.getNif().equals(d[1])) {
                if (!d[3].equals("null")) {
                    File photoFile = new File(Routes.FILE.getFolderPhotos() + sep + p.getNif()
                            + ".png");
                    photoFile.delete();
                }
            } else {
                textoNuevo += d[0] + "\t" + d[1] + "\t" + d[2] + "\t" + d[3]
                        + "\n";
            }
        }
        rafRW.setLength(0);
        rafRW.writeBytes(textoNuevo);
        rafRW.close();
    }

    @Override
    public void deleteAll() throws IOException {
        File file = new File(Routes.FILE.getDataFile());
        file.delete();
        file.createNewFile();
        file = new File(Routes.FILE.getFolderPhotos());
        for(File f : file.listFiles())
            f.delete();
    }
    
    @Override
    public void update(Person p) throws IOException {
        delete(p);
        insert(p);
    }

}
