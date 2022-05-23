/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitair;

import bbdd.BD_BitAir;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelos.Cliente;
import modelos.Reserva;
import modelos.Viaje;

/**
 *
 * @author administrador
 */
public class BitAir {

    /**
     * @param args the command line arguments
     */
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // TODO code application logic here
        BD_BitAir bd = new BD_BitAir("BitAir");
        int opcion = 0;
        String DNI_emple;
        String COD_emple;
        Cliente cl = null;
        boolean conectado = false;
        String FormaPago;
        Path p;

        Viaje vj;

        System.out.println("Como quieres acceder: 1) Empleado 2) Cliente");
        do {
            opcion = sc.nextInt();
            if (opcion != 1 && opcion != 2) {
                System.out.println("Tipo incorrecto, seleccione 1 o 2");
            }
        } while (opcion != 1 && opcion != 2);
        switch (opcion) {
            case 1:
                System.out.println("Presentar credenciales");
                
                    sc.nextLine();
                    System.out.println("Anota dni");
                    do {
                    DNI_emple = sc.nextLine();
                 
                    /**
                     * @author César Fraile Fecha creación: 20/05/2022
                     * Fecha ultima modificación: 18/05/2022 //val emple
                     */
                    if (bd.valEmple(DNI_emple) == false) {
                        System.out.println("Dni erroneo, no se puede iniciar sesión");
                       
                    } else {
                        System.out.println("Se ha podido iniciar sesión");
                        
                    }
                } while (bd.valEmple(DNI_emple) == false);
                System.out.println("Anota opcion");
                System.out.println("1.Alta viaje");
                System.out.println("2.Baja viaje");

                int Ident_Viaje,
                 NumReservados,
                 NumTotal;
                String Origen,
                 Destino,
                 Descripcion,
                 respuesta;
                LocalDate FechaIda,
                 FechaVuelta,
                 actual = LocalDate.now(),
                 fecha = null;
                Double Precio;

                do {
                    opcion = sc.nextInt();
                    if (opcion != 1 && opcion != 2) {
                        System.out.println("Tipo incorrecto, seleccione 1 o 2");
                    }
                } while (opcion != 1 && opcion != 2);
                switch (opcion) {

                    /**
                     * @author Adriana Hernández Fecha creación: 11/05/2022
                     * Fecha ultima modificación: 18/05/2022
                     */
                    case 1:
                        //Alta viaje
                        Ident_Viaje = bd.buscarIdentViaje() + 1;
                        sc.nextLine();
                        System.out.println("Anota origen: ");
                        Origen = sc.nextLine();
                        System.out.println("Anota destino: ");
                        Destino = sc.nextLine();

                        do {
                            System.out.println("Anota Fecha Ida: (dd/mm/yyyy)");
                            fecha = leeFecha("Fecha en formato incorrecto, vuelve a anotar: ", "dd/LL/yyy");
                            if (fecha.isBefore(actual)) {
                                System.out.println("La fecha no puede ser anterior");
                            }
                        } while (fecha.isBefore(actual));
                        FechaIda = fecha;

                        do {
                            System.out.println("Anota Fecha Vuelta: (dd/mm/yyyy)");
                            fecha = leeFecha("Fecha en formato incorrecto, vuelve a anotar: ", "dd/LL/yyy");
                            if (fecha.isBefore(actual) || fecha.isBefore(FechaIda)) {
                                System.out.println("La fecha no puede ser anterior a hoy ni a la de ida");
                            }
                        } while (fecha.isBefore(actual) || fecha.isBefore(FechaIda));
                        FechaVuelta = fecha;

                        System.out.println("Anota descripción: ");
                        Descripcion = sc.nextLine();
                        System.out.println("Anota total de plazas: ");
                        NumTotal = sc.nextInt();
                        NumReservados = 0;
                        do {
                            System.out.println("Anota precio por persona: ");
                            Precio = sc.nextDouble();
                            if (Precio < 0) {
                                System.out.println("El precio no puede ser menor que 0, prueba otra vez");
                            }
                        } while (Precio < 0);
                        vj = new Viaje(Ident_Viaje, Origen, Destino, FechaIda, FechaVuelta, Descripcion, NumTotal, NumReservados, Precio);

                        if (bd.añadir(vj)) {
                            System.out.println("Viaje añadido con éxito");
                        } else {
                            System.out.println("No se ha podido añadir, avise a BitAir");
                        }
                        break;
                    case 2:
                        //Baja viaje
                        System.out.println("Anota Identificador del Viaje que quieres dar de baja: ");
                        Ident_Viaje = sc.nextInt();
                        if (bd.buscarViaje(Ident_Viaje) == 0) {
                            System.out.println("No se ha encontrado un Viaje con ese indentificador, consideramos que no quieres dar de baja ningun viaje, muchas gracias!");
                        } else {
                            if (bd.buscarReservasAsociadas(Ident_Viaje) == 1) {
                                do {
                                    System.out.println("Hay reservas asociadas, quieres proseguir con la baja: ");
                                    sc.nextLine();
                                    respuesta = sc.nextLine();
                                    if (respuesta.equalsIgnoreCase("SI")) {
                                        if (bd.borrarReservasAsociadas(Ident_Viaje) >= 1) {
                                            System.out.println("Se han borrado las reservas, ahora daremos de baja el viaje");
                                        } else {
                                            System.out.println("No había nada que borrar");
                                        }
                                    } else {
                                        System.out.println("No se va a proseguir con la baja del viaje.");
                                        break;
                                    }
                                } while (!respuesta.equalsIgnoreCase("SI") && !respuesta.equalsIgnoreCase("NO"));
                            }
                            if (bd.borrarViaje(Ident_Viaje) == 1) {
                                System.out.println("Se ha borrado con éxito");
                            } else {
                                System.out.println("No se ha podido borrar, avise a BitAir");
                            }
                        }
                }
                break;
            case 2:

                String DNI,
                 Nombre,
                 Usuario,
                 Password,
                 Email;
                boolean Especial;
                Vector<Cliente> clientes = new Vector<Cliente>();

                System.out.println("Para entrar a nuestra plataforma debes estar Registrado (Si ya lo estas, Inicie sesion");
                System.out.println("Anota opcion");
                do {
                    System.out.println("1. Registrar / 2. Iniciar Sesion");
                    do {
                        opcion = sc.nextInt();
                        if (opcion != 1 && opcion != 2) {
                            System.out.println("Tipo incorrecto, seleccione 1 o 2");
                        }
                    } while (opcion != 1 && opcion != 2);
                    switch (opcion) {
                        case 1:

                            //registrar
                            System.out.println("Has elegido la opción de registrarte en la aplicación.");
                            System.out.println("Siga los siguientes pasos para hacerlo de manera satisfactoria.");
                            System.out.println("Anota DNI: ");
                            sc.nextLine();
                            do {
                                DNI = sc.nextLine();
                                if (comprobarDNI(DNI) == false) {
                                    System.out.println("dni incorrecto, vuelve a anotar");
                                }
                            } while (comprobarDNI(DNI) == false);
                            System.out.println("Anota nombre: ");
                            do {

                                Nombre = sc.nextLine();

                            } while (Nombre.isEmpty());

                            System.out.println("Anota tu nombre de usuario: ");
                            do {

                                Usuario = sc.nextLine();

                            } while (Usuario.isEmpty());
                            System.out.println("Anota tu contraseña: ");
                            do {

                                Password = sc.nextLine();

                            } while (Password.isEmpty());
                            System.out.println("Anota tu e-mail: ");
                            do {
                                Email = sc.nextLine();
                            } while (comprobarCorreo(Email) == false);
                            System.out.println("¿Tienes algún tipo de discapacidad? TRUE / FALSE ");
                            Especial = sc.nextBoolean();

                            /*Buscar en la base de datos con un metodo ()*/
                            cl = new Cliente(DNI, Nombre, Usuario, Password, Email, Especial);

                            if (bd.buscarClienteRegistrado(DNI) == null) {

                                int filas;
                                filas = bd.Registro(cl);
                                switch (filas) {
                                    case 1:
                                        System.out.println("Cliente registrado correctamente.");
                                        conectado = true;
                                        break;
                                    case 0:
                                        System.out.println("Cliente no registrado correctamente.");
                                        break;
                                }

                            } else {
                                System.out.println("Este cliente ya está registrado.");
                                break;
                            }

                            //registrar
                            /*El cliente es el que se registra*/
                            /*Revisar en clase la forma de saber si esta en la bb.dd o no.*/
                            break;
                        case 2:
                            //inciar sesion
                            sc.nextLine();
                            System.out.println("Has elegido inicar sesión en la aplicación.");
                            System.out.println("Nombre de usuario: ");
                            Usuario = sc.nextLine();
                            System.out.println("Contraseña: ");
                            Password = sc.nextLine();

                            cl = bd.buscarCliente(Usuario, Password);
                            if (cl == null) {
                                System.out.println("Cliente no existe");
                                break;
                            }

                            /*El cliente es el que inicia sesion*/
                            conectado = true;
                            System.out.println("Conectado con éxito.");
                            break;
                    }
                } while (conectado == false);
                break;
        }
        System.out.println("Anota opcion");
        System.out.println("1.Consultar Viajes (Opcion a reserva)");
        System.out.println("2.Cancelar reserva");
        do {
            opcion = sc.nextInt();
            if (opcion != 1 && opcion != 2) {
                System.out.println("Tipo incorrecto, seleccione 1 o 2");
            }
        } while (opcion != 1 && opcion != 2);
        switch (opcion) {
            case 1:
                // ------------- ANGEL ----------------
                        //FILTRAR VIAJES POR PRECIO PARA FACILITAR LA BUSQUEDA AL CLIENTE
                        System.out.println("Anote el precio maximo para mostrarle los viajes disponibles");
                        double Precio = sc.nextDouble();
                        Vector<Viaje> ViajesPrecio = bd.mostrarContactos(Precio);
                        System.out.println("Estos son los viajes disponibles: ");
                        for (int i = 0; i < ViajesPrecio.size(); i++) {
                            System.out.println(ViajesPrecio.get(i).toString());
                        }
                        
                        //ELEGIR VIAJE A RESERVAR
                        System.out.println("Anota el Ident del viaje que desees en caso de no querer reservar anote -1 o un Ident no registrado");
                        int Ident_Viaje = sc.nextInt();
                        if (Ident_Viaje == -1) {
                            break;
                        }
                        int pos = ViajesPrecio.indexOf(new Viaje(Ident_Viaje));
                        if (pos == -1) {
                            System.out.println("No tenemos registros de ese Viaje, entendemos que no estas interesado en nuestros servicios");
                            break;
                        }
                        Viaje ViajeReservar = ViajesPrecio.get(pos);
                        
                        //COMPROBAR QUE HAYA PLAZAS SUFICIENTES
                        System.out.println("¿Cuantas plazas quieres reservar?");
                        int Plazas = sc.nextInt();
                        if (ViajeReservar.getNumReservados() + Plazas >= ViajeReservar.getNumTotal()) {
                            System.out.println("Lo sentimos no quedan plazas suficientes para el Viaje la proxima vez sera");
                            break;
                        } else {
                            System.out.println("El precio a pagar es: " + Plazas * ViajeReservar.getPrecio());
                        }
                        sc.nextLine();
                        
                        //VERIFICAR IDENTIDAD Y PAGAR
                        System.out.println("Enseñe su dni para verificar su identidad");
                        String DNI = sc.nextLine();
                        if (bd.valCliente(cl).equalsIgnoreCase(DNI)) {
                            System.out.println("Anota forma de pago: Tarjeta/Transferencia");
                            do {
                               FormaPago = sc.nextLine();
                                if (!FormaPago.equalsIgnoreCase("Transferencia") && !FormaPago.equalsIgnoreCase("Tarjeta")) {
                                    System.out.println("Anote opcion correcta");
                                }
                            } while (!FormaPago.equalsIgnoreCase("Transferencia") && !FormaPago.equalsIgnoreCase("Tarjeta"));
                        } else {
                            System.out.println("No ese el dni asociado al usuario");
                            break;
                        }
                        
                        //PROCESO DE LA RESERVA
                        System.out.println("Se va a realizar la reserva");
                        Random r = new Random();
                        String cod_emple = String.valueOf(r.nextInt(4) + 1);
                        int Iden_Reserva = bd.NumIden() + 1;
                        Reserva reserva = bd.HacerReserva(DNI, Ident_Viaje, cod_emple, Iden_Reserva, Plazas);
                        if (reserva == null) {
                            System.out.println("En estos momentos no hemos podido realizar la reserva, prueba mas tarde");
                            break;
                        }
                        System.out.println("Reserva realizada");
                        
                        //CREAR RECIBO DE LA RESERVA
                        p = Paths.get(Iden_Reserva + ".txt");
                        try {
                            Files.createFile(p);
                        } catch (IOException ex) {
                            System.out.println("Error al crear recibo");;
                        }
                        Charset charset = Charset.forName("UTF-8");
                        try{
                        BufferedWriter writer = Files.newBufferedWriter(p, charset, TRUNCATE_EXISTING);
                        String cadena = "Recibo de: "+DNI+", Viaje: "+String.valueOf(Ident_Viaje)+", El identificador de la reserva es: "+String.valueOf(Iden_Reserva)+", Gestionada por el empleado: "+cod_emple+", El importe pagado es: "+Plazas * ViajeReservar.getPrecio();
                        writer.write(cadena);
                        writer.close();
                        }catch(IOException ex){
                            System.out.println("Error al escribir el recibo");
                        }
                        System.out.println("Su recibo es el numero "+Iden_Reserva+" reviselo cuando quiera");
                        break;
              
            case 2:
                //cancelar reserva
                System.out.println("Dime el identificador de la reserva: ");
                int idenviaje = sc.nextInt();

                String dnicliente = bd.valCliente(cl);
                long difftotaldias= ChronoUnit.DAYS.between(bd.FechaReserva(idenviaje),LocalDateTime.now());
                Reserva rsv=bd.cancelarReserva(idenviaje, dnicliente);
                if (rsv  == null) {
                    System.out.println("No se puede cancelar la reserva, ya que no coinciden los datos");
                } else {
                    System.out.println("Se ha cancelado");
                    p = Paths.get("Cancelación"+rsv.getIden_Reserva()+".txt");
                    try {
                        if(Files.exists(p)){
                        Files.delete(p);
                        }
                        Files.createFile(p);
                    } catch (IOException ex) {
                        Logger.getLogger(BitAir.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   charset = Charset.forName("UTF-8");
	
		try {
			//Crear fichero con los Datos de la reserva cancelada
			BufferedWriter writer = Files.newBufferedWriter(p, charset,APPEND);
					writer.write("Dni: "+rsv.getDNI());
                                        writer.newLine();
                                        writer.write("Iden Reserva: "+rsv.getIden_Reserva());
                                        writer.newLine();
                                        writer.write("Iden Viaje: "+rsv.getIden_Viaje());
                                        writer.newLine();
                                        writer.write("Plazas Reservadas: "+rsv.getPlazasReservadas());
                                        writer.newLine();
                                        writer.write("Codigo Empleado: "+rsv.getCod_emple());
                                        writer.newLine();
                                        
                                        if(difftotaldias<15){
                                        writer.write("Precio a devolver: Solo se devolverá la mitad del pago.");
                                        writer.newLine();
                                        }else{
                                        writer.write("Precio a devolver: Se devolverá todo el pago del viaje");
                                        writer.newLine();
                                        }
                                        
		    writer.close();
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
                }
                break;
        }
    }

// ---------------- ADRIANA ---------------
    public static LocalDate leeFecha(String msgError, String patron) {
        boolean correcto = false;
        String fecha = null;
        DateTimeFormatter fechaFormateada = DateTimeFormatter.ofPattern(patron);
        LocalDate FechaAnotada = null;
        do {
            try {
                fecha = sc.nextLine();
                FechaAnotada = LocalDate.parse(fecha, fechaFormateada);
                correcto = true;
            } catch (DateTimeParseException e) {
                System.out.println(msgError);
            }
        } while (!correcto);
        return FechaAnotada;
    }

    // ------------- ALEJANDRO -------------
    /* VALIDACIONES */
    /**
     * @author Alejandro Jiménez Miranda
     * @param DNI_Cliente
     * @return boolean
     *
     * Es una función cuyo objetivo es la de validar el formato del DNI
     * introducido.
     *
     */
    public static boolean comprobarDNI(String DNI_Cliente) {

        if (DNI_Cliente.toUpperCase().matches("^[0-9]{8}[A-Z]$")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @author Alejandro Jiménez Miranda
     * @param email
     * @return boolean
     *
     * Es una función cuyo objetivo es validar el formato del correo electrónico
     * introducido por el usuario. El valor aceptado es: palabra123@dominio.com
     *
     */
    public static boolean comprobarCorreo(String email) {

        if (email.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            return true;
        } else {
            return false;
        }
    }

}
