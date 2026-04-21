package Pedido;

import InformacionPedido.EstadoPedido;
import InformacionPedido.Pedido;

import java.util.List;
import java.util.Date;

public interface PedidoDAO {
    void insertar(Pedido pedido);
    void modificar(Pedido pedido);
    void eliminar(String idPedido);
    Pedido obtenerPorId(String idPedido);
    List<Pedido> listarTodos();

    // Consultas extra del negocio
    List<Pedido> listarPorFecha(Date fecha);
    List<Pedido> listarPorRangoFechas(Date inicio, Date fin);
    List<Pedido> listarPorEstado(EstadoPedido estado);
    List<Pedido> listarPorUsuario(int idUsuario);
}