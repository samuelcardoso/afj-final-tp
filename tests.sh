#!/bin/bash

echo "[payment-ms] Starting ..."
./payment-ms/gradlew clean bootRun --settings-file payment-ms/settings.gradle.kts&
PAYMENT_MS_PID=$!

echo "[purchase-ms] Starting ..."
./purchase-ms/gradlew clean bootRun --settings-file purchase-ms/settings.gradle.kts&
PURCHASE_MS_PID=$!

echo "[stock-ms] Starting ..."
./stock-ms/gradlew clean bootRun --settings-file stock-ms/settings.gradle.kts&
STOCK_MS_PID=$!

echo "[tracking-ms] Starting ..."
./track-ms/gradlew clean bootRun --settings-file track-ms/settings.gradle.kts&
TRACK_MS_PID=$!

sleep 60
echo "[Testing]"
echo "TODO !!!"

echo "[Shutting down] Spring Applications..."
kill -9 $PAYMENT_MS_PID $PURCHASE_MS_PID $STOCK_MS_PID $TRACK_MS_PID
