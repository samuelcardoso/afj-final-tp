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
sleep 30

purchase_ms_buy_id=$(curl -s --request POST --url http://localhost:8081/purchase-ms/rest/purchase/buy | tr -d '"')
echo $purchase_ms_buy_id

payment_ms_status=$(curl -o /dev/null -s -w "%{http_code}\n" --request GET --url http://localhost:8080/payment-ms/rest/payment/status/$purchase_ms_buy_id)
echo $payment_ms_status

stock_ms_status=$(curl -o /dev/null -s -w "%{http_code}\n" --request GET --url http://localhost:8080/stock-ms/rest/stock/status/$purchase_ms_buy_id)
echo $stock_ms_status

track_ms_status=$(curl -o /dev/null -s -w "%{http_code}\n" --request GET --url http://localhost:8082/track-ms/rest/track/status/$purchase_ms_buy_id)
echo $track_ms_status

if [ "$track_ms_status" == "200" ]; then
    echo "OK"
else
    echo "Not OK!"
fi

echo "[Shutting down]"

echo "[Shutting down] Apache TomEE..."
./$TOMEE_HOME/bin/shutdown.sh

echo "[Shutting down] Spring Applications..."
kill -9 $PURCHASE_MS_PID $TRACK_MS_PID


