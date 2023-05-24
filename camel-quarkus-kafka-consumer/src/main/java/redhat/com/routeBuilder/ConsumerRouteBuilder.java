package redhat.com.routeBuilder;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import redhat.com.models.Product;

public class ConsumerRouteBuilder extends RouteBuilder{
    protected String KAFKA_TOPIC = "{{kafka.topic}}";
    protected String KAFKA_BOOTSTRAP_SERVERS = "{{kafka.bootstrap.servers}}";
    protected String KAFKA_GROUP_ID = "{{kafka.group.id}}";
    
    @Override
    public void configure() throws Exception {
        
               
        //Route that consumes message to kafka topic
        from("kafka:"+ KAFKA_TOPIC + "?brokers=" + KAFKA_BOOTSTRAP_SERVERS + "&groupId=" + KAFKA_GROUP_ID)
        .routeId("kafkaConsumer")
        .unmarshal(new JacksonDataFormat(Product.class))
        .log("Message received from Kafka : ${body}")
        .to("direct:createFile")
        ;

        //Route insert object on mongoDB
        from("direct:createFile").routeId("createFile")        
        .setHeader(Exchange.FILE_NAME, constant("report.txt"))
        .to( "file:/tmp");
        
        ;
    }
}