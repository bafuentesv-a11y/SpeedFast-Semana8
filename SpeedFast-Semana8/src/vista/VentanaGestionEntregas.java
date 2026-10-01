package vista;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import model.Entrega;
import model.Pedido;
import model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class VentanaGestionEntregas extends JFrame {

    private JTable tablaEntregas;
    private DefaultTableModel modeloTabla;

    private JComboBox<String> cmbFiltro;
    private JComboBox<Pedido> cmbPedidos;
    private JComboBox<Repartidor> cmbRepartidores;

    private JButton btnRefrescar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnVolver;

    private final EntregaDAO entregaDAO;
    private final PedidoDAO pedidoDAO;
    private final RepartidorDAO repartidorDAO;

    public VentanaGestionEntregas() {

        entregaDAO = new EntregaDAO();
        pedidoDAO = new PedidoDAO();
        repartidorDAO = new RepartidorDAO();

        setTitle("SpeedFast - Gestión de Entregas");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // =====================================================
        // TABLA
        // =====================================================

        modeloTabla = new DefaultTableModel();

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Pedido");
        modeloTabla.addColumn("Repartidor");
        modeloTabla.addColumn("Fecha");
        modeloTabla.addColumn("Hora");

        tablaEntregas =
                new JTable(modeloTabla);

        tablaEntregas.setDefaultEditor(
                Object.class,
                null
        );

        JScrollPane scrollPane =
                new JScrollPane(tablaEntregas);

        // =====================================================
        // FILTROS
        // =====================================================

        JPanel panelFiltro =
                new JPanel(
                        new FlowLayout()
                );

        panelFiltro.add(
                new JLabel("Filtrar por:")
        );

        cmbFiltro =
                new JComboBox<>(
                        new String[]{
                                "Todas",
                                "Pedido",
                                "Repartidor"
                        }
                );

        panelFiltro.add(cmbFiltro);

        // =====================================================
        // BOTONES
        // =====================================================

        btnRefrescar =
                new JButton("Refrescar");

        btnEditar =
                new JButton("Editar");

        btnEliminar =
                new JButton("Eliminar");

        btnVolver =
                new JButton("Volver");

        JPanel panelBotones =
                new JPanel();

        panelBotones.add(btnRefrescar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVolver);

        // =====================================================
        // EVENTOS
        // =====================================================

        btnRefrescar.addActionListener(
                e -> cargarEntregas()
        );

        btnEditar.addActionListener(
                e -> editarEntrega()
        );

        btnEliminar.addActionListener(
                e -> eliminarEntrega()
        );

        btnVolver.addActionListener(
                e -> dispose()
        );

        cmbFiltro.addActionListener(
                e -> aplicarFiltro()
        );

        // =====================================================
        // VENTANA
        // =====================================================

        JPanel panelSuperior =
                new JPanel(
                        new BorderLayout()
                );

        panelSuperior.add(
                panelFiltro,
                BorderLayout.CENTER
        );

        add(
                panelSuperior,
                BorderLayout.NORTH
        );

        add(
                scrollPane,
                BorderLayout.CENTER
        );

        add(
                panelBotones,
                BorderLayout.SOUTH
        );

        cargarEntregas();

        setVisible(true);
    }

    // =========================================================
    // CARGAR ENTREGAS
    // =========================================================

    private void cargarEntregas() {

        modeloTabla.setRowCount(0);

        List<Entrega> entregas =
                entregaDAO.readAll();

        cargarFilas(entregas);
    }

    // =========================================================
    // CARGAR FILAS
    // =========================================================

    private void cargarFilas(
            List<Entrega> entregas) {

        List<Pedido> pedidos =
                pedidoDAO.readAll();

        List<Repartidor> repartidores =
                repartidorDAO.readAll();

        for (Entrega entrega : entregas) {

            String pedidoTexto =
                    "#" + entrega.getIdPedido();

            String repartidorTexto =
                    "#" + entrega.getIdRepartidor();

            for (Pedido pedido : pedidos) {

                if (pedido.getId() ==
                        entrega.getIdPedido()) {

                    pedidoTexto =
                            "#" +
                                    pedido.getId() +
                                    " - " +
                                    pedido.getDireccionEntrega();

                    break;
                }
            }

            for (Repartidor repartidor :
                    repartidores) {

                if (repartidor.getId() ==
                        entrega.getIdRepartidor()) {

                    repartidorTexto =
                            "#" +
                                    repartidor.getId() +
                                    " - " +
                                    repartidor.getNombre();

                    break;
                }
            }

            modeloTabla.addRow(
                    new Object[]{
                            entrega.getId(),
                            pedidoTexto,
                            repartidorTexto,
                            entrega.getFecha(),
                            entrega.getHora()
                    }
            );
        }
    }

    // =========================================================
    // FILTROS
    // =========================================================

    private void aplicarFiltro() {

        String filtro =
                (String) cmbFiltro.getSelectedItem();

        if ("Todas".equals(filtro)) {

            cargarEntregas();
            return;
        }

        if ("Pedido".equals(filtro)) {

            String texto =
                    JOptionPane.showInputDialog(
                            this,
                            "Ingrese el ID del pedido:"
                    );

            if (texto == null) {
                return;
            }

            try {

                int idPedido =
                        Integer.parseInt(
                                texto.trim()
                        );

                List<Entrega> entregas =
                        entregaDAO.readByPedido(
                                idPedido
                        );

                modeloTabla.setRowCount(0);

                cargarFilas(entregas);

            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Debe ingresar un número válido.",
                        "Dato inválido",
                        JOptionPane.ERROR_MESSAGE
                );

                cmbFiltro.setSelectedIndex(0);
            }

        } else if ("Repartidor".equals(filtro)) {

            String texto =
                    JOptionPane.showInputDialog(
                            this,
                            "Ingrese el ID del repartidor:"
                    );

            if (texto == null) {
                return;
            }

            try {

                int idRepartidor =
                        Integer.parseInt(
                                texto.trim()
                        );

                List<Entrega> entregas =
                        entregaDAO.readByRepartidor(
                                idRepartidor
                        );

                modeloTabla.setRowCount(0);

                cargarFilas(entregas);

            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Debe ingresar un número válido.",
                        "Dato inválido",
                        JOptionPane.ERROR_MESSAGE
                );

                cmbFiltro.setSelectedIndex(0);
            }
        }
    }

    // =========================================================
    // EDITAR ENTREGA
    // =========================================================

    private void editarEntrega() {

        int filaSeleccionada =
                tablaEntregas.getSelectedRow();

        if (filaSeleccionada == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar una entrega para editar.",
                    "Entrega no seleccionada",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id =
                (int) modeloTabla.getValueAt(
                        filaSeleccionada,
                        0
                );

        // =====================================================
        // BUSCAR LA ENTREGA
        // =====================================================

        List<Entrega> entregas =
                entregaDAO.readAll();

        Entrega entregaEncontrada = null;

        for (Entrega entrega : entregas) {

            if (entrega.getId() == id) {

                entregaEncontrada = entrega;
                break;
            }
        }

        if (entregaEncontrada == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible encontrar la entrega.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        final Entrega entregaSeleccionada =
                entregaEncontrada;

        // =====================================================
        // COMBOBOX DE PEDIDOS
        // =====================================================

        JComboBox<Pedido> cmbPedidos =
                new JComboBox<>();

        List<Pedido> pedidos =
                pedidoDAO.readAll();

        for (Pedido pedido : pedidos) {

            cmbPedidos.addItem(pedido);
        }

        seleccionarPedido(
                cmbPedidos,
                entregaSeleccionada.getIdPedido()
        );

        // Mostrar información corta y legible
        cmbPedidos.setRenderer(
                new DefaultListCellRenderer() {

                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus) {

                        super.getListCellRendererComponent(
                                list,
                                value,
                                index,
                                isSelected,
                                cellHasFocus
                        );

                        if (value instanceof Pedido) {

                            Pedido pedido =
                                    (Pedido) value;

                            setText(
                                    "#" +
                                            pedido.getId() +
                                            " - " +
                                            pedido.getDireccionEntrega()
                            );
                        }

                        return this;
                    }
                }
        );

        // =====================================================
        // COMBOBOX DE REPARTIDORES
        // =====================================================

        JComboBox<Repartidor> cmbRepartidores =
                new JComboBox<>();

        List<Repartidor> repartidores =
                repartidorDAO.readAll();

        for (Repartidor repartidor :
                repartidores) {

            cmbRepartidores.addItem(
                    repartidor
            );
        }

        seleccionarRepartidor(
                cmbRepartidores,
                entregaSeleccionada.getIdRepartidor()
        );

        // Mostrar información corta y legible
        cmbRepartidores.setRenderer(
                new DefaultListCellRenderer() {

                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus) {

                        super.getListCellRendererComponent(
                                list,
                                value,
                                index,
                                isSelected,
                                cellHasFocus
                        );

                        if (value instanceof Repartidor) {

                            Repartidor repartidor =
                                    (Repartidor) value;

                            setText(
                                    "#" +
                                            repartidor.getId() +
                                            " - " +
                                            repartidor.getNombre()
                            );
                        }

                        return this;
                    }
                }
        );

        // =====================================================
        // CAMPOS DE FECHA Y HORA
        // =====================================================

        JTextField txtFecha =
                new JTextField(
                        entregaSeleccionada
                                .getFecha()
                                .toString()
                );

        JTextField txtHora =
                new JTextField(
                        entregaSeleccionada
                                .getHora()
                                .toString()
                );

        // =====================================================
        // PANEL PRINCIPAL DEL FORMULARIO
        // =====================================================

        JPanel panelFormulario =
                new JPanel(
                        new GridBagLayout()
                );

        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        20,
                        10,
                        20
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        6,
                        6,
                        6,
                        6
                );

        gbc.anchor =
                GridBagConstraints.WEST;

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // =====================================================
        // PEDIDO
        // =====================================================

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel("Pedido:"),
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        panelFormulario.add(
                cmbPedidos,
                gbc
        );

        // =====================================================
        // REPARTIDOR
        // =====================================================

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel("Repartidor:"),
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        panelFormulario.add(
                cmbRepartidores,
                gbc
        );

        // =====================================================
        // FECHA
        // =====================================================

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel("Fecha:"),
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        panelFormulario.add(
                txtFecha,
                gbc
        );

        // =====================================================
        // HORA
        // =====================================================

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;

        panelFormulario.add(
                new JLabel("Hora:"),
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        panelFormulario.add(
                txtHora,
                gbc
        );

        // =====================================================
        // BOTONES
        // =====================================================

        JButton btnCancelar =
                new JButton("Cancelar");

        JButton btnGuardar =
                new JButton("Guardar");

        JPanel panelBotones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                8
                        )
                );

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        // =====================================================
        // CREAR DIÁLOGO
        // =====================================================

        JDialog dialogo =
                new JDialog(
                        this,
                        "Editar entrega #" + id,
                        true
                );

        dialogo.setLayout(
                new BorderLayout()
        );

        dialogo.add(
                panelFormulario,
                BorderLayout.CENTER
        );

        dialogo.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        dialogo.setSize(
                500,
                270
        );

        dialogo.setResizable(false);

        dialogo.setLocationRelativeTo(this);

        // =====================================================
        // BOTÓN CANCELAR
        // =====================================================

        btnCancelar.addActionListener(
                e -> dialogo.dispose()
        );

        // =====================================================
        // BOTÓN GUARDAR
        // =====================================================

        btnGuardar.addActionListener(e -> {

            Pedido pedidoSeleccionado =
                    (Pedido) cmbPedidos
                            .getSelectedItem();

            Repartidor repartidorSeleccionado =
                    (Repartidor) cmbRepartidores
                            .getSelectedItem();

            if (pedidoSeleccionado == null ||
                    repartidorSeleccionado == null) {

                JOptionPane.showMessageDialog(
                        dialogo,
                        "Debe seleccionar un pedido y un repartidor.",
                        "Dato inválido",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            LocalDate fecha;
            LocalTime hora;

            try {

                fecha =
                        LocalDate.parse(
                                txtFecha
                                        .getText()
                                        .trim()
                        );

                hora =
                        LocalTime.parse(
                                txtHora
                                        .getText()
                                        .trim()
                        );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        dialogo,
                        "Revise los formatos ingresados.\n\n" +
                                "Fecha: AAAA-MM-DD\n" +
                                "Hora: HH:MM:SS",
                        "Formato inválido",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            // Actualizar objeto
            entregaSeleccionada.setIdPedido(
                    pedidoSeleccionado.getId()
            );

            entregaSeleccionada.setIdRepartidor(
                    repartidorSeleccionado.getId()
            );

            entregaSeleccionada.setFecha(
                    fecha
            );

            entregaSeleccionada.setHora(
                    hora
            );

            // Actualizar en MySQL
            boolean actualizado =
                    entregaDAO.update(
                            entregaSeleccionada
                    );

            if (actualizado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Entrega actualizada correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dialogo.dispose();

                cargarEntregas();

            } else {

                JOptionPane.showMessageDialog(
                        dialogo,
                        "No fue posible actualizar la entrega.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Mostrar ventana
        dialogo.setVisible(true);
    }
    // =========================================================
    // SELECCIONAR PEDIDO
    // =========================================================

    private void seleccionarPedido(
            JComboBox<Pedido> combo,
            int idPedido) {

        for (int i = 0;
             i < combo.getItemCount();
             i++) {

            Pedido pedido =
                    combo.getItemAt(i);

            if (pedido.getId() ==
                    idPedido) {

                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    // =========================================================
    // SELECCIONAR REPARTIDOR
    // =========================================================

    private void seleccionarRepartidor(
            JComboBox<Repartidor> combo,
            int idRepartidor) {

        for (int i = 0;
             i < combo.getItemCount();
             i++) {

            Repartidor repartidor =
                    combo.getItemAt(i);

            if (repartidor.getId() ==
                    idRepartidor) {

                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    // =========================================================
    // ELIMINAR ENTREGA
    // =========================================================

    private void eliminarEntrega() {

        int filaSeleccionada =
                tablaEntregas.getSelectedRow();

        if (filaSeleccionada == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar una entrega para eliminar.",
                    "Entrega no seleccionada",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id =
                (int) modeloTabla.getValueAt(
                        filaSeleccionada,
                        0
                );

        int confirmacion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar la entrega #" +
                                id +
                                "?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (confirmacion !=
                JOptionPane.YES_OPTION) {

            return;
        }

        boolean eliminado =
                entregaDAO.delete(id);

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega eliminada correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarEntregas();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible eliminar la entrega.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}