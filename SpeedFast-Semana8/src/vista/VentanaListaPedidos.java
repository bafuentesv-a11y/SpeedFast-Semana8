package vista;

import dao.PedidoDAO;
import model.EstadoPedido;
import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaListaPedidos extends JFrame {

    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;

    private JButton btnRefrescar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnVolver;

    public VentanaListaPedidos() {

        setTitle("SpeedFast - Lista de Pedidos");
        setSize(950, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        modeloTabla = new DefaultTableModel();

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Dirección");
        modeloTabla.addColumn("Tipo");
        modeloTabla.addColumn("Estado");
        modeloTabla.addColumn("Repartidor");

        tablaPedidos = new JTable(modeloTabla);

        // No permitimos editar directamente las celdas
        tablaPedidos.setDefaultEditor(
                Object.class,
                null
        );

        JScrollPane scrollPane =
                new JScrollPane(tablaPedidos);

        btnRefrescar =
                new JButton("Refrescar");

        btnEditar =
                new JButton("Editar");

        btnEliminar =
                new JButton("Eliminar");

        btnVolver =
                new JButton("Volver");

        btnRefrescar.addActionListener(
                e -> refrescarTabla()
        );

        btnEditar.addActionListener(
                e -> editarPedido()
        );

        btnEliminar.addActionListener(
                e -> eliminarPedido()
        );

        btnVolver.addActionListener(
                e -> dispose()
        );

        JPanel panelInferior =
                new JPanel();

        panelInferior.add(btnRefrescar);
        panelInferior.add(btnEditar);
        panelInferior.add(btnEliminar);
        panelInferior.add(btnVolver);

        add(
                scrollPane,
                BorderLayout.CENTER
        );

        add(
                panelInferior,
                BorderLayout.SOUTH
        );

        cargarPedidos();

        setVisible(true);
    }

    private void cargarPedidos() {

        PedidoDAO pedidoDAO =
                new PedidoDAO();

        List<Object[]> resultados =
                pedidoDAO.listarTodosConRepartidor();

        for (Object[] resultado : resultados) {

            Pedido pedido =
                    (Pedido) resultado[0];

            String repartidor =
                    (String) resultado[1];

            if (repartidor == null ||
                    repartidor.trim().isEmpty()) {

                repartidor = "—";
            }

            modeloTabla.addRow(
                    new Object[]{
                            pedido.getId(),
                            pedido.getDireccionEntrega(),
                            pedido.getTipoEntrega(),
                            pedido.getEstado(),
                            repartidor
                    }
            );
        }
    }

    private void refrescarTabla() {

        modeloTabla.setRowCount(0);

        cargarPedidos();
    }

    /**
     * Edita el pedido seleccionado.
     */
    private void editarPedido() {

        int filaSeleccionada =
                tablaPedidos.getSelectedRow();

        if (filaSeleccionada == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un pedido para editar.",
                    "Pedido no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id =
                (int) modeloTabla.getValueAt(
                        filaSeleccionada,
                        0
                );

        String direccionActual =
                modeloTabla.getValueAt(
                        filaSeleccionada,
                        1
                ).toString();

        String tipoActual =
                modeloTabla.getValueAt(
                        filaSeleccionada,
                        2
                ).toString();

        String estadoActual =
                modeloTabla.getValueAt(
                        filaSeleccionada,
                        3
                ).toString();

        JTextField txtDireccion =
                new JTextField(
                        direccionActual
                );

        JComboBox<String> cmbTipo =
                new JComboBox<>(
                        new String[]{
                                "Comida",
                                "Encomienda",
                                "Express"
                        }
                );

        cmbTipo.setSelectedItem(
                tipoActual
        );

        JComboBox<EstadoPedido> cmbEstado =
                new JComboBox<>(
                        EstadoPedido.values()
                );

        try {

            cmbEstado.setSelectedItem(
                    EstadoPedido.valueOf(
                            estadoActual
                    )
            );

        } catch (IllegalArgumentException e) {

            cmbEstado.setSelectedIndex(0);
        }

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                0,
                                2,
                                10,
                                10
                        )
                );

        panel.add(
                new JLabel("Dirección:")
        );

        panel.add(txtDireccion);

        panel.add(
                new JLabel("Tipo:")
        );

        panel.add(cmbTipo);

        panel.add(
                new JLabel("Estado:")
        );

        panel.add(cmbEstado);

        int resultado =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Editar pedido #" + id,
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (resultado !=
                JOptionPane.OK_OPTION) {

            return;
        }

        String nuevaDireccion =
                txtDireccion
                        .getText()
                        .trim();

        String nuevoTipo =
                (String) cmbTipo
                        .getSelectedItem();

        EstadoPedido nuevoEstado =
                (EstadoPedido) cmbEstado
                        .getSelectedItem();

        // Validación
        if (nuevaDireccion.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "La dirección no puede estar vacía.",
                    "Dato inválido",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (nuevoTipo == null ||
                nuevoEstado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar tipo y estado.",
                    "Dato inválido",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        Pedido pedidoActualizado;

        double distanciaKm = 0;

        switch (nuevoTipo) {

            case "Comida":

                pedidoActualizado =
                        new PedidoComida(
                                id,
                                nuevaDireccion,
                                distanciaKm,
                                nuevoTipo
                        );

                break;

            case "Encomienda":

                pedidoActualizado =
                        new PedidoEncomienda(
                                id,
                                nuevaDireccion,
                                distanciaKm,
                                nuevoTipo
                        );

                break;

            case "Express":

                pedidoActualizado =
                        new PedidoExpress(
                                id,
                                nuevaDireccion,
                                distanciaKm,
                                nuevoTipo
                        );

                break;

            default:

                JOptionPane.showMessageDialog(
                        this,
                        "Tipo de pedido no válido.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
        }

        pedidoActualizado.setEstado(
                nuevoEstado
        );

        PedidoDAO pedidoDAO =
                new PedidoDAO();

        boolean actualizado =
                pedidoDAO.update(
                        pedidoActualizado
                );

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido actualizado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            refrescarTabla();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible actualizar el pedido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Elimina el pedido seleccionado.
     */
    private void eliminarPedido() {

        int filaSeleccionada =
                tablaPedidos.getSelectedRow();

        if (filaSeleccionada == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un pedido para eliminar.",
                    "Pedido no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id =
                (int) modeloTabla.getValueAt(
                        filaSeleccionada,
                        0
                );

        String direccion =
                modeloTabla.getValueAt(
                        filaSeleccionada,
                        1
                ).toString();

        int confirmacion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar el pedido #" +
                                id +
                                "?\n\nDirección: " +
                                direccion,
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (confirmacion !=
                JOptionPane.YES_OPTION) {

            return;
        }

        PedidoDAO pedidoDAO =
                new PedidoDAO();

        boolean eliminado =
                pedidoDAO.delete(id);

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido eliminado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            refrescarTabla();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible eliminar el pedido.\n\n" +
                            "Si el pedido tiene una entrega asociada, " +
                            "la base de datos puede impedir su eliminación.",
                    "No se pudo eliminar",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}