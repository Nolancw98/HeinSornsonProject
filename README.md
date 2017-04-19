Welcome to Eddie and Nolan's Social Media Platform.  

This platform consists of two parts, a server and a client.

The server is resposible for handling pretty much everything.  It checks usernames and passwords and stores them in an ArrayList.
Names are stored in a hashset for easy searching.  The server stores logs in a Queue as well.  This queue is written to a file everytime
there is a post and therefore will never miss a post if the server crashes.  When the server is loaded up again, the Queue is read from
file and pops off the posts everytime a user joins the chat.  The server also stores a state for every client connected to it.  
This determines what to prompt the user with.  Doing all of this stuff server side improves security as well as the ability to have post
history. The PrintWriters for the users are also stored in a Hashset.  Storing them this was is beneficial for when the server has to
output stuff to every PrintWriter in the Hashset.  

The client is resposible for gathering input from the user and sending it to the server.  The client also recieves information from other
users after it is route through the server.  

Although we wish we could have designed a server as simple as this, a lot of credit goes to http://cs.lmu.edu/~ray/notes/javanetexamples/.
This provided the foundation we needed to add the many features that you are currently using.  

Enjoy. 
