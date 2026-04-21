package InformacionPedido;

public class Direccion {
    private String departamento;
    private String provincia;
    private String distrito;
    private String codPostal;
    private String calleNumero;
    private String referencia;

    public Direccion(String departamento, String provincia, String distrito, String codPostal,
                     String calleNumero, String referencia){
        this.departamento = departamento;
        this.provincia = provincia;
        this.distrito = distrito;
        this.codPostal = codPostal;
        this.calleNumero = calleNumero;
        this.referencia = referencia;
    }

    public String getDepartamento(){
        return departamento;
    }

    public void setDepartamento(String departamento){
        this.departamento = departamento;
    }

    public String getProvincia(){
        return provincia;
    }

    public void setProvincia(String provincia){
        this.provincia = provincia;
    }

    public String getDistrito(){
        return distrito;
    }

    public void setDistrito(String distrito){
        this.distrito = distrito;
    }

    public String getCodPostal(){
        return codPostal;
    }

    public void setCodPostal(String codPostal){
        this.codPostal = codPostal;
    }

    public String getCalleNumero(){
        return calleNumero;
    }

    public void setCalleNumero(String calleNumero){
        this.calleNumero = calleNumero;
    }

    public String getReferencia(){
        return referencia;
    }
    
    public void setReferencia(String referencia){
        this.referencia = referencia;
    }
}
