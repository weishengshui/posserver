#!/bin/sh

export REBEL_HOME=misc
export MAVEN_OPTS="-noverify -javaagent:${REBEL_HOME}/jrebel.jar -Drebel.guice_plugin=true ${MAVEN_OPTS}"

mvn3 jetty:run
