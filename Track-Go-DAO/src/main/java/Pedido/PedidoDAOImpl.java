package Pedido;

import InformacionPedido.Transporte;
import Manager.DBManager;
import InformacionEmpresa.Empresa;
import InformacionPedido.Direccion;
import InformacionPedido.EstadoPedido;
import InformacionPedido.Pedido;
import Direccion.DireccionDAOImpl;
import Transporte.TransporteDAOImpl;
import Empresa.EmpresaDAOImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;

public class PedidoDAOImpl implements PedidoDAO {
    private static PedidoDAOImpl instance;
    private static Connection connection;

    private PedidoDAOImpl(){
        connection = DBManager.getInstance().getConnection();
    }

    public static PedidoDAOImpl getInstance(){
        if(instance == null) {
            synchronized (PedidoDAOImpl.class) {
                instance = new PedidoDAOImpl();
            }
        }
        return instance;
    }

    @Override
    public void insertarPedido(Pedido pedido) {
        Direccion direccion = pedido.getDireccion();

        int idDireccion = DireccionDAOImpl.getInstance().insertOrGetDireccion(
                direccion.getDepartamento(),
                direccion.getProvincia(),
                direccion.getDistrito(),
                direccion.getCodPostal(),
                direccion.getCalleNumero(),
                direccion.getReferencia()
        );

        Empresa empresaIngresada = pedido.getEmpresaDeOrigen();
        Empresa existeEmpresa = EmpresaDAOImpl.getInstance().consultarEmpresa(empresaIngresada.getRuc());

        if (existeEmpresa == null) {
            EmpresaDAOImpl.getInstance().AgregarEmpresa(empresaIngresada);
            pedido.setEmpresaDeOrigen(empresaIngresada);
        } else {
            pedido.setEmpresaDeOrigen(existeEmpresa);
        }

        String sql = """
            INSERT INTO pedido (
                idPedido,
                destinatario,
                fechaCreacion,
                fechaActualizacion,
                tarifaEnvio,
                estado,
                idDireccion,
                idEmpresa
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pedido.getIdPedido());
            stmt.setString(2, pedido.getDestinatario());
            stmt.setDate(3, new java.sql.Date(pedido.getFechaCreacion().getTime()));
            stmt.setDate(4, new java.sql.Date(pedido.getFechaActualizacion().getTime()));
            stmt.setDouble(5, pedido.getTarifaEnvio());
            stmt.setString(6, pedido.getEstado().name());
            stmt.setInt(7, idDireccion);
            stmt.setInt(8, pedido.getEmpresaDeOrigen().getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar el pedido", e);
        }
    }

    @Override
    public void modificarPedido(Pedido pedido) {
        /**
         *                 idPedido,
         *                 destinatario,
         *                 fechaCreacion,
         *                 fechaActualizacion,
         *                 tarifaEnvio,
         *                 estado,
         *                 idDireccion,
         *                 idEmpresa
         * */
        String sql = """
                UPDATE pedido
                SET destinatario = ?, fechaCreacion = ?, fechaActualizacion = ?, tarifaEnvio = ?, estado = ?, idEmpresa = ?
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
    public void eliminarPedido(String idPedido) {
        String sql = "DELETE FROM pedido WHERE idPedido = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idPedido);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Pedido obtenerPorId(String idPedido) {
        String sql = "SELECT * FROM pedido WHERE idPedido = ?";

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
    public ArrayList<Pedido> listarTodos() {
        ArrayList<Pedido> pedidos = new ArrayList<>();
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
    public ArrayList<Pedido> listarPedidosPorFecha(java.util.Date fecha) {
        ArrayList<Pedido> pedidos = new ArrayList<>();

        String sql = """
            SELECT * FROM pedido
            WHERE DATE(fecha_creacion) = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, new Date(fecha.getTime()));

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
    public ArrayList<Pedido> listarPorRangoFechas(java.util.Date inicio, java.util.Date fin) {
        ArrayList<Pedido> pedidos = new ArrayList<>();
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
    public ArrayList<Pedido> listarPorEstado(EstadoPedido estado) {
        ArrayList<Pedido> pedidos = new ArrayList<>();
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
    public ArrayList<Pedido> listarPorUsuario(int idUsuario) {
        ArrayList<Pedido> pedidos = new ArrayList<>();
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
    @Override
    public ArrayList<Pedido> ordenarPedidosPorFecha(){
        return null;
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
