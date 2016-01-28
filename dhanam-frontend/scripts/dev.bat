set JAVA_HOME=C:/dev/java/jdk1.8.0_60/

set PATH=C:/dev/java/jdk1.8.0_60/bin;

set CLASSPATH=C:/projects/dhanam/dhanam-frontend/target/classes/.;C:/projects/dhanam/dhanam-frontend/target/lib/*;

java -XXaltjvm=dcevm -javaagent:C:/dev/hotswap-agent.jar=autoHotswap=true -Djava.awt.headless=true -DLOG_DIR=c:/temp -cp "%CLASSPATH%" dhan.frontend.server.App dev