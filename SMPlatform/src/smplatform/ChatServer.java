/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smplatform;
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
    //Does this work?
    //Unique names of clients
    private static HashSet<String> names = new HashSet<String>();
    private static HashSet<String> passwords = new HashSet<String>();
    private static HashSet<Entry> logins = new HashSet<Entry>();
    
    //Set of print writers for all clients
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    
    private static Queue<String> log = new LinkedList<String>();
    private static FileRW files = new FileRW();
    
    public static void main(String[] args) throws Exception
    {
        //log = (Queue<String>) files.read("log.txt");
        System.out.println("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        try
        {
            while(true)
            {
                new Handler(listener.accept()).start();
            }
        }
        finally
        {
            System.out.println("Test");
            files.write(log, "log.txt");
            listener.close();
        }
    }
    private static class Handler extends Thread{
        private Entry userpass;
        private String name;
        private String pass;
        private int newUser;
        private Socket socket;
        private BufferedReader in;
        private ObjectInputStream objin;
        private PrintWriter out;
        
        public Handler(Socket socket)
        {
            this.socket = socket;
        }
        
        public void run()
        {
            try
            {
                
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //objin = new ObjectInputStream(socket.getInputStream());  BREAKS STUFF
                out = new PrintWriter(socket.getOutputStream(), true);
                while(true)
                {
                    out.println("NEWUSER");
                    newUser = in.read();
                    System.out.println(newUser);
                    if(newUser == 48)
                    {
                        out.println("CREATEACCOUNT");
                        try{
                            userpass = (Entry) objin.readObject();
                        }
                        catch(Exception e)
                        {
                        }
                        
                        if(userpass == null)
                        {
                            return;
                        }
                        synchronized(logins)
                        {
                            //Check user pass combinations for conflicts
                            if(!logins.contains(userpass)){
                                logins.add(userpass);
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
                            return;
                        }
                    }
                    
                }
                out.println("NAMEACCEPTED");
                writers.add(out);
                
                for(PrintWriter writer : writers)
                {
                    for(String s : log)
                    {
                        writer.println("MESSAGE " + name + ": " + s);
                    }
                }
                while(true)
                {
                    String input = in.readLine();
                    log.add(input);
                    files.write(log, "log.txt");
                    //Where the server takes in chatted things
                    System.out.println(input);
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
            catch(IOException e)
            {
                System.out.println(e);
            }
            finally
            {
                if(name != null)
                {
                    names.remove(name);
                }
                if(out != null)
                {
                    writers.remove(out);
                }
                try
                {
                    socket.close();
                }
                catch(IOException e)
                {
                    
                }
            }
        }
    }
}
    