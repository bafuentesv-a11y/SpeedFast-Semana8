package model;

import java.util.Random;

/**
 * Representa un repartidor que realiza entregas de manera concurrente.
 */
public class Repartidor implements Runnable {

    private int id;
    private String nombre;
    private boolean disponible;
    private ZonaDeCarga zonaDeCarga;

    /**
     * Constructor de Repartidor para crear un nuevo repartidor.
     */
    public Repartidor(String nombre, boolean disponible, ZonaDeCarga zonaDeCarga) {
        this.nombre = nombre;
        this.disponible = disponible;
        this.zonaDeCarga = zonaDeCarga;
    }

    /**
     * Constructor utilizado cuando el repartidor viene desde la base de datos.
     */
    public Repartidor(int id, String nombre, boolean disponible, ZonaDeCarga zonaDeCarga) {
        this.id = id;
        this.nombre = nombre;
        this.disponible = disponible;
        this.zonaDeCarga = zonaDeCarga;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public ZonaDeCarga getZonaDeCarga() {
        return zonaDeCarga;
    }

    /**
     * Ejecuta las entregas de los pedidos retirados
     * desde la zona de carga compartida.
     */
    @Override
    public void run() {

        Random random = new Random();

        while (true) {

            Pedido pedido = zonaDeCarga.retirarPedido();

            // Si no quedan pedidos pendientes, el repartidor termina.
            if (pedido == null) {
                break;
            }

            // Cambia el estado del pedido a EN_REPARTO.
            pedido.setEstado(EstadoPedido.EN_REPARTO);

            System.out.println(
                    "[Repartidor: " + nombre + "] Retirando "
                            + pedido.getClass().getSimpleName()
                            + " #" + pedido.getId()
                            + " - Estado: " + pedido.getEstado()
            );

            try {

                // Simula un tiempo de entrega aleatorio.
                int tiempo = 1000 + random.nextInt(3000);

                Thread.sleep(tiempo);

                // La entrega finaliza correctamente.
                pedido.setEstado(EstadoPedido.ENTREGADO);

                System.out.println(
                        "[Repartidor: " + nombre + "] Pedido #"
                                + pedido.getId()
                                + " entregado."
                                + " Estado: " + pedido.getEstado()
                );

            } catch (InterruptedException e) {

                System.out.println(
                        "[Repartidor: " + nombre
                                + "] La entrega fue interrumpida."
                );

                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println(
                "[Repartidor: " + nombre
                        + "] Ha terminado sus entregas."
        );
    }

    /**
     * Representación textual del repartidor.
     */
    @Override
    public String toString() {
        return nombre
                + (disponible ? " (Disponible)" : " (No disponible)");
    }
}