/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smplatform;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author nolancw98
 */
public class SMPlatform {

    /**
     * This the default main method for netbeans.  This only was used for
     * Testing purposes
     */
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        FileRW l = new FileRW();
        ArrayList<String> ret = (ArrayList<String>) l.read("pass.txt");
        while(ret.size() > 0)
        {
           System.out.println(ret.remove(0));
        }
//        System.out.println("Create an account");
//        System.out.println("USER: ");
//        String user = s.nextLine();
//        System.out.println("PASS: ");
//        String pass = s.nextLine();
        ArrayList<String> words = new ArrayList<String>();
        words.add("hello");
        words.add("hi");
        
        l.write(words.clone(), "pass.txt");     
    }
}
