/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

/**
 *
 * @author administrador
 */
public class Reserva {
    private String DNI;
    private int Iden_Viaje;
    private int Iden_Reserva;
    private int PlazasReservadas;
    private String cod_emple;

    public Reserva(String DNI, int Iden_Viaje, int Iden_Reserva, int PlazasReservadas, String cod_emple) {
        this.DNI = DNI;
        this.Iden_Viaje = Iden_Viaje;
        this.Iden_Reserva = Iden_Reserva;
        this.PlazasReservadas = PlazasReservadas;
        this.cod_emple = cod_emple;
    }

    public String getDNI() {
        return DNI;
    }

    public int getIden_Viaje() {
        return Iden_Viaje;
    }

    public int getIden_Reserva() {
        return Iden_Reserva;
    }

    public int getPlazasReservadas() {
        return PlazasReservadas;
    }

    public String getCod_emple() {
        return cod_emple;
    }
            
}
