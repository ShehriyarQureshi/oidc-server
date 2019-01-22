# GSMA Mobile Connect Profile Implementation (In Active Development)
This is part of my internship work at Ufone. It aims to directly be used or serve as a blueprint for implementing Mobile Connect.

I've selected the MIT license so that Operator specific implementation can be used without having to open source the operator's middleware integration code.

## What it aims to be
I've written a blog about how I think GSMA Mobile Connect should be implemented. This project aims to follow that architecture design.

[You can read it here](https://medium.com/@Shehriyar.Qureshi/my-take-on-implementing-gsma-mobile-connect-dca0f64c6d3b)

## Installation Steps

I am running this on Tomcat. I'm not sure if you can run this on another Java Servlet Container without any change in code but this should work for Tomcat.

I have a script called `send_to_tom.sh` which automatically builds the project with maven and copies the file to Tomcat directory (for *nix users).

You should build the project with `mvn clean verify` (that's what I'm using for now) and depending on your OS, move it to tomcat so it can use it.

Note that these steps will vary depending on your OS. I believe you will know how to build a project with maven and set it up to run on Tomcat depending on whatever IDE/OS you're using.


## Trying it out

Please check out the [User Guide](UserGuide.md)
