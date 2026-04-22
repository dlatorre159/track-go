package Pedido;

import InformacionPedido.EstadoPedido;
import InformacionPedido.Pedido;

import java.util.ArrayList;
import java.util.Date;

public interface PedidoDAO {
    void insertar(Pedido pedido);
    void modificar(Pedido pedido);
    void eliminar(String idPedido);
    Pedido obtenerPorId(String idPedido);
    ArrayList<Pedido> listarTodos();

    // Consultas extra del negocio
    ArrayList<Pedido> listarPorFecha(Date fecha);
    ArrayList<Pedido> listarPorRangoFechas(Date inicio, Date fin);
    ArrayList<Pedido> listarPorEstado(EstadoPedido estado);
    ArrayList<Pedido> listarPorUsuario(int idUsuario);
}