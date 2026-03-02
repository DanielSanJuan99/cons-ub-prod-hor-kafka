package com.duoc.cons_ub_prod_hor_kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.duoc.cons_ub_prod_hor_kafka.dto.HorarioDTO;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public static final String TOPIC_HORARIOS = "horarios";

    @Bean
    NewTopic topicHorarios() {
        Map<String, String> configs = new HashMap<>();
        configs.put("retention.ms", "43200000"); // 12 horas
        return new NewTopic(TOPIC_HORARIOS, 3, (short) 1).configs(configs);
    }

    @Bean
    ProducerFactory<String, HorarioDTO> horarioProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        configProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,
                "org.apache.kafka.clients.producer.RoundRobinPartitioner");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    KafkaTemplate<String, HorarioDTO> horarioKafkaTemplate() {
        return new KafkaTemplate<>(horarioProducerFactory());
    }
}
