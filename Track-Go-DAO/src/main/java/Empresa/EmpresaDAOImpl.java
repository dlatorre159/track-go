package Empresa;
import Manager.DBManager;

import java.sql.*;

import InformacionEmpresa.Empresa;

public class EmpresaDAOImpl implements EmpresaDAO {
    private static EmpresaDAOImpl instance;
    private static Connection connection;

    private EmpresaDAOImpl(){
        connection = DBManager.getInstance().getConnection();
    }

    public static EmpresaDAOImpl getInstance(){
        if(instance == null) {
            synchronized (EmpresaDAOImpl.class) {
                instance = new EmpresaDAOImpl();
            }
        }
        return instance;
    }

    @Override
    public void AgregarEmpresa(Empresa empresa){
        String sql = "INSERT INTO empresa (nombre,RUC, direccion,sector, fechaFundacion)" +
                "VALUES (?,?,?,?,?)";
        java.sql.Date fechaFundacionSQL = new java.sql.Date(empresa.getFechaFundacion().getTime());
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            //stmt.setInt(1,empresa.getId());
            stmt.setString(1,empresa.getNombre());
            stmt.setString(2, empresa.getRuc());
            stmt.setString(3,empresa.getDireccion());
            stmt.setString(4,empresa.getSector());
            stmt.setDate(5,fechaFundacionSQL);
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
        String sql = "UPDATE empresa SET nombre = ?, RUC = ?, direccion = ?, sector = ?, fechaFundacion = ? " +
                "WHERE RUC = ?";
        java.sql.Date fechaFundacionSQL = new java.sql.Date(empresa.getFechaFundacion().getTime());
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1,empresa.getNombre());
            stmt.setString(2, empresa.getRuc());
            stmt.setString(3,empresa.getDireccion());
            stmt.setString(4,empresa.getSector());
            stmt.setDate(5,fechaFundacionSQL);

            stmt.setString(6,ruc);
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

    @Override
    public Empresa consultarEmpresa(String ruc){
        String sql = "SELECT idEmpresa, nombre, ruc, direccion, sector, fechaFundacion FROM empresa WHERE ruc = ? LIMIT 1";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            //PRUEBAAA
            stmt.setString(1, ruc);

            ResultSet rs = stmt.executeQuery();
            if(!rs.next()) {
                System.out.println("No existe la empresa indicada con el ruc: " + ruc);
                return null;
            }

            int idEmpresa = rs.getInt(1);
            String nombre = rs.getString(2);
            String rucDb = rs.getString(3);
            String direccion = rs.getString(4);
            String sector = rs.getString(5);
            Date date = rs.getDate(6);

            Empresa empresa = new Empresa(nombre, rucDb,direccion,sector,date);
            empresa.setId(idEmpresa);

            return empresa;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
