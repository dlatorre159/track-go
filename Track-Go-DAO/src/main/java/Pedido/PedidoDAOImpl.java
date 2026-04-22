package Pedido;

<<<<<<< HEAD
import InformacionPedido.Transporte;
=======
import Empresa.EmpresaDAOImpl;
>>>>>>> c246e75beed93324dedd98b3e4d6ba46c59fe2bc
import Manager.DBManager;
import InformacionEmpresa.Empresa;
import InformacionPedido.Direccion;
import InformacionPedido.DetalleDePedido;
import InformacionPedido.EstadoPedido;
import InformacionPedido.Pedido;
<<<<<<< HEAD
import Direccion.DireccionDAOImpl;
import Transporte.TransporteDAOImpl;
import Empresa.EmpresaDAOImpl;
=======
import Transporte.TransporteDAOImpl;
>>>>>>> c246e75beed93324dedd98b3e4d6ba46c59fe2bc

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

public class PedidoDAOImpl implements PedidoDAO {
    private static PedidoDAOImpl instance;
    private static Connection connection;

<<<<<<< HEAD
    private PedidoDAOImpl(){
        connection = DBManager.getInstance().getConnection();
    }

    public static PedidoDAOImpl getInstance(){
        if(instance == null) {
            synchronized (PedidoDAOImpl.class) {
                instance = new PedidoDAOImpl();
            }
=======
    
    private static PedidoDAOImpl instance;

    private PedidoDAOImpl() {}

    public static synchronized PedidoDAOImpl getInstance() {
        if (instance == null) {
            instance = new PedidoDAOImpl();
>>>>>>> c246e75beed93324dedd98b3e4d6ba46c59fe2bc
        }
        return instance;
    }

    
    @Override
<<<<<<< HEAD
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
=======
    public void insertar(Pedido pedido) {
        try (Connection conn = DBManager.getConexion()) {

            conn.setAutoCommit(false);
            try {
                int idDireccion = insertarDireccion(conn, pedido.getDireccion());
                insertarPedido(conn, pedido, idDireccion);
                insertarDetalles(conn, pedido);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar pedido: " + e.getMessage());
            throw new RuntimeException(e);
>>>>>>> c246e75beed93324dedd98b3e4d6ba46c59fe2bc
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
<<<<<<< HEAD
                SET destinatario = ?, fechaCreacion = ?, fechaActualizacion = ?, tarifaEnvio = ?, estado = ?, idEmpresa = ?
=======
                SET destinatario       = ?,
                    fechaActualizacion = ?,
                    tarifaEnvio        = ?,
                    estado             = ?,
                    placa              = ?,
                    idEmpleado         = ?
>>>>>>> c246e75beed93324dedd98b3e4d6ba46c59fe2bc
                WHERE idPedido = ?
                """;
        try (Connection conn = DBManager.getConexion();
             PreparedStatement cmd = conn.prepareStatement(sql)) {

            cmd.setString(1, pedido.getDestinatario());
            cmd.setDate  (2, new Date(new java.util.Date().getTime()));
            cmd.setDouble(3, pedido.getTarifaEnvio());
            cmd.setString(4, pedido.getEstado().name());

            if (pedido.getDetalleTransporte() != null) {
                cmd.setString(5, pedido.getDetalleTransporte().getPlaca());
            } else {
                cmd.setNull(5, Types.VARCHAR);
            }

            cmd.setInt   (6, pedido.getIdUsuario());
            cmd.setString(7, pedido.getIdPedido());
            cmd.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al modificar pedido: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
<<<<<<< HEAD
    public void eliminarPedido(String idPedido) {
        String sql = "DELETE FROM pedido WHERE idPedido = ?";
=======
    public void eliminar(String idPedido) {
        String sql = "DELETE FROM pedido WHERE idPedido = ?";
        try (Connection conn = DBManager.getConexion();
             PreparedStatement cmd = conn.prepareStatement(sql)) {

            cmd.setString(1, idPedido);
            cmd.executeUpdate();
>>>>>>> c246e75beed93324dedd98b3e4d6ba46c59fe2bc

        } catch (SQLException e) {
            System.err.println("Error al eliminar pedido: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public Pedido obtenerPorId(String idPedido) {
        String sql = "SELECT * FROM pedido WHERE idPedido = ?";
<<<<<<< HEAD
=======
        try (Connection conn = DBManager.getConexion();
             PreparedStatement cmd = conn.prepareStatement(sql)) {
>>>>>>> c246e75beed93324dedd98b3e4d6ba46c59fe2bc

            cmd.setString(1, idPedido);
            try (ResultSet rs = cmd.executeQuery()) {
                if (rs.next()) {
                    return mapearPedido(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener pedido: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Pedido> listarTodos() {
        String sql = "SELECT * FROM pedido";
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection conn = DBManager.getConexion();
             PreparedStatement cmd = conn.prepareStatement(sql);
             ResultSet rs = cmd.executeQuery()) {

            while (rs.next()) {
                pedidos.add(mapearPedido(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar pedidos: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return pedidos;
    }

    @Override
<<<<<<< HEAD
    public ArrayList<Pedido> listarPedidosPorFecha(java.util.Date fecha) {
        ArrayList<Pedido> pedidos = new ArrayList<>();
=======
    public List<Pedido> listarPorRangoFechas(java.util.Date inicio, java.util.Date fin) {
        String sql = "SELECT * FROM pedido WHERE fechaCreacion BETWEEN ? AND ?";
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection conn = DBManager.getConexion();
             PreparedStatement cmd = conn.prepareStatement(sql)) {
>>>>>>> c246e75beed93324dedd98b3e4d6ba46c59fe2bc

            cmd.setDate(1, new Date(inicio.getTime()));
            cmd.setDate(2, new Date(fin.getTime()));
            try (ResultSet rs = cmd.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapearPedido(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar pedidos por fecha: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return pedidos;
    }

    @Override
    public List<Pedido> listarPorEstado(EstadoPedido estado) {
        String sql = "SELECT * FROM pedido WHERE estado = ?";
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection conn = DBManager.getConexion();
             PreparedStatement cmd = conn.prepareStatement(sql)) {

            cmd.setString(1, estado.name());
            try (ResultSet rs = cmd.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapearPedido(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar pedidos por estado: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return pedidos;
    }

    @Override
    public List<Pedido> listarPorTransportista(int idUsuario) {
        String sql = "SELECT * FROM pedido WHERE idEmpleado = ?";
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection conn = DBManager.getConexion();
             PreparedStatement cmd = conn.prepareStatement(sql)) {

            cmd.setInt(1, idUsuario);
            try (ResultSet rs = cmd.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapearPedido(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar pedidos por transportista: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return pedidos;
    }
    @Override
    public ArrayList<Pedido> ordenarPedidosPorFecha(){
        return null;
    }



    private int insertarDireccion(Connection conn, Direccion dir) throws SQLException {
        String sql = """
                INSERT INTO direccion (departamento, provincia, distrito,
                                       codPostal, calleNumero, referencia)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement cmd = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {

            cmd.setString(1, dir.getDepartamento());
            cmd.setString(2, dir.getProvincia());
            cmd.setString(3, dir.getDistrito());
            cmd.setString(4, dir.getCodPostal());
            cmd.setString(5, dir.getCalleNumero());
            cmd.setString(6, dir.getReferencia());
            cmd.executeUpdate();

            try (ResultSet keys = cmd.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt("idDireccion");
                }
            }
        }
        throw new SQLException("No se generó idDireccion tras el INSERT.");
    }

    private void insertarPedido(Connection conn, Pedido pedido,
                                 int idDireccion) throws SQLException {
        String sql = """
                INSERT INTO pedido (
                    idPedido, destinatario, fechaCreacion, fechaActualizacion,
                    tarifaEnvio, estado, idDireccion, placa, idEmpleado
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement cmd = conn.prepareStatement(sql)) {

            cmd.setString(1, pedido.getIdPedido());
            cmd.setString(2, pedido.getDestinatario());
            cmd.setDate  (3, new Date(new java.util.Date().getTime()));
            cmd.setDate  (4, new Date(new java.util.Date().getTime()));
            cmd.setDouble(5, pedido.getTarifaEnvio());
            cmd.setString(6, pedido.getEstado().name());
            cmd.setInt   (7, idDireccion);

            if (pedido.getDetalleTransporte() != null) {
                cmd.setString(8, pedido.getDetalleTransporte().getPlaca());
            } else {
                cmd.setNull(8, Types.VARCHAR);
            }

            cmd.setInt(9, pedido.getIdUsuario());
            cmd.executeUpdate();
        }
    }

    private void insertarDetalles(Connection conn, Pedido pedido) throws SQLException {
        if (pedido.getDetalleDePedido() == null
                || pedido.getDetalleDePedido().isEmpty()) {
            return;
        }
        String sql = """
                INSERT INTO detalleDePedido (descripcion, cantidad, idPedido)
                VALUES (?, ?, ?)
                """;
        for (DetalleDePedido detalle : pedido.getDetalleDePedido()) {
            try (PreparedStatement cmd = conn.prepareStatement(sql)) {
                cmd.setString(1, detalle.getDescripcion());
                cmd.setInt   (2, detalle.getCantidad());
                cmd.setString(3, pedido.getIdPedido());
                cmd.executeUpdate();
            }
        }
    }



    private Direccion obtenerDireccion(int idDireccion) throws SQLException {
        String sql = "SELECT * FROM direccion WHERE idDireccion = ?";
        try (Connection conn = DBManager.getConexion();
             PreparedStatement cmd = conn.prepareStatement(sql)) {

            cmd.setInt(1, idDireccion);
            try (ResultSet rs = cmd.executeQuery()) {
                if (rs.next()) {
                    return new Direccion(
                            rs.getString("departamento"),
                            rs.getString("provincia"),
                            rs.getString("distrito"),
                            rs.getString("codPostal"),
                            rs.getString("calleNumero"),
                            rs.getString("referencia")
                    );
                }
            }
        }
        return null;
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        
        int idUsuario = rs.getInt("idEmpleado");
        if (rs.wasNull()) {
            idUsuario = rs.getInt("idAdministrador");
        }

       
        Direccion direccion = obtenerDireccion(rs.getInt("idDireccion"));

       
        Empresa empresa = null;
        int idEmpresa = rs.getInt("idEmpresa");
        if (!rs.wasNull()) {
            empresa = new EmpresaDAOImpl().obtenerPorId(idEmpresa);
        }

        Pedido pedido = new Pedido(
                rs.getString("destinatario"),
                rs.getDouble("tarifaEnvio"),
                EstadoPedido.valueOf(rs.getString("estado")),
                idUsuario,
                direccion,
                empresa
        );

        pedido.setIdPedido(rs.getString("idPedido"));

        
        String placa = rs.getString("placa");
        if (placa != null && !placa.isBlank()) {
            pedido.setDetalleTransporte(
                    new TransporteDAOImpl().obtenerPorId(placa)
            );
        }

        return pedido;
    }
}
