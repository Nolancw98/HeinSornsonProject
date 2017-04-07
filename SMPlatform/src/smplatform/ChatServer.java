/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smplatform;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
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
import java.util.Stack;

/**
 *
 * @author nolancw98
 */
public class ChatServer {

    private static final int PORT = 9001;
    //Unique names of clients
    private static HashSet<String> names = new HashSet<String>();
    private static HashSet<Entry> logins = new HashSet<Entry>();

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
            files.write(log, "log.txt");
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
            }
            return output;
        }

        public void run() {

            Boolean nameTaken = true;

            //Debugging Variables
            names.add("admin");
            logins.add(new Entry("admin", "admin"));
            System.err.println(log);
            System.err.println(names);
            System.err.println(logins);
            
            login = new Entry();
            int step = 0; //Case Number
            String output = ""; //The string outputted to the client

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                while (true) {
                    out.println(state(step));
                    if (step == 0) {
                        System.err.println(output);
                        int newUser = 0;
                        newUser = in.read();
                        System.out.println("FUCK:" + newUser);
                        if (newUser == 48) {
                            step = 1;
                        } else if (newUser == 49) {
                            step = 3;
                        } else {
                            step = 0;
                        }
                    } 
                    else if (step == 1) {
                        System.err.println(output);
                        String user = in.readLine();
                        synchronized (names) {
                            if (!names.contains(user)) {
                                login.setUser(user);
                                System.err.println(user);
                                name = user;
                                step = 2;
                            } else {
                                step = 1;
                            }
                        }
                    } 
                    else if (step == 2) {
                        System.err.println(output);
                        String pass = in.readLine();
                        login.setPass(pass);
                        System.err.println(pass);
                        synchronized (logins) {
                            if (!logins.contains(login)) {
                                step = 4;
                            } else {
                                step = 0;
                            }
                        }
                    } 
                    else if (step == 3) {
                        System.err.println(output);
                        String user = in.readLine();
                        if (names.contains(user)) {
                            step = 4;
                        } else {
                            step = 0;
                        }
                    } 
                    else if (step == 4) {
                        System.err.println(output);
                        writers.add(out);
                        while (true) {
                            String input = in.readLine();
                            log.add(input);
                            files.write(log, "log.txt");
                            //Where the server takes in chatted things
                            System.err.println(input);
                            if (input == null) {
                                return;
                            }
                            for (PrintWriter writer : writers) {
                                writer.println("MESSAGE " + name + ": " + input);
                            }
                        }
                    }
                }
            } catch (Exception e) {
            } /*
            try
            {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                while(true)
                {
                    out.println("NEWUSER");
                    newUser = in.read();
                    System.out.println(newUser);
                    if(newUser == 48)
                    {
                        //login = new Entry();
                        out.println("CREATEACCOUNT");
                        name = in.readLine();
                        synchronized(names)
                        {
                            if(!names.contains(name))
                            {
                                nameTaken = false;
                                login.setUser(name);
                                System.err.println(name);
                            }
                        }
                        if(!nameTaken)
                        {
                            out.println("CREATEPASS");
                            pass = in.readLine();
                            login.setPass(pass);
                            System.err.println(pass);
                        }
                        else
                        {
                            out.println("CREATEACCOUNT");
                        }
                        
                        
                        System.err.println(login.getPass());
                        System.err.println(login.getUser());
                        
                        synchronized(logins)
                        {
                            //Check user pass combinations for conflicts
                            if(!logins.contains(login)){
                                logins.add(login);
                                break;
                            }
                        }
                    }
                    else if(newUser == 49)
                    {
                        out.println("SUBMITNAME");
                        name = in.readLine();
                        if(name == null)
                        {
                            return;
                        }
                        if(names.contains(name))
                        {
                            break;
                        }
                    }
                    
                }
                out.println("NAMEACCEPTED");
                writers.add(out);
                /*
                for(PrintWriter writer : writers)
                {
                    for(String s : log)
                    {
                        writer.println("MESSAGE " + name + ": " + s);
                    }
                }
             */ /*
                in.readLine();
                while(true)
                {
                    String input = in.readLine();
                    log.add(input);
                    files.write(log, "log.txt");
                    //Where the server takes in chatted things
                    System.err.println(input);
                    if(input == null)
                    {
                        return;
                    }
                    for(PrintWriter writer : writers)
                    {
                        writer.println("MESSAGE " + name + ": " + input);
                    }
                }
            }
            catch(Exception e)
            {
                System.err.println(e);
            }
             */ finally {
                if (name != null) {
                    names.remove(name);
                }
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
