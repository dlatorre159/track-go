package Empresa;
import InformacionEmpresa.Empresa;

public interface EmpresaDAO {
    void AgregarEmpresa(Empresa empresa);
    void EliminarEmpresa(Empresa empresa);
    void ModificarEmpresa(String ruc);
}
