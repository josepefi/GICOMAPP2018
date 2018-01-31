/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import bobjects.Usuario;
import dao.MuestraDAO;
import dao.TaxaDAO;
import database.Transacciones;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import utils.FileFunctions;

/**
 *
 * @author Alejandro
 */
public class TaxaController extends HttpServlet {

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
        if (session == null || session.isNew() || session.getAttribute("userObj") == null) {
            //session expirada o invalida
            String url = "index.jsp";
            //mandar mensaje de session expirada o a página de error / sesión expirada
            request.setAttribute("msg", "Su sesi&oacute;n expir&oacute;");
            request.getRequestDispatcher(url).forward(request, response);
            return;
        }
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
        if (userPath.equals("/searchTaxa")) {
            String taxText = request.getParameter("taxa");
            String createImage = request.getParameter("grafica");
            String campanas = request.getParameter("campanas");
            String source = request.getParameter("source");
            if (source == null || source.length() < 3) {
                source = "AMP";
            }
            //AMP SHOT
            /**
             * La idea de esta bandera es en algún momento validar si el usuario
             * está buscando sobre múltiples campañas o sólo una. En dado caso
             * esta bandera pasa a false, así el nombre de lass estaciones es
             * "tal cual" de lo contrario, se agrega un número equivalente al
             * número de crucero
             */
            boolean multiplesCampanas = true;
            // createImage = "true";
            ServletContext sc = getServletContext();
            //String workingDir = sc.getInitParameter("workingDir");
            String scriptDir = sc.getInitParameter("scriptDir");
            String workingDir = sc.getRealPath("") + "\\filestmp\\";
            String rCommand = sc.getInitParameter("execR");
            workingDir = workingDir.replaceAll("\\\\", "/");
            FileFunctions fu = new FileFunctions();
            fu.validateFile(workingDir, true);
            String taxId = taxText.indexOf("-") != -1 ? taxText.substring(0, taxText.indexOf("-")).trim() : taxText;
            String name = taxText.indexOf("-") != -1 && taxText.indexOf("(") != -1 ? taxText.substring(taxText.indexOf("-") + 1, taxText.indexOf("(")).trim() : taxText;
            String rank = taxText.indexOf("(") != -1 && taxText.indexOf(")") != -1 ? taxText.substring(taxText.indexOf("(") + 1, taxText.indexOf(")")).trim() : taxText;
            TaxaDAO tdao = new TaxaDAO(transacciones);
            ArrayList<ArrayList<String>> tabla;
            if (source.equals("AMP")) {
                String idAnalisis = "2";
                //ACA analisis traer de variable de contexto
                tabla = tdao.getConteosByTaxName(rank, name, idAnalisis);
            } else {
                tabla = tdao.getConteosByTaxNameShotgun(rank, name);
            }
            long total = tdao.getConteos();
            if (tabla != null && tabla.size() > 0) {
                session.setAttribute("tablaTaxa", tabla);
                //número de registros
                //request.setAttribute("count", tabla.size());
                //request.setAttribute("result", tabla);
                //número de secuencias
                request.setAttribute("seqs", "" + total);
                PrintWriter out = response.getWriter();
                response.setContentType("text/html");
                response.setCharacterEncoding("UTF-8");

                out.println("<script>");
                out.println("$(document).ready(function () {");
                out.println("$('#taxonomia').DataTable({");
                out.println("responsive: true");
                out.println("});");
                out.println("});");
                out.println("</script>");

                out.println("<div class=\"panel-body\">");
                out.println("<div class=\"dataTable_wrapper\">");
                out.println("<div class=\"dataTable_wrapper\">");

                //downloadTaxaTable
                out.println("<table width=\"100%\" class=\"table table-striped table-bordered table-hover\" id=\"taxonomia\">");
                out.println("<thead>");
                if (source.equals("AMP")) {
                    out.println("<tr><th></th><th>Marcador</th><th>Muestra</th><th>Tipo</th><th>Profundidad</th><th>Secuencias</th><th>A.Relativa %</th></tr>");
                } else {
                    out.println("<tr><th></th><th>Metagenoma</th><th>Muestra</th><th>Tipo</th><th>Profundidad</th><th>Secuencias</th><th>A.Relativa %</th></tr>");
                }
                out.println("</head>");
                out.println("<tbody>");
                for (ArrayList<String> bg : tabla) {

                    out.println("<tr>");

                    if (source.equals("AMP")) {
                        out.println("<td style='text-align:center;'><input type='checkbox' value='" + bg.get(0) + "' name='checkmarcador'></td>");
                        out.println("<td><a href = 'showMarcador?idMarcador=" + bg.get(0) + "' target='_blank'>" + bg.get(1) + "</a></td>");//0 y 1
                    } else {
                        out.println("<td style='text-align:center;'><input type='checkbox' disabled value='" + bg.get(0) + "' name='checkmarcador'></td>");
                        out.println("<td><a href = 'showMetagenoma?idMetagenoma=" + bg.get(0) + "' target='_blank'>" + bg.get(1) + "</a></td>");//0 y 1
                    }
                    out.println("<td><a href = 'showMuestra?idMuestra=" + bg.get(2) + "' target='_blank'>" + bg.get(3) + "</a></td>");
                    out.println("<td>" + bg.get(4) + "</td>");
                    out.println("<td>" + bg.get(5) + " - " + bg.get(6) + "</td>");//5y6
                    out.println("<td>" + bg.get(7) + "</td>");//7
                    out.println("<td>" + bg.get(8) + "</td>");//8
                    out.println("</tr>");
                }
                out.println("</tbody>");
                out.println("</table>");

                out.println("<button onclick=\"window.location.href='downloadTaxaTable'\"  class=\"btn btn-primary glyphicon glyphicon-download-alt\" id='ObtenerVal'> Descargar Tabla</button>");
                if (source.equals("AMP")) {//sólo para marcadores se despliega la opción de descargar secuencias
                    out.println("<button onclick=\"descargaSeqsMarcador('" + rank + "','" + name + "')\" class=\"btn btn-primary glyphicon glyphicon-download-alt\" id='ObtenerVal'> Descargar secuencias</button>");
                }
                out.println("</div>");
                out.println("</div>");
                if (createImage != null && createImage.equals("true")) {
                    ArrayList<String> files = tdao.createArchivosAbundancia(tabla, workingDir, multiplesCampanas);
                    boolean findFirst = false;
                    String active = "class=\"active\"";
                    String active2 = "class=\"tab-pane fade in active \"";
                    StringBuilder headerTab = new StringBuilder();
                    StringBuilder tabs = new StringBuilder();
                    for (String file : files) {
                        try {
                            // System.out.println("Rscript c:\\Users\\Alejandro\\Documents\\Cursos\\R\\Project\\release\\taxascript.R " + workingDir + fileName + " " + longitudInicial + " " + longitudFinal + " " + latitudInicial + " " + latitudFinal + " " + cellWG + " " + cellHG + " " + fDir);
                            //Process proc = Runtime.getRuntime().exec("c:/Program Files/R/R-3.0.3/bin/Rscript c:/Users/Alejandro/Documents/Cursos/R/Project/release/taxascript.R " + workingDir + fileName + " " + longitudInicial + " " + longitudFinal + " " + latitudInicial + " " + latitudFinal + " " + cellW + " " + cellH + " " + fDir+" "+taxaGroups);
                            //String commandLine = "c:/Program Files/R/R-3.0.3/bin/Rscript \"" + workingDir + "scripts/scriptDiversidad.R\" \"" + workingDir + "\" " + nameMatriz + " " + sc.getRealPath("") + fileNameRare + " " + sc.getRealPath("") + fileNameRenyi + " " + betaIndex + " " + sc.getRealPath("") + fileNameBeta + " " + imgExtraTitle;        
                            String imageName = file.substring(0, file.lastIndexOf(".")) + ".png";

                            Process proc = Runtime.getRuntime().exec(rCommand + " " + scriptDir + "script_abundancia.R " + workingDir + " " + scriptDir + " " + file + " " + imageName);
                            //Process proc = Runtime.getRuntime().exec("c:/Program Files/R/R-3.0.3/bin/Rscript " + scriptDir + "script_abundancia.R " + workingDir +" "+ scriptDir + " " +file + " " + imageName);
                            proc.waitFor();
                            InputStream inputstream = proc.getInputStream();
                            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
                            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
                            String line = "";
                            while ((line = bufferedreader.readLine()) != null) {
                                //Si el mensaje de error viene desde nuestro script de R
                                //es decir un error controlado
                                //   System.out.println(line);
                                if (line.indexOf("ERROR") != -1) {
                                    System.err.println(line);
                                }
                            }
                            //
                            if (proc.waitFor() != 0) {
                                System.err.println("exit value = " + proc.exitValue());
                            } else {

                                if (!findFirst) {
                                    headerTab.append("<div class=\"panel panel-default\">");
                                    headerTab.append("<div class=\"panel-body\">");
                                    headerTab.append("<ul class=\"nav nav-tabs\">");
                                }
                                if (imageName.contains("SED")) {
                                    //out.println("<li class=\"active\"><a href=\"#sedimentos\" data-toggle=\"tab\">SEDIMENTOS</a></li>");
                                    headerTab.append("<li ").append(active).append("><a href=\"#sedimentos\" data-toggle=\"tab\">SEDIMENTOS</a></li>");
                                    tabs.append("<div ").append(active2).append(" id=\"sedimentos\">");
                                    tabs.append("<br>");
                                    tabs.append("<div align=\"center\">");
                                    tabs.append(" <h4>ABUNDANCIA EN SEDIMENTOS</h4>");
                                    tabs.append("<img src=\"filestmp/").append(imageName).append("\" style=\"width:40%;cursor:zoom-in\"\n" + "onclick=\"document.getElementById('modal01').style.display = 'block'\">\n" + "<div id=\"modal01\" class=\"w3-modal\" onclick=\"this.style.display = 'none'\">\n" + "<span class=\"w3-closebtn w3-hover-red w3-container w3-padding-16 w3-display-topright\">&times;</span>\n" + "<div class=\"w3-modal-content w3-animate-zoom\">\n" + "<img src=\"filestmp/").append(imageName).append("\" style=\"width:100%\">\n</div>\n</div></div></div>");

                                } else if (imageName.contains("MIN")) {
                                    headerTab.append("<li ").append(active).append("><a href=\"#minoxi\" data-toggle=\"tab\">MÍNIMO DE OXÍGENO</a></li>");
                                    tabs.append("<div ").append(active2).append(" id=\"minoxi\">");
                                    tabs.append("<br>");
                                    tabs.append("<div align=\"center\">");
                                    tabs.append(" <h4>ABUNDANCIA EN MINIMO DE OXIGENO</h4>");
                                    tabs.append("<img src=\"filestmp/").append(imageName).append("\" style=\"width:40%;cursor:zoom-in\"\n" + "onclick=\"document.getElementById('modal02').style.display = 'block'\">\n" + "<div id=\"modal02\" class=\"w3-modal\" onclick=\"this.style.display = 'none'\">\n" + "<span class=\"w3-closebtn w3-hover-red w3-container w3-padding-16 w3-display-topright\">&times;</span>\n" + "<div class=\"w3-modal-content w3-animate-zoom\">\n" + "<img src=\"filestmp/").append(imageName).append("\" style=\"width:100%\">\n</div>\n</div></div></div>");

                                } else if (imageName.contains("MAX")) {
                                    headerTab.append("<li ").append(active).append("><a href=\"#maxfluo\" data-toggle=\"tab\">MÁXIMO DE FLUORESCENCIA</a></li>");
                                    tabs.append("<div ").append(active2).append(" id=\"maxfluo\">");
                                    tabs.append("<br>");
                                    tabs.append("<div align=\"center\">");
                                    tabs.append(" <h4>ABUNDANCIA EN MÁXIMO DE FLUORESCENCIA</h4>");
                                    tabs.append("<img src=\"filestmp/").append(imageName).append("\" style=\"width:40%;cursor:zoom-in\"\n" + "onclick=\"document.getElementById('modal03').style.display = 'block'\">\n" + "<div id=\"modal03\" class=\"w3-modal\" onclick=\"this.style.display = 'none'\">\n" + "<span class=\"w3-closebtn w3-hover-red w3-container w3-padding-16 w3-display-topright\">&times;</span>\n" + "<div class=\"w3-modal-content w3-animate-zoom\">\n" + "<img src=\"filestmp/").append(imageName).append("\" style=\"width:100%\">\n</div>\n</div></div></div>");
                                } else if (imageName.contains("MIL")) {
                                    //out.println("ABUNDANCIA EN MIL METROS");
                                    headerTab.append("<li ").append(active).append("><a href=\"#milmetros\" data-toggle=\"tab\">MIL METROS</a></li>");
                                    tabs.append("<div ").append(active2).append(" id=\"milmetros\">");
                                    tabs.append("<br>");
                                    tabs.append("<div align=\"center\">");
                                    tabs.append(" <h4>ABUNDANCIA EN MIL METROS</h4>");
                                    tabs.append("<img src=\"filestmp/").append(imageName).append("\" style=\"width:40%;cursor:zoom-in\"\n" + "onclick=\"document.getElementById('modal04').style.display = 'block'\">\n" + "<div id=\"modal04\" class=\"w3-modal\" onclick=\"this.style.display = 'none'\">\n" + "<span class=\"w3-closebtn w3-hover-red w3-container w3-padding-16 w3-display-topright\">&times;</span>\n" + "<div class=\"w3-modal-content w3-animate-zoom\">\n" + "<img src=\"filestmp/").append(imageName).append("\" style=\"width:100%\">\n</div>\n</div></div></div>");
                                } else if (imageName.contains("FON")) {
                                    headerTab.append("<li ").append(active).append("><a href=\"#fondo\" data-toggle=\"tab\">FONDO</a></li>");
                                    tabs.append("<div ").append(active2).append(" id=\"fondo\">");
                                    tabs.append("<br>");
                                    tabs.append("<div align=\"center\">");
                                    tabs.append(" <h4>ABUNDANCIA EN FONDO</h4>");
                                    tabs.append("<img src=\"filestmp/").append(imageName).append("\" style=\"width:40%;cursor:zoom-in\"\n" + "onclick=\"document.getElementById('modal05').style.display = 'block'\">\n" + "<div id=\"modal05\" class=\"w3-modal\" onclick=\"this.style.display = 'none'\">\n" + "<span class=\"w3-closebtn w3-hover-red w3-container w3-padding-16 w3-display-topright\">&times;</span>\n" + "<div class=\"w3-modal-content w3-animate-zoom\">\n" + "<img src=\"filestmp/").append(imageName).append("\" style=\"width:100%\">\n</div>\n</div></div></div>");
                                }
                                if (!findFirst) {
                                    findFirst = true;
                                    active = "";
                                    active2 = "class=\"tab-pane faden \"";
                                }

                                //out.println("<img src='filestmp/" + imageName + "'  width='500px' height='500px' >");
                                //out.println("</div>");
                            }
                        } catch (Exception e) {
                            System.err.println("" + e.getLocalizedMessage());
                            e.printStackTrace();
                        }
                    }
                    //cerramos el header
                    headerTab.append("</ul>");
                    headerTab.append("<div class=\"tab-content\">");
                    tabs.append("</div>");
                    tabs.append("</div> ");
                    out.println(headerTab);
                    out.println(tabs);
                }
                out.close();
            } else {
                // request.setAttribute("count", "" + 0);
                // request.setAttribute("seqs", "" + total);
                PrintWriter out = response.getWriter();
                response.setContentType("text/html");
                response.setCharacterEncoding("UTF-8");

                out.println("<p style=\"color:red;\">No hay resultados</p>");

                out.close();
            }
            //String url = "/WEB-INF/view/taxa/taxonomy.jsp";
            //request.getRequestDispatcher(url).forward(request, response);
            //return;
        } else if (userPath.equals("/taxonomia")) {
            RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/taxa/taxonomy.jsp");
            view.forward(request, response);
            return;
        } else if (userPath.equals("/downloadTaxaTable")) {
            Object tablaTaxa = session.getAttribute("tablaTaxa");
            if (tablaTaxa != null) {
                ArrayList<ArrayList<String>> tabla = (ArrayList<ArrayList<String>>) tablaTaxa;
                StringBuilder resTabla = new StringBuilder("Marcador\tMuestra\tTipo de Muestra\tProfundidad\tSecuencias\tAbundancia Relativa\n");
                for (ArrayList<String> bg : tabla) {
                    resTabla.append(bg.get(1)).append("\t").append(bg.get(3))
                            .append("\t").append(bg.get(4)).append("\t").append(bg.get(5))
                            .append(" - ").append(bg.get(6)).append("\t").append(bg.get(7))
                            .append("\t").append(bg.get(8)).append("\n");
                }
                response.setContentType("text/tsv");
                response.setHeader("Content-Disposition", "attachment; filename=\"TablaAbundancia.tsv\"");
                OutputStream outputStream = response.getOutputStream();
                // String outputResult = "xxxx, yyyy, zzzz, aaaa, bbbb, ccccc, dddd, eeee, ffff, gggg\n";
                outputStream.write(resTabla.toString().getBytes());
                outputStream.flush();
                outputStream.close();
            } else {
                response.setContentType("text/tsv");
                response.setHeader("Content-Disposition", "attachment; filename=\"TablaAbundancia.tsv\"");
                OutputStream outputStream = response.getOutputStream();
                String outputResult = "ERROR AL CREAR LA TABLA\n";
                outputStream.write(outputResult.getBytes());
                outputStream.flush();
                outputStream.close();
            }
        } else if (userPath.equals("/creaMatriz")) {
            String nivelTaxonomico = request.getParameter("nivel");
            String source = request.getParameter("source");
            String idMarcador = request.getParameter("idSrc");
            String cabecera = request.getParameter("cabecera");
            String archivo = request.getParameter("toFile");
            String normalizar = request.getParameter("norm");
            String degradadores = request.getParameter("degradadores");

            boolean isAmplicon = false;
            if (source == null || source.length() < 1 || source.equals("amplicon")) {
                isAmplicon = true;
            }
            boolean norm = true;
            if (normalizar != null && normalizar.equals("crudo")) {
                norm = false;
            }
            boolean withHeader = false;
            if (cabecera != null && cabecera.equals("true")) {
                withHeader = true;
            }
            boolean toFile = false;
            if (archivo != null && archivo.equals("true")) {
                toFile = true;
            }
            String orgName = request.getParameter("orgName");
            if (orgName == null || orgName.length() < 1) {
                orgName = "taxon";
            }

            response.setContentType("text/tsv");

            String matriz = "";
            if (nivelTaxonomico != null && idMarcador != null) {
                TaxaDAO tdao = new TaxaDAO(transacciones);
                if (degradadores != null && degradadores.equals("true") && nivelTaxonomico.equals("genus")) {
                    tdao.setSoloDegradadores(true);
                }
                //TODO: traer variable tipo de analisis taxonomico
                String idanalisis = "2";//HC a ParallelMeta
                matriz = tdao.generaMatrizAbundancia(nivelTaxonomico, idMarcador, orgName, withHeader, norm, toFile, isAmplicon, idanalisis);
                if (toFile) {
                    response.setHeader("Content-Disposition", "attachment; filename=\"MatrizAbundancia_" + nivelTaxonomico + "_" + idMarcador + ".tsv\"");
                    OutputStream outputStream = response.getOutputStream();
                    // String outputResult = "xxxx, yyyy, zzzz, aaaa, bbbb, ccccc, dddd, eeee, ffff, gggg\n";
                    outputStream.write(matriz.getBytes());
                    outputStream.flush();
                    outputStream.close();
                } else {
                    request.setAttribute("tabla", matriz);
                    RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/taxa/matriz.jsp");
                    view.forward(request, response);
                    return;
                }
            } else {

                matriz = "NO DATA";
                response.setHeader("Content-Disposition", "attachment; filename=\"ErrorMatriz.tsv\"");
                OutputStream outputStream = response.getOutputStream();
                // String outputResult = "xxxx, yyyy, zzzz, aaaa, bbbb, ccccc, dddd, eeee, ffff, gggg\n";
                outputStream.write(matriz.getBytes());
                outputStream.flush();
                outputStream.close();

            }

        } else if (userPath.equals("/matrices")) {
            TaxaDAO tdao = new TaxaDAO(transacciones);
            //tdao.generaTablaDegradadores();
            String htmlTable = tdao.generaTablaDegradadores(); //va desde <table> hasta <table>          
            request.setAttribute("tabla", htmlTable);

            RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/taxa/matrices.jsp");
            view.forward(request, response);
            return;
        } else if (userPath.equals("/matrizFactory")) {
            String nivelTaxonomico = request.getParameter("nivel");
            String trimName = request.getParameter("trim");
            String tipoProfundidad = request.getParameter("tipoProfundidad");//"SED-FON";//
            String campanas = request.getParameter("campanas");//"2-3";//2=mmf
            String normalizar = request.getParameter("conteos");
            String orgName = request.getParameter("orgName");
            String xtraPoled = request.getParameter("xtraPoled");
            String degradadores = request.getParameter("degradadores");
            String toFile = request.getParameter("toFile");
            String provincias = request.getParameter("provincias");//"2-3";//2=mmf
            boolean toHTML = false;
            if (toFile != null && toFile.equals("false")) {
                toHTML = true;
            }
            boolean trim = true;
            if (trimName != null && trimName.equals("false")) {
                trim = false;
            }
            boolean norm = true;
            if (normalizar != null && normalizar.equals("CRUDO")) {
                norm = false;
            }
            boolean xtra = false;
            if (xtraPoled != null && xtraPoled.equals("true")) {
                xtra = true;
            }
            if (orgName == null || orgName.length() < 1) {
                orgName = "TAXON";
            }

            String matriz = "";
            if (nivelTaxonomico != null) {
                TaxaDAO tdao = new TaxaDAO(transacciones);
                if (degradadores != null && degradadores.equals("true") && nivelTaxonomico.equals("genus")) {
                    tdao.setSoloDegradadores(true);
                }
                if (xtra) {
                    matriz = tdao.generaMatrizFiltrosXtraPoledHTML(nivelTaxonomico, trim, tipoProfundidad, campanas, provincias, orgName, norm, toHTML);
                } else {
                    //matriz = tdao.generaMatrizFiltros(nivelTaxonomico, trim, tipoProfundidad, campanas, orgName, norm);
                    matriz = tdao.generaMatrizFiltrosHTML(nivelTaxonomico, trim, tipoProfundidad, campanas, provincias, orgName, norm, toHTML);
                }
                // response.setHeader("Content-Disposition", "attachment; filename=\"MatrizAbundancia.tsv\"");
            } else {
                matriz = "NO DATA";

            }
            // boolean toHTML = true;
            if (toHTML) {
                request.setAttribute("tabla", matriz);
                RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/taxa/matriz.jsp");
                view.forward(request, response);
                return;
            } else {
                response.setContentType("text/tsv");
                response.setHeader("Content-Disposition", "attachment; filename=\"MatrizAbundancia.tsv\"");
                OutputStream outputStream = response.getOutputStream();
                // String outputResult = "xxxx, yyyy, zzzz, aaaa, bbbb, ccccc, dddd, eeee, ffff, gggg\n";
                outputStream.write(matriz.getBytes());
                outputStream.flush();
                outputStream.close();
            }
        } else if (userPath.equals("/degradadores")) {
            TaxaDAO tdao = new TaxaDAO(transacciones);
            tdao.generaTablaDegradadores();
            String htmlTable = tdao.generaTablaDegradadores(); //va desde <table> hasta <table>
            /*
             request.setAttribute("tabla", matriz);
             RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/view/taxa/matriz.jsp");
             view.forward(request, response);
             return;
             */
        } else if (userPath.equals("/agregaTaxon")) {
            String taxText = request.getParameter("taxa");
            String taxId = taxText.indexOf("-") != -1 ? taxText.substring(0, taxText.indexOf("-")).trim() : taxText;
            ArrayList<ArrayList> agregar = transacciones.getAgregarTaxon(taxId);

            PrintWriter out = response.getWriter();
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");

            for (ArrayList<String> ag : agregar) {
                out.println("<tr>");
                out.println("<td>" + ag.get(0) + "</td>");
                out.println("<td>" + ag.get(1) + "</td>");
                out.println("<td>" + ag.get(2) + "</td>");
                out.println("</tr>");
            }
            out.close();

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
