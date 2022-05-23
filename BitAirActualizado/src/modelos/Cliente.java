/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.util.Objects;

/**
 *
 * @author administrador
 */
public class Cliente {
    private String DNI;
    private String Nombre;
    private String Usuario;
    private String Password;
    private String Correo;
    private boolean Especial;
    
    public Cliente(String dni, String nombre, String usuario, String password, String correo, boolean especial) {
        this.DNI = dni;
        this.Nombre = nombre;
        this.Usuario = usuario;
        this.Password = password;
        this.Correo = correo;
        this.Especial = especial;
    }

    public Cliente(String usuario, String password) {
        this.Usuario = usuario;
        this.Password = password;
    }

    public Cliente(String dni) {
        this.DNI = dni;
    }

    public String getDNI() {
        return DNI;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getUsuario() {
        return Usuario;
    }

    public String getPassword() {
        return Password;
    }

    public String getCorreo() {
        return Correo;
    }

    public boolean isEspecial() {
        return Especial;
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
        final Cliente other = (Cliente) obj;
        if (!Objects.equals(this.DNI, other.DNI)) {
            return false;
        }
        return true;
    }

    
    
}
