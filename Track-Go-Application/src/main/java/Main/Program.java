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

        String ruc="123456789";

        try{
            //CREATE
            Empresa empresa=new Empresa("EmpresitaFantasma",
                    ruc,
                    "Av. Narnia 12",
                    "Ola",
                    new Date());

            empresaDAO.AgregarEmpresa(empresa);
            System.out.println("Se creo la empresita: "+empresa.getRuc());

            //READ
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

        } finally {
            empresaDAO.EliminarEmpresa(ruc);

            System.out.println("Limpieza Completada");
        }

        TransporteDAO transporteDAO = TransporteDAOImpl.getInstance();
        String placa= "TRR-001";
        try{
            //CREATE
            Transporte t = new Transporte("TRR-001", "Terreneitor", "Fotorama", "traccion 4x4 y dos turbo motores");

            transporteDAO.AgregarTransporte(t);
            System.out.println("Terrenitor en la banca");

            //READ
            Transporte tBD = transporteDAO.buscarDetallesTransporte(placa);

            if (tBD != null) {
                System.out.println("Transporte encontrado:");
                System.out.println(tBD);
            } else {
                System.out.println("No existe el transporte");
            }

            //UPDATE

            if (tBD != null) {
                tBD.setMarca("Mega Force");
                tBD.setModelo("Ultra 4x4 Pasa Todo");

                transporteDAO.ModificarTransporte(placa, tBD);
                System.out.println("Transporte actualizado: ahora supera cualquier terreno conocido");
            }

            //READ LUEGO DE UPDATE
            Transporte actualizado = transporteDAO.buscarDetallesTransporte(placa);
            System.out.println("Después del update:");
            System.out.println(actualizado);

            //Listamos todos los transportes
            System.out.println("Lista de transportes:");
            ArrayList<Transporte> lista = transporteDAO.obtenerTodosLosTransportes();

            for (Transporte tr : lista) {
                System.out.println(tr);
            }
        }
        finally {
            transporteDAO.EliminarTransporte(placa);

            DBManager.closeConnection();
            System.out.println("Limpieza completada");
        }

    }
}
