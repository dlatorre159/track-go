package dao.Pedido;

import DBManager.DBManager;
import InformacionEmpresa.Empresa;
import InformacionPedido.Direccion;
import InformacionPedido.EstadoPedido;
import InformacionPedido.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    private Connection connection;

    public PedidoDAOImpl() {
        try {
            this.connection = DBManager.getInstance().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertar(Pedido pedido) {
        String sql = """
                INSERT INTO pedido
                (id_pedido, destinatario, fecha_creacion, fecha_actualizacion, tarifa_envio, estado, id_usuario)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, pedido.getIdPedido());
            ps.setString(2, pedido.getDestinatario());
            ps.setDate(3, new Date(pedido.getFechaCreacion().getTime()));
            ps.setDate(4, new Date(pedido.getFechaActualizacion().getTime()));
            ps.setDouble(5, pedido.getTarifaEnvio());
            ps.setString(6, pedido.getEstado().name());
            ps.setInt(7, pedido.getIdUsuario());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modificar(Pedido pedido) {
        String sql = """
                UPDATE pedido
                SET destinatario = ?, fechaActualizacion = ?, tarifaEnvio = ?, estado = ?,
                    idDireccion = ?, placa = ?, idEmpleado = ?, idAdministrador = ?, idEmpresa = ?
                WHERE idPedido = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, pedido.getDestinatario());
            ps.setDate(2, new Date(pedido.getFechaActualizacion().getTime()));
            ps.setDouble(3, pedido.getTarifaEnvio());
            ps.setString(4, pedido.getEstado().name());
            ps.setInt(5, pedido.getIdUsuario());
            ps.setString(6, pedido.getIdPedido());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(String idPedido) {
        String sql = "DELETE FROM pedido WHERE id_pedido = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idPedido);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Pedido obtenerPorId(String idPedido) {
        String sql = "SELECT * FROM pedido WHERE id_pedido = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPedido(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Pedido> listarTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pedidos.add(mapearPedido(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    @Override
    public List<Pedido> listarPorRangoFechas(java.util.Date inicio, java.util.Date fin) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = """
                SELECT * FROM pedido
                WHERE fecha_creacion BETWEEN ? AND ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, new Date(inicio.getTime()));
            ps.setDate(2, new Date(fin.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapearPedido(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    @Override
    public List<Pedido> listarPorEstado(EstadoPedido estado) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE estado = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, estado.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapearPedido(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    @Override
    public List<Pedido> listarPorUsuario(int idUsuario) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE id_usuario = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapearPedido(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();

        pedido.setIdPedido(rs.getString("id_pedido"));
        pedido.setDestinatario(rs.getString("destinatario"));
        pedido.setFechaCreacion(rs.getDate("fecha_creacion"));
        pedido.setFechaActualizacion(rs.getDate("fecha_actualizacion"));
        pedido.setTarifaEnvio(rs.getDouble("tarifa_envio"));
        pedido.setEstado(EstadoPedido.valueOf(rs.getString("estado")));
        pedido.setIdUsuario(rs.getInt("id_usuario"));

        // Temporalmente null hasta implementar otros DAO
        Direccion direccion = null;
        Empresa empresa = null;
        pedido.setDireccion(direccion);
        pedido.setEmpresaDeOrigen(empresa);

        return pedido;
    }
}
