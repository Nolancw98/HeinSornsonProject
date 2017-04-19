Welcome to Eddie and Nolan's Social Media Platform.  

This platform consists of two parts, a server and a client.

The server is responsible for handling pretty much everything.  It checks usernames and passwords and stores them in an ArrayList.
Names are stored in a hashset for easy searching.  The server stores logs in a Queue as well.  This queue is written to a file everytime there is a post and therefore will never miss a post if the server crashes.  When the server is loaded up again, the Queue is read from file and pops off the posts everytime a user joins the chat.  The server also stores a state for every client connected to it.  This determines what to prompt the user with.  Doing all of this stuff server side improves security as well as the ability to have post history. The PrintWriters for the users are also stored in a Hashset.  Storing them this was is beneficial for when the server has to output stuff to every PrintWriter in the Hashset.  

The client is responsible for gathering input from the user and sending it to the server.  The client prompts the user for an IP of the server, the port of the server, and then determines if the user is a returning user.  If yes, it directs the user to a login prompt, if no, it directs the user to a create account prompt.  The client also recieves information from other users after it is route through the server.  The client receieves a full copy the log the minute the login is successful and is then added to the running list of printwriters that the server has to print to.  

Although we wish we could have designed a server as simple as this, a lot of credit goes to http://cs.lmu.edu/~ray/notes/javanetexamples/. This provided the foundation we needed to add the many features that you are currently using.  

Enjoy. 
