package com.duoc.cons_ub_prod_hor_kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.cons_ub_prod_hor_kafka.dto.HorarioDTO;
import com.duoc.cons_ub_prod_hor_kafka.service.HorarioProducerService;
import com.duoc.cons_ub_prod_hor_kafka.service.KafkaAdminListenerService;

@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

    @Autowired
    private HorarioProducerService horarioProducerService;

    @Autowired
    private KafkaAdminListenerService kafkaAdminListenerService;

    /**
     * POST - Enviar un horario manualmente a Kafka
     */
    @PostMapping
    public ResponseEntity<String> enviarHorario(@RequestBody HorarioDTO horario) {
        horarioProducerService.enviarHorario(horario);
        return ResponseEntity.ok("Horario enviado a Kafka: " + horario.toString());
    }

    /**
     * POST - Pausar el listener de ubicaciones
     */
    @PostMapping("/listener/{id}/pausar")
    public ResponseEntity<String> pausarListener(@PathVariable String id) {
        kafkaAdminListenerService.pausarListener(id);
        return ResponseEntity.ok("Listener " + id + " pausado");
    }

    /**
     * POST - Reanudar el listener de ubicaciones
     */
    @PostMapping("/listener/{id}/reanudar")
    public ResponseEntity<String> reanudarListener(@PathVariable String id) {
        kafkaAdminListenerService.reanudarListener(id);
        return ResponseEntity.ok("Listener " + id + " reanudado");
    }

    /**
     * GET - Obtener estado del listener
     */
    @GetMapping("/listener/{id}/estado")
    public ResponseEntity<String> obtenerEstadoListener(@PathVariable String id) {
        boolean pausado = kafkaAdminListenerService.obtenerEstadoListener(id);
        return ResponseEntity.ok("Listener " + id + " esta " + (pausado ? "pausado" : "activo"));
    }

    /**
     * GET - Health check del servicio
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Servicio cons-ub-prod-hor-kafka activo");
    }
}
