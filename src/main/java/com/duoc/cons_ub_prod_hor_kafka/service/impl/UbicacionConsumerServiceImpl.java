package com.duoc.cons_ub_prod_hor_kafka.service.impl;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.duoc.cons_ub_prod_hor_kafka.config.KafkaConsumerConfig;
import com.duoc.cons_ub_prod_hor_kafka.dto.HorarioDTO;
import com.duoc.cons_ub_prod_hor_kafka.dto.UbicacionVehiculoDTO;
import com.duoc.cons_ub_prod_hor_kafka.service.HorarioProducerService;
import com.duoc.cons_ub_prod_hor_kafka.service.UbicacionConsumerService;

@Service
public class UbicacionConsumerServiceImpl implements UbicacionConsumerService {

    @Autowired
    private HorarioProducerService horarioProducerService;

    private final Random random = new Random();

    private static final List<String> PARADAS = Arrays.asList(
            "Parada Terminal", "Parada Plaza Central", "Parada Hospital",
            "Parada Universidad", "Parada Mall", "Parada Metro",
            "Parada Estadio", "Parada Parque"
    );

    @Override
    @KafkaListener(id = "ubicacionListener", topics = KafkaConsumerConfig.TOPIC_UBICACIONES,
            groupId = KafkaConsumerConfig.CONSUMER_GROUP_ID)
    public void consumirUbicacion(UbicacionVehiculoDTO ubicacion, Acknowledgment ack) {
        try {
            System.out.println("[CONSUMER] Ubicacion recibida: " + ubicacion.toString());

            // Procesar la ubicacion y generar un horario actualizado
            HorarioDTO horario = procesarUbicacionYGenerarHorario(ubicacion);

            // Publicar el horario al topico "horarios"
            horarioProducerService.enviarHorario(horario);

            ack.acknowledge();
            System.out.println("[CONSUMER] Ubicacion procesada y horario generado: " + horario.toString());

        } catch (Exception e) {
            System.out.println("[CONSUMER] ERROR procesando ubicacion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Logica de procesamiento: recibe una ubicacion y genera un horario estimado.
     * Simula la estimacion de hora de llegada a la siguiente parada basado en la ubicacion.
     */
    private HorarioDTO procesarUbicacionYGenerarHorario(UbicacionVehiculoDTO ubicacion) {
        HorarioDTO horario = new HorarioDTO();
        horario.setId(System.nanoTime());
        horario.setVehiculoId(ubicacion.getVehiculoId());
        horario.setRuta(ubicacion.getRuta());
        horario.setLatitud(ubicacion.getLatitud());
        horario.setLongitud(ubicacion.getLongitud());

        // Simula parada cercana
        String paradaCercana = PARADAS.get(random.nextInt(PARADAS.size()));
        horario.setParadaNombre(paradaCercana);

        // Simula hora de llegada (hora actual + entre 1 y 15 minutos)
        LocalTime ahora = LocalTime.now(ZoneId.of("America/Santiago"));
        int minutosEstimados = random.nextInt(15) + 1;
        LocalTime horaLlegada = ahora.plusMinutes(minutosEstimados);
        LocalTime horaSalida = horaLlegada.plusMinutes(2);

        horario.setHoraLlegada(horaLlegada.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        horario.setHoraEstimadaSalida(horaSalida.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        horario.setTimestamp(Instant.now().toString());

        return horario;
    }
}
