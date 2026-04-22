package Transporte;

import InformacionPedido.Transporte;
import java.util.ArrayList;

public interface TransporteDAO {
    void AgregarTransporte(Transporte transporte);
    void ModificarTransporte(String placa, Transporte transporte);
    void EliminarTransporte(String placa);
    Transporte buscarDetallesTransporte(String placa);
    ArrayList<Transporte> obtenerTodosLosTransportes();
}
