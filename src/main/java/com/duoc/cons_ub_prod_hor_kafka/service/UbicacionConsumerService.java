package com.duoc.cons_ub_prod_hor_kafka.service;

import org.springframework.kafka.support.Acknowledgment;

import com.duoc.cons_ub_prod_hor_kafka.dto.UbicacionVehiculoDTO;

public interface UbicacionConsumerService {

    void consumirUbicacion(UbicacionVehiculoDTO ubicacion, Acknowledgment ack);
}
