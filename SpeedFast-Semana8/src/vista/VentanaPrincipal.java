package vista;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class VentanaPrincipal extends JFrame {

    private JButton btnRegistrar;
    private JButton btnListar;
    private JButton btnIniciarEntrega;
    private JButton btnRepartidores;
    private JButton btnEntregas;

    public VentanaPrincipal() {

        setTitle("SpeedFast - Gestión de Entregas");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Título
        JLabel titulo = new JLabel(
                "SPEEDFAST - GESTIÓN DE ENTREGAS",
                JLabel.CENTER
        );

        // Panel de botones
        JPanel panelBotones = new JPanel();

        panelBotones.setLayout(
                new GridLayout(
                        5,
                        1,
                        10,
                        10
                )
        );

        // Crear botones
        btnRegistrar =
                new JButton("Registrar Pedido");

        btnListar =
                new JButton("Listar Pedidos");

        btnIniciarEntrega =
                new JButton(
                        "Asignar Repartidor / Iniciar Entrega"
                );

        btnRepartidores =
                new JButton(
                        "Gestionar Repartidores"
                );

        btnEntregas =
                new JButton(
                        "Gestionar Entregas"
                );

        // Agregar botones al panel
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnListar);
        panelBotones.add(btnIniciarEntrega);
        panelBotones.add(btnRepartidores);
        panelBotones.add(btnEntregas);

        // Agregar componentes a la ventana
        add(
                titulo,
                BorderLayout.NORTH
        );

        add(
                panelBotones,
                BorderLayout.CENTER
        );

        // Registrar pedido
        btnRegistrar.addActionListener(
                e -> new VentanaRegistroPedido()
        );

        // Listar pedidos
        btnListar.addActionListener(
                e -> new VentanaListaPedidos()
        );

        // Asignar repartidor
        btnIniciarEntrega.addActionListener(
                e -> new VentanaAsignarRepartidor()
        );

        // Gestionar repartidores
        btnRepartidores.addActionListener(
                e -> new VentanaGestionRepartidores()
        );

        // Gestionar entregas
        btnEntregas.addActionListener(
                e -> new VentanaGestionEntregas()
        );

        setVisible(true);
    }
}