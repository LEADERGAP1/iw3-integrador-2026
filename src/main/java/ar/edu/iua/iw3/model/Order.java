
package ar.edu.iua.iw3.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "ordenes",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_orden_numero",
        columnNames = "numero_orden"
    )
)
@Getter
@Setter
public class Order {

    // 1. IDENTIFICACION

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_orden", nullable = false)
    private Integer numeroOrden;

    // 2. RELACIONES CON ENTIDADES EXTERNAS

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camion_id", nullable = false)
    private Camion camion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chofer_id", nullable = false)
    private Chofer chofer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    // 3. PLANIFICACION Y RECEPCION DESDE SAP

    @Column(name = "fecha_carga_prevista", nullable = false)
    private LocalDateTime fechaCargaPrevista;

    @Column(name = "fecha_recepcion", nullable = false)
    private LocalDateTime fechaRecepcion;

    @Column(name = "preset_kg", nullable = false,
            precision = 15, scale = 3)
    private BigDecimal presetKg;

    // 4. ESTADO DE LA ORDEN

    @Column(nullable = false)
    private int estado = 1;

    // 5. PESAJE INICIAL DESDE TMS

    @Column(name = "peso_inicial_kg",
            precision = 15, scale = 3)
    private BigDecimal pesoInicialKg;

    @Column(name = "fecha_pesaje_inicial")
    private LocalDateTime fechaPesajeInicial;

    @Column(name = "clave_activacion", length = 5)
    private String claveActivacion;

    // 6. PROCESO DE CARGA

    @Column(name = "fecha_inicio_carga")
    private LocalDateTime fechaInicioCarga;

    @Column(name = "fecha_fin_carga")
    private LocalDateTime fechaFinCarga;

    @Column(name = "ultima_masa_kg",
            precision = 15, scale = 3)
    private BigDecimal ultimaMasaKg;

    @Column(name = "ultima_densidad",
            precision = 12, scale = 6)
    private BigDecimal ultimaDensidadValor;

    @Column(name = "ultima_temperatura_c",
            precision = 8, scale = 3)
    private BigDecimal ultimaTemperaturaC;

    @Column(name = "ultimo_caudal_kg_h",
            precision = 15, scale = 3)
    private BigDecimal ultimoCaudalKgHora;

    @Column(name = "fecha_ultima_medicion")
    private LocalDateTime fechaUltimaMedicion;

    // 7. PESAJE FINAL DESDE TMS

    @Column(name = "peso_final_kg",
            precision = 15, scale = 3)
    private BigDecimal pesoFinalKg;

    @Column(name = "fecha_pesaje_final")
    private LocalDateTime fechaPesajeFinal;

    // 8. COMPATIBILIDAD TEMPORAL CON EL CODIGO EXISTENTE

    @Transient
    public String getPassword() {
        return claveActivacion;
    }

    public void setPassword(String password) {
        this.claveActivacion = password;
    }

    @Transient
    public double getPreset() {
        return presetKg != null
                ? presetKg.doubleValue() : 0.0;
    }

    public void setPreset(double preset) {
        this.presetKg = BigDecimal.valueOf(preset);
    }

    @Transient
    public double getTara() {
        return pesoInicialKg != null
                ? pesoInicialKg.doubleValue() : 0.0;
    }

    public void setTara(double tara) {
        this.pesoInicialKg = BigDecimal.valueOf(tara);
    }

    @Transient
    public double getPesajeFinal() {
        return pesoFinalKg != null
                ? pesoFinalKg.doubleValue() : 0.0;
    }

    public void setPesajeFinal(double pesajeFinal) {
        this.pesoFinalKg = BigDecimal.valueOf(pesajeFinal);
    }

    @Transient
    public double getUltimaMasaAcumulada() {
        return ultimaMasaKg != null
                ? ultimaMasaKg.doubleValue() : 0.0;
    }

    public void setUltimaMasaAcumulada(double masa) {
        this.ultimaMasaKg = BigDecimal.valueOf(masa);
    }

    @Transient
    public double getUltimaDensidad() {
        return ultimaDensidadValor != null
                ? ultimaDensidadValor.doubleValue() : 0.0;
    }

    public void setUltimaDensidad(double densidad) {
        this.ultimaDensidadValor = BigDecimal.valueOf(densidad);
    }

    @Transient
    public double getUltimaTemperatura() {
        return ultimaTemperaturaC != null
                ? ultimaTemperaturaC.doubleValue() : 0.0;
    }

    public void setUltimaTemperatura(double temperatura) {
        this.ultimaTemperaturaC = BigDecimal.valueOf(temperatura);
    }
}
