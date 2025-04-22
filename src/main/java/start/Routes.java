package start;

import java.io.File;

/**
 *
 * @author Fran Perez
 * @version 1.1.0
 */
public enum Routes {

    APPIMAGES("images", null, null, null, null, null, null, null, null),
    FILE ("File", "Photos", "dataFile.txt", null, null, null, null, null, null),
    FILES ("FileSer", null, "dataFile.ser", null, null, null, null, null, null ),
    DB("SQL_DataBase", "Photos", null, "jdbc:mysql://localhost:3306", "?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true", "root", "", "people", "person"),
    DBO("JPA_DataBase", null, null, "objectdb://localhost/people.odb;user=admin;password=admin", null, null, null, null, null);

    private final String folderPath;
    private final String folderPhotos;
    private final String dataFile;
    private final String dbServerAddress;
    private final String dbServerComOpt;
    private final String dbServerUser;
    private final String dbServerPassword;
    private final String dbServerDB;
    private final String dbServerTABLE;
    
    
    private Routes (String folderPath, String folderPhotos, String dataFile,
            String dbServerAddress, String dbServerComOpt, String dbServerUser,
            String dbServerPassword, String dbServerDB, String dbServerTABLE){
        String userDir = System.getProperty("user.dir");
        this.folderPath = userDir + File.separator + folderPath;
        this.folderPhotos = this.folderPath + File.separator + folderPhotos;
        this.dataFile = this.folderPath + File.separator + dataFile;
        this.dbServerAddress = dbServerAddress;
        this.dbServerComOpt = dbServerComOpt;
        this.dbServerUser = dbServerUser;
        this.dbServerPassword = dbServerPassword;
        this.dbServerDB = dbServerDB;
        this.dbServerTABLE = dbServerTABLE;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public String getFolderPhotos() {
        return folderPhotos;
    }

    public String getDataFile() {
        return dataFile;
    }

    public String getDbServerAddress() {
        return dbServerAddress;
    }

    public String getDbServerComOpt() {
        return dbServerComOpt;
    }

    public String getDbServerUser() {
        return dbServerUser;
    }

    public String getDbServerPassword() {
        return dbServerPassword;
    }

    public String getDbServerDB() {
        return dbServerDB;
    }

    public String getDbServerTABLE() {
        return dbServerTABLE;
    }
    
    
    
    
    
}
