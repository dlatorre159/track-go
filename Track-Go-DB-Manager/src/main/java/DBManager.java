import java.sql.Connection;
import java.sql.SQLException;


public abstract class DBManager {
    protected String host;
    protected int puerto;
    protected String esquema;
    protected String usuario;
    protected String password;

    protected DBManager() {}

    protected DBManager(String host, int puerto, String esquema, String password, String usuario){
        try{
            this.host = host;
            this.puerto = puerto;
            this.esquema = esquema;
            this.usuario = usuario;
            this.password = password;
        } catch (RuntimeException e) {
            System.out.println("Error al conectar a la base de datos ");
            throw new RuntimeException(e);
        }
    }

    public abstract Connection getConnection() throws SQLException, ClassNotFoundException;
}
