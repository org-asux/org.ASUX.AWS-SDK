## Each INPUT-file must be generated using the command:-
##      mvn -DoutputFile=${THISFILE}   dependency:tree
## It should then be edited - to REMOVE the 1st 3 characters of each line.
## To help others, copy these 5 header lines!   Add a few blank lines to make everyone feel comfortable editing this file!
## That's it!

### Notice: extra ':'	
io.netty:netty-transport-native-epoll:jar:linux-x86_64:4.1.33.Final:runtime	### <---- REPLACE THIS LINE .. with following-line.
io.netty:netty-transport-native-epoll:jar:4.1.33.Final:runtime

###========================================================

org.slf4j:slf4j-api:jar:1.7.25:compile			### <-- This comes from "dependency:tree" .. but you need the following!!

org.slf4j:slf4j-simple:jar:1.7.28:runtime
###	(RUNTIME ERROR):   SLF4J: Failed to load class org.slf4j.impl.StaticLoggerBinder
###	This warning message is reported when the org.slf4j.impl.StaticLoggerBinder class could not be loaded into memory.
###	This happens when no appropriate SLF4J binding could be found on the class path.
###	Placing one (and only one) of slf4j-nop.jar slf4j-simple.jar, slf4j-log4j12.jar, slf4j-jdk14.jar or logback-classic.jar on the class path should solve the problem.
###	SINCE 1.6.0 As of SLF4J version 1.6, in the absence of a binding, SLF4J will default to a no-operation (NOP) logger implementation.
###	If you are responsible for packaging an application and do not care about logging, then placing slf4j-nop.jar on the class path of your application will get rid of this warning message.
###	Note that embedded components such as libraries or frameworks should not declare a dependency on any SLF4J binding but only depend on slf4j-api. When a library declares a compile-time dependency on a SLF4J binding, it imposes that binding on the end-user, thus negating SLF4J's purpose.

###========================================================


#EoF
