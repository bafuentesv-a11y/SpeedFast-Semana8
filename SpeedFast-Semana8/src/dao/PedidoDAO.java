package dao;

import model.EstadoPedido;
import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    /**
     * Crea un nuevo pedido en la base de datos.
     *
     * @param pedido pedido que se desea guardar
     * @return ID generado por MySQL, o -1 si ocurrió un error
     */
    public int create(Pedido pedido) {

        String sql =
                "INSERT INTO pedido (direccion, tipo, estado) " +
                        "VALUES (?, ?, ?)";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(
                             sql,
                             java.sql.Statement.RETURN_GENERATED_KEYS
                     )) {

            statement.setString(
                    1,
                    pedido.getDireccionEntrega()
            );

            statement.setString(
                    2,
                    pedido.getTipoEntrega()
            );

            statement.setString(
                    3,
                    pedido.getEstado().name()
            );

            int filas = statement.executeUpdate();

            if (filas == 0) {
                return -1;
            }

            // Obtener el ID generado automáticamente por MySQL
            try (ResultSet clavesGeneradas =
                         statement.getGeneratedKeys()) {

                if (clavesGeneradas.next()) {

                    int idGenerado =
                            clavesGeneradas.getInt(1);

                    pedido.setId(idGenerado);

                    System.out.println(
                            "Pedido creado correctamente. ID: "
                                    + idGenerado
                    );

                    return idGenerado;
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al crear el pedido: "
                            + e.getMessage()
            );
        }

        return -1;
    }

    /**
     * Método de compatibilidad con el código actual
     * de VentanaRegistroPedido.
     */
    public int crear(Pedido pedido) {
        return create(pedido);
    }

    /**
     * Obtiene todos los pedidos almacenados.
     *
     * @return lista de pedidos
     */
    public List<Pedido> readAll() {

        List<Pedido> pedidos =
                new ArrayList<>();

        String sql =
                "SELECT id, direccion, tipo, estado " +
                        "FROM pedido";

        try (Connection conexion =
                     ConexionDB.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql);
             ResultSet resultado =
                     statement.executeQuery()) {

            while (resultado.next()) {

                int id =
                        resultado.getInt("id");

                String direccion =
                        resultado.getString("direccion");

                String tipo =
                        resultado.getString("tipo");

                String estadoTexto =
                        resultado.getString("estado");

                EstadoPedido estado =
                        EstadoPedido.valueOf(
                                estadoTexto
                        );

                double distanciaKm = 0;

                Pedido pedido;

                switch (tipo) {

                    case "Comida":

                        pedido =
                                new PedidoComida(
                                        id,
                                        direccion,
                                        distanciaKm,
                                        tipo
                                );

                        break;

                    case "Encomienda":

                        pedido =
                                new PedidoEncomienda(
                                        id,
                                        direccion,
                                        distanciaKm,
                                        tipo
                                );

                        break;

                    case "Express":

                        pedido =
                                new PedidoExpress(
                                        id,
                                        direccion,
                                        distanciaKm,
                                        tipo
                                );

                        break;

                    default:
                        continue;
                }

                pedido.setEstado(estado);

                pedidos.add(pedido);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al leer los pedidos: "
                            + e.getMessage()
            );
        }

        return pedidos;
    }

    /**
     * Método de compatibilidad con el código actual
     * de las ventanas.
     */
    public List<Pedido> listarTodos() {
        return readAll();
    }

    /**
     * Actualiza un pedido existente.
     *
     * @param pedido pedido con los datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean update(Pedido pedido) {

        String sql =
                "UPDATE pedido " +
                        "SET direccion = ?, tipo = ?, estado = ? " +
                        "WHERE id = ?";

        try (Connection conexion =
                     ConexionDB.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setString(
                    1,
                    pedido.getDireccionEntrega()
            );

            statement.setString(
                    2,
                    pedido.getTipoEntrega()
            );

            statement.setString(
                    3,
                    pedido.getEstado().name()
            );

            statement.setInt(
                    4,
                    pedido.getId()
            );

            int filas =
                    statement.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar el pedido: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Elimina un pedido por su ID.
     *
     * @param id identificador del pedido
     * @return true si fue eliminado correctamente
     */
    public boolean delete(int id) {

        String sql =
                "DELETE FROM pedido WHERE id = ?";

        try (Connection conexion =
                     ConexionDB.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setInt(1, id);

            int filas =
                    statement.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al eliminar el pedido: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Obtiene todos los pedidos junto con el último
     * repartidor asignado a cada uno.
     *
     * Retorna:
     * [0] = Pedido
     * [1] = nombre del repartidor
     */
    public List<Object[]> listarTodosConRepartidor() {

        List<Object[]> resultados =
                new ArrayList<>();

        String sql =
                "SELECT " +
                        "p.id, " +
                        "p.direccion, " +
                        "p.tipo, " +
                        "p.estado, " +
                        "( " +
                        "   SELECT r.nombre " +
                        "   FROM entrega e " +
                        "   INNER JOIN repartidor r " +
                        "       ON e.id_repartidor = r.id " +
                        "   WHERE e.id_pedido = p.id " +
                        "   ORDER BY e.id DESC " +
                        "   LIMIT 1 " +
                        ") AS repartidor " +
                        "FROM pedido p " +
                        "ORDER BY p.id";

        try (Connection conexion =
                     ConexionDB.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql);
             ResultSet resultado =
                     statement.executeQuery()) {

            while (resultado.next()) {

                int id =
                        resultado.getInt("id");

                String direccion =
                        resultado.getString("direccion");

                String tipo =
                        resultado.getString("tipo");

                String estadoTexto =
                        resultado.getString("estado");

                String nombreRepartidor =
                        resultado.getString(
                                "repartidor"
                        );

                EstadoPedido estado =
                        EstadoPedido.valueOf(
                                estadoTexto
                        );

                double distanciaKm = 0;

                Pedido pedido;

                switch (tipo) {

                    case "Comida":

                        pedido =
                                new PedidoComida(
                                        id,
                                        direccion,
                                        distanciaKm,
                                        tipo
                                );

                        break;

                    case "Encomienda":

                        pedido =
                                new PedidoEncomienda(
                                        id,
                                        direccion,
                                        distanciaKm,
                                        tipo
                                );

                        break;

                    case "Express":

                        pedido =
                                new PedidoExpress(
                                        id,
                                        direccion,
                                        distanciaKm,
                                        tipo
                                );

                        break;

                    default:
                        continue;
                }

                pedido.setEstado(estado);

                resultados.add(
                        new Object[]{
                                pedido,
                                nombreRepartidor
                        }
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar pedidos con repartidor: "
                            + e.getMessage()
            );
        }

        return resultados;
    }
}