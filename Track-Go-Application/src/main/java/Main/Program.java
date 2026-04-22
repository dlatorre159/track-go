package Main;

import Empresa.EmpresaDAO;
import Empresa.EmpresaDAOImpl;
import InformacionEmpresa.Empresa;
import InformacionPedido.*;
import Manager.DBManager;
import Transporte.TransporteDAO;
import Transporte.TransporteDAOImpl;
import Pedido.PedidoDAO;
import Pedido.PedidoDAOImpl;
import java.util.Date;
import java.util.ArrayList;

public class Program {
    public static void main(String[] args) {

        DBManager.getInstance();

        EmpresaDAO empresaDAO = EmpresaDAOImpl.getInstance();
        TransporteDAO transporteDAO = TransporteDAOImpl.getInstance();
        PedidoDAO pedidoDAO = PedidoDAOImpl.getInstance();
        String idPedido= null;
        String ruc="123456789";
        String placa1 = "TRR-001";
        String placa2 = "TRR-002";
        String placa3 = "TRR-003";

        try{
            // CRUD EMPRESA
            System.out.println("\n=== EMPRESA ===");
            //CREATE
            Empresa empresa=new Empresa("EmpresitaFantasma",
                    ruc,
                    "Av. Narnia 12",
                    "Ola",
                    new Date());

            empresaDAO.AgregarEmpresa(empresa);
            System.out.println("Se creo la empresita: "+empresa.getRuc());

            //READ
            System.out.println("Empresa leída:");
            Empresa empresaBD=empresaDAO.consultarEmpresa(ruc);
            System.out.println(empresaBD);

            //UPDATE
            empresa.setNombre("Emprestia Fantasmona cambiada");
            empresa.setDireccion("Av. Nueva vida 2000");

            empresaDAO.ModificarEmpresa(ruc,empresa);
            System.out.println("Empresa actualizada");
            //OTRO READ
            System.out.println("Empresa después del update:");
            System.out.println(empresaDAO.consultarEmpresa(ruc));


            // CRUD TRANSPORTE
            System.out.println("\n=== TRANSPORTE ===");
            Transporte t1 = new Transporte(placa1, "Terreneitor", "Fotorama", "4x4 Turbo");
            System.out.println("Transporte Creado: " + placa1);
            Transporte t2 = new Transporte(placa2, "Camión Urbano", "Volvo", "FH16");
            System.out.println("Transporte Creado: " + placa1);
            Transporte t3 = new Transporte(placa3, "Mini Van", "Toyota", "Hiace");
            System.out.println("Transporte Creado: " + placa1);

            //CREATE
            transporteDAO.AgregarTransporte(t1);
            transporteDAO.AgregarTransporte(t2);
            transporteDAO.AgregarTransporte(t3);


            //READ uno
            System.out.println("\nBuscamos Tansporte con placa (" + placa2 + "):");
            Transporte tBD = transporteDAO.buscarDetallesTransporte(placa2);

            if (tBD != null) {
                System.out.println("Transporte encontrado:");
                System.out.println(tBD);
            } else {
                System.out.println("No existe el transporte");
            }

            //UPDATE

            if (tBD != null) {
                tBD.setMarca("Volvo Actualizado");
                tBD.setModelo("FH16 XL");

                transporteDAO.ModificarTransporte(placa2, tBD);
                System.out.println("Transporte actualizado: " + placa2);
            }

            //READ LUEGO DE UPDATE

            System.out.println("Después del update:");
            System.out.println(transporteDAO.buscarDetallesTransporte(placa2));


            //Listamos todos los transportes
            System.out.println("Lista de transportes:");
            ArrayList<Transporte> lista = transporteDAO.obtenerTodosLosTransportes();

            for (Transporte tr : lista) {
                System.out.println(tr);
            }

            // Borramos uno
            transporteDAO.EliminarTransporte(placa1);
            System.out.println("\nSe eliminó: " + placa1);

            // Lista sin el transporte Deleteado
            System.out.println("\nLista después de eliminar:");
            for (Transporte tr : transporteDAO.obtenerTodosLosTransportes()) {
                System.out.println(tr);
            }

            //PEDIDO

            System.out.println("\n=== PEDIDO ===");
            Direccion direccion = new Direccion(
                    "Lima",
                    "Lima",
                    "San Miguel",
                    "15087",
                    "Av. La Marina 123",
                    "Frente a la UNI"
            );
            Empresa empresaOrigen = empresaDAO.consultarEmpresa(ruc);

            Pedido pedido = new Pedido(
                    "Juan Perez",
                    25.50,
                    EstadoPedido.EN_AGENCIA,
                    1,
                    direccion,
                    empresaOrigen
            );

            //CREATE

            pedidoDAO.insertarPedido(pedido);
            idPedido = pedido.getIdPedido();
            System.out.println("Pedido creado: " + pedido.getIdPedido());

            // READ
            Pedido pedidoBD = pedidoDAO.obtenerPorId(pedido.getIdPedido());
            System.out.println("Pedido leído:");
            System.out.println(pedidoBD);

            // UPDATE sin transporte
            pedido.setDestinatario("Juan Perez Actualizado");
            pedido.setTarifaEnvio(35.00);
            pedido.setEstado(EstadoPedido.SALIDA_A_RUTA);

            pedidoDAO.modificarPedido(pedido);
            System.out.println("Pedido actualizado sin transporte");

            // ASIGNAR TRANSPORTE
            Transporte transporteAsignado = transporteDAO.buscarDetallesTransporte(placa2);
            pedido.setDetalleTransporte(transporteAsignado);

            // UPDATE con transporte
            pedidoDAO.modificarPedido(pedido);
            System.out.println("Pedido actualizado con transporte");

            // READ FINAL
            System.out.println("Pedido final:");
            System.out.println(pedidoDAO.obtenerPorId(pedido.getIdPedido()));

        } finally {
            empresaDAO.EliminarEmpresa(ruc);
            transporteDAO.EliminarTransporte("TRR-002");
            transporteDAO.EliminarTransporte("TRR-003");
            if (idPedido != null) {
                pedidoDAO.eliminarPedido(idPedido);
            }
            System.out.println("\nLimpieza final Completada");
            DBManager.closeConnection();

        }

    }
}
