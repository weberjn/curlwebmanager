# curlWebManager

This is a Web Gui to start downloads with curl. Enter curl commands or http urls.

The application is secured by web application security. Add a user with the role curlmgr to your application server.

Click the Reload icon to update the status display.

After the download is completed, the process information is still displayed. Remove it with clean or clean all.

# Security

This must be run under web application security. Without, it is the mother of all security holes, as you can enter free curl commands.

# supported Application servers

Should run on all Webapp 3.1 servers, i.e. Tomcat 8.5, Wildfly 10. Needs Java 8.

For best energy efficency run on Raspberry pi.

# License

The application is under the Apache License 2.0

Copyright 2016-2017 by Jürgen Weber


