package Empresa;
import Manager.DBManager;
import java.sql.Connection;
import InformacionEmpresa.Empresa;
import java.sql.Date;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class EmpresaDAOImpl implements EmpresaDAO {
    private static EmpresaDAOImpl instance;
    private static Connection connection;

    private EmpresaDAOImpl(){
        connection = DBManager.getConnection();
    }

    public EmpresaDAOImpl getIntance(){
        if(instance == null) {
            synchronized (EmpresaDAOImpl.class) {
                instance = new EmpresaDAOImpl();
            }
        }
        return instance;
    }

    @Override
    public void AgregarEmpresa(Empresa empresa){
        String sql = "INSERT INTO empresa (idEmpresa,nombre,RUC, direccion,sector, fechaFundacion)" +
                "VALUES (?,?,?,?,?,?)";
        java.sql.Date fechaFundacionSQL = new java.sql.Date(empresa.getFechaFundacion().getTime());
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1,empresa.getId());
            stmt.setString(2,empresa.getNombre());
            stmt.setString(3, empresa.getRuc());
            stmt.setString(4,empresa.getDireccion());
            stmt.setString(5,empresa.getSector());
            stmt.setDate(6,fechaFundacionSQL);
            int filasAfectadas = stmt.executeUpdate();
            if(filasAfectadas == 1){
                System.out.println("Se ha agregado una empresa");
            }else{
                System.out.println("Se han agregado" + filasAfectadas + "empresas");
            }
        }catch(SQLException e){
            System.out.println("No se ha podido agregar ninguna empresa");
        }
    }

    @Override
    public void EliminarEmpresa(String ruc){
        String sql = "DELETE FROM empresa WHERE RUC = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1,ruc);
            int filasAfectadas = stmt.executeUpdate();
            //Dado que el RUC es unico, tendria sentido que solo se elimine una sola fila
            //Pero mantendremos el mensaje para verificar en las pruebas
            if(filasAfectadas == 1){
                System.out.println("Se ha elimnado la empresa con RUC N°" + ruc);
            }else{
                System.out.println("Se han eliminado las empresas con RUC N°" + ruc);
            }
        }catch(SQLException e){
            System.out.println("No se encontró ninguna empresa con RUC N°" + ruc);
        }
    }

    @Override
    public void ModificarEmpresa(String ruc, Empresa empresa){
        String sql = "UPDATE empresa SET idEmpresa = ?, nombre = ?, RUC = ?, direccion = ?, sector = ?, fechaFundacion = ? " +
                "WHERE RUC = ?";
        java.sql.Date fechaFundacionSQL = new java.sql.Date(empresa.getFechaFundacion().getTime());
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1,empresa.getId());
            stmt.setString(2,empresa.getNombre());
            stmt.setString(3, empresa.getRuc());
            stmt.setString(4,empresa.getDireccion());
            stmt.setString(5,empresa.getSector());
            stmt.setDate(6,fechaFundacionSQL);
            stmt.setString(7,ruc);
            int filasAfectadas = stmt.executeUpdate();
            if(filasAfectadas == 1){
                System.out.println("Se ha modificado la empresa con RUC N°" + ruc);
            }else{
                System.out.println("Se han modificado las empresas con RUC N°" + ruc);
            }
        }catch(SQLException e){
            System.out.println("No se encontró ninguna empresa con RUC N°" + ruc);
        }
    }
}
