package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Entrega {

    private int id;
    private int idPedido;
    private int idRepartidor;
    private LocalDate fecha;
    private LocalTime hora;

    public Entrega(int idPedido, int idRepartidor,
                   LocalDate fecha, LocalTime hora) {

        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void setIdRepartidor(int idRepartidor) {
        this.idRepartidor = idRepartidor;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        return "Entrega{" +
                "id=" + id +
                ", idPedido=" + idPedido +
                ", idRepartidor=" + idRepartidor +
                ", fecha=" + fecha +
                ", hora=" + hora +
                '}';
    }
}