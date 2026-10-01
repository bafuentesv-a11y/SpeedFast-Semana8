package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Representa la zona de carga compartida por los repartidores.
 */
public class ZonaDeCarga {

    private final List<Pedido> pedidos;

    /**
     * Constructor de ZonaDeCarga.
     */
    public ZonaDeCarga() {
        pedidos = new ArrayList<>();
    }

    /**
     * Agrega un pedido a la zona de carga.
     */
    public synchronized void agregarPedido(Pedido pedido) {

        pedidos.add(pedido);

        System.out.println(
                "[Zona de Carga] Pedido #" + pedido.getId()
                        + " agregado. Estado: "
                        + pedido.getEstado()
        );
    }

    /**
     * Retira de manera sincronizada un pedido pendiente.
     * El método synchronized evita que dos repartidores
     * retiren el mismo pedido al mismo tiempo.
     */
    public synchronized Pedido retirarPedido() {

        Iterator<Pedido> iterator = pedidos.iterator();

        while (iterator.hasNext()) {

            Pedido pedido = iterator.next();

            if (pedido.getEstado() == EstadoPedido.PENDIENTE) {

                iterator.remove();

                System.out.println(
                        "[Zona de Carga] Pedido #"
                                + pedido.getId()
                                + " retirado. Estado: "
                                + pedido.getEstado()
                );

                return pedido;
            }
        }

        return null;
    }

    /**
     * Verifica si existen pedidos pendientes.
     */
    public synchronized boolean hayPedidosPendientes() {

        for (Pedido pedido : pedidos) {

            if (pedido.getEstado() == EstadoPedido.PENDIENTE) {
                return true;
            }
        }

        return false;
    }

    /**
     * Retorna la cantidad de pedidos existentes en la zona.
     */
    public synchronized int cantidadPedidos() {
        return pedidos.size();
    }
}