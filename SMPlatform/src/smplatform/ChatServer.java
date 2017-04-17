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
import java.util.Date;
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
    
    // Instantiate a Date object
    private static Date date = new Date();
     
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
                    output = "SUBMITPASS";//Submit password
                    break;
            }
            return output;
        }
        public void saveEntry(Entry ent) throws IOException
        {
            PrintWriter outFile = new PrintWriter(new FileWriter("entries.txt", true));
            
            outFile.append(ent.getUser() + "\n");
            outFile.append(ent.getPass()+ "\n");
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
        public void saveLog(String p) throws IOException
        {
            PrintWriter outFile = new PrintWriter(new FileWriter("log.txt", true));
            //FileWriter outFile = new FileWriter("log.txt", true);
            
            outFile.append(p + "\n");
            outFile.close();
        }
        
        public Queue<String> readLog() throws IOException
        {
            String post;
            Queue<String> ret = new LinkedList<String>();
            try
            {
                FileReader fr = new FileReader("log.txt");
                Scanner postsIn = new Scanner(fr);
                while(postsIn.hasNext())
                {
                    post = postsIn.nextLine();
                    System.out.println(post);
                    ret.add(post);
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
                log = readLog();
                System.out.println(log);
            }
            catch(Exception eof)
            {
                
            }
                  
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
                        //System.out.println(newUser);
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
                        if (!pass.equals("")) {
                            boolean valid = true;
                            login.setPass(pass);
                            for(int i = 0; i < logins.size(); i++)
                            {
                                if(logins.get(i).toString().equals(login))
                                {
                                    valid = false;
                                }
                            }
                            if(valid)
                            {
                                System.out.println("PASS: " + pass);
                                logins.add(login);
                                try{
                                    saveEntry(login);
                                }
                                catch(Exception exserial)
                                {

                                }
                                step = 4;
                            }
                            else
                            {
                                step = 0;
                            }
                           
                        } else {
                            step = 0;
                        }
                    } 
                    else if (step == 3) {
                        System.out.println(state(step));
                        String user = in.readLine();
                        
                        if (!user.equals("")) {
                            check.setUser(user);
                            for(int i = 0; i < logins.size(); i++)
                            {
                                if(logins.get(i).getUser().equals(check.getUser()))
                                {
                                    name = user; 
                                    step = 6;
                                    break;
                                }
                                else
                                {
                                    System.out.println("Invalid Login");
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
                        //System.out.println(readLog());
                        
                        writers.add(out);
                        for (PrintWriter writer : writers) {
                                
                            while(!log.isEmpty())
                            {
                                System.out.println("writers loop: " + log.peek());
                                writer.println("MESSAGE: " + log.peek());
                                log.remove();
                                        
                            }
                                
                            }
                        while (true) {
                            String input = in.readLine();
                            
                            if(input.startsWith("TITLE"))
                            {
                                input += in.readLine();
                            }
                            log.add(input);
                            //System.out.println(log);
                            //files.write(log, "log.txt");
                            //Where the server takes in chatted things
                            System.out.println(input);
                            if (input == null) {
                                return;
                            }
                            for (PrintWriter writer : writers) {
                                
                                LogPost post = new LogPost(name, input, date.toString());
                                
                                writer.println("MESSAGE: " + post.toString());
                                saveLog(post.toString());
                            }
                        }
                    }
                    else if(step == 6)
                    {
                        System.out.println(state(step));
                        String pass = in.readLine();
                        
                        if(!pass.equals(""))
                        {
                            check.setPass(pass);
                            for(int i = 0; i < logins.size(); i++)
                            {
                                if(logins.get(i).getPass().equals(check.getPass()))
                                {
                                    step = 4;
                                    break;
                                }
                                else
                                {
                                    System.out.println("Invalid Pass");
                                }
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
