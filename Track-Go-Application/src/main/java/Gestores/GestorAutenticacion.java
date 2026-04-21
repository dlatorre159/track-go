package Gestores;

import InformacionUsuario.Administrador;
import InformacionUsuario.Cargo;
import InformacionUsuario.Empleado;
import InformacionUsuario.Usuario;

public class GestorAutenticacion {
    public static Usuario login(int userId, String password) {
    /**
        Usuario usuarioPreIdentificado;

        String cargoRecuperado = UsuarioDAO.consultarCargo(userId);


        if(cargoRecuperado.equals(Cargo.ADMINISTRADOR.name())){
            usuarioPreIdentificado = new Administrador();
        } else {
            usuarioPreIdentificado = new Empleado();
        }

        return usuarioPreIdentificado;
*/
    return null;
    }

    public static Usuario loginAsEmployee(int userId) {
  /**
        Usuario usuarioRequerido;
        String cargoRecuperado = UsuarioDAO.consultarCargo(userId);
        if(cargoRecuperado.equals(Cargo.ADMINISTRADOR.name())){
            System.out.println("No puedes acceder a la sesion de un administrador");
            return null;
        } else {
            usuarioRequerido = new Empleado();
        }

        return usuarioRequerido;
  */
  return null;
    }


}
