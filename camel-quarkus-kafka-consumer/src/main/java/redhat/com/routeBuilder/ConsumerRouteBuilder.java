package redhat.com.routeBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

public class ConsumerRouteBuilder extends RouteBuilder{
    protected String KAFKA_TOPIC = "{{kafka.topic}}";
    protected String KAFKA_BOOTSTRAP_SERVERS = "{{kafka.bootstrap.servers}}";
    protected String KAFKA_GROUP_ID = "{{kafka.group.id}}";

    protected String FileNameId = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US).format(new Date());
    
    @Override
    public void configure() throws Exception {
        
               
        //Route that consumes message to kafka topic
        from("kafka:"+ KAFKA_TOPIC + "?brokers=" + KAFKA_BOOTSTRAP_SERVERS + "&groupId=" + KAFKA_GROUP_ID)
        .routeId("kafkaConsumer")
        .log("Message received from Kafka : ${body}")
        .setHeader(Exchange.FILE_NAME, constant("message-" + FileNameId + ".txt"))
        .to( "file:/tmp");
        ;
    }
}