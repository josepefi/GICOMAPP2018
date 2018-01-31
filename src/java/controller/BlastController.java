/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import bobjects.Usuario;
import dao.GenomaDAO;
import dao.JobDAO;
import dao.MetagenomaDAO;
import database.Transacciones;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import job.BlastProcess;
import job.BlastProperties;
import job.BlastResult;
import job.Job;


@MultipartConfig
public class BlastController extends HttpServlet {

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

         HttpSession session = request.getSession(false);
        if (session  == null || session.isNew() || session.getAttribute("userObj") == null) {
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
            if (userPath.equals("/blast")) {

                GenomaDAO genomaDAO = new GenomaDAO(transacciones);
                MetagenomaDAO metaDAO = new MetagenomaDAO(transacciones);

                ArrayList<ArrayList<String>> genomas = genomaDAO.getGenomasBlast("ORDER BY genome_name");
                String where = " WHERE gen_num_total>0 AND visible = true ORDER BY meta_name"; //a cambiarr: "where seq_num_total >0";
                ArrayList<ArrayList<String>> metagenomas = metaDAO.getMetagenomasBlast(where);
                request.setAttribute("genomas", genomas);
                request.setAttribute("metagenomas", metagenomas);
                // Falta tabla de JOBS
                JobDAO jDao = new JobDAO(transacciones);
                ArrayList<Job> jobs = jDao.getUserJobs(user.getIdUsuario());
                request.setAttribute("jobs", jobs);
                String url = "/WEB-INF/view/blast/blast.jsp";
                request.getRequestDispatcher(url).forward(request, response);
            } else if (userPath.equals("/blastSearch")) {
                //IMPLEMENTAR leer PARAMETROS idgenomas idmetagenomas
                ServletContext sc = getServletContext();
                //directorio donde estan todos los blast por job
                //local: C:/Users/Alejandro/Documents/Projects/GCT3NG/blast/
                //akai /home/abdala/blast/
                String blastdir = sc.getInitParameter("blastdir");
                String blastdbs = sc.getInitParameter("blastdbs");
                String outFile = sc.getInitParameter("jobOutFile");
                String metagenomas = request.getParameter("idmetagenomas");
                String genomas = request.getParameter("idgenomas");
                String analysisType = request.getParameter("algoritmo");
                String evalue = request.getParameter("evalue");
                String host = request.getRemoteHost();
                PrintWriter outPW = response.getWriter();
                double eval;
                try {
                    eval = Double.parseDouble(evalue);
                } catch (Exception e) {//null pointer o number format
                    eval = 10;//default
                }

                String jobName = request.getParameter("name");
                String inputType = request.getParameter("inputType");
                Transacciones blastTransacciones = getNewConexion();
                //el proceso de blast se lleva la conexion, durante su ejecución 
                //actualiza datos en la BD y al final la conexin se muere, por eso es
                //necesario crear una nueva conex y pasarsela al jobDAO el cual es el
                //objeto que proporciona conectividad a la BD a BlastProcess.
                JobDAO job = new JobDAO(blastTransacciones);
                String jobURL = "";
                if (inputType.equals("file")) {
                    Collection<Part> parts = request.getParts();
                    Part filePart = null;
                    String partName;
                    //iteramos y buscamos solo los que sean archivos
                    for (Part part : parts) {
                        if (part.getName().startsWith("seqFile")) {
                            filePart = part;
                            partName = getFilename(part);
                            break;
                        }
                    }
                    boolean withIntergenic = false;
                    if (analysisType.toLowerCase().equals("blastn")) {
                        withIntergenic = true;
                    }
                    jobURL = job.saveFastaFile(user.getIdUsuario(), jobName, analysisType, eval, blastdir, "query.fasta", host, filePart, metagenomas, genomas, blastdbs, withIntergenic);

                } else {
                    String seq = request.getParameter("seq");

                    boolean withIntergenic = false;
                    if (analysisType.toLowerCase().equals("blastn")) {
                        withIntergenic = true;
                    }
                    jobURL = job.writeFastaFile(user.getIdUsuario(), jobName, analysisType, eval, blastdir, "query.fasta", host, seq, metagenomas, genomas, blastdbs, withIntergenic);

                }
                if (jobURL.toLowerCase().indexOf("error") != -1) {
                    outPW.print(jobURL);//jobUID trae error
                    outPW.close();
                    return;
                } else {
                    String id = jobURL.substring(0, jobURL.indexOf("-"));
                    String uID = jobURL.substring(jobURL.indexOf("-") + 1);

                    String workingDir = blastdir + id + "/";
                    String cmd = sc.getInitParameter(analysisType.toLowerCase());
                    BlastProperties props = new BlastProperties(workingDir, cmd);
                    String dbPath = "\'" + job.getMetagenoma_dbs() + " " + job.getGenoma_dbs() + "\'";
                    for (String db : job.getMetagenoma_dbs().split(" ")) {
                        if (db.trim().length() > 1) {
                            props.insertDataBase(db);
                        }
                    }
                    for (String db : job.getGenoma_dbs().split(" ")) {
                        if (db.trim().length() > 1) {
                            props.insertDataBase(db);
                        }
                    }
                    props.setDatabase(dbPath);
                    props.setQuery("query.fasta");
                    props.setOut_file(outFile);
                    props.setEvalueFromString(evalue);
                    //props.setOutfmt("\"6 qseqid sseqid pident length mismatch gapopen qstand sstart send  evalue bitscore stitle\"");
                    props.setOutfmt("6");
                    BlastProcess blast = new BlastProcess(id);
                    blast.setProperties(props);
                    blast.setjDao(job);
                    Thread blastThread = new Thread(blast);
                    blastThread.start();
                    outPW.print(uID);
                    outPW.close();
                    //response.sendRedirect("showJob?jobURL=" + uID);
                }
            } else if (userPath.equals("/showJob")) {
                //PrintWriter outPW = response.getWriter();
                String idJob = request.getParameter("jobURL");
                JobDAO jDao = new JobDAO(transacciones);
                Job job = jDao.initJobObject(idJob);
                if (job == null) {
                    String url = "/WEB-INF/view/error/error.jsp";
                    request.setAttribute("msg", "Error inicializando BLAST Job: " +idJob + "<br>Por favor comunicarse con el administrador del sistema ");
                    RequestDispatcher view = request.getRequestDispatcher(url);
                    view.forward(request, response);
                    return;
                }
                request.setAttribute("job", job);
                if (job.getStatus().equals("Terminado")) {
                    ServletContext sc = getServletContext();
                    String blastdir = sc.getInitParameter("blastdir");
                    String outFile = sc.getInitParameter("jobOutFile");
                    String workingDir = blastdir + job.getId_job() + "/";
                    //  String workingDir = j
                    //esto es muy importante pues crea el prepared statement que se encargara de traer toda la info complementaria del BLAST
                    jDao.compilaQueryGenInfo();
                    ArrayList<BlastResult> results = jDao.readBlastOutFile(workingDir + outFile + ".sorted", 100);
                    if (results != null) {
                        request.setAttribute("table", results);
                    }
                    /* for(BlastResult result: results){
                     outPW.print(result.getQuery() + " \t " + result.getTarget());
                     }
                     outPW.close();*/
                }
                request.setAttribute("job", job);
                RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/blast/blastJob.jsp");
                view.forward(request, response);
            } else if (userPath.equals("/deleteJob")) {
                PrintWriter outPW = response.getWriter();
                //String idJob = request.getParameter("id");
                String urlJob = request.getParameter("url");            
                JobDAO jDao = new JobDAO(transacciones);
                String log = jDao.deleteJob(urlJob);
                if (log.length() == 0) {
                    outPW.close();
                } else {
                    outPW.print(log);
                    outPW.close();
                }
            }
        }
    }

    private static String getFilename(Part part) {
        // System.out.println("Request header");
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
            }
            // System.out.println(cd);

        }
        return null;
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
    }

}
