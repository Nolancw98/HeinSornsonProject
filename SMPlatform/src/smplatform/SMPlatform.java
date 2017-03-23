/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smplatform;

import java.util.Scanner;

/**
 *
 * @author nolancw98
 */
public class SMPlatform {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Create an account");
        System.out.println("USER: ");
        String user = s.nextLine();
        System.out.println("PASS: ");
        String pass = s.nextLine();
        
        LoginReader l = new LoginReader();
        l.getTable().put(user, pass);
        
        //l.write();
        
        //l.read();
        System.out.println("Login: ");
        String ckUser = s.nextLine();
        String ckPass = s.nextLine();
        
        if(l.getTable().get(ckUser).equals(ckPass))
        {
            System.out.println("true");
        }
        else
        {
            System.out.println("false");
        }
    }
}
