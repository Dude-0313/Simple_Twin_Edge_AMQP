// Start broker with :
//  docker compose up in Twin directory
// docker build -t twin-docker .
// docker run --net="host" twin-docker

package com.springTwin.springtwinClient;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.EnableScheduling; 
import org.springframework.scheduling.annotation.Scheduled; 
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.json.*; 
@SpringBootApplication
@EnableScheduling
public class Application {

	public static final boolean NON_DURABLE = false;
	public static final String QUEUE_NAME = "twinQueue";
	public static final String XCHNG_NAME = "twinExchange";

	public static final String TWN_QUEUE_NAME = "twinMotor.telemetry";
	public static final String TWN_XCHNG_NAME = "twinExchange";

	twinSetup twins;
	RabbitTemplate rabbitTemplate;
	
	public Application() {
		try {
			System.out.println("Creating Twins ...");
			twins = new twinSetup(2);
			System.out.println("Twins online ...");
		} catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);

	}

	@Bean
	public ApplicationRunner runner(RabbitTemplate template) {
		this.rabbitTemplate = template;
		return args->{
			template.convertAndSend(QUEUE_NAME, "Test Message");
		};
	}
	
	@Bean
	public Queue testQueue() {
		return new Queue(QUEUE_NAME,NON_DURABLE);
	}

	// Create a Topic Exchange for Telemetry data
	// Direct – the exchange forwards the message to a queue based on a routing key
	// Fanout – the exchange ignores the routing key and forwards the message to all bounded queues
	// Topic – the exchange routes the message to bounded queues using the match between a pattern defined on the exchange and the routing keys attached to the queues
	// Headers – in this case, the message header attributes are used, instead of the routing key, to bind an exchange to one or more queues
	@Bean
	public TopicExchange twinExchange() {
		System.out.println("Creating twin topic exchange..");
		return new TopicExchange(TWN_XCHNG_NAME);
	}
	
	@Bean
	public Queue twinQueue() {
		System.out.println("Creating twin queue..");
		return new Queue(TWN_QUEUE_NAME,NON_DURABLE);
	}
	
	@RabbitListener(queues = QUEUE_NAME)
	public void listen(String in) {
		System.out.println("Message from queue : "+ in);
	}
	
	@Bean
	public Binding declareTwinBinding() {
		BindingBuilder.bind(twinQueue()).to(twinExchange()).with(twins.getRoutingKey(1));
		return BindingBuilder.bind(twinQueue()).to(twinExchange()).with(twins.getRoutingKey(2));
	}
	
	@Scheduled(fixedDelay=5000L)
	public void publishTelemetry() {
		int x=0;
		for(x=1;x<=twins.get_device_count();x++) {
			JSONObject jsonObject = twins.get_device_telemetry(x);
			if(jsonObject == null) return;
			String message = jsonObject.toString();		
			System.out.println("Publishing telemetry for motor : "+x);
			System.out.println(message);
			rabbitTemplate.convertAndSend(TWN_XCHNG_NAME,twins.getRoutingKey(x),message);
		}
	}
}
