/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.Transacciones;
import bobjects.Usuario;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
public class UsuarioDAO {

    Transacciones transacciones;

    public UsuarioDAO(Transacciones transacciones) {
        this.transacciones = transacciones;
    }

    /**
     * Autentica un usuario en la BD
     *
     * @param correo el correo que ingresó el usuario para autenticarse
     * @param pass el password
     * @return null si no existe o credenciales inválidas, usuario inicializado
     * de otra forma
     * @author Alejandro Abdala
     */
    public Usuario authentUser(String correo, String pass) {
        int id = transacciones.authentUser(correo, pass);
        if (id != -1) {
            Usuario user = new Usuario(id);
            ArrayList<ArrayList<String>> u = transacciones.getUser(id);
            user.setNombres(u.get(0).get(0));
            user.setApellidos(u.get(0).get(1));
            user.setCorreo(u.get(0).get(2));
            user.setTerminos(u.get(0).get(3));
            //agregar lo de los permisos!!
            return user;
        } else {
            return null;
        }
    }
   
    
   
    
}

