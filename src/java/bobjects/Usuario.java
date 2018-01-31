/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bobjects;

import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
public class Usuario {
    private int idUsuario;
    private int idInstitucion;
    private String user;
    private String nombres;
    private String apellidos;
    private String correo;
    private String cite;
    private String terminos;
    //wild keys para otras relaciones
    private String acciones; //como ser wildkey para muestreo usuario
    private String comentarios;
     String dbx_ref;
    ArrayList<Permiso> permisos;
    public void addPermiso(Permiso permiso) {
        this.permisos.add(permiso);
    }

    public boolean containsPermiso(Permiso permiso) {
        for (Permiso p : permisos) {
            if (p.getIdPermiso() == permiso.getIdPermiso()) {
                return true;
            }
        }
        return false;
    }
    public String getUser() {
    return user;
    }
     public void setUser(String user) {
    this.user = user;
    }
    public ArrayList<Permiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(ArrayList<Permiso> permisos) {
        this.permisos = permisos;
    }
    
    public Usuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getAcciones() {
        return acciones;
    }

    public void setAcciones(String acciones) {
        this.acciones = acciones;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdInstitucion() {
        return idInstitucion;
    }

    public void setIdInstitucion(int idInstitucion) {
        this.idInstitucion = idInstitucion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCite() {
        return cite;
    }

    public void setCite(String cite) {
        this.cite = cite;
    }
    
        public String getTerminos() {
        return terminos;
    }

    public void setTerminos(String terminos) {
        this.terminos = terminos;
    }
    
}
