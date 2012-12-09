If you have decided to complete hw9 with your own Facelook interface, you can safely ignore this file. Otherwise, read on.

This file contains basic guidelines to help you get started if you have decided to work with the staff provided interface as opposed to your own.

Most of the functionality of Facelook has been implemented in our interface, the exception being any interfaces dependent on information from the backend (e.g. the list of status posts). 

The following have spaces for you to fill in your own code to communicate with your backend. They are marked off by //TODO comments in each of the files.

FriendListPanel.java
LoginPanel.java
NewsFeedPanel.java
ProfilePanel.java
RegisterPanel.java

To get the servers running, run the main method found in ...hw9.backend.Server.java. To change the number and location of the servers, change the values found
in ServerConstants.java.

For testing purposes, the servers were set up so that they could all run on the same machine in separate threads. For this reason each thread takes a different port, 
incrementing the base port by the server ID to get that servers port.

To change the cache size, simply change the CACHE_SIZE variable in Cache.java