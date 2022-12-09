#!/usr/bin/env bash

VAR=100
for ((i = 1; i <= VAR; i++)); do
    kafka-topics --bootstrap-server localhost:9092 --delete --topic "test-${i}" 
done
