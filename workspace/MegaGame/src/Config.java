import java.io.File;
import java.io.IOException;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

/**
 * File: Config.java
 * Authors: B. Adam, C. Buescher, T. Pickens, C. Schmunsler
 * Last Modified By: C. Schmunsler
 */

/**
 * Facilitates saving and retrieving data from a config file. 
 */
public class Config {
    private Ini ini;
    private boolean autowrite;
    
    public Config(String file, boolean autowrite) throws IOException {
        File inifile = new File(file);
        try {
            this.ini = new Ini(inifile);
        } catch (InvalidFileFormatException e) {
            throw new IOException("Invalid file format. Make sure the file has not been edited and is a valid INI file.");
        }
        this.autowrite = autowrite;
    }

    public boolean isAutowrite() {
        return autowrite;
    }

    public void setAutowrite(boolean autowrite) {
        this.autowrite = autowrite;
    }
    
    public void write() throws IOException  {
        ini.store();
    }
    
    public String get(String scope, String key) {
        return ini.get(scope,key);
    }
    
    public int getInt(String scope, String key) {
        return ini.get(scope,key,int.class);
    }
    
    public double getDouble(String scope, String key) {
        return ini.get(scope,key,double.class);
    }
    
    public boolean getBoolean(String scope, String key) {
        return ini.get(scope,key,boolean.class);
    }
    
    public void set(String scope, String key, Object value) throws IOException {
        if (!ini.containsKey(scope)) {
            ini.add(scope);
        }
        ini.get(scope).add(key, value);
        if (autowrite) {
            write();
        }
    }
    
}
