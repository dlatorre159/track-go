
import Manager.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;







public class TestDBManager {

    public static void prueba1(){
        try{
            DBManager.getInstance();
            try(Connection connection = DBManager.getConnection();
                PreparedStatement stmt = connection.prepareStatement("SELECT 1");
            ){
                ResultSet rs = stmt.executeQuery();
                while(rs.next()){
                    System.out.println(rs.getInt(1));
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public static void main(String[] args){
        prueba1();
    }
}
