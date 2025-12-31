#!/bin/sh
DIRNAME=$(dirname "$0")
APP_HOME=$DIRNAME
exec java -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"