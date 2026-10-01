package model;

/**
 * Representa un pedido de tipo express.
 */
public class PedidoExpress extends Pedido implements Rastreable {

    public PedidoExpress(int idPedido, String direccionEntrega,
                         double distanciaKm, String tipoEntrega) {

        super(idPedido, direccionEntrega, distanciaKm, tipoEntrega);
    }

    @Override
    public int calcularTiempoEntrega() {
        return getDistanciaKm() > 5 ? 15 : 10;
    }

    @Override
    public void asignarRepartidor() {
        System.out.println("[Pedido Express]");
        System.out.println("Asignando repartidor...");
        System.out.println("-> Repartidor más cercano con disponibilidad inmediata encontrado.");
    }

    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("[Pedido Express]");
        System.out.println("-> Pedido asignado manualmente a " + nombreRepartidor);
    }

    @Override
    public void reservar() {
        System.out.println("[Pedido Express]");
        System.out.println("-> Pedido express reservado con prioridad.");
    }

    @Override
    public void verHistorial() {
        System.out.println("[Pedido Express]");
        System.out.println("-> Historial del pedido consultado.");
        System.out.println("-> Pedido creado y asignado para entrega express.");
    }
}