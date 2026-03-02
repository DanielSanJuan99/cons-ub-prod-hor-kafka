package com.duoc.cons_ub_prod_hor_kafka.service.impl;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.duoc.cons_ub_prod_hor_kafka.config.KafkaProducerConfig;
import com.duoc.cons_ub_prod_hor_kafka.dto.HorarioDTO;
import com.duoc.cons_ub_prod_hor_kafka.service.HorarioProducerService;

@Service
public class HorarioProducerServiceImpl implements HorarioProducerService {

    @Autowired
    private KafkaTemplate<String, HorarioDTO> horarioKafkaTemplate;

    @Override
    public void enviarHorario(HorarioDTO horario) {
        horario.setId(System.nanoTime());
        horario.setTimestamp(Instant.now().toString());

        horarioKafkaTemplate.send(KafkaProducerConfig.TOPIC_HORARIOS, horario);
        System.out.println("[PRODUCER] Horario enviado a Kafka: " + horario.toString());
    }
}
