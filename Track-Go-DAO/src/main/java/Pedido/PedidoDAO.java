package Pedido;

import InformacionPedido.EstadoPedido;
import InformacionPedido.Pedido;

import java.util.ArrayList;
import java.util.Date;

public interface PedidoDAO {
    void insertarPedido (Pedido pedido);
    void eliminarPedido (String idPedido);
    void modificarPedido (Pedido pedido);
    Pedido obtenerPorId(String idPedido);
    ArrayList<Pedido> listarTodos();
    ArrayList<Pedido> listarPedidosPorFecha(Date fecha);
    ArrayList<Pedido> ordenarPedidosPorFecha();
    ArrayList<Pedido> listarPorRangoFechas(Date inicio, Date fin);
    ArrayList<Pedido> listarPorEstado(EstadoPedido estado);
    ArrayList<Pedido> listarPorUsuario(int idUsuario);
}