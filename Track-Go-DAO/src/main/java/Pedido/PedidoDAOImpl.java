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

        Empresa empresaBD = EmpresaDAOImpl.getInstance()
                .consultarEmpresa(pedido.getEmpresaDeOrigen().getRuc());

        if (empresaBD == null) {
            EmpresaDAOImpl.getInstance().AgregarEmpresa(pedido.getEmpresaDeOrigen());
            empresaBD = EmpresaDAOImpl.getInstance()
                    .consultarEmpresa(pedido.getEmpresaDeOrigen().getRuc());
        }

        pedido.setEmpresaDeOrigen(empresaBD);

        String sql = """
            INSERT INTO pedido (idPedido,
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
            stmt.setInt(8, empresaBD.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar el pedido", e);
        }
    }

    @Override
    public void modificarPedido(Pedido pedido) {
        //Se ve lo de la Direccion
        Direccion direccion = pedido.getDireccion();

        int idDireccion = DireccionDAOImpl.getInstance().insertOrGetDireccion(
                direccion.getDepartamento(),
                direccion.getProvincia(),
                direccion.getDistrito(),
                direccion.getCodPostal(),
                direccion.getCalleNumero(),
                direccion.getReferencia()
        );
        //Vemos lo de la Empresa
        Empresa empresaBD = EmpresaDAOImpl.getInstance()
                .consultarEmpresa(pedido.getEmpresaDeOrigen().getRuc());

        if (empresaBD == null) {
            EmpresaDAOImpl.getInstance().AgregarEmpresa(pedido.getEmpresaDeOrigen());
            empresaBD = EmpresaDAOImpl.getInstance()
                    .consultarEmpresa(pedido.getEmpresaDeOrigen().getRuc());
        }
        pedido.setEmpresaDeOrigen(empresaBD);

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
                SET destinatario = ?,
                    fechaActualizacion = ?,
                    tarifaEnvio = ?,
                    estado = ?,
                    idDireccion = ?,
                    placa = ?,
                    idEmpresa = ?,
                    idEmpleado = ?,
                    idAdministrador = ?
                WHERE idPedido = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, pedido.getDestinatario());
            ps.setDate(2, new java.sql.Date(pedido.getFechaActualizacion().getTime()));
            ps.setDouble(3, pedido.getTarifaEnvio());
            ps.setString(4, pedido.getEstado().name());

            ps.setInt(5, idDireccion);

            if (pedido.getDetalleTransporte() != null) {
                ps.setString(6, pedido.getDetalleTransporte().getPlaca());
            } else {
                ps.setNull(6, java.sql.Types.VARCHAR);
            }
            ps.setInt(7, empresaBD.getId());
            ps.setNull(8, java.sql.Types.INTEGER);
            ps.setNull(9, java.sql.Types.INTEGER);
            ps.setString(10, pedido.getIdPedido());
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
            int filasAfectadas=ps.executeUpdate();
            if (filasAfectadas>0){
                System.out.println("Pedido eliminado correctamente: " + idPedido);
            }else{
                System.out.println("No se encontró el pedido a Eliminar: " + idPedido);
            }
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
            WHERE DATE(fechaCreacion) = ?
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
                WHERE fechaCreacion BETWEEN ? AND ?
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
        String sql = "SELECT * FROM pedido WHERE idUsuario = ?";

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

        pedido.setIdPedido(rs.getString("idPedido"));
        pedido.setDestinatario(rs.getString("destinatario"));

        pedido.setFechaCreacion(rs.getDate("fechaCreacion"));
        pedido.setFechaActualizacion(rs.getDate("fechaActualizacion"));

        pedido.setTarifaEnvio(rs.getDouble("tarifaEnvio"));
        pedido.setEstado(EstadoPedido.valueOf(rs.getString("estado")));

        pedido.setIdUsuario(rs.getInt("idEmpleado"));

        // Direccion
        int idDireccion = rs.getInt("idDireccion");
        if (!rs.wasNull()) {
            Direccion direccion = DireccionDAOImpl.getInstance()
                    .obtenerPorId(idDireccion);
            pedido.setDireccion(direccion);
        } else {
            pedido.setDireccion(null);
        }
        //Empresa
        int idEmpresa = rs.getInt("idEmpresa");
        if (!rs.wasNull()) {
            Empresa empresa = EmpresaDAOImpl.getInstance()
                    .obtenerPorId(idEmpresa);
            pedido.setEmpresaDeOrigen(empresa);
        } else {
            pedido.setEmpresaDeOrigen(null);
        }

        String placa = rs.getString("placa");
        if (placa != null) {
            Transporte transporte = TransporteDAOImpl.getInstance()
                    .buscarDetallesTransporte(placa);

            pedido.setDetalleTransporte(transporte);
        } else {
            pedido.setDetalleTransporte(null);
        }

        return pedido;
    }
}