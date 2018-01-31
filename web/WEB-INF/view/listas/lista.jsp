<%-- 
    Document   : lista
    Created on : 6/06/2017, 12:32:38 PM
    Author     : Jose Pefi
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="bobjects.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession sesion = request.getSession(false);
    // response.setHeader("Cache-Control", "no-cache");
    // response.setHeader("Cache-Control", "no-store");
    // response.setHeader("Pragma", "no-cache");
    // response.setDateHeader("Expires", 0);
    if (session == null) {
        response.sendRedirect("index.jsp");
    }
    Usuario usuario = (Usuario) sesion.getAttribute("userObj");
    String nombreCompleto = usuario.getNombres() + " " + usuario.getApellidos();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Bootstrap Core CSS -->
        <link href="bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- MetisMenu CSS -->
        <link href="bower_components/metisMenu/dist/metisMenu.min.css" rel="stylesheet">
        <!-- Timeline CSS -->
        <link href="dist/css/timeline.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link href="dist/css/sb-admin-2.css" rel="stylesheet">
        <!-- Custom Fonts -->
        <link href="bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">


        <!-- DataTables CSS -->
        <link href="bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.css" rel="stylesheet">
        <!-- DataTables Responsive CSS -->
        <link href="bower_components/datatables-responsive/css/dataTables.responsive.css" rel="stylesheet">     

        <!-- jQuery -->
        <script src="bower_components/jquery/dist/jquery.min.js"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

        <!-- Metis Menu Plugin JavaScript -->
        <script src="bower_components/metisMenu/dist/metisMenu.min.js"></script>

        <!-- Custom Theme JavaScript -->
        <script src="dist/js/sb-admin-2.js"></script>
        

        <!-- DataTables JavaScript -->
        <script src="bower_components/datatables/media/js/jquery.dataTables.min.js"></script>
        <script src="bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.min.js"></script>
        <script src="bower_components/datatables-responsive/js/dataTables.responsive.js"></script>  
        <script src="js/creaMatriz.js"></script> 
      
                <!--ESCRIPT QUE ACTIVA EL BUSCADOR EN LA TABLA MIS BUSQUEDAS-->
        <script>
            $(document).ready(function () {
                $('#tabla-listas').DataTable({
                    responsive: true,
                    pageLength: 10
                });
            });
        </script> 
        <title>Listas</title>
    </head>
    <body>
        <div id="wrapper">

            <!-- Navigation -->
            <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0; padding-top:10px; padding-right:15px; background-color:#ffffff; z-index: 0;">
                <div class="col-lg-9">
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                            <span class="sr-only">Toggle navigation</span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button>

                        <span class="b1"><img id="logos" src="images/logotipo4.png" alt="logo" width="85%" height="100px" style="padding-left:10px;" /></span>
                        <!--<img id="logos" src="images/logosistema2.png" alt="logo" width="40%" height="60px"  />-->
                    </div>
                </div>
                <!-- /.navbar-header -->

                <ul class="nav navbar-top-links navbar-right">
                    <li class="dropdown" style="color: #337ab7">

                        <br>

                        <i class="fa fa-user fa-fw" ></i>            
                        <%                        
                            out.print(nombreCompleto);
                        %>


                    </li>
                    <!-- /.dropdown -->
                </ul>
                <!-- /.navbar-top-links -->

                <div class="navbar-default sidebar" role="navigation">
                    <div class="sidebar-nav navbar-collapse">
                        <ul class="nav" id="side-menu">
                            <li>
                                <a href="homeCamp"><i class="fa fa-edit fa-fw"></i> HOME</a>
                            </li>

                            <li>
                                <a href="blast" ><i class="fa fa-edit fa-fw"></i> BLAST</a>
                            </li>
                            <li>
                                <a href="taxonomia" ><i class="fa fa-edit fa-fw"></i>TAXONOMÍA</a>
                            </li>
                            <li>
                                <a href="matrices"><i class="fa fa-edit fa-fw"></i>MATRICES</a>
                            </li>
                            <li>
                            <a href="#"><i class="fa fa-edit fa-fw"></i>LISTAS<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="showLista?idLista=1">Amplicones</a>
                                </li>
                                <li>
                                    <a href="showLista?idLista=2">Metagenomas</a>
                                </li>
                                <li>
                                    <a href="showLista?idLista=3">Genomas</a>
                                </li>
                                <li>
                                    <a href="showLista?idLista=4">Muestras</a>
                                </li>
                            </ul>
                            </li> 
                           <li>
                            <a href="#"><i class="fa fa-edit fa-fw"></i>METABOLISMO<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                    <a href="metabolismo">Rutas</a>
                                </li>
                                <!--<li>
                                    <a href="enzimas">Enzimas degradadoras</a>
                                </li>-->
                            </ul>
                        </li> 
                            <li>
                                <a href="CerrarSesion"><i class="fa fa-edit fa-fw"></i> SALIR</a>
                            </li>
                        </ul>
                    </div>
                    <!-- /.sidebar-collapse -->
                </div>
                <!-- /.navbar-static-side -->
            </nav>

            <div id="page-wrapper">
                
                <div class="row">                     
                    <div class="col-lg-12">

                        <h2 style="color:#337ab7;">LISTAS</h2>
                    </div>
                    <!-- /.col-lg-12 -->
                </div>
                
                <br>
                <!-- /.row -->
                <div class="row">

                    <div class="col-lg-12" id="contenido">
                        

                        <div class="panel panel-default">
                            <!-- <div class="panel-heading">
                                 Basic Tabs
                             </div>-->
                            <!-- /.panel-heading -->
                            <div class="panel-body">
                                             <div class="col-lg-12">
                                            <div class="panel panel-default">
                                                
                                                    <%
                                                        if (request.getAttribute("titulo") != null && request.getAttribute("titulo") != "Muestras") {
                                                    %>
                                                    <div class="panel-heading">
                                                    <center><b style="color:#d9534f;"><%= request.getAttribute("titulo")%></b></center>
                                                    </div>
                                                                                                    <!-- /.panel-heading -->
                                                <div class="panel-body">
                                                   
                                                    <div class="dataTable_wrapper">
                                                        <table width="100%" class="table table-striped table-bordered table-hover" id="tabla-listas">
                                                            <thead>
                                                                <tr>
                                                                    <th>Clave de acceso</th>
                                                                    <th>Campaña</th>
                                                                    <th>Estación</th>
                                                                    <th>Colecta</th>
                                                                    <th>Muestra</th>
                                                                    <th>Profundidad</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody >
                                                                <%
                                                                 ArrayList<ArrayList> listas = null;
                                                                    Object listasObj = request.getAttribute("listas");

                                                                    if (listasObj != null) {
                                                                        listas = (ArrayList<ArrayList>) listasObj;
                                                                    }
                                                                    if (listas != null) {
                                                                        for (int li = 0; li < listas.size(); li++) {
                                                                  
                                                                %> 
                                                                <tr style="text-align: left;" class="meta">
                                                                    <td><a href="<%= listas.get(li).get(0) %>" target="_blank"><%= listas.get(li).get(1)%></a></td>
                                                                    <td><%= listas.get(li).get(2) %></td>
                                                                    <td><%= listas.get(li).get(4) %></td>
                                                                    <td><%= listas.get(li).get(5) %></td>
                                                                    <td><a href="<%= listas.get(li).get(6) %>" target="_blank"><%= listas.get(li).get(7)%></a></td>
                                                                    <td><%= listas.get(li).get(8) %></td>
                                                                </tr>
                                                                <%
                                                                       }
                                                                   }
                                                                %>

                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <%
                                                    }
                                                else{
                                                %>
                                                    <div class="panel-heading">
                                                    <center><b style="color:#d9534f;"><%= request.getAttribute("titulo")%></b></center>
                                                    </div>
                                                <!-- /.panel-heading -->
                                                <div class="panel-body">
                                                   
                                                    <div class="dataTable_wrapper">
                                                        <table width="100%" class="table table-striped table-bordered table-hover" id="tabla-listas">
                                                            <thead>
                                                                <tr>
                                                                    <th>Muestra</th>
                                                                    <th>Campaña</th>
                                                                    <th>Estación</th>
                                                                    <th>Colecta</th>
                                                                    <th>Fecha</th>
                                                                    <th>Profundidad</th>
                                                                    <th>Amplicones</th>
                                                                    <th>Metagenomas</th>
                                                                    <th>Genomas</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody >
                                                                <%
                                                                 ArrayList<ArrayList> listas = null;
                                                                    Object listasObj = request.getAttribute("listas");

                                                                    if (listasObj != null) {
                                                                        listas = (ArrayList<ArrayList>) listasObj;
                                                                    }
                                                                    if (listas != null) {
                                                                        for (int li = 0; li < listas.size(); li++) {
                                                                  
                                                                %> 
                                                                <tr style="text-align: left;" class="meta">
                                                                    <td><a href="showMuestra?idMuestra=<%= listas.get(li).get(0) %>" target="_blank"><%= listas.get(li).get(1)%></a></td>
                                                                    <td><%= listas.get(li).get(2) %></td>
                                                                    <td><%= listas.get(li).get(3) %></td>
                                                                    <td><%= listas.get(li).get(4) %></td>
                                                                    <td><%= listas.get(li).get(5) %></td>
                                                                    <td><%= listas.get(li).get(6) %></td>
                                                                    <td><%= listas.get(li).get(7) %></td>
                                                                    <td><%= listas.get(li).get(8) %></td>
                                                                    <td><%= listas.get(li).get(9) %></td>
                                                                </tr>
                                                                <%
                                                                       }
                                                                   }
                                                                %>

                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>                                                    
                                                 <%   
                                                   }
                                                %>
                                                
                                                <!-- /.panel-body -->
                                            </div>

                                        </div>
                            </div>
                        </div>
                                                        
                    </div>
                    <!-- /.panel-body -->
                </div>

            </div>

        </div>

    </div>
    <!-- /#page-wrapper -->

</div>  
           
</body>
</html>


