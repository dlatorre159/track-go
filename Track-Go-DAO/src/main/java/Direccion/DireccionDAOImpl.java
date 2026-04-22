package Direccion;

import Manager.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DireccionDAOImpl implements DireccionDAO{
    private static DireccionDAOImpl instance;
    private static Connection connection;

    private DireccionDAOImpl(){
        connection = DBManager.getInstance().getConnection();
    }

    public static DireccionDAOImpl getInstance(){
        if(instance == null) {
            synchronized (DireccionDAOImpl.class) {
                instance = new DireccionDAOImpl();
            }
        }
        return instance;
    }

    @Override
    public int insertOrGetDireccion(String departamento, String provincia, String distrito, String codPostal,
                                    String calleNumero, String referencia){
        String sqlDireccionExiste = "SELECT idDireccion FROM direccion WHERE codPostal = ? AND calleNumero = ? LIMIT 1";
        try(PreparedStatement stmt = connection.prepareStatement(sqlDireccionExiste)){
            stmt.setString(1, codPostal.trim());
            stmt.setString(2, calleNumero.trim().toLowerCase());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idDireccion");
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la direccion existente",e);
        }

        String insertNewDireccion = """
            INSERT INTO direccion
            (departamento, provincia, distrito, codPostal, calleNumero, referencia)
            VALUES (?, ?, ?, ?, ?, ?)
        """;


        try(PreparedStatement stmt2 = connection.prepareStatement(insertNewDireccion, PreparedStatement.RETURN_GENERATED_KEYS)){
            stmt2.setString(1, departamento);
            stmt2.setString(2, provincia);
            stmt2.setString(3, distrito);
            stmt2.setString(4, codPostal);
            stmt2.setString(5, calleNumero);
            stmt2.setString(6, referencia);

            int affectedRows = stmt2.executeUpdate();

            if(affectedRows == 0){
                throw new SQLException("No se puede insertar la nueva direccion");
            }

            try(ResultSet generatedKeys = stmt2.getGeneratedKeys()){
                if(generatedKeys.next()){
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("No se obtuvo el id generado de la direccion");
                }
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
