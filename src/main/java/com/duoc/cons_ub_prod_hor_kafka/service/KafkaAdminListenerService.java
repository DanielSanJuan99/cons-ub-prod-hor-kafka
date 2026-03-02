package com.duoc.cons_ub_prod_hor_kafka.service;

public interface KafkaAdminListenerService {

    void pausarListener(String id);

    void reanudarListener(String id);

    boolean obtenerEstadoListener(String id);
}
