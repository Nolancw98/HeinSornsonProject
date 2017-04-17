/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
Eddie School:172.30.17.41
Eddie Joe's: 192.168.1.170
 */
package smplatform;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
/**
 *
 * @author nolancw98
 */
public class ChatServerClient {
    BufferedReader in;
    PrintWriter out;
    JFrame frame = new JFrame("Chatter");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(40, 60);
    JButton makePost = new JButton("Post");
    JPanel subPanel = new JPanel();
    
    JFrame post = new JFrame("Post");
    JTextField title = new JTextField(40);
    JTextArea body = new JTextArea(30, 40);
    JButton send = new JButton("Send");
    
    
    public ChatServerClient(){
        makePost.setEnabled(false);
        textField.setEditable(false);
        messageArea.setEditable(false);
        messageArea.setWrapStyleWord(false);
        frame.setLayout(new BorderLayout());
        
        subPanel.add(makePost, "North");
        subPanel.add(textField,"South");
        
        frame.add(subPanel, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.setResizable(false);
        messageArea.setBackground(Color.WHITE);
        frame.pack();
        
        body.setEditable(false);
        title.setEditable(false);
        title.setText("");
        body.setText("");
        post.getContentPane().add(title, "North");
        post.getContentPane().add(body, "Center");
        post.getContentPane().add(send, "South");
        post.setVisible(false);
        post.pack();
        body.setWrapStyleWord(true);
        
        textField.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                out.println(textField.getText());
                textField.setText("");
            }
        });
        makePost.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent but)
            {
                post.setVisible(true);
                title.setEditable(true);
                body.setEditable(true);
                
            }
        });
        send.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent se)
            {
                out.println("TITLE: " + title.getText());
                out.println("\nBODY: " + body.getText());
                title.setText("Title");
                body.setText("Body");
                post.setVisible(false);
            }
        });
    }
    
    private String getNewUser()
    {
        return JOptionPane.showInputDialog(frame, "Enter your desired Username", "Register", JOptionPane.QUESTION_MESSAGE);
    }
    
    private String getNewPass()
    {
        return JOptionPane.showInputDialog(frame, "Enter your Password", "Register", JOptionPane.QUESTION_MESSAGE);
    }
    
    private int getReturning()
    {
        return JOptionPane.showConfirmDialog(frame, "Are you a new user?", "Login/Register", JOptionPane.YES_NO_OPTION);
    }
    
    private String getServerAddress()
    {
        return JOptionPane.showInputDialog(frame, "Enter IP address of the Server:", "Welcome to the Chatter", JOptionPane.QUESTION_MESSAGE);
    }
    private String getName()
    {
        return JOptionPane.showInputDialog(frame, "Login:", "Enter your Username", JOptionPane.PLAIN_MESSAGE);
    }
    private String getPass()
    {
        return JOptionPane.showInputDialog(frame, "Password:", "Enter your Password", JOptionPane.PLAIN_MESSAGE);
    }
    private void run() throws IOException
    {
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 9001);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        while(true)
        {
            String line = in.readLine();
            if(line.startsWith("NEWUSER"))
            {
                out.println(getReturning());
            }
            else if(line.startsWith("CREATEACCOUNT"))
            {
                out.println(getNewUser());
            }
            else if(line.startsWith("CREATEPASS"))
            {
                out.println(getNewPass());
            }
            else if(line.startsWith("SUBMITNAME"))
            {
                out.println(getName());
            }
            else if(line.startsWith("SUBMITPASS"))
            {
                out.println(getPass());
            }
            else if(line.startsWith("NAMEACCEPTED"))
            {
                textField.setEditable(true);
                makePost.setEnabled(true);
            }
            else if(line.startsWith("MESSAGE"))
            {
                messageArea.append(line.substring(8) + "\n");
            }
        }
    }

    public static void main(String[] args) throws Exception
    {
        ChatServerClient client = new ChatServerClient();   
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}