package Empresa;
import InformacionEmpresa.Empresa;

public interface EmpresaDAO {
    void AgregarEmpresa(Empresa empresa);
    void EliminarEmpresa(String ruc);
    void ModificarEmpresa(String ruc, Empresa empresa);
}
