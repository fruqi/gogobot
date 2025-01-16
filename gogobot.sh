#!/bin/bash

JAR_FILE="/opt/app.jar"

if [ ! -f "$JAR_FILE" ]; then
    echo "JAR file does not exist: $JAR_FILE"
    exit 1
fi

java -jar "$JAR_FILE"
