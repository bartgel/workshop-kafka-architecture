# Using schema registry to validate messages

## Create a schema

```bash
curl --request POST \
  --url http://localhost:8081/subjects/test/versions \
  --header 'content-type: application/json' \
  --header 'user-agent: vscode-restclient' \
  --data '{"schema":"{\"type\": \"record\",\"name\": \"test\",\"fields\":[{\"type\": \"string\",\"name\": \"field1\"},{\"type\": \"int\",\"name\": \"field2\"}]}","schemaType": "AVRO"}'
```

## Producing data with a schema
```bash
cat test.avro | kafka-avro-console-producer \
 --broker-list localhost:9092 \
 --topic test  \
 --property schema.registry.url=http://localhost:8081 \
 --property value.schema.id=1
```

## Consuming data with a schema
```bash
kafka-avro-console-consumer \
 --bootstrap-server localhost:9092 \
 --topic test  \
 --from-beginning \
 --group avro-group \
 --property schema.registry.url=http://localhost:8081 \
 --property value.schema.id=1
```