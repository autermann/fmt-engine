Title: Flashmob Toolkit Service Installation Guide
Author: Christian Autermann
Date: August 11, 2012
Base Header Level: 3

# Flashmob Toolkit Service Installation Guide #

## Installing Dependencies ##
- Install [Maven][1]
- Install and start [MongoDB][2]
- Install and start [Tomcat][3] 6 or 7

## Building ##
Switch to the project directory and run the following:

	mvn -DskipTests package

## Installing ##
After building move the generated WAR file to the webapps folder of your Tomcat installation (e.g. `/var/lib/tomcat6/webapps`).
	
	sudo mv target/fmt.war ${webapps}

This job can also be done by uploading the WAR file to the manager application using the GUI or CLI interface:
	
	curl -u ${user}:${pass} http://localhost:8080/manager/deploy?path=/fmt&war=${path_to_project}/target/fmt.war


## Developing ##
- Install [Netbeans][4] and import the project folder
- Install [Eclipse][5]
	- Install [m2eclipse][6] and import the project folder as a Maven project
	- Run `mvn eclipse:eclipse` and import the project folder as a Java project

[1]: http://maven.apache.org/
[2]: http://www.mongodb.org/
[3]: http://tomcat.apache.org/
[4]: http://netbeans.org/
[5]: http://www.eclipse.org/
[6]: http://www.sonatype.org/m2eclipse/
