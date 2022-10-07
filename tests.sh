#!/bin/bash

echo "Setting env vars..."

MVN_HOME="tools/apache-maven-3.8.6"
GRADLE_HOME="tools/gradle-7.5.1"
TOMEE_HOME="tools/apache-tomee-microprofile-9.0.0-M8"
PATH=$PATH:"$MVN_HOME\bin":"$GRADLE_HOME\bin":$TOMEE_HOME
export PATH

echo "[Building and Running]"

echo "[payment-ms] Building ..."
mvn clean package -f payment-ms/pom.xml

echo "[payment-ms] Deploying ..."
cp ./payment-ms/target/payment-ms.war $TOMEE_HOME/webapps/

echo "[stock-ms] Building ..."
mvn clean package -f stock-ms/pom.xml

echo "[stock-ms] Deploying ..."
cp ./stock-ms/target/stock-ms.war $TOMEE_HOME/webapps/

echo "[Starting Servers]"
./$TOMEE_HOME/bin/startup.sh

echo "[purchase-ms] Starting ..."
gradle clean bootRun --settings-file purchase-ms/settings.gradle.kts&
PURCHASE_MS_PID=$!

echo "[tracking-ms] Starting ..."
gradle clean bootRun --settings-file track-ms/settings.gradle.kts&
TRACK_MS_PID=$!

echo "[Testing]"
sleep 60

echo "[Shutting down]"

echo "[Shutting down] Apache TomEE..."
./$TOMEE_HOME/bin/shutdown.sh

echo "[Shutting down] Spring Applications..."
kill -9 $PURCHASE_MS_PID $TRACK_MS_PID


