/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smplatform;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
    JTextArea messageArea = new JTextArea(8, 40);
    
    public ChatServerClient(){
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField,"North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();
        
        textField.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }
    private String getServerAddress()
    {
        return JOptionPane.showInputDialog(frame, "Enter IP address of the Server:", "Welcome to the Chatter", JOptionPane.QUESTION_MESSAGE);
    }
    private String getName()
    {
        return JOptionPane.showInputDialog(frame, "Choose a username:", "Screen name selection", JOptionPane.PLAIN_MESSAGE);
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
            if(line.startsWith("SUBMITNAME"))
            {
                out.println(getName());
            }
            else if(line.startsWith("NAMEACCEPTED"))
            {
                textField.setEditable(true);
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