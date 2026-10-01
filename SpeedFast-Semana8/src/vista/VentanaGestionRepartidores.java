package vista;

import dao.RepartidorDAO;
import model.Repartidor;
import model.ZonaDeCarga;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaGestionRepartidores extends JFrame {

    private JTable tablaRepartidores;
    private DefaultTableModel modeloTabla;

    private JTextField txtNombre;

    private JButton btnRegistrar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnRefrescar;
    private JButton btnVolver;

    private RepartidorDAO repartidorDAO;

    public VentanaGestionRepartidores() {

        setTitle("SpeedFast - Gestión de Repartidores");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        repartidorDAO =
                new RepartidorDAO();

        // -----------------------------
        // TABLA
        // -----------------------------

        modeloTabla =
                new DefaultTableModel();

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");

        tablaRepartidores =
                new JTable(modeloTabla);

        tablaRepartidores.setDefaultEditor(
                Object.class,
                null
        );

        JScrollPane scrollPane =
                new JScrollPane(tablaRepartidores);

        // -----------------------------
        // PANEL DE REGISTRO
        // -----------------------------

        JPanel panelRegistro =
                new JPanel(
                        new FlowLayout()
                );

        panelRegistro.add(
                new JLabel("Nombre:")
        );

        txtNombre =
                new JTextField(20);

        panelRegistro.add(txtNombre);

        btnRegistrar =
                new JButton("Registrar");

        panelRegistro.add(btnRegistrar);

        // -----------------------------
        // BOTONES
        // -----------------------------

        btnEditar =
                new JButton("Editar");

        btnEliminar =
                new JButton("Eliminar");

        btnRefrescar =
                new JButton("Refrescar");

        btnVolver =
                new JButton("Volver");

        JPanel panelBotones =
                new JPanel();

        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnVolver);

        // -----------------------------
        // EVENTOS
        // -----------------------------

        btnRegistrar.addActionListener(
                e -> registrarRepartidor()
        );

        btnEditar.addActionListener(
                e -> editarRepartidor()
        );

        btnEliminar.addActionListener(
                e -> eliminarRepartidor()
        );

        btnRefrescar.addActionListener(
                e -> refrescarTabla()
        );

        btnVolver.addActionListener(
                e -> dispose()
        );

        // -----------------------------
        // VENTANA
        // -----------------------------

        add(
                panelRegistro,
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

        cargarRepartidores();

        setVisible(true);
    }

    /**
     * Carga los repartidores desde la base de datos.
     */
    private void cargarRepartidores() {

        modeloTabla.setRowCount(0);

        List<Repartidor> repartidores =
                repartidorDAO.readAll();

        for (Repartidor repartidor :
                repartidores) {

            modeloTabla.addRow(
                    new Object[]{
                            repartidor.getId(),
                            repartidor.getNombre()
                    }
            );
        }
    }

    /**
     * Registra un nuevo repartidor.
     */
    private void registrarRepartidor() {

        String nombre =
                txtNombre.getText().trim();

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar el nombre del repartidor.",
                    "Dato inválido",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        Repartidor repartidor =
                new Repartidor(
                        nombre,
                        true,
                        new ZonaDeCarga()
                );

        boolean creado =
                repartidorDAO.create(
                        repartidor
                );

        if (creado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor registrado correctamente.\n" +
                            "ID asignado: " +
                            repartidor.getId(),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            txtNombre.setText("");

            cargarRepartidores();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible registrar el repartidor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Edita el repartidor seleccionado.
     */
    private void editarRepartidor() {

        int filaSeleccionada =
                tablaRepartidores.getSelectedRow();

        if (filaSeleccionada == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un repartidor para editar.",
                    "Repartidor no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id =
                (int) modeloTabla.getValueAt(
                        filaSeleccionada,
                        0
                );

        String nombreActual =
                modeloTabla.getValueAt(
                        filaSeleccionada,
                        1
                ).toString();

        String nuevoNombre =
                JOptionPane.showInputDialog(
                        this,
                        "Ingrese el nuevo nombre:",
                        nombreActual
                );

        if (nuevoNombre == null) {
            return;
        }

        nuevoNombre =
                nuevoNombre.trim();

        if (nuevoNombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "El nombre no puede estar vacío.",
                    "Dato inválido",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        Repartidor repartidor =
                new Repartidor(
                        id,
                        nuevoNombre,
                        true,
                        new ZonaDeCarga()
                );

        boolean actualizado =
                repartidorDAO.update(
                        repartidor
                );

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor actualizado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarRepartidores();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible actualizar el repartidor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Elimina el repartidor seleccionado.
     */
    private void eliminarRepartidor() {

        int filaSeleccionada =
                tablaRepartidores.getSelectedRow();

        if (filaSeleccionada == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un repartidor para eliminar.",
                    "Repartidor no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id =
                (int) modeloTabla.getValueAt(
                        filaSeleccionada,
                        0
                );

        String nombre =
                modeloTabla.getValueAt(
                        filaSeleccionada,
                        1
                ).toString();

        int confirmacion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar al repartidor " +
                                nombre +
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
                repartidorDAO.delete(id);

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor eliminado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarRepartidores();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible eliminar el repartidor.\n\n" +
                            "Si tiene entregas asociadas, " +
                            "la base de datos puede impedir su eliminación.",
                    "No se pudo eliminar",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Actualiza la tabla.
     */
    private void refrescarTabla() {

        cargarRepartidores();
    }
}