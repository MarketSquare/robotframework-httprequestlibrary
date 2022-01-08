# REST testing library for Robot Framework
Introduction
------------
Robot Framework's library to test REST interfaces.

Library is mostly following structure of [Requests-library](https://github.com/bulkan/robotframework-requests).

* More information about this library can be found in the
  [Keyword Documentation](https://repo1.maven.org/maven2/com/github/marketsquare/robotframework-httprequestlibrary/0.0.18/robotframework-httprequestlibrary-0.0.18.html).
* For keyword completion in RIDE you can download this
  [Library Specs](https://repo1.maven.org/maven2/com/github/marketsquare/robotframework-httprequestlibrary/0.0.18/robotframework-httprequestlibrary-0.0.18.xml)
  and place it in your PYTHONPATH.

Usage
-----
If you are using the [robotframework-maven-plugin](http://robotframework.org/MavenPlugin/) you can
use this library by adding the following dependency to 
your pom.xml:

    <dependency>
        <groupId>com.github.marketsquare</groupId>
        <artifactId>robotframework-httprequestlibrary</artifactId>
        <version>0.0.18</version>
    </dependency>
    
With Gradle, library can be use by importing it as a dependency in build.gradle:

    runtime('com.github.marketsquare:robotframework-httprequestlibrary:0.0.18')
    
If you are not using any dependency management too, you can use the
[jar-with-dependencies](https://repo1.maven.org/maven2/com/github/marketsquare/robotframework-httprequestlibrary/0.0.18/robotframework-httprequestlibrary-0.0.18-jar-with-dependencies.jar),
which contains all required libraries. Running tests with that is done with command:
    
    java -jar robotframework-httprequestlibrary-0.0.18-jar-with-dependencies.jar <test directory> 

Library import in Robot tests can be done with:

|                    |                                 |
| ----------------   | ------------------------------- | 
| *** Settings ***   |                                 |                 
| Library            | HttpRequestLibrary              |   
   
Example
-------
Usage examples can be found at [Tests-folder](/src/test/robotframework/acceptance).
