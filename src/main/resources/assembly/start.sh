#!/usr/bin/env bash

main_class=com.blsq.admin.BlAdminRun
server_name=BlAdminRun
server_id= #""

if [ -n "$JAVA_HOME" ]; then
    if [ -x "$JAVA_HOME/bin/java" ]; then
        JAVACMD="$JAVA_HOME/bin/java"
    else
        echo "** ERROR: java under JAVA_HOME=$JAVA_HOME cannot be executed"
        exit 1
    fi
else
    JAVACMD=`which java 2> /dev/null`
    if [ -z "$JAVACMD" ]; then
        JAVACMD=java
    fi
fi

# Verify that it is java 5+
javaVersion=`$JAVACMD -version 2>&1 | grep "java version" | egrep -e "1\.[678]"`
if [ -z "$javaVersion" ]; then
    $JAVACMD -version
    echo "** ERROR: The Java of $JAVACMD version is not 1.6 and above."
    exit 1
fi

if [ -z "$SERVER_HOME" ]; then
    SERVER_HOME=`dirname "$0"`/..
fi

# Verify minimal JVM props are set
#hasMinHeapSize=`echo "$JAVA_OPTIONS" | grep "\\-Xms"`
#if [ -z "$hasMinHeapSize" ]; then
#    JAVA_OPTIONS="$JAVA_OPTIONS -Xms256"
#fi
#hasMaxHeapSize=`echo "$JAVA_OPTIONS" | grep "\\-Xmx"`
#if [ -z "$hasMaxHeapSize" ]; then
#    JAVA_OPTIONS="$JAVA_OPTIONS -Xmx1g"
#fi
#hasMinPermSize=`echo "$JAVA_OPTIONS" | grep "\\-XX:PermSize"`
#if [ -z "$hasMinPermSize" ]; then
#    JAVA_OPTIONS="$JAVA_OPTIONS -XX:PermSize=32m"
#fi
#hasMaxPermSize=`echo "$JAVA_OPTIONS" | grep "\\-XX:MaxPermSize"`
#if [ -z "$hasMaxPermSize" ]; then
#    JAVA_OPTIONS="$JAVA_OPTIONS -XX:MaxPermSize=128m"
#fi
#hasMinNewSize=`echo "$JAVA_OPTIONS" | grep "\\-XX:NewSize"`
#if [ -z "$hasMinNewSize" ]; then
#    JAVA_OPTIONS="$JAVA_OPTIONS -XX:NewSize=256m"
#fi
#hasMaxNewSize=`echo "$JAVA_OPTIONS" | grep "\\-XX:MaxNewSize"`
#if [ -z "$hasMaxNewSize" ]; then
#    JAVA_OPTIONS="$JAVA_OPTIONS -XX:MaxNewSize=512m"
#fi
#hasGcFlags=`echo "$JAVA_OPTIONS" | grep "\\-XX:-Use.*GC"`
#if [ -z "$hasGcFlags" ]; then
#    JAVA_OPTIONS="$JAVA_OPTIONS -XX:-UseConcMarkSweepGC -XX:+UseParNewGC"
#fi

JAVA_OPTIONS="$JAVA_OPTIONS -server -Dserver.home=$SERVER_HOME -Dfile.encoding=UTF8 -Dserver.name=$server_name"

LIB_DIR=$SERVER_HOME/lib

CLASSPATH="$CLASSPATH:$SERVER_HOME/conf"
# Add all jars under the lib dir to the classpath
for i in `ls $LIB_DIR/*.jar`
do
    CLASSPATH="$CLASSPATH:$i"
done

echo "Runing: exec $JAVACMD $JAVA_OPTIONS -cp \"$CLASSPATH\" $main_class $server_id $@"
exec "$JAVACMD" $JAVA_OPTIONS -cp "$CLASSPATH" $main_class $server_id "$@" &
