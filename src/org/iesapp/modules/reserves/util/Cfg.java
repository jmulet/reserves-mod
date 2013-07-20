/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.iesapp.modules.reserves.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.iesapp.framework.util.CoreCfg;

/**
 *
 * @author Josep
 */
public class Cfg {

    public static String lookAndFeel = "Nimbus";
    public static String fontName = "Helvetica";
    public static int fontSize = 12;
    public static int pid = -1;
    private static ArrayList<String> actions;
    private static String RESERVESINI = "config/reserves.ini";

    public static void initializeModule() {
        Cfg.readIniFile();
    }

    private static void readIniFile() {

        File propfile = new File(CoreCfg.contextRoot+File.separator+Cfg.RESERVESINI);
        if (!propfile.exists()) {
            saveIni();
        }


        Properties props = new Properties();
        //try retrieve data from file
        try {
            FileInputStream filestream = new FileInputStream(CoreCfg.contextRoot+File.separator+Cfg.RESERVESINI);
            props.load(filestream);

            lookAndFeel = props.getProperty("lookAndFeel");
            fontName = props.getProperty("fontName");
            String message = props.getProperty("fontSize");
            fontSize = ((int) Double.parseDouble(message));

            filestream.close();
        } catch (IOException e) {
           //
        }
    }

    public static void saveIni() {
        Properties props = new Properties();

        try {

            props.setProperty("lookAndFeel", lookAndFeel);
            props.setProperty("fontName", fontName);
            props.setProperty("fontSize", "" + fontSize);

            FileOutputStream filestream = new FileOutputStream(CoreCfg.contextRoot+File.separator+Cfg.RESERVESINI);
            props.store(filestream, null);
            filestream.close();

        } catch (IOException ex) {
            Logger.getLogger(Cfg.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
