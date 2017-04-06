/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smplatform;

/**
 *
 * @author nolancw98
 */
public class Entry {
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
}
