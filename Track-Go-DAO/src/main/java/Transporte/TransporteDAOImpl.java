package Transporte;

import Manager.DBManager;
import InformacionPedido.Transporte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class TransporteDAOImpl implements TransporteDAO {
    public TransporteDAOImpl() {}

    @Override
    public void insertar(Transporte transporte) {
        String sql = """
                INSERT INTO transporte (placa, tipo, marca, modelo)
                VALUES (?, ?, ?, ?)
                """;
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement cmd = conn.prepareStatement(sql)) {

            cmd.setString(1, transporte.getPlaca());
            cmd.setString(2, transporte.getTipo());
            cmd.setString(3, transporte.getMarca());
            cmd.setString(4, transporte.getModelo());
            cmd.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar transporte: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modificar(Transporte transporte) {
        String sql = """
                UPDATE transporte
                SET tipo   = ?,
                    marca  = ?,
                    modelo = ?
                WHERE placa = ?
                """;
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement cmd = conn.prepareStatement(sql)) {

            cmd.setString(1, transporte.getTipo());
            cmd.setString(2, transporte.getMarca());
            cmd.setString(3, transporte.getModelo());
            cmd.setString(4, transporte.getPlaca());  // WHERE al final
            cmd.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al modificar transporte: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(String placa) {
        String sql = "DELETE FROM transporte WHERE placa = ?";
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement cmd = conn.prepareStatement(sql)) {

            cmd.setString(1, placa);
            cmd.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar transporte: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transporte obtenerPorId(String placa) {
        String sql = "SELECT * FROM transporte WHERE placa = ?";
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement cmd = conn.prepareStatement(sql)) {

            cmd.setString(1, placa);
            try (ResultSet rs = cmd.executeQuery()) {
                if (rs.next()) {
                    return mapearTransporte(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener transporte: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Transporte> listarTodos() {
        String sql = "SELECT * FROM transporte";
        List<Transporte> transportes = new ArrayList<>();
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement cmd = conn.prepareStatement(sql);
             ResultSet rs = cmd.executeQuery()) {

            while (rs.next()) {
                transportes.add(mapearTransporte(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar transportes: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return transportes;
    }

    private Transporte mapearTransporte(ResultSet rs) throws SQLException {
        return new Transporte(
                rs.getString("placa"),
                rs.getString("tipo"),
                rs.getString("marca"),
                rs.getString("modelo")
        );
    }

}
