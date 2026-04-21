package InformacionPedido;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;

public class HistorialDePedido {
    private Timestamp instante;
    private int idUsuario;
    private EstadoPedido estado;
    private Duration duracion;
    private String observacionIncidencia;

    public HistorialDePedido(Timestamp instante, int idUsuario, EstadoPedido estado,
                      Duration duracion, String observacionIncidencia){
        this.instante = instante;
        this.idUsuario = idUsuario;
        this.estado = estado;
        this.duracion = duracion;
        this.observacionIncidencia = observacionIncidencia;
    }

    public Timestamp getInstante(){
        return instante;
    }

    public void setInstante(Timestamp instante){
        this.instante = instante;
    }

    public int getIdUsuario(){
        return idUsuario;
    }

    public void setCodigoEmpleado(int idUsuario){
        this.idUsuario = idUsuario;
    }

    public EstadoPedido getEstado(){
        return estado;
    }

    public void setEstado(EstadoPedido estado){
        this.estado = estado;
    }

    public Duration getDuracion(){
        return duracion;
    }

    public void setDuracion(Duration duracion){
        this.duracion = duracion;
    }
}
