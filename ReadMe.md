# Demo - From Kafka Streams To Kafka Connectors

#### This Repository is created to help those who wants to set up Confluent Kafka Cluster, MySql in Docker Desktop. Then produce the messages using Kafka Producer Template, then these messages will be processed using Kafka Streams Processor and then posted to MySql using Kafka Connect API (with JDBC Connector)
#### I have used below tech stack

* ##### Spring Boot
* ##### Spring Integration
* ##### Spring Kafka
* ##### Kafka streams
* ##### Confluent Kafka [Connect - JDBC Connector, Schema Registry, Avros]
* ##### Kotlin

###### REST Ops

````
# Available Connectors
http://localhost:8083/connectors

#Installed Plugins
http://localhost:8083/connector-plugins

#Install a JDBC Connector Plugin
curl -X POST \
  -H "Content-Type: application/json" \
  --data '{ "name" : "mysql-sink-connector", "config" : { "connection.url" : "jdbc:mysql://mysql:3306/deals_db", "connection.user" : "<replace_username>", "connection.password" : "<replace_password>", "connection.attempts" : "3", "connection.backoff.ms" : "5000", "table.whitelist" : "deals_db.ALERTSUBSCRIPTION", "table.name.format" : "deals_db.ALERTSUBSCRIPTION", "db.timezone" : "UTC", "connector.class" : "io.confluent.connect.jdbc.JdbcSinkConnector", "dialect.name" : "MySqlDatabaseDialect", "auto.create" : "true", "auto.evolve" : "true", "tasks.max" : "1", "batch.size" : "10", "topics" : "deals_topic_processed", "value.converter.schema.enable":"true", "key.converter.schemas.enable": "false", "key.converter" :"org.apache.kafka.connect.storage.StringConverter", "value.converter" : "io.confluent.connect.avro.AvroConverter", "value.converter.schema.registry.url" : "http://schema-registry:8081", "insert.mode" : "INSERT", "pk.mode" : "none" }}' \
  http://localhost:8083/connectors

#Get Installed Plugin Status
http://localhost:8083/connectors/mysql-sink-connector/status
````

###### Maven Commands

* Setup the Environment i.e. Kafka & MySQL with below command
````
    $> docker compose up -d
````
<img src="https://github.com/ashokgudise/kafka-streams-to-custom-connectors-demo/blob/main/docker-desktop.png" style=" width:900px ; height:600px "> 

* Start Kafka Producer with below command
````
    $> cd kafka-producer
    $> mvn spring-boot:run -Dspring.profiles.active=avro
````
<img src="https://github.com/ashokgudise/kafka-streams-to-custom-connectors-demo/blob/main/producer.png" style=" width:900px ; height:600px "> 

* If you just want to consume above messages start consumer app

````
    $> cd kafka-consumer
    $> mvn spring-boot:run -Dspring.profiles.active=avro
````

* To post to messages to Database using JDBC Connector, start the processor app with below command
````
    $> cd stream-processor
    $> mvn spring-boot:run -Dspring.profiles.active=avro
````
<img src="https://github.com/ashokgudise/kafka-streams-to-custom-connectors-demo/blob/main/processor.png" style=" width:900px ; height:600px "> 

###### Open Database Explorer of your own choice (I am using IntelliJ - Database Tool)

<img src="https://github.com/ashokgudise/kafka-streams-to-custom-connectors-demo/blob/main/mysql_db.png" style=" width:900px ; height:600px "> 

###### Query the Table that you have provided in schema

<img src="https://github.com/ashokgudise/kafka-streams-to-custom-connectors-demo/blob/main/query_table.png" style=" width:900px ; height:600px "> 

###### You should see data flowing to Mysql 

<img src="https://github.com/ashokgudise/kafka-streams-to-custom-connectors-demo/blob/main/mysql_data.png" style=" width:900px ; height:600px "> 

### Helpful References
 [Kafka Connector Ref- MySQL ](https://dev.to/cosmostail/mysql-8-kafka-connect-tutorial-on-docker-479p)

 [Database Identifiers, Case Sensitivity Guidelines](https://alberton.info/dbms_identifiers_and_case_sensitivity.html)

 [Troubleshooting JDBC Connectors](https://stackoverflow.com/questions/60763108/unable-to-post-jdbc-kafka-sink-connector-config-using-rest-call)

 [Kafka Connect Official Github Repo](https://github.com/confluentinc/demo-scene/tree/master/connect-jdbc)

 [Confluent Cluster Docker YML Reference](https://github.com/confluentinc/demo-scene/blob/master/syslog/docker-compose.yml)

 [Confluent Kafka Examples - Demo Scene](https://github.com/confluentinc/demo-scene/blob/master/syslog/docker-compose.yml)

### ~ Happy Learning!   
### Questions with setup?  [Find me in LinkedIn](https://www.linkedin.com/in/ashokgudise/)
