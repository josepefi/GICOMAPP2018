<%-- 
    Document   : muestra
    Created on : 6/01/2017, 11:00:24 AM
    Author     : Jose Pefi
--%>

<%@page import="bobjects.Medicion"%>
<%@page import="bobjects.Muestreo"%>
<%@page import="bobjects.Muestra"%>
<%@page import="job.BlastResult"%>
<%@page import="job.Job"%>
<%@page import="bobjects.Usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ArrayList"%>
<%@page import="database.Transacciones"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession sesion = request.getSession(false);
   // response.setHeader("Cache-Control", "no-cache");
   // response.setHeader("Cache-Control", "no-store");
   // response.setHeader("Pragma", "no-cache");
    //response.setDateHeader("Expires", 0);
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

        <script>
            $(document).ready(function() {
                $('#tabla-muestra').DataTable({
                    responsive: true,
                    pageLength: 10

                });
            });
            $(document).ready(function() {
                $('#tabla-muestra2').DataTable({
                    responsive: true,
                    pageLength: 10

                });
            });
        </script> 
        <script src="http://maps.google.com/maps/api/js?key=AIzaSyBfAtZqx-idIbmuhOlAOOnELI4WK0P9mKg"></script>
        <script type="text/javascript">

            function funciones() {
                mapa();

            }
            window.onload = funciones;

        </script>         


        <title>Muestra</title>
    </head>
    <body>
        <div id="wrapper">

            <!-- Navigation -->
            <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0; padding-top:10px; padding-right:15px; background-color:#ffffff;">
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
                        <%                        out.print(nombreCompleto);
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
                    <%
                        Object muetraObj = request.getAttribute("muestra");
                        Muestra muestra = muetraObj != null ? (Muestra) muetraObj : null;
                        Object mueObj = request.getAttribute("muestreo");
                        Muestreo muestreo = mueObj != null ? (Muestreo) mueObj : null;
                        if (muestreo != null && muestra != null) {

                    %>    

                    <div class="col-lg-12">

                        <h2 style="color:#337ab7;">Muestra</h2><h4 class="page-header" style="color:#d9534f; margin-top:0px;"><%= muestra.getEtiqueta()%></h4> 
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
                                <!-- Nav tabs -->
                                <ul class="nav nav-tabs">
                                    <li class="active"><a href="#colecta" data-toggle="tab">Colecta</a>
                                    </li>
                                    <li><a href="#procesamiento" data-toggle="tab">Procesamiento</a>
                                    </li>                                 
                                </ul>


                                <!-- Tab panes -->
                                <div class="tab-content">
                                    <div class="tab-pane fade in active " id="colecta">
                                        <br>
                                        <div class="col-md-6">                      
                                            <div class="panel panel-default" style="border:none;">
                                                <!-- /.panel-heading -->
                                                <div class="panel-body">
                                                    <div class="table table-striped table-bordered table-hover" width="100%">
                                                        <table class="table table-striped">
                                                            <tbody>
                                                                <tr style="border-top:none;">
                                                                    <td style="padding:10px;"><b>Clave colecta:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= muestreo.getEtiqueta()%></em></td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Campaña:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= muestreo.getIdCampana()%></em></td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Estación:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= muestreo.getIdEstacion()%></em></td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Coordenadas:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><b>Lat:</b> <%= muestreo.getLatitud_r().getCoordenadas()%> </em> <em><b>Lon:</b><%= muestreo.getLongitud_r().getCoordenadas()%></em></td>
                                                                </tr> 
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Fecha:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><b>FI:</b> <%= muestreo.getFechaInicial().toWebString()%></em>  
                                                                        <script>
                                                                           if("<%= muestreo.getFechaFinal().toWebString()%>" !== ""){
                                                                                 document.write("<em><b>FF:</b> <%= muestreo.getFechaFinal().toWebString()%></em>");
                                                                            
                                                                        }
                                                                        </script>
                                                                    </td>
                                                                </tr> 
                                                                <tr>
                                                                    
                                                                <td style="padding:10px;"><b>Tipo Profundidad:</b></td>
                                                                <td style="padding:10px; text-align:right; color:#777; font-size:87%;">
                                                            <script>
                                                                    if("<%= muestreo.getTipo_prof()%>" == "FONDO"){
                                                                        document.write(" <em>FONDO</em>");
                                                                    }else if("<%= muestreo.getTipo_prof()%>" == "MAX_F"){
                                                                        document.write(" <em>MÁXIMA FLUORESCENCIA</em>");
                                                                    }else if("<%= muestreo.getTipo_prof()%>" == "MIL"){
                                                                        document.write("<em>MIL METROS</em>");
                                                                    }else if("<%= muestreo.getTipo_prof()%>" == "MIN_O2"){
                                                                        document.write("<em>MINIMO DE OXIGENO</em>");
                                                                    }else if("<%= muestreo.getTipo_prof()%>" == "SED"){
                                                                    document.write("<em>SEDIMENTO</em>");
                                                                    }
                                                                   
                                                            </script>
                                                            
                                                                 </td> 
                                                                </tr>  
                                                                <tr>
                                                                    
                                                                    <td style="padding:10px;"><b>Profundidad:</b></td>
                                                                    
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;">  <em> <%= muestreo.getProfundidad() %> <em>mts</em></em></td>
                                                                </tr>                                                                  
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Bioma:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><a href="<%= muestreo.getBioma().getUrl()%>"  target="_blank"><%= muestreo.getBioma().getIdTerm()%></a> - <%= muestreo.getBioma().getName()%></em></td>
                                                                </tr>                                       
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Material ambiental:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><a href="<%= muestreo.getEnv_material().getUrl()%>"  target="_blank"><%= muestreo.getEnv_material().getIdTerm()%></a> - <%= muestreo.getEnv_material().getName()%></em></td>
                                                                </tr> 
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Caracteristica ambiental:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><a href="<%= muestreo.getEnv_feature().getUrl()%>"  target="_blank"><%= muestreo.getEnv_feature().getIdTerm()%></a> - <%= muestreo.getEnv_feature().getName()%></em></td>
                                                                </tr>                                                                 
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Protocolo de muestreo:</b></td>
                                                                    <td style="padding:10px; text-align:justify; color:#777; font-size:87%;"><em><%= muestreo.getProtocolo()%></em></td>
                                                                </tr>   
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Comentarios:</b></td>
                                                                    <td style="padding:10px; text-align:justify; color:#777; font-size:87%;"><em><%= muestreo.getComentarios() %></em></td>
                                                                </tr>                                                                
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                    <!-- /.table-responsive -->
                                                </div>
                                                <!-- /.panel-body -->
                                            </div>                               
                                            <%
                                                }
                                            %>       


                                        </div>
                                        <div class="col-md-6">
                                            <div class="panel-body">
                                                <div class="dataTable_wrapper">
                                                    <table width="100%" class="table table-striped table-bordered table-hover" id="tabla-muestra2">
                                                        <thead>
                                                            <tr>
                                                                <th>Variable</th>
                                                                <th>Unidades</th>
                                                                <th>Valor</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>

                                                            <%
                                                                for (Medicion m : muestreo.getMediciones()) {
                                                            %>
                                                            <tr>
                                                                <td><%= m.getNombre()%></td>
                                                                <td><%= m.getUnidades()%></td>
                                                                <td><%= m.getMedicion_t1()%></td>                                                    
                                                            </tr>
                                                            <%
                                                                }
                                                            %>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>   
                                            <%
                                             //   Object mue2Obj = request.getAttribute("muestreo");
                                                //  Muestreo muestreo2 = mue2Obj != null ? (Muestreo) mue2Obj : null;
                                                if (muestreo != null) {

                                            %>    

                                            <script>
                                                function mapa() {
                                                    var puntoCentral = new google.maps.LatLng(<%= muestreo.getLatitud_r().getCoordenadas()%>, <%= muestreo.getLongitud_r().getCoordenadas()%>);
                                                    var opciones = {
                                                        zoom: 8,
                                                        center: puntoCentral,
                                                        mapTypeId: google.maps.MapTypeId.HYBRID
                                                    };

                                                    var iconoAncla = 'images/estacion.png';
                                                    var iconoMuestra = 'images/muestra.png';
                                                    var div = document.getElementById('mapa');
                                                    var map = new google.maps.Map(div, opciones);
                                                    var marker = new google.maps.Marker({
                                                        position: new google.maps.LatLng(<%= muestreo.getLatitud_r().getCoordenadas()%>, <%= muestreo.getLongitud_r().getCoordenadas()%>),
                                                        map: map,
                                                            title: "Muestra",
                                                                icon: iconoMuestra
                                                    });
                                                    var marker2 = new google.maps.Marker({
                                                                    position: new google.maps.LatLng(<%= muestreo.getLatitud_estacion().getCoordenadas()%>, <%= muestreo.getLongitud_estacion().getCoordenadas()%>),
                                                                    map: map,
                                                                    title: "Estación",
                                                                    icon: iconoAncla
                                                                    });

                                                                            var flightPlanCoordinates = [
                                                        {lat: <%= muestreo.getLatitud_r().getCoordenadas()%>, lng: <%= muestreo.getLongitud_r().getCoordenadas()%>},
                                                                {lat: <%= muestreo.getLatitud_estacion().getCoordenadas()%>, lng: <%= muestreo.getLongitud_estacion().getCoordenadas()%>}
                                                    ];
                                                                            var flightPath = new google.maps.Polyline({
                                                                            path: flightPlanCoordinates,
                                                                            geodesic: true,
                                                                        strokeColor: '#FF0000',
                                                                        strokeOpacity: 1.0,
                                                                            strokeWeight: 2
                                                    });

                                                    flightPath.setMap(map);
                                                                            }
                                                                            </script>

                                            <h2 style="color:#337ab7;">Mapa</h2>
                                            <hr> 
                                            <p>
                                                <b>Estación:</b> <%= muestreo.getIdEstacion()%> (<%= muestreo.getLatitud_estacion().getCoordenadas()%>,<%= muestreo.getLongitud_estacion().getCoordenadas()%>)
                                            </p>
                                            <p>
                                                Distancia entre la estaci&oacute;n y el punto de la toma de muestra es:<b> <%= muestreo.getDistanciaEstacion()>1?""+muestreo.getDistanciaEstacion()+" Km.":""+muestreo.getDistanciaEstacion()*1000+"mts."%> </b>
                                            </p>
                                            <%}%>                  
                                            <div id="mapa" style="width:100%; height:200px">

                                            </div>

                                        </div>                                    
                                    </div>



                                    <div class="tab-pane faden " id="procesamiento">
                                        <br>
                                        <div class="col-md-12">
                                            <%
                                                if (muestra != null) {

                                            %>   
                                            <div class="panel panel-default" style="border:none;">
                                                <!-- /.panel-heading -->
                                                <div class="panel-body">
                                                    <div class="table table-striped table-bordered table-hover" width="100%">
                                                        <table class="table table-striped">
                                                            <tbody>
                                                                <tr style="border-top:none;">
                                                                    <td style="padding:10px;"><b>Etiqueta de la muestra:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= muestra.getEtiqueta()%></em></td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Contenedor:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= muestra.getContenedor()%></em></td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Temperatura contenedor:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= muestra.getcontenedor_temp()%></em></td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Relación oxigeno:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= muestra.getreal_to_oxy()%></em></td>
                                                                </tr> 
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Tamaño:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= muestra.getSamp_size()%></em></td>
                                                                </tr> 
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Protocolo: </b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= muestra.getprotocolo()%></em></td>
                                                                </tr>                                       
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Notas:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= muestra.getNotas()%></em></td>
                                                                </tr>                                        
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                    <!-- /.table-responsive -->
                                                </div>
                                                <!-- /.panel-body -->
                                            </div>                                            
                                            <%
                                            }
                                            %>

                                        </div>
                                    </div>                                 
                                </div>


                                <div class="tab-pane fade" id="mapaPuntos">
                                    <div class="col-md-12">
                                        <div id="mapadiv" style="width:100%; height:200px">

                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->


                </div>

            </div>

        </div>
        <!-- /#page-wrapper -->

    </div>                                        
</body>
</html>
