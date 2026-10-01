package model;

import java.util.ArrayList;
import java.util.List;

public class GestorPedidos {

    private static final List<Pedido> pedidos = new ArrayList<>();

    public static void agregarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    public static List<Pedido> obtenerPedidos() {
        return pedidos;
    }
}