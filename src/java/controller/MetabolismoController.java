/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.MetMapDAO;
import bobjects.Usuario;
import dao.ListaDAO;
import dao.StateMap;
import database.Transacciones;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.Colores;

/**
 *
 * @author Jose PeFi
 */
public class MetabolismoController extends HttpServlet {

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
        } else if (userPath.equals("/metabolismo")) {
            String idLista = "2";
            String titulo = "";
            String where = "";
            ListaDAO listaDAO = new ListaDAO(transacciones);
            ArrayList<ArrayList> listasGenomas = listaDAO.getListaGenomas(where);
            request.setAttribute("listaGenomas", listasGenomas);
            RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/WEB-INF/view/metabolismo/metabolismo.jsp");
            dispatch.forward(request, response);

        } else if (userPath.equals("/enzimas")) {
            String where = "";
            ListaDAO listaDAO = new ListaDAO(transacciones);
            ArrayList<ArrayList> listasGenomas = listaDAO.getListaGenomas(where);
            request.setAttribute("listaGenomas", listasGenomas);
            RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/WEB-INF/view/metabolismo/enzimas.jsp");
            dispatch.forward(request, response);

        } else if (userPath.equals("/nivel2")) {

            String nivel2 = request.getParameter("clave");
            int claveNivel = Integer.parseInt(nivel2);
            ArrayList<ArrayList> catNivel2 = transacciones.getCat1Metabolismo(claveNivel);

            for (ArrayList<ArrayList> n2 : catNivel2) {

                //System.out.println("Es visible:" + n2.get(2));
            }
            PrintWriter out = response.getWriter();
            response.setContentType("text/html");

            response.setCharacterEncoding("UTF-8");
            out.println("<div class=\"form-group\" id=\"nivel2\">");
            out.println("<label>Rutas Metabolicas</label>");
            out.println("<select class=\"form-control\" name=\"nivel2\">");
            out.println("<option value=\"-1\">Seleccionar...</option>");
            for (ArrayList<ArrayList> n2 : catNivel2) {

                out.println("<option value=\" " + n2.get(0) + "-" + n2.get(2) + " \">" + n2.get(1) + "</option>");

                //System.out.println("Resultado nivel 2:"+n2);
            }
            out.println("</select>");
            out.println("</div>");

            out.close();
        } else if (userPath.equals("/generarVia")) {

            String met = request.getParameter("metagenoma");

            String string = request.getParameter("nivel2");
            String[] claveVisible = string.split("-");
            String clave2 = claveVisible[0];
            String visible = claveVisible[1];
            String clave22 = clave2.replace(" ", "");
            String visible2 = visible.replace(" ", "");

            String img = clave22.substring(2);

            Integer metodo = Integer.parseInt(request.getParameter("metodo"));

            String[] parts = met.split(",");

            String[] mets = new String[parts.length];

            for (int i = 0; i < parts.length; i++) {
                mets[i] = parts[i];
                //System.out.println("El arreglo tiene:" + mets[i]);
            }

            MetMapDAO mMap = new MetMapDAO(mets, clave22, metodo);
            List<String> claves = mMap.getKeys();
            int L = mMap.getDataColCount();

            PrintWriter out = response.getWriter();
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");

            out.println("<script>");
            out.println("$(document).ready(function () {");
            out.println("$('#tabla-resultados').DataTable({");
            out.println("responsive: true,");
            out.println("pageLength: 10");
            out.println("});");
            out.println("});");
            out.println("</script>");

            //System.out.println("Numero de filas" + mMap.getRowCount());
            if (mMap.getRowCount() == 0) {
                out.println("<div class=\"panel-body\" >\n"
                        + "<div class=\"dataTable_wrapper\">\n"
                        + "<table width=\"100%\" class=\"table table-striped table-bordered \" id=\"tabla-resultados\">\n"
                        + "<thead>\n"
                        + "<tr>\n"
                        + "<th>EC</th>\n"
                        + "<th>Enzima</th>\n"
                );
                for (int k = 0; k < L; k++) {
                    out.println("<th>" + mMap.getColTitle(k) + "</th\n>");
                }
                out.println("</tr>\n"
                        + "</thead>\n"
                        + "</tbody>\n"
                        + "</table>\n"
                        + "</div>\n"
                        + "</div>");

                out.println("<div class=\"alert alert-danger\" style=\" font-size:14px; text-align:center;\">Su consulta no arrojo resultados </div>");

            } else {

                //Procesamiento del dibujo de las cajas
                StateMap sMap = new StateMap(clave22, L);

                mMap.fixIntervals(4);

                //mMap.fixIntervals(0, 4);  //Fija los intervalos para la serie 0
                //mMap.fixIntervals(1, 4); //Fija los intervalos para la serie 1
                int nBoxes = sMap.getNumBoxes() - 1; //Obtienene el numero de cajas a dibujar

                out.println("<div class=\"panel-body\" >\n"
                        + "<div class=\"dataTable_wrapper\">\n"
                        + "<table width=\"100%\" class=\"table table-striped table-bordered \" id=\"tabla-resultados\">\n"
                        + "<thead>\n"
                        + "<tr>\n"
                        + "<th>EC</th>\n"
                        + "<th>Enzima</th>\n"
                );
                for (int k = 0; k < L; k++) {
                    out.println("<th>" + mMap.getColTitle(k) + "</th\n>");
                }
                out.println("</tr>\n"
                        + "</thead>\n"
                        + "<tbody>");
                for (String s : claves) {
                    String stRow = "<tr><td style=\"\">" + s + "</td>\n"
                            + "<td style=\"word-break: break-all;\">" + mMap.getEnzimeName(s) + "</td>\n";
                    for (int k = 0; k < L; k++) {
                        //System.out.println("Los valores son:" + s + "-" + mMap.getEnzimeName(s) + "-" + mMap.getColValue(s, k));
                        stRow = stRow + "<td style=\"word-break: break-all;\">" + mMap.getColValue(s, k) + "</td>\n";

                    }
                    //System.out.println(stRow);

                    out.println(stRow);
                    //out.println("/tr");
                }
                out.println("</tbody>\n"
                        + "</table>\n"
                        + "</div>\n"
                        + "</div>");

                if ("1".equals(visible2)) {

                    out.println("<div class=\"col-lg-2\">\n"
                            + "<button onclick=\"generaHistograma()\" id=\"generaHistograma\" class=\"btn btn-info btn-lg\">Generar Histograma</button>\n"
                            + "</div>");
                    out.println("<div class=\"col-lg-2\"></div>");
                    out.println("<div class=\"col-lg-2\">\n"
                            + "<button onclick=\"generaImagen()\" id=\"generarImagen\" class=\"btn btn-info btn-lg\">Generar Imagen</button>\n"
                            + "</div>");

                    out.println("<br>");
                    out.println("<hr>");

                } else if ("0".equals(visible2)) {
                    out.println("<div class=\"col-lg-2\">\n"
                            + "<button onclick=\"generaHistograma()\" id=\"generaHistograma\" class=\"btn btn-info btn-lg\">Generar Histograma</button>\n"
                            + "</div>");
                    out.println("<br>");
                    out.println("<hr>");
                }

                out.println("<div id=\"histograma\" style=\"display:;\"><canvas id=\"bar-chart-grouped\" width=\"800\" height=\"350\"></canvas></div>\n"
                        + "                        <br>");
                out.println("<div id=\"rutaImagen\" style=\"display:;\"> <canvas id=\"img\" width=\"1280\" height=\"1100\">\n"
                        + "                        </canvas></div>");

                String labelsss = mMap.getHistogramLabels().replace(" ", "");

                String datos1 = "";
                String datos2 = "";
                String datos3 = "";

                int tam = 0;

                if (L > 3) {
                    tam = 2;
                } else {
                    tam = L - 1;
                }

                switch (tam) {
                    case 0:
                        datos1 = mMap.getHistogramData(0);
                        break;

                    case 1:
                        datos1 = mMap.getHistogramData(0);
                        datos2 = mMap.getHistogramData(1);
                        break;

                    case 2:
                        datos1 = mMap.getHistogramData(0);
                        datos2 = mMap.getHistogramData(1);
                        datos3 = mMap.getHistogramData(2);
                        break;
                }

                String funcion = "new Chart(document.getElementById(\"bar-chart-grouped\"),{type:'bar',data:{" + labelsss + ",datasets:[{" + datos1 + "},{" + datos2 + "},{" + datos3 + "}]},options:{title:{display:true,text:'Diagrama'}}})";

                out.println("<script>");
                out.println("function generaHistograma(){");
                out.println("" + funcion + "");
                out.println("}");
                out.println("</script>");

                out.println("<script>");
                out.println("function generaImagen(){\n"
                        + "                    //recojemos el canvas poniendo la id del canvas html5 para relacionarlo\n"
                        + "                    var canvas = document.getElementById(\"img\");\n"
                        + "                    //Cojemos la 2D para dibujar en él\n"
                        + "                    var context = canvas.getContext(\"2d\");\n"
                        + "                    //creamos la nueva imagen \n"
                        + "                    var img = new Image(1077, 850);\n"
                        + "                    //le decimos la ruta de la imagen\n"
                        + "                    img.src = \"images/map" + img + ".png\";\n"
                        + "\n"
                        + "                    img.onload = function () {\n"
                        + "                        //incluyo la imagen en el canvas\n"
                        + "                        context.drawImage(img, 0, 0);\n"
                        + "                        context.font = \"9px sans-serif\";\n"
                        + "                        context.textAlign = \"center\";\n"
                        + "                        context.textBaseline = \"middle\";\n"
                );
                for (int k = 1; k < nBoxes; k++) {  //La variable k corre sobre el número de cajas
                    String ECStr = sMap.getEC(k); //Obtiene el EC de la k-esima caja
                    if (mMap.containsEC(ECStr)) { //verdadero si el EC obtenido tiene datos
                        for (int i = 0; i < L; i++) { //La variable i corre sobre el número de columnas o series.
                            String val = mMap.getColValue(ECStr, i); //Obtiene el valor de la i-esima columna para ECStr.

                            //System.out.println("Los valores de las comunas son:"+ val);
                            //System.out.println("context.fillStyle=\""+Colores.setHexColor(rank, i)+"\";"); // Obtiene el valor que le corresponde segun la categoria dada en rank a la i-esima columna.
                            //System.out.println("context.fillRect(" + sMap.getSubBox(k, i) + ");"); //Obtiene la fracción de área a para la k-esima caja
                            // System.out.println("context.strokeText(\\\""+ECStr+"\\\", "  + sMap.getBoxMeanPoint(k) +  ");");
                            out.println("context.fillStyle=\"" + mMap.getHexColor(ECStr, i) + "\";\n");
                            out.println("context.fillRect(" + sMap.getSubBox(k, i) + ");\n");
                            out.println("context.strokeText(\"" + ECStr + "\", " + sMap.getBoxMeanPoint(k) + ");\n");
                        }
                        //System.out.println("context.strokeRect("+sMap.getFrameBoxHTML(k)+");"); //Dibuja el marco de la k-esima caja
                        out.println("context.strokeRect(" + sMap.getFrameBoxHTML(k) + ");\n");
                    }
                }

                /**
                 * *****************Comienza procesamiento de la leyenda
                 * HTML**************
                 */
                String legendParam = sMap.getLegendParameters();
                String legendBox = mMap.getLegendBox();
                System.out.println("Cadena1" + legendParam);
                System.out.println("Cadena2" + legendBox);

                out.println("var legendParam = " + legendParam + ";  //Parametros que controlan el aspecto de la leyenda\n"
                        + "var legendBox = " + legendBox + ";  //Datos de la leyenda\n"
                        + "\n"
                        + "for(var i=0;i<legendBox.length;i++){ \n"
                        + "legendBox[i].longitud = (legendBox[i].boxGrad.length*legendParam.wBox+context.measureText(legendBox[i].serieTitle).width+legendParam.dTitle); \n"
                        + "} \n"
                        + "var xP=legendParam.deltaX-legendParam.margin;  \n"
                        + "var l = legendBox.length;\n"
                        + "for(var i = 0; i<l; i++){ \n"
                        + "    xP = xP - legendBox[i].longitud-legendParam.gap; \n"
                        + "} \n"
                        + "xP=xP+legendParam.gap; \n"
                        + "for(var j=0;j<l;j++){\n"
                        + "    var n = legendBox[j].labels.length;\n"
                        + "    for(var i=0;i<n;i++){ \n"
                        + "        var xD=xP+i*legendParam.wBox; \n"
                        + "        if(i<n-1){     \n"
                        + "        var limInf=legendBox[j].labels[i];\n"
                        + "        if (i==0){"
                        + "             limInf=limInf-1;"
                        + "} \n"
                        + "            context.fillStyle=legendBox[j].boxGrad[i]; \n"
                        + "            context.fillRect(xD, legendParam.vertPos, legendParam.wBox, legendParam.hBox); \n"
                        + "            context.textBaseline=\"top\"; \n"
                        + "            context.textAlign=\"center\"; \n"
                        + "            context.strokeText(limInf, xD, legendParam.vertPos + legendParam.hBox+4);\n"
                        + "        } \n"
                        + "        else{ \n"
                        + "            context.strokeText(legendBox[j].labels[i], xD, legendParam.vertPos + legendParam.hBox+4); \n"
                        + "            context.strokeRect(xP-1.8, legendParam.vertPos-0.5, (xD-xP)+1.5, legendParam.hBox+1); \n"
                        + "            context.textBaseline=\"middle\"; \n"
                        + "            context.textAlign=\"start\"; \n"
                        + "            context.strokeText(legendBox[j].serieTitle, xD+legendParam.dTitle, legendParam.vertPos+legendParam.hBox/2);\n"
                        + "        }                     \n"
                        + "    } \n"
                        + "    xP=xP+legendBox[j].longitud+legendParam.gap; \n"
                        + "}");

                /* En el siguinete array  se almacenan todos los parametros que se precisan
            *  para dibujar la leyenda.
            * param[0-(l-1)]=coords horizontales de la primera series.
            * param[l]=coord vertical de la primera serie y0.
            * param[l+1]= altura de los cuadros de las categorias hBox. 
            * param[l+2]= ancho del cuadro para las categorias lcb.
            * param[l+3]= Separacion entre las series dSeries.
                 */
 /*    int[] param = new int[L+4]; 
            // En el siguinte array se guardan la cantidad de categorias por cada serie y 
            // la longitud de cada titulo de serie.
                int[] legendDim = new int[2*L];
                out.println("context.textBaseline=\"top\";");
                for (int j=0;j<L;j++){
                    legendDim[2*j] = mMap.getNumCategorias(j);
                    legendDim[2*j+1]=mMap.getColTitle(j).length();
                }
                param = sMap.setParamLegend(legendDim);
                int yjb = param[L]+param[L+1]+2;//Fijamos aqui la altura a la que se escribe los limites de intervalos.
                int xj;//+10;
                for (int j=0;j<L;j++){//j corre sobre las series
                    int nLegendBoxes;
                    nLegendBoxes = legendDim[2*j];
                    String[] colores = mMap.getColorSerie(j);
                    int wide = nLegendBoxes*param[L+2];
                    xj = param[j];
                    for(int i=0;i<nLegendBoxes;i++){ //i  corre sobre las categorias
                        int xi =xj+i*param[L+2];
                        out.println("context.fillStyle=\""+ colores[i] + "\";");
                        out.println("context.fillRect(" + xi + ", " + param[L] + ", " + param[L+2] + ", " + param[L+1] + ");");
                        out.println("context.strokeText(" + "\""+ mMap.getCategoryValue(j, i)+  "\"" + ", " + xi + ", " + yjb + ");");
                    }
                    if(nLegendBoxes>0){
                    out.println("context.strokeText(\"" + mMap.getCategoryValue(j, nLegendBoxes) + "\" , " + (xj+wide) + ", " + yjb + ");");
                    out.println("context.strokeRect("+ (xj-2) + ", " + (param[L]-1) + ", " + (wide+2) + ", " + param[L+1] + ");");
                    }
                }  */
                //Escribe los títulos de las series.
                /*    out.println("context.textBaseline=\"middle\";");
                out.println("context.textAlign=\"start\";");
                for (int j=0;j<L;j++){//j corre sobre las series
                    int nLegendBoxes;
                    nLegendBoxes = legendDim[2*j];                   
                    int wide = nLegendBoxes*param[L+2];
                    xj = param[j];
                    if (nLegendBoxes>0){
                    out.println("context.strokeText(" + "\"" + mMap.getColTitle(j) + "\", "+ (xj+wide+5) + ", " + (param[L]+param[L+1]/2) + ");");
                    }
                }  */
                //***************FIN del precesamiento de la LEYENDA HTML**************/
                out.println("}\n");

                out.println("");

                out.println("}");
                out.println("</script>");
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
            if (!transacciones.estamosConectados()) {
                return null;
            }
            return transacciones;
            //  transacciones = new Transacciones(datasource.getConnection());
        } catch (Exception e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, "Transacciones - getNewConexion", e);
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
