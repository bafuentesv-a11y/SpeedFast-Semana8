package model;

public class PedidoEncomienda extends Pedido implements Cancelable {

    public PedidoEncomienda(int idPedido, String direccionEntrega,
                            double distanciaKm, String tipoEntrega) {
        super(idPedido, direccionEntrega, distanciaKm, tipoEntrega);
    }

    @Override
    public int calcularTiempoEntrega() {
        return (int) Math.round(20 + (1.5 * getDistanciaKm()));
    }

    @Override
    public void mostrarResumen() {
        super.mostrarResumen();
        System.out.println("Factor de duración: 20 min base + 1,5 min por km.");
    }

    @Override
    public void asignarRepartidor() {
        System.out.println("[Pedido Encomienda]");
        System.out.println("Asignando repartidor...");
        System.out.println("-> Repartidor disponible para encomiendas asignado.");
    }

    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("[Pedido Encomienda]");
        System.out.println("-> Pedido asignado manualmente a " + nombreRepartidor);
    }

    @Override
    public void reservar() {
        System.out.println("[Pedido Encomienda]");
        System.out.println("-> Pedido de encomienda reservado correctamente.");
    }

    @Override
    public void cancelar() {
        System.out.println("[Pedido Encomienda]");
        System.out.println("-> Pedido de encomienda cancelado.");
    }
}