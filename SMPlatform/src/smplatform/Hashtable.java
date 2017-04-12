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
public class Hashtable<U,P> {
    private final static int TABLE_SIZE = 8;
    //private Entry<U,P> [] table;
    public Hashtable()
    {
        //table = (Entry<U,P>[]) new Entry[TABLE_SIZE];
        for(int i = 0; i < TABLE_SIZE; i++)
        {
            //table[i] = null;
        }
    }
    public int createHash(U user){
        int hash = user.hashCode() % TABLE_SIZE;
        return hash;
    }
    public P get(U user)
    {
        int hash = createHash(user);
        //while(table[hash] != null && !(table[hash].getUser().equals(user)))
        {
            hash = (hash++) % TABLE_SIZE;
        }
        //if(table[hash] == null)
        {
            return null;
        }
        //return table[hash].getPass();
    }
    public void put(U user, P pass)
    {
        int hash = createHash(user);
        //while(table[hash] != null && !(table[hash].getUser().equals(user)))
        {
            hash = (hash++) % TABLE_SIZE;
        }
        //table[hash] = new Entry(user, pass);
    }
}
