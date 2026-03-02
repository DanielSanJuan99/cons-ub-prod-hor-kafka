package com.duoc.cons_ub_prod_hor_kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UbicacionVehiculoDTO {

    private Long id;
    private String vehiculoId;
    private Double latitud;
    private Double longitud;
    private String ruta;
    private String timestamp;
}
