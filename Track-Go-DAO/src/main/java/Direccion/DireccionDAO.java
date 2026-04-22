package Direccion;

public interface DireccionDAO {
    int insertOrGetDireccion(String departamento, String provincia, String distrito, String codPostal,
                                    String calleNumero, String referencia);
}
