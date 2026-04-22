package Transporte;

import InformacionPedido.Transporte;
import java.util.List;



public interface TransporteDAO {

    void insertar(Transporte transporte);


    void modificar(Transporte transporte);


    void eliminar(String placa);


    Transporte obtenerPorId(String placa);


    List<Transporte> listarTodos();
}
