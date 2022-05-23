
/*
 * BD_BitAir: Operaciones de inserci�n, borrado y b�squeda en la tabla contactos
 */
package bbdd;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelos.*;

public class BD_BitAir extends BD_Conector {

    private static Statement s;
    private static ResultSet reg;

    public BD_BitAir(String bbdd) {
        super(bbdd);
    }

    // ---------------- ADRIANA -----------------
    public int buscarIdentViaje() {
        String cadena = "SELECT MAX(Ident_Viaje) FROM Viajes";
        try {
            int t = 0;
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(cadena);
            if (reg.next()) {
                t = reg.getInt(1);
            }			
            s.close();
            this.cerrar();
            return t;
        } catch (SQLException e) {
            return 0;
        }
    }

    public boolean añadir(Viaje vj) {
        String cadena = "INSERT INTO Viajes VALUES('" + vj.getIdent_Viaje() + "','" + vj.getOrigen() + "','" + vj.getDestino() + "','" + vj.getFechaIda() + "','" + vj.getFechaVuelta() + "','" + vj.getDescripción() + "','" + vj.getNumReservados() + "','" + vj.getNumTotal() + "','" + vj.getPrecio() + "')";

        try {
            this.abrir();
            s = c.createStatement();
            s.executeUpdate(cadena);
            s.close();
            this.cerrar();
            return true;
        } catch (SQLException e) {
            this.cerrar();
            return false;
        }

    }

    public int buscarViaje(int ident) {
        String cadena = "SELECT count(*) FROM Viajes WHERE Ident_Viaje=" + ident;
        try {
            int t = 0;
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(cadena);
            if (reg.next()) {
                t = reg.getInt(1);
            }

            s.close();
            this.cerrar();
            return 1;
        } catch (SQLException e) {

            return 0;

        }

    }

    public int buscarReservasAsociadas(int ident) {
        String cadena = "SELECT count(*) FROM Reservas WHERE Ident_Viajes=" + ident;
        try {
            String t = "";
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(cadena);
            if (reg.next()) {
                t = reg.getString(1);
            }

            s.close();
            this.cerrar();
            return 1;
        } catch (SQLException e) {

            return 0;

        }

    }

    public int borrarReservasAsociadas(int ident) {
        String cadena = "DELETE FROM Reservas WHERE Ident_Viaje=" + ident;

        try {
            this.abrir();
            s = c.createStatement();
            int filas = s.executeUpdate(cadena);
            s.close();
            this.cerrar();
            return filas;

        } catch (SQLException e) {
            this.cerrar();
            return -1;
        }
    }

    public int borrarViaje(int ident) {
        String cadena = "DELETE FROM Viajes WHERE Ident_Viaje=" + ident;

        try {
            this.abrir();
            s = c.createStatement();
            int filas = s.executeUpdate(cadena);
            s.close();
            this.cerrar();
            return filas;

        } catch (SQLException e) {
            this.cerrar();
            return -1;
        }
    }

    // ------------- ALEJANDRO ----------------
    /**
     * @author Alejandro Jiménez Miranda
     * @param cl
     * @return filas
     * @date 12/05/2022
     * @lastmod 18/05/2022
     *
     * Inserta el cliente en la base de datos o sino ocurre una excepción.
     *
     */
    public int Registro(Cliente cl) {
        String cadena = "INSERT INTO Clientes VALUES('" + cl.getDNI() + "','" + cl.getNombre() + "','" + cl.getUsuario() + "','" + cl.getPassword() + "','" + cl.getCorreo() + "','" + (cl.isEspecial() ? 1 : 0) + "')";

        try {
            this.abrir();
            s = c.createStatement();
            int filas = s.executeUpdate(cadena);
            s.close();
            this.cerrar();
            return filas;
        } catch (SQLException e) {
            System.out.println("No se pudo realizar el registro del usuario");
            int filas = 0;
            return filas;
        }

    }

    /**
     * @author Alejandro Jiménez Miranda
     * @return Cliente
     * @date 15/05/2022
     * @lastmod 18/05/2022 Se trata de una función que busca un cliente ya
     * registrado en el sistema con todos los datos
     *
     */
    public Cliente buscarClienteRegistrado(String dni) {
        Cliente cl = null;
        String cadena = "SELECT * FROM Clientes WHERE DNI = '" + dni + "'";
        try {

            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(cadena);
            if (reg.next()) {
                cl = new Cliente(reg.getString("DNI"), reg.getString("Nombre"), reg.getString("Usuario"), reg.getString("Password"), reg.getString("Correo"), reg.getBoolean(("Especial")));
            }

            s.close();
            this.cerrar();
            return cl;
        } catch (SQLException e) {

            return null;

        }

    }

