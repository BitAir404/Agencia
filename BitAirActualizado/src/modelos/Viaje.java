/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author administrador
 */
public class Viaje {
    private int Ident_Viaje;
    private String Origen;
    private String Destino;
    private LocalDate FechaIda;
    private LocalDate FechaVuelta;
    private String Descripción;
    private int NumReservados;
    private int NumTotal;
    private Double Precio;

    public Viaje(int Iden_viaje, String Origen, String Destino, LocalDate FechaIda, LocalDate FechaVuelta, String Descripción, int NumReservados, int NumTotal, Double Precio) {
        this.Ident_Viaje = Iden_viaje;
        this.Origen = Origen;
        this.Destino = Destino;
        this.FechaIda = FechaIda;
        this.FechaVuelta = FechaVuelta;
        this.Descripción = Descripción;
        this.NumReservados = NumReservados;
        this.NumTotal = NumTotal;
        this.Precio = Precio;
    }

    public int getIdent_Viaje() {
        return Ident_Viaje;
    }

    public String getOrigen() {
        return Origen;
    }

    public String getDestino() {
        return Destino;
    }

    public LocalDate getFechaIda() {
        return FechaIda;
    }

    public LocalDate getFechaVuelta() {
        return FechaVuelta;
    }

    public String getDescripción() {
        return Descripción;
    }

    public int getNumReservados() {
        return NumReservados;
    }

    public int getNumTotal() {
        return NumTotal;
    }

    public void setIdent_Viaje(int Ident_Viaje) {
        this.Ident_Viaje = Ident_Viaje;
    }

    public void setOrigen(String Origen) {
        this.Origen = Origen;
    }

    public void setDestino(String Destino) {
        this.Destino = Destino;
    }

    public void setFechaIda(LocalDate FechaIda) {
        this.FechaIda = FechaIda;
    }

    public void setFechaVuelta(LocalDate FechaVuelta) {
        this.FechaVuelta = FechaVuelta;
    }

    public void setDescripción(String Descripción) {
        this.Descripción = Descripción;
    }

    public void setNumReservados(int NumReservados) {
        this.NumReservados = NumReservados;
    }

    public void setNumTotal(int NumTotal) {
        this.NumTotal = NumTotal;
    }

    public Double getPrecio() {
        return Precio;
    }

    @Override
    public String toString() {
        return "Viaje{" + "Ident_Viaje=" + Ident_Viaje + ", Origen=" + Origen + ", Destino=" + Destino + ", FechaIda=" + FechaIda + ", FechaVuelta=" + FechaVuelta + ", Descripci\u00f3n=" + Descripción + ", NumReservados=" + NumReservados + ", NumTotal=" + NumTotal + ", Precio=" + Precio + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Viaje other = (Viaje) obj;
        if (this.Ident_Viaje != other.Ident_Viaje) {
            return false;
        }
        return true;
    }
    
    
    public Viaje(int Ident_Viaje) {
        this.Ident_Viaje = Ident_Viaje;
    }
    
    
}
