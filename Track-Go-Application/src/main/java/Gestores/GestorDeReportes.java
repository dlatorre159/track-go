package Gestores;

import InformacionPedido.EstadoPedido;
import InformacionPedido.Pedido;
import Pedido.PedidoDAO;

import java.util.ArrayList;
import java.util.Date;


public class GestorDeReportes {

    private PedidoDAO pedidoDAO;

    public GestorDeReportes(PedidoDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
    }

    public String generarReportePedidosPorFecha(Date inicio, Date fin) {
        ArrayList<Pedido> pedidos = pedidoDAO.listarPorRangoFechas(inicio, fin);

        String reporte = "=== REPORTE DE PEDIDOS POR FECHA ===\n";
        reporte += "Fecha inicio: " + inicio + "\n";
        reporte += "Fecha fin: " + fin + "\n";
        reporte += "Total encontrados: " + pedidos.size() + "\n\n";

        for (Pedido pedido : pedidos) {
            reporte += formatearPedido(pedido);
        }

        return reporte;
    }

    public String generarReportePedidosPorEstado(EstadoPedido estado) {
        ArrayList<Pedido> pedidos = pedidoDAO.listarPorEstado(estado);

        String reporte = "=== REPORTE DE PEDIDOS POR ESTADO ===\n";
        reporte += "Estado: " + estado + "\n";
        reporte += "Total encontrados: " + pedidos.size() + "\n\n";

        for (Pedido pedido : pedidos) {
            reporte += formatearPedido(pedido);
        }

        return reporte;
    }

    public String generarReportePedidosPorTransportista(int idUsuario) {
        ArrayList<Pedido> pedidos = pedidoDAO.listarPorUsuario(idUsuario);

        String reporte = "=== REPORTE DE PEDIDOS POR TRANSPORTISTA ===\n";
        reporte += "ID Transportista: " + idUsuario + "\n";
        reporte += "Total encontrados: " + pedidos.size() + "\n\n";

        for (Pedido pedido : pedidos) {
            reporte += formatearPedido(pedido);
        }

        return reporte;
    }

    private String formatearPedido(Pedido pedido) {
        String texto = "";
        texto += "ID Pedido: " + pedido.getIdPedido() + "\n";
        texto += "Destinatario: " + pedido.getDestinatario() + "\n";
        texto += "Fecha de creación: " + pedido.getFechaCreacion() + "\n";
        texto += "Tarifa de envío: " + pedido.getTarifaEnvio() + "\n";
        texto += "Estado: " + pedido.getEstado() + "\n";
        texto += "------------------------------\n";
        return texto;
    }
}
