set REBEL_HOME=misc
set MAVEN_OPTS=-noverify -javaagent:%REBEL_HOME%/jrebel.jar -Drebel.guice_plugin=true %MAVEN_OPTS%


mvn -Djetty.port=8082 jetty:run
