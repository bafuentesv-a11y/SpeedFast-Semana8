package vista;

import dao.PedidoDAO;
import model.EstadoPedido;
import model.GestorPedidos;
import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;

import javax.swing.*;
import java.awt.*;

public class VentanaRegistroPedido extends JFrame {

    private JTextField txtDireccion;
    private JComboBox<String> cmbTipo;
    private JButton btnGuardar;
    private JButton btnVolver;

    public VentanaRegistroPedido() {

        setTitle("SpeedFast - Registrar Pedido");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel lblDireccion = new JLabel("Dirección:");
        JLabel lblTipo = new JLabel("Tipo:");

        txtDireccion = new JTextField();

        cmbTipo = new JComboBox<>(
                new String[]{"Comida", "Encomienda", "Express"}
        );

        btnGuardar = new JButton("Guardar");
        btnVolver = new JButton("Volver");

        panel.add(lblDireccion);
        panel.add(txtDireccion);

        panel.add(lblTipo);
        panel.add(cmbTipo);

        panel.add(new JLabel());
        panel.add(btnGuardar);

        panel.add(new JLabel());
        panel.add(btnVolver);

        add(panel);

        // Evento Guardar
        btnGuardar.addActionListener(e -> guardarPedido());

        // Evento Volver
        btnVolver.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void guardarPedido() {

        String direccion = txtDireccion.getText().trim();
        String tipo = (String) cmbTipo.getSelectedItem();

        // Validar dirección
        if (direccion.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar una dirección.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Distancia solicitada por el modelo de Semana 5
        double distanciaKm = 0;

        Pedido pedido;

        // Crear el pedido según el tipo seleccionado
        switch (tipo) {

            case "Comida":

                pedido = new PedidoComida(
                        0,
                        direccion,
                        distanciaKm,
                        tipo
                );

                break;

            case "Encomienda":

                pedido = new PedidoEncomienda(
                        0,
                        direccion,
                        distanciaKm,
                        tipo
                );

                break;

            case "Express":

                pedido = new PedidoExpress(
                        0,
                        direccion,
                        distanciaKm,
                        tipo
                );

                break;

            default:

                JOptionPane.showMessageDialog(
                        this,
                        "Debe seleccionar un tipo de pedido.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
        }

        // Todo pedido nuevo comienza como PENDIENTE
        pedido.setEstado(EstadoPedido.PENDIENTE);

        // Crear en la base de datos
        PedidoDAO pedidoDAO = new PedidoDAO();

        int idGenerado = pedidoDAO.crear(pedido);

        // Verificar si la creación fue exitosa
        if (idGenerado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible guardar el pedido en la base de datos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Mantener también el pedido en la lista de la aplicación
        GestorPedidos.agregarPedido(pedido);

        JOptionPane.showMessageDialog(
                this,
                "Pedido creado correctamente.\nID asignado: " + idGenerado,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Limpiar formulario
        txtDireccion.setText("");
        cmbTipo.setSelectedIndex(0);
    }
}