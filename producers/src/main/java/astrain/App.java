package astrain;

import java.io.InputStreamReader;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;

import com.opencsv.CSVReader;

@SpringBootApplication
public class App {

    private final Logger logger = LoggerFactory.getLogger(App.class);

    private final Producer producer;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(App.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

    @Bean
    public CommandLineRunner CommandLineRunnerBean() {
        return (args) -> {
            for (String arg : args) {
                switch (arg) {
                    case "--producer":

                        // change data file for different datasets and
                        // make sure to update topics accordingly
                        var res = getClass().getClassLoader()
                                .getResourceAsStream("data/SensorLocationMetaData.csv");
                        if (res == null) {
                            throw new IllegalArgumentException("file not found!");
                        }

                        try (CSVReader reader = new CSVReader(new InputStreamReader(res))) {
                            reader.readNext();
                            String[] lineInArray;
                            while ((lineInArray = reader.readNext()) != null) {
                                String[] line = lineInArray[0].split(";");
                                logger.info(String.format("KEY is: %s", line[0]));
                                producer.sendMessage(line[0], lineInArray[0]);
                            }
                        }

                        break;
                    case "--consumer":
                        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry
                                .getListenerContainer("myConsumer");
                        listenerContainer.start();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Autowired
    App(Producer producer) {
        this.producer = producer;
    }

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

}