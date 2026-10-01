package model;

/**
 * Clase abstracta que representa los datos comunes de cualquier pedido.
 */
public abstract class Pedido {

    private int id;
    private String direccionEntrega;
    private double distanciaKm;
    private String tipoEntrega;
    private EstadoPedido estado;

    /**
     * Constructor de Pedido.
     */
    public Pedido(int id, String direccionEntrega,
                  double distanciaKm, String tipoEntrega) {

        this.id = id;
        this.direccionEntrega = direccionEntrega;
        this.distanciaKm = distanciaKm;
        this.tipoEntrega = tipoEntrega;
        this.estado = EstadoPedido.PENDIENTE;
    }

    // ==============================
    // GETTERS
    // ==============================

    public int getId() {
        return id;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public String getTipoEntrega() {
        return tipoEntrega;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    // ==============================
    // SETTERS
    // ==============================

    public void setId(int id) {
        this.id = id;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public void setTipoEntrega(String tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    /**
     * Actualiza el estado utilizando el nombre del estado.
     */
    public void setEstado(String nuevoEstado) {
        this.estado = EstadoPedido.valueOf(nuevoEstado);
    }

    // ==============================
    // MÉTODOS COMUNES
    // ==============================

    /**
     * Muestra la información del pedido.
     */
    public void mostrarResumen() {

        System.out.println("Pedido #" + id);
        System.out.println("Dirección: " + direccionEntrega);
        System.out.printf("Distancia: %.1f km%n", distanciaKm);
        System.out.println("Tipo de entrega: " + tipoEntrega);
        System.out.println("Estado: " + estado);
    }

    // ==============================
    // MÉTODOS ABSTRACTOS
    // ==============================

    /**
     * Cada subclase calcula su propio tiempo de entrega.
     */
    public abstract int calcularTiempoEntrega();

    /**
     * Cada subclase define cómo asignar un repartidor.
     */
    public abstract void asignarRepartidor();

    /**
     * Permite realizar una asignación manual.
     */
    public abstract void asignarRepartidor(String nombreRepartidor);

    /**
     * Cada subclase define cómo se reserva el pedido.
     */
    public abstract void reservar();

    // ==============================
    // TOSTRING
    // ==============================

    @Override
    public String toString() {

        return "Pedido{" +
                "id=" + id +
                ", direccionEntrega='" + direccionEntrega + '\'' +
                ", distanciaKm=" + distanciaKm +
                ", tipoEntrega='" + tipoEntrega + '\'' +
                ", estado=" + estado +
                '}';
    }
}