#!/usr/bin/env bash

# Create topics
VAR=100
for ((i = 0; i <= VAR; i++)); do
    kafka-topics --bootstrap-server localhost:9092 --create --topic "test-${i}" --partitions 3  --config min.insync.replicas=2 --replication-factor 3
done


# --replica-placement ./placement.json


# Generate traffic
#VAR=5
#for ((i = 0; i <= VAR; i++)); do
#    kafka-producer-perf-test --topic "test-${i}" --num-records 1000000 --record-size 1000 --throughput 10000000 --producer-props bootstrap.servers=localhost:9092
#done

# get metrics from prometheus
curl 'http://localhost:9090/api/v1/query?query=kafka_server_replicamanager_leadercount' | jq '.data.result[] | { "broker" : .metric.instance, "leader count" : .value[1] }'
