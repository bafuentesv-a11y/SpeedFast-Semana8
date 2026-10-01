package model;

public class PedidoComida extends Pedido implements Despachable {

    public PedidoComida(int idPedido, String direccionEntrega,
                        double distanciaKm, String tipoEntrega) {
        super(idPedido, direccionEntrega, distanciaKm, tipoEntrega);
    }

    @Override
    public int calcularTiempoEntrega() {
        return (int) Math.round(15 + (2 * getDistanciaKm()));
    }

    @Override
    public void mostrarResumen() {
        super.mostrarResumen();
        System.out.println("Factor de duración: 15 min base + 2 min por km.");
    }

    @Override
    public void asignarRepartidor() {
        System.out.println("[Pedido Comida]");
        System.out.println("Asignando repartidor...");
        System.out.println("-> Repartidor especializado en entregas de comida asignado.");
    }

    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("[Pedido Comida]");
        System.out.println("-> Pedido asignado manualmente a " + nombreRepartidor);
    }

    @Override
    public void reservar() {
        System.out.println("[Pedido Comida]");
        System.out.println("-> Pedido de comida reservado correctamente.");
    }

    @Override
    public void despachar() {
        System.out.println("[Pedido Comida]");
        System.out.println("-> Pedido de comida despachado correctamente.");
    }
}