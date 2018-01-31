/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import bobjects.Usuario;
import dao.KronaDAO;
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
 * @author Alejandro
 */
public class KronaController extends HttpServlet {

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
            //en lugar de mandar a index manda a error por que aca solo viene por inner frame
            String url = "/WEB-INF/view/error/error.jsp";
            //mandar mensaje de session expirada o a página de error / sesión expirada
            request.setAttribute("msg", "Su sesi&oacute;n expir&oacute;");
            request.getRequestDispatcher(url).forward(request, response);
            return;
        } else {
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

            if (userPath.equals("/showKrona")) {
                //es el id del elemento metagenoma o marcador
                String idkrona = request.getParameter("idkrona");
                String src = request.getParameter("src");//marcador o metagenoma
                if (idkrona != null && src != null) {
                    int id = -1;
                    try {
                        id = Integer.parseInt(idkrona);
                    } catch (NumberFormatException nfe) {
                        request.setAttribute("msg", "Error inicializando krona: " + idkrona + "<br>Por favor comunicarse con el administrador del sistema ");
                        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/enconstruccion.jsp");
                        view.forward(request, response);
                        return;
                    }
                    if (!src.toLowerCase().equals("marcador") && !src.toLowerCase().equals("metagenoma")) {
                        request.setAttribute("msg", "Error inicializando krona: " + idkrona + " fuente incorrecta!: " + src + "<br>Por favor comunicarse con el administrador del sistema ");
                        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/enconstruccion.jsp");
                        view.forward(request, response);
                        return;
                    }
                    KronaDAO kdao = new KronaDAO(transacciones);
                    String html = kdao.readKrona(id, src);
                    if (html.equals("ERROR")) {
                        request.setAttribute("msg", "Error inicializando krona: " + idkrona + " - Leyendo archivo Krona - <br>Por favor comunicarse con el administrador del sistema ");
                        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/enconstruccion.jsp");
                        view.forward(request, response);
                        return;
                    } else {
                        response.setContentType("text/html");
                        response.setCharacterEncoding("UTF-8");
                        PrintWriter out = response.getWriter();
                        out.print(html);
                        out.close();
                    }
                } else {
                    request.setAttribute("msg", "Error con solicitud de archivo KRONA<br/>Comunicarse con el administrador del sistema<br/>Gracias!");
                    String url = "/WEB-INF/view/error/error.jsp";
                }

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
