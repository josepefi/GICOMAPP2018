/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import bobjects.Marcador;
import bobjects.Usuario;
import dao.KronaDAO;
import dao.MarcadorDAO;
import dao.TaxaDAO;
import database.Transacciones;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Alejandro
 */
public class MarcadorController extends HttpServlet {

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
            if (userPath.equals("/showMarcador")) {
                String idMarcador = request.getParameter("idMarcador");
                if (idMarcador != null) {
                    try {
                        Integer.parseInt(idMarcador);
                        TaxaDAO tdao = new TaxaDAO(transacciones);
                        //tdao.generaTablaDegradadores();
                        String htmlTable = tdao.generaTablaDegradadores(); //va desde <table> hasta <table>          
                        request.setAttribute("tabla", htmlTable);

                        MarcadorDAO mDAO = new MarcadorDAO(transacciones);
                        Marcador marcador = mDAO.initMarcador(idMarcador);
                        request.setAttribute("marcador", marcador);
                        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/marcador/marcador.jsp");
                        view.forward(request, response);
                        return;
                    } catch (NumberFormatException nfe) {
                        request.setAttribute("msg", "Error inicializando Librería: " + idMarcador + "<br>Por favor comunicarse con el administrador del sistema ");
                        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/error.jsp");
                        view.forward(request, response);
                        return;
                    }
                } else {
                    request.setAttribute("msg", "Error inicializando Librería: " + idMarcador + "<br>Por favor comunicarse con el administrador del sistema ");
                    RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/error.jsp");
                    view.forward(request, response);
                    return;
                }
            } else if (userPath.equals("/kronaAmp")) {
                String idkrona = request.getParameter("idkrona");
                if (idkrona != null) {
                    int id = -1;
                    try {
                        id = Integer.parseInt(idkrona);
                    } catch (NumberFormatException nfe) {
                        request.setAttribute("msg", "Error inicializando krona: " + idkrona + "<br>Por favor comunicarse con el administrador del sistema ");
                        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/error.jsp");
                        view.forward(request, response);
                        return;
                    }

                    //  KronaDAO kdao = new KronaDAO(transacciones);
                    //  String html = kdao.readKronaFile(id);
                    response.setContentType("text/html");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter out = response.getWriter();
                    // out.println("<iframe src=\"kronas/krona"+idkrona.trim()+".html\" width=100%; height=500px; frameborder=\"0\"></iframe>");
                    out.println(" <frameset>");
                    out.println(" <iframe src=\"kronaAmp2?idkrona=" + idkrona + "\"> width=100%; height=500px; frameborder='0'> </iframe>");
                    out.println(" </frameset>");
                    out.close();
                }
                //este es el chido
                // out.println("<iframe src=\"kronas/krona"+idkrona.trim()+".html\" width=100%; height=500px; frameborder=\"0\"></iframe>");
                //  out.println("<iframe src=\"dist/kronas/krona"+idkrona.trim()+".html\" width=100%; height=500px; frameborder=\"0\"></iframe>");
                // out.println("<iframe src=\"file://WEB-INF/view/kronas/krona" + idkrona.trim() + ".html\" width=100%; height=500px; frameborder=\"0\"><iframe>");

// out.println("<iframe src=\"file://filestmp/krona.html\" width=100%; height=500px; frameborder=\"0\"><iframe>");
                // out.println("<iframe src=\"filestmp/krona.html\" width=100%; height=500px; frameborder=\"0\"><iframe>");
                //out.println("<iframe src=\"http://localhost:8080/CIGOM/krona.html\" width=100%; height=500px; frameborder=\"0\"><iframe>");
                //out.println("<iframe src=\"file://C:\\Users\\Alejandro\\Documents\\Projects\\pemex\\Amplicones\\kronas\\B8_FON_1\\krona.html\" width=100%; height=500px; frameborder=\"0\"><iframe>");
                //  out.println("<iframe src='data:text/html;' width=100%; height=500px; frameborder=\"0\">" + html + "<iframe>");
                //  out.println(html);             
            } else if (userPath.equals("/kronaAmp2")) {
                // en realidad el id es el id del marcador
                String idkrona = request.getParameter("idkrona");
                if (idkrona != null) {
                    int id = -1;
                    try {
                        id = Integer.parseInt(idkrona);
                    } catch (NumberFormatException nfe) {
                        request.setAttribute("msg", "Error inicializando krona: " + idkrona + "<br>Por favor comunicarse con el administrador del sistema ");
                        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/error/enconstruccion.jsp");
                        view.forward(request, response);
                        return;
                    }
                    KronaDAO kdao = new KronaDAO(transacciones);
                    String html = kdao.readKronaFile(id);
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
