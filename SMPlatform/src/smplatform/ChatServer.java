/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smplatform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

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

        /**
         * Determines the case that needs to be outputted to the client.
         * This format allows stepping forward and backward
         * @param step the case number
         * @return the String to be outputted to the client
         */
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
        /**
         * Saves and Entry(consisting of a user and password) to file
         * @param ent
         * @throws IOException 
         */
        public void saveEntry(Entry ent) throws IOException
        {
            PrintWriter outFile = new PrintWriter(new FileWriter("entries.txt", true));
            
            outFile.append(ent.getUser() + "\n");
            outFile.append(ent.getPass()+ "\n");
            outFile.close();
        }
        /**
         * Reads an Entry(consisting of a user and password from the file
         * @return an Entry set with user and password
         * @throws IOException 
         */
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
        /**
         * Writes the chat log to a file
         * @param p the String to be saved
         * @throws IOException 
         */
        public void saveLog(String p) throws IOException
        {
            PrintWriter outFile = new PrintWriter(new FileWriter("log.txt", true));
            //FileWriter outFile = new FileWriter("log.txt", true);
            
            outFile.append(p + "\n");
            outFile.close();
        }
        
        /**
         * Reads a chat log from file
         * @return A Queue of Strings consisting of all stored posts
         * @throws IOException 
         */
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

        /**
         * The main method of this program.  See documentation located on
         * While loop below
         */
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
                
                /**
                 * This is the main while loop.  This handles all of the logic
                 * using the state() method.  By adjusting step, the program
                 * moves forward and backward through the prompts
                 */
                while (true) {
                    out.println(state(step));
                    /**
                     * This first prompt allows the user to create a new
                     * account or sign in to an existing one.  
                     */
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
                    /**
                     * This prompts the user for their user name to create
                     * an account.  It also checks to make sure than username
                     * is not already in use.  
                     */
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
                    /**
                     * This prompts the user for a password to complete the 
                     * create account process.  
                     */
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
                    /**
                     * This prompts the user for an existing username and
                     * checks that it exists in logins.  
                     */
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
                                    names.add(name);
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
                    /**
                     * After the user has successfully created an account or
                     * signed in, they are taken here.  The while loop
                     * below handles messaging.  
                     */
                    else if (step == 4) {
                        System.out.println(state(step));
                        //System.out.println(readLog());
                        
                        writers.add(out);
                        /**
                         * This for loop prints posts from the log.  
                         */
                        while(!log.isEmpty()){
                            for (PrintWriter writer : writers) {    
                                System.out.println("writers loop: " + log.peek());
                                writer.println("MESSAGE: " + log.peek());
                            }
                            log.remove();
                        }
                        
                        /**
                         * This while loop allows users to send messages to 
                         * everyone signed in.  
                         */
                        while (true) {
                            String input = in.readLine();
                            
                            //if(input.startsWith(""))
                            
                            //if(input.startsWith("TITLE"))
                            //{
                                //input += in.readLine();
                            //}
                            log.add(input);
                            //Where the server takes in chatted things
                            System.out.println(input);
                            if (input == null) {
                                return;
                            }
                            for (PrintWriter writer : writers) {
                                
                                LogPost post = new LogPost(name, input, date.toString());
                                
                                if(input.contains("BODY") || input.contains("----"))
                                {
                                    if(input.contains("BODY"))
                                    {
                                        input = input.substring(6);
                                        post = new LogPost(name, input, date.toString());
                                    }
                                    writer.println("MESSAGE: " + post.bodyToString());
                                    saveLog(post.bodyToString());
                                }
                                else if(input.contains("TITLE"))
                                {
                                    input = input.substring(7);
                                    post = new LogPost(name, input, date.toString());
                                    writer.println("MESSAGE: " + post.toString());
                                    saveLog(post.toString());
                                }
                                else
                                {
                                    writer.println("MESSAGE: " + post.bodyToString());
                                    saveLog(post.bodyToString());
                                }
                            }
                        }
                    }
                    /**
                     * This prompts the user for a password to complete the
                     * login process.  It also checks to make sure than the 
                     * login the user is using exists.   
                     */
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
                    writers.clear();
                }
                try {
                    socket.close();
                } catch (IOException e) {

                }
            }
        }
    }
}