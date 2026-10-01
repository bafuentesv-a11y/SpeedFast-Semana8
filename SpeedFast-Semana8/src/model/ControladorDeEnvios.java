package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Controla las operaciones generales de los envíos.
 */
public class ControladorDeEnvios
        implements Despachable, Cancelable, Rastreable {

    private final List<Pedido> pedidos;
    private final List<String> historial;

    /**
     * Constructor del controlador.
     */
    public ControladorDeEnvios() {
        pedidos = new ArrayList<>();
        historial = new ArrayList<>();
    }

    /**
     * Agrega un pedido al sistema.
     */
    public void agregarPedido(Pedido pedido) {

        pedidos.add(pedido);

        System.out.println(
                "Pedido " + pedido.getId()
                        + " agregado al sistema."
        );
    }

    /**
     * Muestra todos los pedidos registrados.
     */
    public void mostrarPedidos() {

        System.out.println("\n===== PEDIDOS REGISTRADOS =====");

        for (Pedido pedido : pedidos) {

            pedido.mostrarResumen();

            System.out.println(
                    "Tiempo estimado: "
                            + pedido.calcularTiempoEntrega()
                            + " minutos"
            );

            System.out.println("------------------------------");
        }
    }

    /**
     * Reserva todos los pedidos registrados.
     */
    public void reservarPedidos() {

        System.out.println("\n===== RESERVA DE PEDIDOS =====");

        for (Pedido pedido : pedidos) {

            pedido.reservar();

            historial.add(
                    "Pedido "
                            + pedido.getId()
                            + " - Reservado"
            );
        }
    }

    /**
     * Registra el despacho de los pedidos.
     */
    @Override
    public void despachar() {

        System.out.println("\n===== DESPACHO =====");

        for (Pedido pedido : pedidos) {

            System.out.println(
                    "Pedido "
                            + pedido.getId()
                            + " despachado correctamente."
            );

            historial.add(
                    "Pedido "
                            + pedido.getId()
                            + " - Despachado"
            );
        }
    }

    /**
     * Cancela el último pedido registrado.
     */
    @Override
    public void cancelar() {

        System.out.println("\n===== CANCELACIÓN =====");

        if (pedidos.isEmpty()) {
            System.out.println("No existen pedidos para cancelar.");
            return;
        }

        Pedido pedido = pedidos.get(pedidos.size() - 1);

        System.out.println(
                "Pedido "
                        + pedido.getId()
                        + " cancelado correctamente."
        );

        historial.add(
                "Pedido "
                        + pedido.getId()
                        + " - Cancelado"
        );
    }

    /**
     * Cancela un pedido utilizando su identificador.
     */
    public void cancelar(int idPedido) {

        for (Pedido pedido : pedidos) {

            if (pedido.getId() == idPedido) {

                System.out.println(
                        "Pedido "
                                + pedido.getId()
                                + " cancelado correctamente."
                );

                historial.add(
                        "Pedido "
                                + pedido.getId()
                                + " - Cancelado"
                );

                return;
            }
        }

        System.out.println(
                "No se encontró el pedido " + idPedido
        );
    }

    /**
     * Muestra el historial de operaciones.
     */
    @Override
    public void verHistorial() {

        System.out.println("\n===== HISTORIAL =====");

        if (historial.isEmpty()) {
            System.out.println(
                    "No existen operaciones registradas."
            );
            return;
        }

        for (String registro : historial) {
            System.out.println("- " + registro);
        }
    }
}