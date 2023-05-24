package redhat.com.routeBuilder;


import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import redhat.com.models.Product;


public class ConsumerRouteBuilder extends RouteBuilder{
    protected String KAFKA_TOPIC = "{{kafka.topic}}";
    protected String KAFKA_BOOTSTRAP_SERVERS = "{{kafka.bootstrap.servers}}";
    protected String KAFKA_GROUP_ID = "{{kafka.group.id}}";
    protected String MONGO_DB_HOST = "{{mongo.db.host}}";
    protected String MONGO_DB_DATABASE = "{{mongo.db.database}}";
    protected String MONGO_DB_COLLECTION = "{{mongo.db.collection}}";
    protected String MONGO_DB_USERNAME = "{{mongo.db.username}}";
    protected String MONGO_DB_PASSWORD = "{{mongo.db.password}}";
    @Override
    public void configure() throws Exception {
        
               
        //Route that consumes message to kafka topic
        from("kafka:"+ KAFKA_TOPIC + "?brokers=" + KAFKA_BOOTSTRAP_SERVERS + "&groupId=" + KAFKA_GROUP_ID)
        .routeId("kafkaConsumer")
        .unmarshal(new JacksonDataFormat(Product.class))
        .log("Message received from Kafka : ${body}")
        .to("direct:insertMongoDb")
        ;

        //Route insert object on mongoDB
        from("direct:insertMongoDb").routeId("insertMongoDb")        
        .to("mongodb:mongoBean?hosts="+ MONGO_DB_HOST +"&username="+MONGO_DB_USERNAME+"&password="+MONGO_DB_PASSWORD+"&database="+ MONGO_DB_DATABASE +"&collection="+ MONGO_DB_COLLECTION +"&operation=insert")
        
        ;
    }
}