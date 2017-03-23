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
public class Entry<U,P> {
    private U user;
    private P pass;
    public Entry(U user, P pass)
    {
        this.user = user;
        this.pass = pass;
    }
    public U getUser()
    {
        return user;
    }
    public P getPass()
    {
        return pass;
    }
}
