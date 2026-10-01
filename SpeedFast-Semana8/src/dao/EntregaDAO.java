package dao;

import model.Entrega;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

    /**
     * Crea una nueva entrega en la base de datos.
     *
     * También actualiza el estado del pedido a ENTREGADO.
     *
     * @param entrega entrega que se desea guardar
     * @return true si se guardó correctamente
     */
    public boolean create(Entrega entrega) {

        String sqlEntrega =
                "INSERT INTO entrega " +
                        "(id_pedido, id_repartidor, fecha, hora) " +
                        "VALUES (?, ?, ?, ?)";

        String sqlEstado =
                "UPDATE pedido " +
                        "SET estado = ? " +
                        "WHERE id = ?";

        Connection conexion = null;

        try {

            conexion = ConexionDB.conectar();

            // Iniciamos una transacción para que ambas operaciones
            // se realicen correctamente o ninguna se aplique.
            conexion.setAutoCommit(false);

            try (PreparedStatement statementEntrega =
                         conexion.prepareStatement(
                                 sqlEntrega,
                                 java.sql.Statement.RETURN_GENERATED_KEYS
                         );
                 PreparedStatement statementEstado =
                         conexion.prepareStatement(sqlEstado)) {

                // Guardar la entrega
                statementEntrega.setInt(
                        1,
                        entrega.getIdPedido()
                );

                statementEntrega.setInt(
                        2,
                        entrega.getIdRepartidor()
                );

                statementEntrega.setDate(
                        3,
                        java.sql.Date.valueOf(
                                entrega.getFecha()
                        )
                );

                statementEntrega.setTime(
                        4,
                        java.sql.Time.valueOf(
                                entrega.getHora()
                        )
                );

                int filas =
                        statementEntrega.executeUpdate();

                if (filas == 0) {
                    conexion.rollback();
                    return false;
                }

                // Obtener el ID generado por MySQL
                try (ResultSet clavesGeneradas =
                             statementEntrega.getGeneratedKeys()) {

                    if (clavesGeneradas.next()) {

                        entrega.setId(
                                clavesGeneradas.getInt(1)
                        );
                    }
                }

                // Cambiar el estado del pedido a ENTREGADO
                statementEstado.setString(
                        1,
                        "ENTREGADO"
                );

                statementEstado.setInt(
                        2,
                        entrega.getIdPedido()
                );

                statementEstado.executeUpdate();

                // Confirmar ambas operaciones
                conexion.commit();

                System.out.println(
                        "Entrega guardada correctamente y " +
                                "pedido actualizado a ENTREGADO."
                );

                return true;
            }

        } catch (SQLException e) {

            if (conexion != null) {

                try {
                    conexion.rollback();
                } catch (SQLException errorRollback) {

                    System.out.println(
                            "Error al revertir la operación: "
                                    + errorRollback.getMessage()
                    );
                }
            }

            System.out.println(
                    "Error al crear la entrega: "
                            + e.getMessage()
            );

            return false;

        } finally {

            if (conexion != null) {

                try {
                    conexion.setAutoCommit(true);
                    conexion.close();

                } catch (SQLException e) {

                    System.out.println(
                            "Error al cerrar la conexión: "
                                    + e.getMessage()
                    );
                }
            }
        }
    }

    /**
     * Método de compatibilidad con el código actual
     * de VentanaAsignarRepartidor.
     */
    public void guardar(Entrega entrega) {
        create(entrega);
    }

    /**
     * Obtiene todas las entregas almacenadas.
     *
     * @return lista de entregas
     */
    public List<Entrega> readAll() {

        List<Entrega> entregas =
                new ArrayList<>();

        String sql =
                "SELECT id, id_pedido, id_repartidor, fecha, hora " +
                        "FROM entrega " +
                        "ORDER BY id";

        try (Connection conexion =
                     ConexionDB.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql);
             ResultSet resultado =
                     statement.executeQuery()) {

            while (resultado.next()) {

                int id =
                        resultado.getInt("id");

                int idPedido =
                        resultado.getInt("id_pedido");

                int idRepartidor =
                        resultado.getInt("id_repartidor");

                java.time.LocalDate fecha =
                        resultado.getDate("fecha")
                                .toLocalDate();

                java.time.LocalTime hora =
                        resultado.getTime("hora")
                                .toLocalTime();

                Entrega entrega =
                        new Entrega(
                                idPedido,
                                idRepartidor,
                                fecha,
                                hora
                        );

                entrega.setId(id);

                entregas.add(entrega);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al leer las entregas: "
                            + e.getMessage()
            );
        }

        return entregas;
    }

    /**
     * Obtiene las entregas asociadas a un pedido.
     *
     * @param idPedido identificador del pedido
     * @return lista de entregas
     */
    public List<Entrega> readByPedido(int idPedido) {

        List<Entrega> entregas =
                new ArrayList<>();

        String sql =
                "SELECT id, id_pedido, id_repartidor, fecha, hora " +
                        "FROM entrega " +
                        "WHERE id_pedido = ? " +
                        "ORDER BY id";

        try (Connection conexion =
                     ConexionDB.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setInt(1, idPedido);

            try (ResultSet resultado =
                         statement.executeQuery()) {

                while (resultado.next()) {

                    Entrega entrega =
                            crearEntregaDesdeResultado(
                                    resultado
                            );

                    entregas.add(entrega);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al buscar entregas del pedido: "
                            + e.getMessage()
            );
        }

        return entregas;
    }

    /**
     * Obtiene las entregas realizadas por un repartidor.
     *
     * @param idRepartidor identificador del repartidor
     * @return lista de entregas
     */
    public List<Entrega> readByRepartidor(
            int idRepartidor) {

        List<Entrega> entregas =
                new ArrayList<>();

        String sql =
                "SELECT id, id_pedido, id_repartidor, fecha, hora " +
                        "FROM entrega " +
                        "WHERE id_repartidor = ? " +
                        "ORDER BY id";

        try (Connection conexion =
                     ConexionDB.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setInt(1, idRepartidor);

            try (ResultSet resultado =
                         statement.executeQuery()) {

                while (resultado.next()) {

                    Entrega entrega =
                            crearEntregaDesdeResultado(
                                    resultado
                            );

                    entregas.add(entrega);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al buscar entregas del repartidor: "
                            + e.getMessage()
            );
        }

        return entregas;
    }

    /**
     * Actualiza una entrega existente.
     *
     * @param entrega entrega con los datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean update(Entrega entrega) {

        String sql =
                "UPDATE entrega " +
                        "SET id_pedido = ?, " +
                        "id_repartidor = ?, " +
                        "fecha = ?, " +
                        "hora = ? " +
                        "WHERE id = ?";

        try (Connection conexion =
                     ConexionDB.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    entrega.getIdPedido()
            );

            statement.setInt(
                    2,
                    entrega.getIdRepartidor()
            );

            statement.setDate(
                    3,
                    java.sql.Date.valueOf(
                            entrega.getFecha()
                    )
            );

            statement.setTime(
                    4,
                    java.sql.Time.valueOf(
                            entrega.getHora()
                    )
            );

            statement.setInt(
                    5,
                    entrega.getId()
            );

            int filas =
                    statement.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar la entrega: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Elimina una entrega por su ID.
     *
     * @param id identificador de la entrega
     * @return true si fue eliminada correctamente
     */
    public boolean delete(int id) {

        String sql =
                "DELETE FROM entrega WHERE id = ?";

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
                    "Error al eliminar la entrega: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Convierte una fila del ResultSet en un objeto Entrega.
     */
    private Entrega crearEntregaDesdeResultado(
            ResultSet resultado) throws SQLException {

        int id =
                resultado.getInt("id");

        int idPedido =
                resultado.getInt("id_pedido");

        int idRepartidor =
                resultado.getInt("id_repartidor");

        java.time.LocalDate fecha =
                resultado.getDate("fecha")
                        .toLocalDate();

        java.time.LocalTime hora =
                resultado.getTime("hora")
                        .toLocalTime();

        Entrega entrega =
                new Entrega(
                        idPedido,
                        idRepartidor,
                        fecha,
                        hora
                );

        entrega.setId(id);

        return entrega;
    }
}