package Usuario;

import InformacionPedido.Transporte;
import InformacionUsuario.*;
import Manager.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UsuarioDAOImpl implements UsuarioDAO{
    private static UsuarioDAOImpl instance;
    private static Connection connection;

    private UsuarioDAOImpl(){
        connection = DBManager.getInstance().getConnection();
    }
    
    public static UsuarioDAOImpl getInstance(){
        if(instance == null){
            synchronized (UsuarioDAOImpl.class){
                instance = new UsuarioDAOImpl();
            }
        }
        return instance;
    }

    @Override
    public void agregarUsuarioEmpleado(Empleado empleado){
        String sql = "INSERT INTO empleado (codigoEmpleado, dni, nombres, apellidos, correo," +
                "contrasenhaHash, telefono, fechaRegistro, estado, cargo, licencia, turno)" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        java.sql.Date fechaRegistro = new java.sql.Date(empleado.getFechaRegistro().getTime());
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1,empleado.getCodigoEmpleado());
            stmt.setString(2,empleado.getDni());
            stmt.setString(3,empleado.getNombres());
            stmt.setString(4,empleado.getApellidos());
            stmt.setString(5,empleado.getCorreo());
            stmt.setString(6,empleado.getContrasenhaHash());
            stmt.setString(7,empleado.getTelefono());
            stmt.setDate(8,fechaRegistro);
            stmt.setInt(9,empleado.getEstado() ? 1:0);
            stmt.setInt(10,empleado.getCargo().ordinal());
            stmt.setString(11,empleado.getLicencia());
            stmt.setInt(12,empleado.getTurno().ordinal());
            int filasAfectadas = stmt.executeUpdate();
            if(filasAfectadas == 1){
                System.out.println("Se ha agregado un empleado");
            }else{
                System.out.println("Se han agregado" + filasAfectadas + "empleados");
            }
        }catch(SQLException e){
                System.out.println("No se ha podido agregar ningún empleado");
        }
    }

    @Override
    public void agregarUsuarioAdministrador(Administrador administrador){
        String sql = "INSERT INTO administrador (codigoEmpleado, dni, nombres, apellidos, correo," +
                "contrasenhaHash, telefono, fechaRegistro, estado, cargo, nivelDeAcceso, isManager)" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        java.sql.Date fechaRegistro = new java.sql.Date(administrador.getFechaRegistro().getTime());
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1,administrador.getCodigoEmpleado());
            stmt.setString(2,administrador.getDni());
            stmt.setString(3,administrador.getNombres());
            stmt.setString(4,administrador.getApellidos());
            stmt.setString(5,administrador.getCorreo());
            stmt.setString(6,administrador.getContrasenhaHash());
            stmt.setString(7,administrador.getTelefono());
            stmt.setDate(8,fechaRegistro);
            stmt.setInt(9,administrador.getEstado() ? 1:0);
            stmt.setInt(10,administrador.getCargo().ordinal());
            stmt.setString(11,administrador.getNivelDeAcceso());
            stmt.setInt(12,administrador.getIsManager()?1:0);
            int filasAfectadas = stmt.executeUpdate();
            if(filasAfectadas == 1){
                System.out.println("Se ha agregado un administrador");
            }else{
                System.out.println("Se han agregado" + filasAfectadas + "administradores");
            }
        }catch(SQLException e){
            System.out.println("No se ha podido agregar ningún administrador");
        }
    }

    @Override
    public void eliminarUsuarioEmpleado(int idUsuario){
        String sql = "DELETE FROM empleado WHERE idUsuario = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1,idUsuario);
            int filasAfectadas = stmt.executeUpdate();
            if(filasAfectadas == 1){
                System.out.println("Se ha eliminado un empleado");
            }else{
                System.out.println("Se han agregado" + filasAfectadas + "empleados");
            }
        }catch(SQLException e){
            System.out.println("No se ha podido agregar ningún empleado");
        }
    }

    @Override
    public void eliminarUsuarioAdministrador(int idUsuario){
        String sql = "DELETE FROM administrador WHERE idUsuario = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1,idUsuario);
            int filasAfectadas = stmt.executeUpdate();
            if(filasAfectadas == 1){
                System.out.println("Se ha eliminado un administrador");
            }else{
                System.out.println("Se han agregado" + filasAfectadas + "administradores");
            }
        }catch(SQLException e){
            System.out.println("No se ha podido agregar ningún administrador");
        }
    }

    @Override
    public void modificarUsuarioEmpleado(int idUsuario, Empleado empleado){
        String sql = "UPDATE empleado SET codigoEmpleado = ?, dni = ?, nombres = ?, apellidos = ?, correo = ?," +
                "contrasenhaHash = ?, telefono = ?, fechaRegistro = ?, estado = ?, cargo = ?, licencia = ?, turno = ?" +
                "WHERE idUsuario = ?";
        java.sql.Date fechaRegistro = new java.sql.Date(empleado.getFechaRegistro().getTime());
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1,empleado.getCodigoEmpleado());
            stmt.setString(2,empleado.getDni());
            stmt.setString(3,empleado.getNombres());
            stmt.setString(4,empleado.getApellidos());
            stmt.setString(5,empleado.getCorreo());
            stmt.setString(6,empleado.getContrasenhaHash());
            stmt.setString(7,empleado.getTelefono());
            stmt.setDate(8,fechaRegistro);
            stmt.setInt(9,empleado.getEstado() ? 1:0);
            stmt.setInt(10,empleado.getCargo().ordinal());
            stmt.setString(11,empleado.getLicencia());
            stmt.setInt(12,empleado.getTurno().ordinal());
            stmt.setInt(13,idUsuario);
            int filasAfectadas = stmt.executeUpdate();
            if(filasAfectadas == 1){
                System.out.println("Se ha modificado un empleado");
            }else{
                System.out.println("Se han modificado" + filasAfectadas + "empleados");
            }
        }catch(SQLException e){
            System.out.println("No se ha podido modificar ningún empleado");
        }
    }

    @Override
    public void modificarUsuarioAdministrador(int idUsuario, Administrador administrador){
        String sql = "UPDATE empleado SET codigoEmpleado = ?, dni = ?, nombres = ?, apellidos = ?, correo = ?," +
                "contrasenhaHash = ?, telefono = ?, fechaRegistro = ?, estado = ?, cargo = ?, nivelDeAcceso = ?, isManager = ?" +
                "WHERE idUsuario = ?";
        java.sql.Date fechaRegistro = new java.sql.Date(administrador.getFechaRegistro().getTime());
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1,administrador.getCodigoEmpleado());
            stmt.setString(2,administrador.getDni());
            stmt.setString(3,administrador.getNombres());
            stmt.setString(4,administrador.getApellidos());
            stmt.setString(5,administrador.getCorreo());
            stmt.setString(6,administrador.getContrasenhaHash());
            stmt.setString(7,administrador.getTelefono());
            stmt.setDate(8,fechaRegistro);
            stmt.setInt(9,administrador.getEstado() ? 1:0);
            stmt.setInt(10,administrador.getCargo().ordinal());
            stmt.setString(11,administrador.getNivelDeAcceso());
            stmt.setInt(12,administrador.getIsManager()?1:0);
            stmt.setInt(13,idUsuario);
            int filasAfectadas = stmt.executeUpdate();
            if(filasAfectadas == 1){
                System.out.println("Se ha modificado un administrador");
            }else{
                System.out.println("Se han modificado" + filasAfectadas + "administradores");
            }
        }catch(SQLException e){
            System.out.println("No se ha podido modificar ningún administrador");
        }
    }

    //PRIMERO DEBEMOS SABER QUE TIPO DE USUARIO ES
    @Override
    public Usuario recuperarUsuario(int idUsuario){
        Usuario aux;
        String sql = "SELECT * FROM empleado WHERE idUsuario = ?" +
                "UNION SELECT * FROM administrador WHERE idUsuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idUsuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String cargo = rs.getString("cargo");
                if("ADMINISTRADOR".equals(cargo)){
                    aux = new Administrador();
                    aux.setIdUsuario(rs.getInt("idUsuario"));
                    aux.setCodigoEmpleado(rs.getString("codigoEmpleado"));
                    aux.setDni(rs.getString("dni"));
                    aux.setNombres(rs.getString("nombres"));
                    aux.setApellidos(rs.getString("apellidos"));
                    aux.setCorreo(rs.getString("correo"));
                    aux.setContrasenhaHash(rs.getString("contrasenhaHash"));
                    aux.setTelefono(rs.getString("telefono"));
                    aux.setFechaRegistro(rs.getDate("fechaRegistro"));
                    aux.setEstado(rs.getBoolean("estado"));
                    String cargoString = rs.getString("cargo");
                    Cargo cargoAux = Cargo.valueOf(cargoString.toUpperCase());
                    aux.setCargo(cargoAux);
                    ((Administrador)aux).setNivelDeAcceso(rs.getString("nivelDeAcceso"));
                    ((Administrador)aux).setIsManager(rs.getInt("isManager")==1);
                }else{
                    //Es empleado
                    aux = new Empleado();
                    aux.setIdUsuario(rs.getInt("idUsuario"));
                    aux.setCodigoEmpleado(rs.getString("codigoEmpleado"));
                    aux.setDni(rs.getString("dni"));
                    aux.setNombres(rs.getString("nombres"));
                    aux.setApellidos(rs.getString("apellidos"));
                    aux.setCorreo(rs.getString("correo"));
                    aux.setContrasenhaHash(rs.getString("contrasenhaHash"));
                    aux.setTelefono(rs.getString("telefono"));
                    aux.setFechaRegistro(rs.getDate("fechaRegistro"));
                    aux.setEstado(rs.getBoolean("estado"));
                    String cargoString = rs.getString("cargo");
                    Cargo cargoAux = Cargo.valueOf(cargoString.toUpperCase());
                    aux.setCargo(cargoAux);
                    ((Empleado)aux).setLicencia(rs.getString("licencia"));
                    String turnoString = rs.getString("turno");
                    Turno turnoAux = Turno.valueOf(turnoString.toUpperCase());
                    ((Empleado) aux).setTurno(turnoAux);
                }
            } else {
                System.out.println("No se encontró el usuario con id: " + idUsuario);
                return null;
            }
            rs.close();
            return aux;
        } catch (SQLException e) {
            System.out.println("Error al buscar el usuario con id" + idUsuario);
        }
        return null;
    }

    public String recuperarContrasenha(int idUsuario){
        return null;
    }

    public Cargo consultarCargo(int idUsuario){
        return null;
    }

    public void actualizarEstadoAdministrador(int idUsuario, boolean estado){

    }
}
