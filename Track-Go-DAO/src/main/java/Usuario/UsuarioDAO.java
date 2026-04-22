package Usuario;

import InformacionUsuario.Administrador;
import InformacionUsuario.Cargo;
import InformacionUsuario.Empleado;
import InformacionUsuario.Usuario;

public interface UsuarioDAO {
    void agregarUsuarioEmpleado(Empleado empleado);
    void agregarUsuarioAdministrador(Administrador administrador);
    void eliminarUsuarioEmpleado(int idUsuario);
    void eliminarUsuarioAdministrador(int idUsuario);
    void modificarUsuarioEmpleado(int idUsuario, Empleado empleado);
    void modificarUsuarioAdministrador(int idUsuario, Administrador administrador);
    Usuario recuperarUsuario(int idUsuario);
    String recuperarContrasenha(int idUsuario);
    Cargo consultarCargo(int idUsuario);
    void actualizarEstadoAdministrador(int idUsuario, boolean estado);
}