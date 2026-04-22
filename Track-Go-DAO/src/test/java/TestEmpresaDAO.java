import InformacionEmpresa.Empresa;
import Empresa.EmpresaDAOImpl;

import java.util.Date;

public class TestEmpresaDAO {

    public static void main(String[] args){
        try{
            Empresa empresa = new Empresa("Mango Academy 3", "20202025", "Tangamandapio", "Test", new Date());
            EmpresaDAOImpl.getInstance().AgregarEmpresa(empresa);


        } catch(Exception e){
            e.printStackTrace();
        }

    }
}
