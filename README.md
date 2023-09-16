# AstrAIn services

> "Astreine Services! -unknown"


## Notes

The producer is used to push the sensor data etc to the topics in Confluent Cloud. The file and topics need to be manually updated in the code for now.

Once the file and topics are changed, run the producer as per instructions below.

To test the data on the topics, run the consumer as per instructions below

## Usage

Connection details are currently hard coded in the `application.yaml` file.
- `SERVERS`: replace with confluent cloud cluster endpoint
- `APIKEY`: replace with API key which has access to all topics in the cluster (use global access for the demo)
- `APISECRET`: replace with API secret for the key


Build
```
gradle build
```

Run the producer
```
gradle bootRun --args='--producer'
```

Run the consumer
```
gradle bootRun --args='--consumer'
```

 



