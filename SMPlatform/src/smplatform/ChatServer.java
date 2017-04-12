/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smplatform;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.EOFException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author nolancw98
 */
public class ChatServer {

    private static final int PORT = 9001;
    //Unique names of clients
    private static HashSet<String> names = new HashSet<String>();
    private static ArrayList<Entry> logins = new ArrayList<Entry>();

    //Set of print writers for all clients
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

    private static Queue<String> log = new LinkedList<String>();
    private static FileRW files = new FileRW();

    public static void main(String[] args) throws Exception {
        //log = (Queue<String>) files.read("log.txt");
        System.out.println("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            System.out.println("Test");
            //files.write(log, "log.txt");
            listener.close();
        }
    }
    private static class Handler extends Thread {

        private Entry login;
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public String state(int step) {
            String output = "";
            switch (step) {
                case 0:
                    output = "NEWUSER"; //Prompt yes or no\
                    break;
                case 1:
                    output = "CREATEACCOUNT"; //Prompt for new username
                    break;
                case 2:
                    output = "CREATEPASS"; //Prompt for pass
                    break;
                case 3:
                    output = "SUBMITNAME"; //Prompt for old username
                    break;
                case 4:
                    output = "NAMEACCEPTED"; //add the writer for the user
                    break;
                case 5:
                    output = "MESSAGE"; //Sent when messaging
                    break;
                case 6:
                    output = "SUBMITPASS";
                    break;
            }
            return output;
        }
        public void saveEntry(Entry ent) throws IOException
        {
            PrintWriter outFile = new PrintWriter(new FileWriter("entries.txt"));
            
            outFile.println(ent.getUser());
            outFile.println(ent.getPass());
            outFile.close();
        }
        public ArrayList<Entry> readEntry() throws IOException
        {
            String user;
            String pass;
            ArrayList<Entry> ret = new ArrayList<>();
            try
            {
                FileReader fr = new FileReader("entries.txt");
                Scanner entriesIn = new Scanner(fr);
                while(entriesIn.hasNext())
                {
                    user = entriesIn.nextLine();
                    pass = entriesIn.nextLine();
                    Entry ent = new Entry(user, pass);
                    System.out.println(ent);
                    ret.add(ent);
                }
            }
            catch(Exception e)
            {
                
            }
            return ret;
        }

        public void run() {
            try
            {
                logins = readEntry();
            }
            catch(Exception eof)
            {
                
            }
            
            //Debugging Variables
            names.add("admin");
            logins.add(new Entry("admin", "admin"));
            System.out.println(log);
            System.out.println(names);
            System.out.println(logins);
            
            login = new Entry();
            Entry check = new Entry();
            int step = 0; //Case Number

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                while (true) {
                    out.println(state(step));
                    if (step == 0) {
                        System.out.println(state(step));
                        int newUser = 0;
                        newUser = in.read();
                        System.out.println("FUCK:" + newUser);
                        if (newUser == 48) {
                            in.readLine();
                            step = 1;
                        } else if (newUser == 49) {
                            in.readLine();
                            step = 3;
                        } else {
                            step = 0;
                        }
                        
                    }
                    else if (step == 1) {
                        System.out.println(state(step));
                        String user = in.readLine();
                        if (!user.equals("") && !names.contains(user)) {
                            names.add(user);
                            name = user;
                            login.setUser(user);
                            System.out.println("NAME: " + name);
                            step = 2;
                        } else {
                            //step = 1;
                        }
                    } 
                    else if (step == 2) {
                        System.out.println(state(step));
                        String pass = in.readLine();
                        if (!pass.equals("") && !logins.contains(login)) {
                            login.setPass(pass);
                            System.out.println("PASS: " + pass);
                            logins.add(login);
                            try{
                                saveEntry(login);
                            }
                            catch(Exception exserial)
                            {
                                
                            }
                            step = 4;
                           
                        } else {
                            step = 0;
                        }
                    } 
                    else if (step == 3) {
                        System.out.println(state(step));
                        String user = in.readLine();
                        
                        if (!user.equals("")) {
                            check.setUser(user);
                            System.out.println("Check: " + check.userToString());
                            for(int i = 0; i < logins.size(); i++)
                            {
                                if(logins.get(i).userToString().equals(check.userToString()))
                                {
                                    System.out.println("Logins.get(i): " + logins.get(i).userToString());
                                    name = user;
                                    step = 5;
                                    break;
                                }
                                else
                                {
                                    System.out.println("Invalid User");
                                    step = 0;
                                }
                            //name = user;
                            //step = 4;
                            }
                        }
                        else {
                            step = 0;
                        }
                    } 
                    else if (step == 4) {
                        System.out.println(state(step));
                        writers.add(out);
                        while (true) {
                            String input = in.readLine();
                            log.add(input);
                            //files.write(log, "log.txt");
                            //Where the server takes in chatted things
                            System.out.println(input);
                            if (input == null) {
                                return;
                            }
                            for (PrintWriter writer : writers) {
                                writer.println("MESSAGE " + name + ": " + input);
                            }
                        }
                    }
                    else if(step == 5)
                    {
                        System.out.println(state(step));
                        String pass = in.readLine();
                        
                        if (!pass.equals("")) {
                            check.setPass(pass);
                            for(int i = 0; i < logins.size(); i++)
                            {
                                if(logins.get(i).passToString().equals(check.passToString()))
                                {
                                    step = 4;
                                    break;
                                }
                                else
                                {
                                    System.out.println("Invalid Login");
                                    step = 3;
                                }
                            //name = user;
                            //step = 4;
                            }
                        }
                        else 
                        {
                            step = 3;
                        }
                    }
                }
            } catch (Exception e) {
            } finally {
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {

                }
            }
        }
    }
}
