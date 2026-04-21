package dbManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBManager{
    private static DBManager instance;
    private static Connection connection;
    private String host;
    private int port;
    private String esquema;
    private String usuario;
    private String password;


    private DBManager(){
        ResourceBundle db = ResourceBundle.getBundle("db");
        this.host = db.getString("db.host");
        this.port = Integer.parseInt(db.getString("db.puerto"));
        this.esquema = db.getString("db.esquema");
        this.usuario = db.getString("db.usuario");
        this.password = db.getString("db.password");

        String url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.esquema;

        try{
            connection = DriverManager.getConnection(url,this.usuario,this.password);
            if(connection != null && !connection.isClosed()){
                System.out.println("Conexión exitosa a la Base de Datos");
            }
        }catch (SQLException e){
            System.out.println("Error al conectar con la Base de Datos");
        }
    }

    public static DBManager getInstance(){
        if(instance == null){
            synchronized (DBManager.class){
                if(instance == null){
                    instance = new DBManager();
                }
            }
        }
        return instance;
    }

    public static Connection getConnection(){
        return connection;
    }

    public static void closeConnection(){
        try{
            if(connection != null && !connection.isClosed()){
                connection.close();
                System.out.println("Conexión cerrada");
            }
        }catch(SQLException e){
            System.out.println("Error al cerrar la conexión");
        }
    }
}