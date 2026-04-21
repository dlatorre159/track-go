package InformacionPedido;

import InformacionEmpresa.Empresa;
import Interfaces.Actualizable;
import Interfaces.Consultable;
import Interfaces.Trazable;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.ArrayList;

public class Pedido implements Actualizable, Consultable, Trazable{
    private static int CONTADOR = 0;
    private static double TARIFA_ESTANDAR_POR_PRODUCTO = 5.20;

    private String idPedido;
    private String destinatario;
    private Date fechaCreacion;
    private Date fechaActualizacion;
    private double tarifaEnvio;
    private EstadoPedido estado;
    private ArrayList<DetalleDePedido> detalleDePedido;
    private Transporte detalleTransporte;
    private int idUsuario;
    private Direccion direccion;
    private ArrayList<HistorialDePedido> historialDePedido;
    private Empresa empresaDeOrigen;

    public Pedido(String destinatario, double tarifaEnvio,
                  EstadoPedido estado, int idUsuario,
                  Direccion direccion, Empresa empresaDeOrigen){
        this.idPedido = "PD-" + ++CONTADOR;
        this.estado = EstadoPedido.EN_AGENCIA;
        this.fechaCreacion = new Date();
        this.fechaActualizacion = new Date();
        this.detalleTransporte = null;

        this.destinatario = destinatario;
        this.tarifaEnvio = tarifaEnvio;
        this.idUsuario = idUsuario;
        this.direccion = direccion;
        this.empresaDeOrigen = empresaDeOrigen;

        this.detalleDePedido = new ArrayList<>();
        this.historialDePedido = new ArrayList<>();
    }

     // Constructor vacío
    public Pedido() {
        this.detalleDePedido = new ArrayList<>();
        this.historialDePedido = new ArrayList<>();
        this.detalleTransporte = null;
    }

    public String getIdPedido(){
        return this.idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getDestinatario(){
        return destinatario;
    }

    public Date getFechaCreacion(){
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public double getTarifaEnvio(){
        return tarifaEnvio;
    }

    public void setEstado(EstadoPedido estado){
        this.estado = estado;
    }

    public EstadoPedido getEstado(){
        return estado;
    }

    public ArrayList<DetalleDePedido> getDetalleDePedido(){
        return detalleDePedido;
    }

    public void setDetalleDePedido(ArrayList<DetalleDePedido> detalleDePedido) {
        this.detalleDePedido = detalleDePedido;
    }

    public Transporte getDetalleTransporte(){
        return detalleTransporte;
    }

    public void setDetalleTransporte(Transporte detalleTransporte){
        this.detalleTransporte = detalleTransporte;
    }

    public int getIdUsuario(){
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario){
        this.idUsuario = idUsuario;
    }

    public void setDireccion(Direccion direccion){
        this.direccion = direccion;
    }

    public Direccion getDireccion(){
        return this.direccion;
    }

    public ArrayList<HistorialDePedido> getHistorialDePedido(){
        return historialDePedido;
    }

    public void setHistorialDePedido(ArrayList<HistorialDePedido> historialDePedido) {
        this.historialDePedido = historialDePedido;
    }

    public void setEmpresaDeOrigen(Empresa empresaDeOrigen) {
        this.empresaDeOrigen = empresaDeOrigen;
    }

    public Empresa getEmpresaDeOrigen(){
        return empresaDeOrigen;
    }

    //Esta asignacion es la inicial, cuando recien se le asigna un transporte al pedido
    public void asignarInformacionTransporte(int usuarioId, Transporte transporte){
        this.idUsuario = usuarioId;
        this.detalleTransporte = transporte;
    }

    public void agregarDetalle(String descripcion, int cantidad){
        DetalleDePedido detalle = new DetalleDePedido(descripcion,cantidad);
        detalleDePedido.add(detalle);
    }

    public void eliminarDetalle(int index){
        if(index < 0){
            System.out.println("Indice no valido");
            return;
        }
        this.detalleDePedido.remove(index);
    }

    private void recalcularTarifaEnvio(){
        //AQUI FALTA BUSCAR UNA LOGICA REAL DE COSTO DE ENVIO
        this.tarifaEnvio = this.detalleDePedido.size() * TARIFA_ESTANDAR_POR_PRODUCTO;
    }

    public HistorialDePedido consultarTracking(){
        if(this.historialDePedido.isEmpty()){
            System.out.println("No hay historial para el pedido solicitado");
            return null;
        }else{
            return historialDePedido.getLast();
        }
    }

    @Override
    public void actualizarEstado(int estado){
        switch (estado){
            case 1:
                this.estado = EstadoPedido.EN_AGENCIA;
                break;
            case 2:
                this.estado = EstadoPedido.SALIDA_A_RUTA;
                break;
            case 3:
                this.estado = EstadoPedido.ENTREGADO;
                break;
            default:
                this.estado = EstadoPedido.SIN_REGISTRAR;
                break;
        }
    }

    @Override
    public void registrarEstado(){
        Duration duracion = Duration.between(this.fechaActualizacion.toInstant(), Instant.now());

        HistorialDePedido nuevoEstado = new HistorialDePedido(Timestamp.from(Instant.now()),this.idUsuario, this.estado, duracion, "ACTUALIZACION DE ESTADO");
        this.historialDePedido.add(nuevoEstado);

        this.fechaActualizacion = new Date();
    }

    @Override
    public int consultarEstadoActual(){
        HistorialDePedido historial = consultarTracking();
        EstadoPedido estadoActual = historial.getEstado();
        return estadoActual.ordinal();
    }
}
