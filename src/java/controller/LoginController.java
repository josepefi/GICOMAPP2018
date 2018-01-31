package controller;

import bobjects.PuntoMapa;
import bobjects.RegistroResumen;
import bobjects.Usuario;
import dao.CampanaDAO;
import dao.UsuarioDAO;
import database.Transacciones;
import java.io.IOException;
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

/**
 *
 * @author Jose Peralta
 */
public class LoginController extends HttpServlet {

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
        Transacciones transacciones = null;
        if (session != null && !session.isNew()) {
            transacciones = (Transacciones) session.getAttribute("transacciones");
        }
        if (transacciones == null) {
            transacciones = getNewConexion();
            // session.setAttribute("transacciones", transacciones);
        }
        if (transacciones == null || !transacciones.testConnection()) {
            request.setAttribute("msg", "Error al establecer conexion con la Base de datos");
            String url = "index.jsp";
            request.getRequestDispatcher(url).forward(request, response);
            return;
        }

        //AUTENTIFICACION DE USUARIOS
        String userPath = request.getServletPath();
        if (userPath.equals("/log")) {
            session = request.getSession();
            String user = request.getParameter("usuario");
            String pass = request.getParameter("password");

         //   String email = request.getParameter("email");
         //   System.out.println("El email del usuario es :"+email);

            try {
                if (user == null || user.length() < 5 || pass == null || pass.length() < 4) {
                    String url = "index.jsp";
                    // request.setAttribute("msg", "Please enter user name and password");
                    request.setAttribute("msg", "Por favor ingresar usuario y contraseña validos");
                    request.getRequestDispatcher(url).forward(request, response);
                    return;
                }
                UsuarioDAO udao = new UsuarioDAO(transacciones);
                Usuario usuario = udao.authentUser(user, pass);

                if (usuario == null) {
                    // response.sendRedirect("index.jsp");
                    String url = "index.jsp";
                    //request.setAttribute("msg", "Wrong user or password");
                    request.setAttribute("msg", "Usuario o contraseña incorrecta");
                    request.getRequestDispatcher(url).forward(request, response);
                    return;
                } else {
                    session.setAttribute("transacciones", transacciones);
                    session.setAttribute("userObj", usuario);
                    session.setAttribute("terminos", usuario.getTerminos());
                    session.setAttribute("transacciones", transacciones);
                    String idCampanaStr = request.getParameter("idCampana");
                    CampanaDAO campanaDAO = new CampanaDAO(transacciones);
                    int idCampana;
                    if (idCampanaStr == null) {
                        idCampana = campanaDAO.lastIDCampana();
                    } else {
                        try {
                            idCampana = Integer.parseInt(idCampanaStr);
                        } catch (NumberFormatException nfe) {
                            idCampana = campanaDAO.lastIDCampana();
                        }
                    }

                    String searchEstacion = request.getParameter("estacion");
                    boolean puntosXestaciones = true;
                    if (searchEstacion != null && !searchEstacion.equals("estacion")) {
                        puntosXestaciones = false;
                        request.setAttribute("puntosXestacion", "false");
                    } else {
                        request.setAttribute("puntosXestacion", "true");
                    }
                    ArrayList<PuntoMapa> puntos = campanaDAO.getEstacionesCampana(idCampana, puntosXestaciones); //se va al response
                    //ArrayList<PuntoMapa> puntos = campanaDAO.getEstacionesCampana(idCampana); //se va al response
                    //ArrayList<ArrayList<String>> resumenCampana = campanaDAO.getResumenCampana(idCampana);
                    ArrayList<RegistroResumen> resumen = campanaDAO.getResumenCampanaRegistro(idCampana);
                    ArrayList<ArrayList<String>> resumenProducto = campanaDAO.getResumenCampanaByProducto(idCampana);
                    ArrayList<ArrayList<String>> campanas = campanaDAO.getCampanas();
                    ArrayList<ArrayList<String>> campanasid = campanaDAO.getCampanasId(idCampana);
                    request.setAttribute("puntos", puntos);
                    request.setAttribute("resumen", resumen);
                    request.setAttribute("resumenProducto", resumenProducto);
                    request.setAttribute("campana", campanas);
                    request.setAttribute("campanaid", campanasid);
                    RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/WEB-INF/view/home.jsp");
                    dispatch.forward(request, response);
                    return;
                }
            } finally {
                //out.close();
            }
        } else if (userPath.equals("/homeCamp")) {
            if (session == null || session.getAttribute("userObj") == null) {
                String url = "index.jsp";
                //mandar mensaje de session expirada o a página de error / sesión expirada
                request.setAttribute("msg", "Su sesi&oacute;n expir&oacute;");
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }
            String opMapa = request.getParameter("op");
            //  System.out.println("opcionMapa="+opMapa);
            String idCampanaStr = request.getParameter("idCampana");
            //   System.out.println("id="+idCampanaStr);
            CampanaDAO campanaDAO = new CampanaDAO(transacciones);
            int idCampana;
            if (idCampanaStr == null) {
                idCampana = campanaDAO.lastIDCampana();

            } else {
                try {
                    idCampana = Integer.parseInt(idCampanaStr);
                } catch (NumberFormatException nfe) {
                    idCampana = campanaDAO.lastIDCampana();
                }

            }
            String searchEstacion = request.getParameter("estacion");
            boolean puntosXestaciones = true;
            if (searchEstacion != null && !searchEstacion.equals("estacion")) {
                puntosXestaciones = false;
                request.setAttribute("puntosXestacion", "false");
            } else {
                request.setAttribute("puntosXestacion", "true");
            }
            ArrayList<PuntoMapa> puntos = campanaDAO.getEstacionesCampana(idCampana, puntosXestaciones); //se va al response
            //ArrayList<PuntoMapa> puntos = campanaDAO.getEstacionesCampana(idCampana); //se va al response
            ArrayList<RegistroResumen> resumen = campanaDAO.getResumenCampanaRegistro(idCampana);
            // ArrayList<ArrayList<String>> resumenCampana = campanaDAO.getResumenCampana(idCampana);
            ArrayList<ArrayList<String>> campanas = campanaDAO.getCampanas();
            ArrayList<ArrayList<String>> campanasid = campanaDAO.getCampanasId(idCampana);
            ArrayList<ArrayList<String>> resumenProducto = campanaDAO.getResumenCampanaByProducto(idCampana);

            request.setAttribute("opMapa", opMapa);
            request.setAttribute("puntos", puntos);
            //request.setAttribute("resumenCampana", resumenCampana);
            request.setAttribute("resumen", resumen); //
            request.setAttribute("campana", campanas); //llenar la lista
            request.setAttribute("campanaid", campanasid); //para dejar seleccionado la campaña en la lista  
            //request.setAttribute("opMapa", opMapa);
            request.setAttribute("resumenProducto", resumenProducto);
            RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/WEB-INF/view/home.jsp");
            dispatch.forward(request, response);
        } else if (userPath.equals("/actualizaT")) {
            String name = request.getParameter("nombre");
            boolean termino = transacciones.updateTerminos(name);
            session.setAttribute("terminos", "1");
        } else if (userPath.equals("/CerrarSesion")) {
            session.invalidate();
            transacciones.desconecta();
            response.sendRedirect("index.jsp");
        } else if (userPath.equals("/Muestra")) {
            RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/WEB-INF/view/muestra.jsp");
            dispatch.forward(request, response);
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
