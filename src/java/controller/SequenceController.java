/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import bobjects.Usuario;
import database.Transacciones;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
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
import utils.SequenceFileCreator;

/**
 *
 * @author Alejandro
 */
public class SequenceController extends HttpServlet {

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
        // response.setContentType("text/html;charset=UTF-8");
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
            if (userPath.equals("/getSequence")) {
                SequenceFileCreator sfc = new SequenceFileCreator(transacciones);
                ServletContext sc = getServletContext();
                String idSeqs = request.getParameter("ids");
                String seqType = request.getParameter("seqType");
                String seqHeader = request.getParameter("seqHeader");
                String downloadType = request.getParameter("dType");

                //   System.out.println("Ids:" + idSeqs + "\n" + "dtype:" + downloadType + "\n" + "seqType:" + seqType + "\n" + "seqHeader:" + seqHeader);
                if (downloadType == null) {
                    downloadType = "seq";
                }
                if (seqHeader == null) {
                    seqHeader = "regular";
                }
                String ext = "";
                if (seqType.trim().toLowerCase().equals("aa")) {
                    ext = "faa";
                } else {
                    ext = "fna";
                }

                String time = ("" + System.currentTimeMillis()).substring(8);
                String fileName = seqType + "_" + /*session.getId()*/ time + "." + ext;
                String fullFile = sc.getRealPath("") + "/filestmp/" + seqType + "_" + /*session.getId()*/ time + "." + ext;
                String dirPath = sc.getRealPath("") + "/filestmp";
                String clustal = sc.getInitParameter("clustalw2");
                String fileType = "";
                boolean serveFile = false;
                if (idSeqs == null || idSeqs.length() < 5) {
                    request.setAttribute("msg", "Error. Se espera por lo menos un identificador");
                    String url = "/WEB-INF/view/error/error.jsp";
                    RequestDispatcher view = request.getRequestDispatcher(url);
                    view.forward(request, response);
                    return;
                }
                if (downloadType.equals("seq")) {
                    serveFile = sfc.generateFileFromIDs(dirPath, fullFile, idSeqs, seqType, seqHeader);
                    fileType = "txt/fna";
                } else {
                    serveFile = sfc.generateAlignFromIDs(dirPath, fullFile, idSeqs, seqType, seqHeader, clustal);
                    fileName += ".align";
                    fullFile += ".align";
                    fileType = "txt/align";
                }
                if (serveFile) {
                    String log = serveFile(fileName, fullFile, response, fileType);
                    if (log.contains("Error")) {
                        request.setAttribute("msg", log);
                        String url = "/WEB-INF/view/error/error.jsp";
                        RequestDispatcher view = request.getRequestDispatcher(url);
                        view.forward(request, response);
                    }
                } else {
                    request.setAttribute("msg", "Error creando archivo de secuencias");
                    String url = "/WEB-INF/view/error/error.jsp";
                    RequestDispatcher view = request.getRequestDispatcher(url);
                    view.forward(request, response);
                }
            } else if (userPath.equals("/serveFile")) {
                String fullFile = request.getParameter("file");
                String fileType = "txt/fasta";
                if (fullFile != null && fullFile.length() > 2) {
                    String fileName = fullFile.substring(fullFile.lastIndexOf("/") + 1);
                    String log = serveFile(fileName, fullFile, response, fileType);
                    if (log.contains("Error")) {
                        request.setAttribute("msg", log);
                        String url = "/WEB-INF/view/error/error.jsp";
                        RequestDispatcher view = request.getRequestDispatcher(url);
                        view.forward(request, response);
                    }
                }

            } else if (userPath.equals("/getSequenceTaxo")) {
                String rank = request.getParameter("rank");
                String valueRank = request.getParameter("valueRank");
                String ids = request.getParameter("ids");
                SequenceFileCreator sfc = new SequenceFileCreator(transacciones);
                //TODO: ID HC a parallel-meta hacer una variable de sistema o en el path 
                String idanalisis = "2";
                String file = sfc.generaTaxoFileSequence(rank, valueRank, ids,idanalisis);
                response.setContentType("text/fasta");
                response.setHeader("Content-Disposition", "attachment; filename=\"Secuencias_" + valueRank.replaceAll(" ", "_") + ".fasta\"");
                OutputStream outputStream = response.getOutputStream();
                if (file == null || file.length() < 1) {
                    file = "ERROR AL CREAR Archivo\n";
                }
                outputStream.write(file.getBytes());
                outputStream.flush();
                outputStream.close();

            }

        }
    }

    private String serveFile(String fileName, String fullFile, HttpServletResponse response, String responseContentType) {
        BufferedOutputStream bos = null;
        try {
            //   String zipLocation = "C:\\clonas_40d.fsa";
            //  response.setContentType("application/fna");
            response.setContentType(responseContentType);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ";");
            FileInputStream fis = new FileInputStream(fullFile);
            bos = new BufferedOutputStream(response.getOutputStream());

            int len;
            byte[] buf = new byte[1024];

            while ((len = fis.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }
        } catch (FileNotFoundException fnfe) {
            return "Error: El archivo solicitado no se encuentra disponible";
        } catch (IOException ioe) {
            return "Error: Problemas de entrada / salida";

        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                Logger.getLogger(SequenceController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "";
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
            Logger.getLogger(BlastController.class
                    .getName()).log(Level.SEVERE, "Transacciones - testConection", e);

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
