/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smplatform;
import java.io.Serializable;
/**
 * Stores users and passwords as an object.  This makes it really easy 
 * to save and manipulate the logins.  
 * @author nolancw98
 */
public class Entry implements Serializable{
    private String user;
    private String pass;
    public Entry()
    {
    }
    public Entry(String user, String pass)
    {
        this.user = user;
        this.pass = pass;
    }
    public String getUser()
    {
        return user;
    }
    public String getPass()
    {
        return pass;
    }
    public void setUser(String u)
    {
        user = u;
    }
    public void setPass(String p)
    {
        pass = p;
    }
    public String toString()
    {
        return getUser();
    }
}
