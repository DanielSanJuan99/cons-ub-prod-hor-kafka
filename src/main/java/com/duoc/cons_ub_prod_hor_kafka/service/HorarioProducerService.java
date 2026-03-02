package com.duoc.cons_ub_prod_hor_kafka.service;

import com.duoc.cons_ub_prod_hor_kafka.dto.HorarioDTO;

public interface HorarioProducerService {

    void enviarHorario(HorarioDTO horario);
}
