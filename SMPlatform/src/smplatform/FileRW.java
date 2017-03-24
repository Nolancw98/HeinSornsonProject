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
public class FileRW{
    public FileRW()
    {
    }
    public void write(Object o, String fileName)
    {
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(fileName));
        } catch (Exception e) {
            Logger.getLogger(FileRW.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            outputStream.writeObject(o);
        } catch (IOException ex) {
            Logger.getLogger(FileRW.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Object read(String fileName)
    {
        ObjectInputStream inputStream = null;
        try{
            inputStream = new ObjectInputStream(new FileInputStream(fileName));
        }
        catch(Exception e)
        {
            Logger.getLogger(FileRW.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            return (Object)inputStream.readObject();
        } catch (Exception ex) {
            Logger.getLogger(FileRW.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
