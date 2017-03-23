/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smplatform;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author nolancw98
 */
public class LoginReader{
    private String log;
    private Hashtable<String, String> t = new Hashtable<>();
    public LoginReader()
    {
        log = "users.txt";
    }
    
    public void write()
    {
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(log));
        } catch (Exception e) {
            Logger.getLogger(LoginReader.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            outputStream.writeObject(t);
        } catch (IOException ex) {
            Logger.getLogger(LoginReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void read()
    {
        ObjectInputStream inputStream = null;
        try{
            inputStream = new ObjectInputStream(new FileInputStream(log));
        }
        catch(Exception e)
        {
            Logger.getLogger(LoginReader.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            t = (Hashtable<String, String>)inputStream.readObject();
        } catch (Exception ex) {
            Logger.getLogger(LoginReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
