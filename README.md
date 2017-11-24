# curl WebManager

This is a Java Servlet Web Gui to start downloads with curl. Enter curl commands or http urls.

The application is secured by web application security. Add a user with the role curlmgr to your application server.

Click the Reload icon to update the status display.

After the download is completed, the process information is still displayed. Remove it with clean or clean all.

# Security

This must be run under web application security. Without, it is the mother of all security holes, as you can enter free curl commands.

# supported Application servers

Should run on all Webapp 3.1 servers, i.e. Tomcat 8.5, Wildfly 10. Needs Java 8.

For best energy efficency run on Raspberry pi.

# Win32

There must be a curl in the PATH, e.g. from a [Git for Windows](https://git-for-windows.github.io/) installation.

# Bugs

curlWebManager is currently single user.

The Java process API does not reliably terminate a process, so there is no way to cancel a curl session. You have to kill curl from the Shell or from [Process Explorer](https://docs.microsoft.com/en-us/sysinternals/downloads/process-explorer) in Windows.

# License

The application is under the Apache License 2.0

Copyright 2016-2017 by Jürgen Weber

# Screenshot

![curlWebManager](doc/curlManager.png?raw=true)



