package Transporte;
import InformacionPedido.Transporte;
import Manager.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransporteDAOImpl implements TransporteDAO {
    private static TransporteDAOImpl instance;
    private static Connection connection;

    private TransporteDAOImpl() {
        connection = DBManager.getInstance().getConnection();
    }

    public static TransporteDAOImpl getInstance() {
        if (instance == null) {
            synchronized (TransporteDAOImpl.class) {
                instance = new TransporteDAOImpl();
            }
        }
        return instance;
    }

    @Override
    public void AgregarTransporte(Transporte transporte) {
        String sql = "INSERT INTO transporte (placa,tipo,marca, modelo) VALUES (?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, transporte.getPlaca());
            stmt.setString(2, transporte.getTipo());
            stmt.setString(3, transporte.getMarca());
            stmt.setString(4, transporte.getModelo());
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 1) {
                System.out.println("Se ha agregado un transporte");
            } else {
                System.out.println("Se han agregado" + filasAfectadas + "transportes");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("No se ha podido agregar ningún transporte");
        }
    }

    @Override
    public void ModificarTransporte(String placa, Transporte transporte) {
        String sql = "UPDATE transporte SET placa = ?, tipo = ?, marca = ?, modelo = ? " +
                "WHERE placa = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, transporte.getPlaca());
            stmt.setString(2, transporte.getTipo());
            stmt.setString(3, transporte.getMarca());
            stmt.setString(4, transporte.getModelo());
            stmt.setString(5, placa);
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 1) {
                System.out.println("Se ha modificado un transporte");
            } else {
                System.out.println("Se han modificado" + filasAfectadas + "transportes");
            }
        } catch (SQLException e) {
            System.out.println("No se ha podido modificar ningún transporte");
        }
    }

    @Override
    public void EliminarTransporte(String placa) {
        String sql = "DELETE FROM transporte WHERE placa = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, placa);
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 1) {
                System.out.println("Se ha eliminado un transporte");
            } else {
                System.out.println("Se han eliminado" + filasAfectadas + "transportes");
            }
        } catch (SQLException e) {
            System.out.println("No se ha podido eliminar ningún transporte");
        }
    }

    @Override
    public Transporte buscarDetallesTransporte(String placa) {
        Transporte aux = new Transporte();
        String sql = "SELECT * FROM transporte WHERE placa = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, placa);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                aux.setPlaca(placa);
                aux.setTipo(rs.getString("tipo"));
                aux.setMarca(rs.getString("marca"));
                aux.setModelo(rs.getString("modelo"));

            } else {
                System.out.println("No se encontró el transporte con placa: " + placa);
                return null;
            }
            rs.close();
            return aux;
        } catch (SQLException e) {
            System.out.println("Error al buscar el transporte con placa" + placa);
        }
        return null;
    }

    @Override
    public ArrayList<Transporte> obtenerTodosLosTransportes(){
        ArrayList<Transporte> transportesAux = new ArrayList<>();
        String sql = "SELECT * FROM transporte";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Transporte aux = new Transporte();
                aux.setPlaca(rs.getString("placa"));
                aux.setTipo(rs.getString("tipo"));
                aux.setMarca(rs.getString("marca"));
                aux.setModelo(rs.getString("modelo"));
                transportesAux.add(aux);

            }
            rs.close();
            return transportesAux;
        } catch (SQLException e) {
            System.out.println("Error al buscar los transportes");
        }
        return null;
    }
}
