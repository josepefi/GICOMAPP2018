/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import bobjects.GenObj;
import bobjects.Usuario;
import dao.GenDAO;
import database.Transacciones;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Jose Pefi
 */
public class GenController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.isNew() || session.getAttribute("userObj") == null) {
            //session expirada o invalida
            String url = "index.jsp";
            //mandar mensaje de session expirada o a página de error / sesión expirada
            request.setAttribute("msg", "Su sesi&oacute;n expir&oacute;");
            request.getRequestDispatcher(url).forward(request, response);
            return;
        }else {
            String userPath = request.getServletPath();
            //Transacciones tiene que ser un variable de sesion
            Transacciones transacciones = (Transacciones) session.getAttribute("transacciones");
            Usuario user = (Usuario) session.getAttribute("userObj");
            if (transacciones == null || !transacciones.testConnection()) {
                transacciones = getNewConexion();
                session.setAttribute("transacciones", transacciones);
            }
            if (transacciones == null || !transacciones.testConnection()) {
                //redireccionar a página de error
                request.setAttribute("msg", "No se puede crear una conexión a la BD<br/>Comunicarse con el administrador del sistema<br/>Gracias!");
                String url = "/WEB-INF/view/error/error.jsp";
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }

            if (userPath.equals("/showGen")) {
                String idGen = request.getParameter("idGen");
                 GenDAO gendao = new GenDAO(transacciones);
                    GenObj gen = gendao.initGen(idGen);
                    request.setAttribute("gen", gen);
                    RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/gen/gen.jsp");
                    view.forward(request, response);
                  
            }   
            if (userPath.equals("/showMaqueta")) {
                    RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/maqueta.jsp");
                    view.forward(request, response);
                  
            }
            
        }
    }
                private Transacciones getNewConexion() {
        Transacciones transacciones;
        try {
            ServletContext sc = getServletContext();
            String usuario = sc.getInitParameter("usuariodb");
            String ip = sc.getInitParameter("ipdb");
            String db = sc.getInitParameter("dbname");
            String password = sc.getInitParameter("password");
            transacciones = new Transacciones(db, usuario, ip, password);
            return transacciones;
            //  transacciones = new Transacciones(datasource.getConnection());
        } catch (Exception e) {
            Logger.getLogger(BlastController.class.getName()).log(Level.SEVERE, "Transacciones - testConection", e);
            return null;

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
