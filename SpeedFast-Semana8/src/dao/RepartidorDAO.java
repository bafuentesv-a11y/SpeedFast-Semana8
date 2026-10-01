package dao;

import model.Repartidor;
import model.ZonaDeCarga;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {

    /**
     * Crea un nuevo repartidor en la base de datos.
     *
     * @param repartidor repartidor que se desea guardar
     * @return true si fue creado correctamente
     */
    public boolean create(Repartidor repartidor) {

        String sql = "INSERT INTO repartidor (nombre) VALUES (?)";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement statement =
                        conexion.prepareStatement(
                                sql,
                                PreparedStatement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(1, repartidor.getNombre());

            int filas = statement.executeUpdate();

            if (filas == 0) {
                return false;
            }

            // Obtener el ID generado automáticamente por MySQL
            try (ResultSet resultado = statement.getGeneratedKeys()) {

                if (resultado.next()) {
                    repartidor.setId(resultado.getInt(1));
                }
            }

            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Error al crear repartidor: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Obtiene todos los repartidores almacenados.
     *
     * @return lista de repartidores
     */
    public List<Repartidor> readAll() {

        List<Repartidor> repartidores = new ArrayList<>();

        String sql =
                "SELECT id, nombre FROM repartidor ORDER BY id";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement statement =
                        conexion.prepareStatement(sql);
                ResultSet resultado =
                        statement.executeQuery()
        ) {

            while (resultado.next()) {

                int id = resultado.getInt("id");

                String nombre =
                        resultado.getString("nombre");

                Repartidor repartidor =
                        new Repartidor(
                                id,
                                nombre,
                                true,
                                new ZonaDeCarga()
                        );

                repartidores.add(repartidor);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al leer repartidores: "
                            + e.getMessage()
            );
        }

        return repartidores;
    }

    /**
     * Actualiza un repartidor existente.
     *
     * @param repartidor repartidor con los datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean update(Repartidor repartidor) {

        String sql =
                "UPDATE repartidor " +
                        "SET nombre = ? " +
                        "WHERE id = ?";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setString(1, repartidor.getNombre());
            statement.setInt(2, repartidor.getId());

            int filas = statement.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar repartidor: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Elimina un repartidor por su ID.
     *
     * @param id identificador del repartidor
     * @return true si fue eliminado correctamente
     */
    public boolean delete(int id) {

        String sql =
                "DELETE FROM repartidor WHERE id = ?";

        try (
                Connection conexion = ConexionDB.conectar();
                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            int filas = statement.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al eliminar repartidor: "
                            + e.getMessage()
            );

            return false;
        }
    }
}