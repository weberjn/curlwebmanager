# curl WebManager

This is a Java Servlet Web GUI to start downloads with curl. Enter curl commands or http urls.

The application is secured by web application security. Add a user with the role curlmgr to your application server.

Click the Reload icon to update the status display.

After the download is completed, the process information is still displayed. Remove it with the clean button.

# Security

This must be run under web application security. Without, it is the mother of all security holes, as you can enter free curl commands.

# supported Application servers

Should run on all Webapp 3.1 servers, e.g. Tomcat 8.5, Wildfly 10. Needs Java 8.

For best energy efficiency run on Raspberry pi.

# Win32

There must be a curl in the PATH, e.g. from a [Git for Windows](https://git-for-windows.github.io/) installation. Windows 10 has it by default.

# Bugs

curlWebManager is currently single user.

# License

The application is under the Apache License 2.0

Copyright 2016-2019 by Jürgen Weber

# Screenshot

![curlWebManager](doc/curlManager.png?raw=true)



