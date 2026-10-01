package vista;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import model.Entrega;
import model.EstadoPedido;
import model.Pedido;
import model.Repartidor;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class VentanaAsignarRepartidor extends JFrame {

    private JComboBox<Pedido> cmbPedidos;
    private JComboBox<Repartidor> cmbRepartidores;
    private JButton btnIniciar;
    private JButton btnVolver;

    private final PedidoDAO pedidoDAO;
    private final RepartidorDAO repartidorDAO;
    private final EntregaDAO entregaDAO;

    public VentanaAsignarRepartidor() {

        pedidoDAO = new PedidoDAO();
        repartidorDAO = new RepartidorDAO();
        entregaDAO = new EntregaDAO();

        setTitle("SpeedFast - Asignar Repartidor");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        JLabel lblPedido =
                new JLabel("Pedido pendiente:");

        JLabel lblRepartidor =
                new JLabel("Repartidor:");

        cmbPedidos =
                new JComboBox<>();

        cmbRepartidores =
                new JComboBox<>();

        btnIniciar =
                new JButton("Asignar / Iniciar Entrega");

        btnVolver =
                new JButton("Volver");

        panel.add(lblPedido);
        panel.add(cmbPedidos);

        panel.add(lblRepartidor);
        panel.add(cmbRepartidores);

        panel.add(new JLabel());
        panel.add(btnIniciar);

        panel.add(new JLabel());
        panel.add(btnVolver);

        add(panel);

        // Cargar información desde la base de datos
        cargarPedidosPendientes();
        cargarRepartidoresDisponibles();

        // Eventos
        btnIniciar.addActionListener(
                e -> iniciarEntrega()
        );

        btnVolver.addActionListener(
                e -> dispose()
        );

        setVisible(true);
    }

    // =========================================================
    // CARGAR PEDIDOS PENDIENTES
    // =========================================================

    private void cargarPedidosPendientes() {

        cmbPedidos.removeAllItems();

        List<Pedido> pedidos =
                pedidoDAO.readAll();

        for (Pedido pedido : pedidos) {

            // Solo se pueden asignar pedidos pendientes
            if (pedido.getEstado()
                    == EstadoPedido.PENDIENTE) {

                cmbPedidos.addItem(pedido);
            }
        }
    }

    // =========================================================
    // CARGAR REPARTIDORES
    // =========================================================

    private void cargarRepartidoresDisponibles() {

        cmbRepartidores.removeAllItems();

        List<Repartidor> repartidores =
                repartidorDAO.readAll();

        for (Repartidor repartidor : repartidores) {

            if (repartidor.isDisponible()) {

                cmbRepartidores.addItem(
                        repartidor
                );
            }
        }
    }

    // =========================================================
    // INICIAR ENTREGA
    // =========================================================

    private void iniciarEntrega() {

        Pedido pedidoSeleccionado =
                (Pedido) cmbPedidos.getSelectedItem();

        Repartidor repartidorSeleccionado =
                (Repartidor) cmbRepartidores.getSelectedItem();

        // Validar pedido
        if (pedidoSeleccionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un pedido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Validar repartidor
        if (repartidorSeleccionado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un repartidor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Verificar nuevamente que el pedido esté pendiente
        if (pedidoSeleccionado.getEstado()
                != EstadoPedido.PENDIENTE) {

            JOptionPane.showMessageDialog(
                    this,
                    "El pedido ya no está disponible para asignación.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarPedidosPendientes();
            return;
        }

        /*
         * Cambiamos el estado utilizando PedidoDAO.
         * Ya no ejecutamos SQL directamente desde la vista.
         */
        pedidoSeleccionado.setEstado(
                EstadoPedido.EN_REPARTO
        );

        boolean actualizado =
                pedidoDAO.update(
                        pedidoSeleccionado
                );

        if (!actualizado) {

            // Si la actualización falla, dejamos
            // el objeto nuevamente en estado pendiente.
            pedidoSeleccionado.setEstado(
                    EstadoPedido.PENDIENTE
            );

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el estado del pedido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Marcar repartidor como ocupado
        repartidorSeleccionado.setDisponible(false);

        JOptionPane.showMessageDialog(
                this,
                "Entrega iniciada.\n\n" +
                        "Pedido: #" +
                        pedidoSeleccionado.getId() +
                        "\nRepartidor: " +
                        repartidorSeleccionado.getNombre(),
                "Entrega iniciada",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Deshabilitar botón mientras se procesa
        btnIniciar.setEnabled(false);

        /*
         * Ejecutamos la entrega en un hilo separado
         * para no congelar la interfaz gráfica.
         */
        Thread hiloEntrega = new Thread(() -> {

            try {

                System.out.println(
                        "Repartidor " +
                                repartidorSeleccionado.getNombre() +
                                " está realizando la entrega del pedido #" +
                                pedidoSeleccionado.getId()
                );

                // Simulación de tiempo de entrega
                Thread.sleep(2000);

                // Crear objeto Entrega
                Entrega entrega = new Entrega(
                        pedidoSeleccionado.getId(),
                        repartidorSeleccionado.getId(),
                        LocalDate.now(),
                        LocalTime.now()
                );

                /*
                 * Guardar la entrega mediante EntregaDAO.
                 *
                 * EntregaDAO.create() también cambia
                 * el pedido a ENTREGADO dentro de
                 * la misma transacción.
                 */
                boolean guardada =
                        entregaDAO.create(entrega);

                if (!guardada) {

                    // Si algo falla, volvemos el pedido
                    // a estado PENDIENTE.
                    pedidoSeleccionado.setEstado(
                            EstadoPedido.PENDIENTE
                    );

                    pedidoDAO.update(
                            pedidoSeleccionado
                    );

                    repartidorSeleccionado.setDisponible(true);

                    SwingUtilities.invokeLater(() -> {

                        btnIniciar.setEnabled(true);

                        JOptionPane.showMessageDialog(
                                this,
                                "No se pudo registrar la entrega.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );

                        cargarPedidosPendientes();
                        cargarRepartidoresDisponibles();
                    });

                    return;
                }

                // La entrega fue guardada correctamente
                repartidorSeleccionado.setDisponible(true);

                SwingUtilities.invokeLater(() -> {

                    btnIniciar.setEnabled(true);

                    JOptionPane.showMessageDialog(
                            this,
                            "La entrega fue completada correctamente.\n\n" +
                                    "Pedido #" +
                                    pedidoSeleccionado.getId() +
                                    " → ENTREGADO",
                            "Entrega completada",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    // Recargar datos desde MySQL
                    cargarPedidosPendientes();
                    cargarRepartidoresDisponibles();
                });

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                /*
                 * Si el hilo se interrumpe, devolvemos
                 * el pedido a PENDIENTE.
                 */
                pedidoSeleccionado.setEstado(
                        EstadoPedido.PENDIENTE
                );

                pedidoDAO.update(
                        pedidoSeleccionado
                );

                repartidorSeleccionado.setDisponible(true);

                SwingUtilities.invokeLater(() -> {

                    btnIniciar.setEnabled(true);

                    JOptionPane.showMessageDialog(
                            this,
                            "La entrega fue interrumpida.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );

                    cargarPedidosPendientes();
                    cargarRepartidoresDisponibles();
                });
            }
        });

        hiloEntrega.start();
    }
}