    /**
     * @author Alejandro Jiménez Miranda
     * @return Cliente
     * @date 12/05/2022
     * @lastmod 18/05/2022 Se trata de una función que busca un cliente ya
     * registrado en el sistema con solo el usuario y la password
     *
     */
    public Cliente buscarCliente(String usu, String passw) {
        Cliente cli = null;
        String cadena = "SELECT Usuario, Password FROM Clientes WHERE Usuario ='" + usu + "' AND Password ='" + passw + "'";
        try {
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(cadena);
            if (reg.next()) {
                cli = new Cliente(reg.getString("Usuario"), reg.getString("Password"));
            }

            s.close();
            this.cerrar();
            return cli;
        } catch (SQLException e) {
            System.out.println("No se ha podido encontrar el cliente.");
            return null;

        }
    }

    //----------------César-----------------
    /**
     * @author César Fraile Garcia
     * @param idenreserva
     * @param dni
     * @lastmod 22/05/2022 Borrar reserva mientras el iden viaje y el dni del usuario coincidan con la reserva
     * @return Reserva
     */
    public Reserva cancelarReserva(int idenreserva, String dni) {
        boolean correcto = true;
        String cadena2 = "SELECT Iden_Viaje FROM Reservas WHERE Iden_Reserva= '" + idenreserva + "'";
        String cadena3 = "SELECT PlazasReservadas FROM Reservas WHERE Iden_Reserva= '" + idenreserva + "'";
        String cadena1 = "DELETE FROM Reservas WHERE DNI='" + dni + "' AND Iden_Reserva= '" + idenreserva + "'";
        String cadena5 = "SELECT * FROM Reservas WHERE Iden_Reserva= '" + idenreserva + "'";
        Reserva rsv = null;
        int plazas = 0,iden = 0;

       try {
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(cadena5);
            if(reg.next()){
            rsv= new Reserva(reg.getString("DNI"),reg.getInt("Iden_Viaje"),reg.getInt("Iden_Reserva"),reg.getInt("PlazasReservadas"),reg.getString("cod_emple"));
            }
       }catch (SQLException ex1) {
            System.out.println("Error Reserva");
            }
        try {
             
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(cadena3);
            if(reg.next()){
            plazas = reg.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error plazas");
        }
        try {
            
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(cadena2);
            if(reg.next()){
            iden = reg.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error plazas");
        }
        /*try {
            plazas = Integer.parseInt(cadena3);
        } catch (NumberFormatException ex) {
            System.out.println("Error en el parse");
        }*/
        try {
            this.abrir();
            s = c.createStatement();
            c.setAutoCommit(false);
            int filas = s.executeUpdate(cadena1);
            System.out.println("Se han borrado " + filas + " filas");
            if (filas > 0) {
                String cadena4 = "UPDATE Viajes set NumReservados=NumReservados-" + plazas + " WHERE Ident_Viaje = (" + iden + ")";
               int filas2 = s.executeUpdate(cadena4);
                if (s.executeUpdate(cadena4) == 0) {
                    System.out.println("No se ha actualizado la tabla Viajes, hacemos rollback");
                    c.rollback();
                    rsv = null;
                } else {
                    System.out.println("Viajes actualizado");
                }

                s.close();
                c.commit();
            }

            this.cerrar();
        } catch (SQLException e) {
            try {
                c.rollback();
            } catch (SQLException ex) {
                System.out.println("Error en el rollback");
            }
            this.cerrar();
            
        }
        return rsv;
    }
    
    /**
     * @autor César Fraile García
     * @param Dni
     * @lastmod 20/05/2022 Validar el dni del empleado para poder iniciar sesión
     * @return  Boolean
     */

    public Boolean valEmple(String Dni) {
        String cadena = "SELECT * FROM Empleados WHERE DNI_emple='" + Dni + "'";
        Boolean val = false;
        try {
            String t = "";
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(cadena);
            if (reg.next()) {
                t = reg.getString(1);
                val=true;
            }
            s.close();
            this.cerrar();
            
        } catch (SQLException e) {

            return val;

        }
return val;
    }
    /**
     * @autor César Fraile García
     * @param cl
     * @lastmod 20/05/2022 Devuelve el dni del cliente que haya iniciado la sesión
     * @return String
     */

    public String valCliente(Cliente cl) {
        String cadena = "SELECT DNI FROM Clientes WHERE Usuario='" + cl.getUsuario() + "'AND Password='" + cl.getPassword() + "'";
        try {
            String t = "";
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(cadena);
            if (reg.next()) {
                t = reg.getString(1);
            }

            s.close();
            this.cerrar();
            return t;
        } catch (SQLException e) {

            return null;

        }

    }
/**
 * @autor César Fraile García
 * @param IdenReserva
 * @lastmod 22/05/2022 Devuelve la fecha de ida del vuelo según el iden de la reserva
 * @return 
 */
    public LocalDate FechaReserva(int IdenReserva) {
        String cadena = "SELECT FechaHoraIda FROM Viajes vj join Reservas rsv on vj.Ident_Viaje=rsv.Iden_Viaje WHERE Iden_Reserva= '" + IdenReserva + "'";
        LocalDate fecha = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-LL-dd");
        

        try {
            String t;
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(cadena);
            if (reg.next()) {
                t = reg.getString(1);
                fecha = LocalDate.parse(t, formatter);
            }

            s.close();
            this.cerrar();
            return fecha;
        } catch (SQLException e) {

            return null;

        }

    }
    
    // ------------- ANGEL ----------------
    
    /**
     * @author Angel Navacerrada Rodriguez
     * @param Precio 
     * @return Viajes
     * @date 12/05/2022
     * @lastmod 18/05/2022
     * Este metodo hace una consulta que recoje en un Vector todos los viajes con un precio igual o menor al recibido
     */
    public Vector<Viaje> mostrarContactos(double Precio) {
        Vector<Viaje> v = new Vector<Viaje>();
        String cadena = "SELECT * FROM Viajes WHERE Precio <= '" + Precio + "'";
        try {

            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(cadena);
            while (reg.next()) {
                v.add(new Viaje(reg.getInt("Ident_Viaje"), reg.getString("Origen"), reg.getString("Destino"), reg.getDate("FechaHoraIda").toLocalDate(), reg.getDate("FechaHoraVuelta").toLocalDate(), reg.getString("Descripción"), reg.getInt("NumReservados"), reg.getInt("NumTotal"), reg.getDouble("Precio")));
            }

            s.close();
            this.cerrar();
            return v;
        } catch (SQLException e) {

            return null;

        }

    }

   

    /**
     * @author Angel Navacerrada Rodriguez
     * @param  
     * @return int
     * @date 12/05/2022
     * @lastmod 22/05/2022
     * Este metodo hace una consulta que recoje el ultimo identificador de las reservas
     */
    public int NumIden() {
        String cadena = "SELECT MAX(Iden_Reserva) FROM Reservas";
        try {
            int t = 0;
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(cadena);
            if (reg.next()) {
                t = reg.getInt(1);
            }

            s.close();
            this.cerrar();
            return t;
        } catch (SQLException e) {

            return -1;

        }
    }

    /**
     * @author Angel Navacerrada Rodriguez
     * @param DNI, Iden_Viaje, Iden_Reserva, cod_emple, PlazasReservadas 
     * @return reserva
     * @date 12/05/2022
     * @lastmod 22/05/2022
     * Este metodo hace una reserva y a su vez actualiza las plazas reservadas para el viaje a reservar
     */
    public Reserva HacerReserva(String DNI, int Iden_Viaje, String cod_emple, int Iden_Reserva, int PlazasReservadas) {
        Reserva reserva = null;
        boolean correcto = true;
        String cadena1 = "INSERT into Reservas VALUES ('" + DNI + "','" + Iden_Viaje + "','" + Iden_Reserva + "','" + PlazasReservadas + "','" + cod_emple + "')";
        String cadena2 = "UPDATE Viajes set NumReservados=NumReservados+" + PlazasReservadas + " WHERE Ident_Viaje=" + Iden_Viaje;

        try {
            this.abrir();
            c.setAutoCommit(false);
            s = c.createStatement();
            s.executeUpdate(cadena1);
            reserva = new Reserva(DNI,Iden_Viaje,Iden_Reserva,PlazasReservadas,cod_emple);
            s.executeUpdate(cadena2);
            if (s.executeUpdate(cadena2) == 0) {
                c.rollback();
                correcto = false;
            } else {
                s.close();
                c.commit();
            }
            this.cerrar();

        } catch (SQLException e) {
            try {
                c.rollback();
            } catch (SQLException ex) {
                System.out.println("Error");
            }
            this.cerrar();
            correcto = false;
        }
        if(correcto==false){
            return null;
        }
        return reserva;
    }

}


