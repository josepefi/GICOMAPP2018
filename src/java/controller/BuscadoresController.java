/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import bobjects.Muestra;
import bobjects.NCBINode;
import bobjects.Usuario;
import dao.MuestraDAO;

import database.Transacciones;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.JsonUtils;
import dao.ncbiNodoDAO;

/**
 *
 * @author Jose Pefi
 */
public class BuscadoresController extends HttpServlet {

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
        HttpSession session = request.getSession();
        if (session == null || session.isNew() || session.getAttribute("userObj") == null) {
            //session expirada o invalida
            String url = "index.jsp";
            //mandar mensaje de session expirada o a página de error / sesión expirada
            request.setAttribute("msg", "Su sesi&oacute;n expir&oacute;");
            request.getRequestDispatcher(url).forward(request, response);
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
            if (userPath.equals("/buscadores")) {
                RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/buscadores.jsp");
                view.forward(request, response);
            } else if (userPath.equals("/detallesBusqueda")) {
                String detalles = request.getParameter("detalles");
                ArrayList<ArrayList> detallesBusqueda = transacciones.getBuscarncbinode(detalles);
                PrintWriter out = response.getWriter();
                response.setContentType("text/html");
                response.setCharacterEncoding("UTF-8");

                for (ArrayList<ArrayList> bg : detallesBusqueda) {
                    out.println("<p style=\"color:red;\">" + bg.get(0) + "--" + bg.get(3) + "--" + bg.get(4) + "--" + bg.get(5) + "</p>");
                }
                //System.out.println(detalles);
            } else if (userPath.equals("/buscaPalabra")) {
                String palabra = request.getParameter("palabra").trim();
                boolean limit = true;
                //System.out.println("La palabra es:"+palabra);
                boolean esID = false;
                try {
                    Integer.parseInt(palabra);
                    esID = true;
                } catch (NumberFormatException nfe) {
                    esID = false;
                }
                ArrayList<ArrayList> ncbiNodes;

                if (esID) {
                    ncbiNodes = transacciones.buscaNodosByTaxID(palabra, limit);
                } else {
                    if (palabra.contains("-")) {
                        palabra = palabra.substring(palabra.indexOf("-") + 1).trim();
                        if (palabra.indexOf("(") != -1) {
                            palabra = palabra.substring(0, palabra.indexOf("(")).trim();
                        }
                    }
                    ncbiNodes = transacciones.buscaNodosByName(palabra, limit);
                }
                PrintWriter out = response.getWriter();
                response.setContentType("text/html");
                response.setCharacterEncoding("UTF-8");
                if (ncbiNodes.size() < 1) {

                    out.println("<div class=\"alert alert-danger\" style=\" font-size:14px; text-align:center;\">No hay resultados para: " + palabra + " </div>");
                } else {
                    out.println("<div class=\"panel-body\" style=\" height:; overflow-y: ;\">");
                    for (ArrayList<String> ncN : ncbiNodes) {
                        String idR = ncN.get(0);
                        String name = ncN.get(1);
                        if (esID) {
                            idR = idR.length() > palabra.length() ? "<font color='red'>" + idR.substring(0, palabra.length()) + "</font>" + idR.substring(palabra.length()) : "<font color='red'>" + idR + "</font>";
                        } else {
                            name = name.length() > palabra.length() ? "<font color='red'>" + name.substring(0, palabra.length()) + "</font>" + name.substring(palabra.length()) : "<font color='red'>" + name + "</font>";
                        }
                        out.println("<div id=" + ncN.get(0) + " class=\"alert alert-info\" onclick=\"capturar(" + ncN.get(0) + ")\" onmouseover=\"this.style.backgroundColor='#fcf8e3'\" onmouseout=\"this.style.backgroundColor='#d9edf7'\" style=\"font-size:12px; cursor:pointer;\" > <b>" + idR + "</b> - " + name + " (<i>" + ncN.get(2) + "</i>)" + " </div>");
                    }
                    out.println("</div>");
                    out.close();
                }

                /*    if (palabra.matches("[0-9]*")) {
                 //System.out.println("Es un número");
                 if (ncbinodeNumeros.size() < 1) {

                 out.println("<div class=\"alert alert-danger\" style=\" font-size:14px; text-align:center;\">No hay resultados para: " + palabra + " </div>");
                 } else {
                 out.println("<div class=\"panel-body\" style=\" height:; overflow-y: ;\">");

                 for (ArrayList<ArrayList> ncN : ncbinodeNumeros) {
                 out.println("<div id=" + ncN.get(0) + " class=\"alert alert-info\" onclick=\"capturar(" + ncN.get(0) + ")\" onmouseover=\"this.style.backgroundColor='#fcf8e3'\" onmouseout=\"this.style.backgroundColor='#d9edf7'\" style=\"font-size:12px; cursor:pointer;\" > <b>" + ncN.get(0) + "</b> - " + ncN.get(1) + " (" + ncN.get(2) + ")" + " </div>");

                 }

                 out.println("</div>");
                 }
                 } else {
                 //System.out.println("No es un número");
                 if (ncbinodeString.size() < 1) {

                 out.println("<div class=\"alert alert-danger\" style=\" font-size:14px; text-align:center;\">No hay resultados para: " + palabra + " </div>");
                 } else {
                 out.println("<div class=\"panel-body\" style=\" height:; overflow-y: ;\">");

                 for (ArrayList<ArrayList> ncS : ncbinodeString) {
                 out.println("<div id=" + ncS.get(0) + " class=\"alert alert-info\" onclick=\"capturar(" + ncS.get(0) + ")\" onmouseover=\"this.style.backgroundColor='#fcf8e3'\" onmouseout=\"this.style.backgroundColor='#d9edf7'\" style=\"font-size:12px; cursor:pointer;\" > <b>" + ncS.get(0) + "</b> - " + ncS.get(1) + " (" + ncS.get(2) + ")" + " </div>");

                 }

                 out.println("</div>");
                 }
                 }*/
                //response.setContentType("application/json");
                //ncbiNodoDAO ncbiDao = new ncbiNodoDAO(transacciones);
                //NCBINode ncbinode = ncbiDao.busqueda(palabra);
                /*if(ncbinode != null || ncbinode.equals("")){
                 String searchList = new Gson().toJson(ncbinode.getName());
                 response.getWriter().write(searchList);
                 }else{
                 String sinResultados ="No se han encontrado resultados para"+palabra;
                
                 String searchList2 = new Gson().toJson(sinResultados);
                 response.getWriter().write(searchList2);
                 }*/
                //Object jobsObj = request.getAttribute("jobs");
                //ArrayList<ArrayList> ncb = null;
                //System.out.println("Palabra a buscar " + palabra);
                //System.out.print("Lista:" + searchList);
                //response.setContentType("application/json");
                //try {
                //ArrayList<String> list = transacciones.getBuscar(term);
                //MuestraDAO mDao = new MuestraDAO(transacciones);
                //Muestra muestra = mDao.busqueda(palabra);
                //String searchList = new Gson().toJson(muestra);
                //response.getWriter().write(searchList);
                //System.out.print("Lista:" + searchList);
                //} catch (Exception e) {
                //    System.err.println(e.getMessage());
                // }*/
                //MuestraDAO mDao = new MuestraDAO(transacciones);
                //Muestra muestra = mDao.busqueda(busqueda);
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
