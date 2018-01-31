<%-- 
    Document   : genoma
    Created on : 26/04/2017, 12:12:19 PM
    Author     : Jose Pefi
--%>

<%@page import="bobjects.NCBINode"%>
<%@page import="bobjects.Genoma"%>
<%@page import="java.util.List"%>
<%@page import="bobjects.ArchivoObj"%>
<%@page import="bobjects.PCRObj"%>
<%@page import="java.util.ArrayList"%>
<%@page import="bobjects.Usuario"%>
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
    Object genomaObj = request.getAttribute("genoma");
    Genoma genoma = genomaObj != null ? (Genoma) genomaObj : null;
    

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

        <!-- Ventana Modal -->
        <link href="css/ventanaModal.css" rel="stylesheet" type="text/css">
        <!-- jQuery -->
        <script src="bower_components/jquery/dist/jquery.min.js"></script>

        <!---TOlTIPS---->
        <link href="themes/1/tooltip.css" rel="stylesheet" type="text/css">
        <script src="themes/1/tooltip.js" type="text/javascript"></script> 

        <!-- Bootstrap Core JavaScript -->
        <script src="bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

        <!-- Metis Menu Plugin JavaScript -->
        <script src="bower_components/metisMenu/dist/metisMenu.min.js"></script>
        <!--ALERTAS-->
        <script src="alerta/dist/sweetalert-dev.js"></script>
        <link rel="stylesheet" href="alerta/dist/sweetalert.css">

        <!-- Custom Theme JavaScript -->
        <script src="dist/js/sb-admin-2.js"></script>

        <!-- DataTables JavaScript -->
        <script src="bower_components/datatables/media/js/jquery.dataTables.min.js"></script>
        <script src="bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.min.js"></script>
        <script src="bower_components/datatables-responsive/js/dataTables.responsive.js"></script>  
        <script src="js/creaMatriz.js"></script>        

        <script>

            //var idKrona = <%//marcador.getIdMarcador();%>;
            function funcionKrona(idKrona) {
                //alert(idKrona);
                $.ajax({
                    type: "POST",
                    url: "kronaAmp",
                    data: {
                        idkrona: idKrona
                    },
                    dataType: "html",
                    beforeSend: function () {
                        //imagen de carga
                        $("#resultadokrona").html("<p align='center'><img src='http://form.cenp.com.br/img/carregando.gif' /></p>");
                    },
                    error: function () {
                        alert("error petici�n ajax");
                    },
                    success: function (data) {
                        //alert("success??");
                        $("#resultadokrona").empty();
                        $("#resultadokrona").append(data);
                    }
                });
            }
        </script>
        <script>
            //var idKrona = <%//marcador.getIdMarcador();%>;
            function funcionKrona2(idKrona) {
                $("#resultadokrona3").html("<frameset><iframe src='kronaAmp2?idkrona=" + idKrona + "' width=100%; height=500px; frameborder='0'> </iframe> </frameset>");
            }
        </script>
        <!--SCRIPT PARA OCULTAR DIV DETALLES-->
        <script>

            var clic = 1;
            // $("#icono-info").on("click", function () {
            function desplegar(idArchivo) {
                //alert(idArchivo);
                if ($('#icono' + idArchivo).attr("value") == 0) {
                    $('#' + idArchivo).show(); //muestro
                    $('#icono' + idArchivo).removeClass('fa fa-plus-circle');//elimina clse del fa fa-plus-circle
                    $('#icono' + idArchivo).addClass('fa fa-minus-circle');//agrega la clase del icono fa fa-minus-circle
                    $('#icono' + idArchivo).attr("value", "1");
                    $('#detalle' + idArchivo).css("background-color", "#ddd");

                } else {
                    $('#' + idArchivo).hide(); //oculto
                    $('#icono' + idArchivo).removeClass('fa fa-minus-circle');//elimina clase fa-minus-circle
                    $('#icono' + idArchivo).addClass('fa fa-plus-circle');//agrega la clase fa fa-plus-circle
                    $('#icono' + idArchivo).attr("value", "0");
                    $('#detalle' + idArchivo).css("background-color", "#f9f9f9");
                }
            }
            //});

        </script>
        <script src="http://maps.google.com/maps/api/js?key=AIzaSyBfAtZqx-idIbmuhOlAOOnELI4WK0P9mKg"></script>
        <script type="text/javascript">

            function funciones() {
                mapa();

            }
            window.onload = funciones;

        </script>          
        <title>Genoma</title>
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
                                <a href="taxonomia" ><i class="fa fa-edit fa-fw"></i>TAXONOM�A</a>
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
                <%
                    if (genoma != null) {

                %>
                <div class="row">                     
                    <div class="col-lg-12">

                        <h2 style="color:#337ab7;">GENOMA</h2><h4 class="page-header" style="color:#d9534f; margin-top:0px;"><%= genoma.getName() %></h4> 
                    </div>
                    <script>
                        console.log("El nombre del genoma:"+<%= genoma.getName() %>);
                    </script>
                </div>
                <%
                   }
                %>
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
                                    <li class="active"><a href="#detalles" data-toggle="tab">Detalles</a>
                                    </li>
                                    <li><a href="#secuenciacion" data-toggle="tab">Secuenciaci�n</a>
                                    </li>
                                    <li><a href="#qc" data-toggle="tab">QC</a>
                                    </li>
                                    <li><a href="#ensamble" id="boton" data-toggle="tab">Ensamble</a>
                                    </li>
                                    <li><a href="#archivos" id="boton" data-toggle="tab">Archivos</a>
                                    </li>
                                   <!-- <li><a id="div-krona" href="#krona3" data-toggle="tab" onclick="funcionKrona2(<%//= marcador.getIdMarcador()%>)">Taxonom�a</a>
                                    </li>-->
                                   <!-- <li><a href="#matriz" data-toggle="tab">Matriz</a>
                                    </li>  -->  
                                    <!--<li><a href="#prediccion" data-toggle="tab">Predicci�n Funcional</a>
                                    </li> -->                                   
                                </ul>


                                <!-- Tab panes -->
                                <div class="tab-content">
                                    <div class="tab-pane fade in active " id="detalles">
                                        <br>
                                        <div class="col-md-7">
                                        <div class="panel panel-default" style="border:none;">
                                            <!-- /.panel-heading -->
                                            <div class="panel-body">
                                                <div class="table table-striped table-bordered " width="100%">
                                                    <table class="table table-striped">
                                                        <%  
                                                            
                                                            if (genoma != null) {
                                                                
                                                            
                                                        %>
                                                        <tbody>
                                                            <tr style="border-top:none;">
                                                                <td style="padding:10px;"><b>Versi�n:</b></td>
                                                                <td style="padding:10px; text-align:justify; color:#777; font-size:87%;"><em><%= genoma.getVersion() %></em></td>
                                                            </tr>
                                                            <tr style="border-top:none;">
                                                                <td style="padding:10px;"><b>Muestra:</b></td>
                                                                <td style="padding:10px; text-align:justify; color:#777; font-size:87%;"><em><a href = 'showMuestra?idMuestra=<%=genoma.getIdmuestra() %>' target='_blank'><%= genoma.getEtiquetaMuestra()%></a></em></td>
                                                            </tr>
                                                            <tr style="border-top:none;">
                                                                <td style="padding:10px;"><b>Profundidad:</b></td>
                                                                <td style="padding:10px; text-align:justify; color:#777; font-size:87%;"><em><%= genoma.getProfundidad() %></a></em></td>
                                                            </tr>        
                                                            <tr style="border-top:none;">
                                                                <td style="padding:10px;"><b>Procesamiento:</b></td>
                                                                <td style="padding:10px; text-align:justify; color:#777; font-size:87%;"><em><%= genoma.getProcesamiento() %></a></em></td>
                                                            </tr>
                                                        </tbody>
                                                        <%
                                                            }
                                                        %>
                                                    </table>
                                                </div>
                                                <!-- /.table-responsive -->
                                            </div>
                                            <!-- /.panel-body -->
                                        </div>   
                                    </div>
                                                   
                                    <div class="col-md-5">
                                            <script>
                                                function mapa() {
                                                    var puntoCentral = new google.maps.LatLng(<%= genoma.getLatitud() %>,<%= genoma.getLongitud() %>);
                                                    var opciones = {
                                                        zoom: 5,
                                                        center: puntoCentral,
                                                        mapTypeId: google.maps.MapTypeId.HYBRID
                                                    };

                                                    var iconoMuestra = 'images/muestra.png';
                                                    var div = document.getElementById('mapa');
                                                    var map = new google.maps.Map(div, opciones);
                                                    var marker = new google.maps.Marker({
                                                        position: new google.maps.LatLng(<%= genoma.getLatitud() %>,<%= genoma.getLongitud() %>),
                                                        map: map,
                                                            title: "Muestra",
                                                                icon: iconoMuestra
                                                    });
                                                   
                                                                            }
                                            </script>

                                            <div class="panel panel-default" style="border:none;">
                                                <!-- /.panel-heading -->
                                                <div class="panel-body" style="padding:0px;">
                                                    <div class="table table-striped table-bordered " width="100%" style="border:none;">
                                                        <table class="table table-striped">

                                                            <tbody>

                                                                <tr>
                                                                    <td style="padding:0px; border-top:0px;">
                                                                        <h5 style="color:#337ab7;">LOCALIZACI�N</h5>
                                                                        <p>
                                                                            <b>Estaci�n:</b> <%= genoma.getEstacion() %> (<%= genoma.getLatitud()%> N,<%= genoma.getLongitud()%> W)
                                                                        </p>
                                                                        <p>            
                                                                        <div id="mapa" style="width:100%; height:200px">

                                                                        </div>      
                                                                    </td>

                                                                </tr>

                                                            </tbody>

                                                        </table>
                                                    </div>
                                                    <!-- /.table-responsive -->
                                                </div>
                                                <!-- /.panel-body -->
                                            </div>                                         
                                        
                                    </div>
                                        <div class="col-md-12">
                                        <div class="panel panel-default" style="border:none;">
                                            <!-- /.panel-heading -->
                                            <div class="panel-body" style="padding-top: 0px;">
                                                <div class="table table-striped table-bordered " width="100%" style="border-top:0px;">
                                                    <table class="table table-striped">
                                                        <%
                                                           if (genoma != null) {
                                                        %>
                                                        <tbody>
                                                            <tr>
                                                                <td style="padding:10px;"><b>Organismo:</b></td>
                                                                <td style="padding:10px; text-align:justify; color:#777; font-size:87%;"><em><%= genoma.getTax_id() %><%= genoma.getStrain() %></em></td>
                                                            </tr>      
                                                            <tr>
                                                                <td style="padding:10px;"><b>Linaje:</b></td>
                                                                <td style="padding:10px; text-align:justify; color:#777; font-size:87%; word-break: break-all;"><em><%= genoma.getTax().getKingdom()%>,<%= genoma.getTax().getPhylum() %>,<%= genoma.getTax().getClas() %>, <%= genoma.getTax().getOrden() %>,<%= genoma.getTax().getFamily() %>,<%= genoma.getTax().getGenus() %>,<%= genoma.getTax().getSpecies() %>,<%= genoma.getTax().getSubspecies() %></em></td>
                                                            </tr>       
                                                            <tr>
                                                                <td style="padding:10px;"><b>Organismo de referencia:</b></td>
                                                                <td style="padding:10px; text-align:justify; color:#777; font-size:87%;"><em><%= genoma.getRef_anot() %></em></td>
                                                            </tr> 
                                                            <tr>
                                                                <td style="padding:10px;"><b>Aislado y crecimiento:</b></td>
                                                                <td style="padding:10px; text-align:justify; color:#777; font-size:87%;"><em><%= genoma.getCrecimiento() %></em></td>
                                                            </tr>                                                            
                                                            <tr>
                                                                <td style="padding:10px;"><b>Descripci�n:</b></td>
                                                                <td style="padding:10px; text-align:justify; color:#777; font-size:87%;"><em><%= genoma.getDesc() %></em></td>
                                                            </tr> 
                                                            <tr>
                                                                <td style="padding:10px;"><b>Metodologia de analisis:</b></td>
                                                                <td style="padding:10px; text-align:justify; color:#777; font-size:87%;"><em><%= genoma.getAnalisis() %></em></td>
                                                            </tr> 
                                                            <tr>
                                                                <td style="padding:10px;"><b>Comentarios:</b></td>
                                                                <td style="padding:10px; text-align:justify; color:#777; font-size:87%;"><em><%= genoma.getComentarios() %></em></td>
                                                            </tr>        
                                                            <tr>
                                                                <td style="padding:10px;"><b>Respaldo cepa:</b></td>
                                                                <td style="padding:10px; text-align:justify; color:#777; font-size:87%;"><em><%= genoma.getRespaldo_org() %></em></td>
                                                            </tr> 
                                                            <tr>
                                                                <td style="padding:10px;"><b>Referencia:</b></td>
                                                                <td style="padding:10px; text-align:justify; color:#777; font-size:87%;"><em><%= genoma.getReferencia() %></em></td>
                                                            </tr>                                                             
                                                        </tbody>
                                                        <%
                                                           }
                                                        %>
                                                    </table>
                                                </div>
                                                <!-- /.table-responsive -->
                                            </div>
                                            <!-- /.panel-body -->
                                        </div>   
                                    </div>                                           
                                    </div>

                                    <div class="tab-pane faden " id="secuenciacion">
                                        <br>                     

                                        <div class="col-md-12">

                                            <div class="panel panel-default" style="border:none;">
                                                <!-- /.panel-heading -->
                                                <h4 style="color:#337ab7;">Obtenci�n del DNA</h4>
                                                <hr> 
                                                <div class="panel-body">
                                                    <div class="table table-striped table-bordered table-hover" width="100%">
                                                        <table class="table table-striped" >
                                                            <%
                                                                if (genoma != null) {
                                                            %>
                                                            <tbody>
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Vol. DNA: </b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= genoma.getCantidad_dna() %></em></td>
                                                                </tr>                                       
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Kit:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= genoma.getClean_up_kit() %></em></td>
                                                                </tr>   
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Metodolog�a:</b></td>
                                                                    <td style="padding:10px; text-align:justify; color:#777; font-size:87%; word-wrap:break-word;"><em><%= genoma.getClean_up_method() %></em></td>
                                                                </tr> 
                                                            </tbody>
                                                            <%}%>
                                                        </table>
                                                    </div>
                                                    <!-- /.table-responsive -->
                                                </div>
                                                <!-- /.panel-body -->
                                            </div> 
                                            <div class="panel panel-default" style="border:none;">
                                                <!-- /.panel-heading -->
                                                <h4 style="color:#337ab7;">Secuenciaci�n</h4>
                                                <hr>
                                                <div class="panel-body">
                                                    <div class="table table-striped table-bordered " width="100%">
                                                        <table class="table table-striped">
                                                            <%
                                                                 if (genoma != null) {
                                                            %>
                                                           <tbody>
                                                                <tr style="border-top:none;">
                                                                    <td style="padding:10px;"><b>Fuente:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><%= genoma.getLibreria().getFuente()%></td>         
                                                                    <td style="padding:10px;"><b>Tipo de secuenciaci�n:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><%= genoma.getTipoSecuenciacion()%> - <%= genoma.getDescTipoSecuenciacion()%></td> 
                                                                </tr>                                                               
                                                                <tr style="border-top:none;">         
                                                                    <td style="padding:10px;"><b>Selecci�n de la librer�a:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><%= genoma.getLibreria().getSeleccion()%></td> 
                                                                    <td style="padding:10px;"><b>Configuraci�n de la librer�a:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><%= genoma.getLibreria().getLayout()%></td> 

                                                                </tr>
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Secuenciador:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><%=genoma.getEquipoSecuenciacion()%></td>         
                                                                    <td style="padding:10px;"><b>Centro de secuenciaci�n:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><%= genoma.getCentro_secuenciacion()%></td>         

                                                                </tr>
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Preparaci�n de la libreria:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;" colspan="3"><%= genoma.getLibreria().getMetodo()%></td>
                                                                    <td></td>
                                                                    <td></td>
                                                                </tr>
                                                            </tbody> 
                                                            <%
                                                                }
                                                            %>
                                                        </table>
                                                    </div>
                                                    <!-- /.table-responsive -->
                                                </div>
                                                <!-- /.panel-body -->
                                            </div>   
                                        </div>

                                    </div>
                                    <div class="tab-pane fade" id="qc">
                                        <br>
                                        <div class="col-md-6">                      
                                            <div class="panel panel-default" style="border:none;">
                                                <!-- /.panel-heading -->
                                                <div class="panel-body" style=" padding-right: 0px; ">
                                                    <div class="table table-striped table-bordered table-hover" width="100%">
                                                        <table class="table table-striped">
                                                            <tbody>
                                                                <tr style="border-top:none;">
                                                                    <td style="padding:10px;"><span style="cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, 'N�mero de secuencias obtenidas.', {position: 0})"></span> <b>Lecturas:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= genoma.getStats().getReads()%></em></td>                                                                   
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding:10px;"><span style="cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, 'N�mero de bases nucleot�dicas que representan el total de las lecturas obtenidas.', {position: 0})"></span> <b>Bases:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= genoma.getStats().getBases()%></em></td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding:10px;"><span style="cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, 'Longitud promedio de las lecturas.', {position: 0})"></span> <b>Logitud promedio:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= genoma.getStats().getLong_avg()%></em></td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding:10px;"><span style="cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, 'Cantidad porcentual de bases G\'s y C\'s dentro de las lecturas', {position: 0})"></span> <b>GC%</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= genoma.getStats().getGc_prc()%></em></td>
                                                                </tr>                                                               
                                                                <tr>
                                                                    <td style="padding:10px;"><span style="cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, 'Valor de calidad promedio de todas las lecturas. Arriba de 20 es aceptable, arriba de 28 es muy buena calidad.', {position: 0})"></span> <b>Promedio QC:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= genoma.getStats().getQc_avg()%></em></td>

                                                                </tr>     

                                                            </tbody>

                                                        </table>
                                                    </div>
                                                    <!-- /.table-responsive -->
                                                </div>
                                                <!-- /.panel-body -->
                                            </div>                               

                                        </div>
                                        <div class="col-md-6"> 
                                            <div class="panel panel-default" style="border:none;">
                                                <!-- /.panel-heading -->
                                                <div class="panel-body" style=" padding-left: 0px; ">
                                                    <div class="table table-striped table-bordered table-hover" width="100%">
                                                        <table class="table table-striped">
                                                            <tbody>
                                                                <tr>
                                                                    <td style="padding:10px;"><span style="cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, 'N�mero de bases ambiguas o con la letra N.', {position: 0})"></span> <b>n�s%:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= genoma.getStats().getNs_prc()%></em></td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding:10px;"><span style="cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, 'Porcentaje de lecturas con promedio de calidad mayor a 20.', {position: 0})"></span> <b>Q20:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= genoma.getStats().getQ20()%></em></td>                                          
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding:10px;"><span style="cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, 'Porcentaje de lecturas con promedio de calidad mayor a 30.', {position: 0})"></span> <b>Q30:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= genoma.getStats().getQ30()%></em></td>                                                     
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding:10px;"><span style="cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, 'Porcentaje de fragmentos que se pudieron extender (lecturas FW y RV).', {position: 0})"></span> <b>Porcentaje combinado:</b></td>
                                                                    <td style="padding:10px; text-align:right; color:#777; font-size:87%;"><em><%= genoma.getStats().getCombined_prc()%></em></td>                                           
                                                                </tr>
                                                                <% //}
                                                                %>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>


                                    </div>  
                                    <div class="tab-pane faden " id="ensamble">
                                        <br>                     

                                        <div class="col-md-12">

                                            <div class="panel panel-default" style="border:none;">
                                                <!-- /.panel-heading -->
                                                <div class="panel-body">
                                                    <div class="table table-striped table-bordered table-hover" width="100%">
                                                        <table class="table table-striped" >
                                                            <%
                                                                if (genoma != null) {
                                                            %>
                                                            <tbody>

                                                                <tr>
                                                                    <td style="padding:10px;"><b>Ensamblador:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><em><%= genoma.getEnsamble().getEnsamblador() %></em></td>
                                                                    <td style="padding:10px;"><b>Contigs:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%; word-wrap:break-word;"><em><%= genoma.getEnsamble().getContigs() %></em></td>
                                                                 
                                                                </tr> 
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Contig m�s largo:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><em><%= genoma.getEnsamble().getLongestContig() %></em></td>
                                                                    <td style="padding:10px;"><b>Contig promedio:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%; word-wrap:break-word;"><em><%= genoma.getEnsamble().getAvgContig() %></em></td>
                                                                                                                               
                                                                </tr>
                                                                                                                             
                                                                <tr>
                                                                    <td style="padding:10px;"><b>N50/N90:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%; word-break: break-all; "><em><%= genoma.getEnsamble().getN5090() %></em></td>
                                                                    <td style="padding:10px;"><b>Lecturas Mapeadas: </b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><em><%= genoma.getEnsamble().getLecturasMapeadas() %>%</em></td>
                                                                                                  
                                                                </tr>                                    
                                                                <tr>
                                                                    <td style="padding:10px;" ><b>Comentarios:</b></td>
                                                                    <td style="padding:10px; text-align:justify; color:#777; font-size:87%;" colspan="2"><em><%= genoma.getEnsamble().getComentarios() %></em></td>
                                                                    <td></td>
                                                                    <td></td>
                                                                </tr>
                                                            </tbody>
                                                            <%}%>
                                                        </table>
                                                    </div>
                                                    <!-- /.table-responsive -->
                                                </div>
                                                <!-- /.panel-body -->
                                            </div> 

                                        </div>

                                    </div>
                                    <div class="tab-pane fade" id="archivos">
                                        <br>
                                        <div class="col-md-12">

                                            <div class="panel panel-default" style="border:none;">
                                                <!-- /.panel-heading -->
                                                <div class="panel-body">
                                                    <div class="table table-striped table-bordered table-hover" width="100%">
                                                        <table class="table table-striped">

                                                            <thead>
                                                                <tr>
                                                                    <th>Nombre</th>
                                                                    <th>Descripci�n</th>
                                                                    <th>Descargar</th>
                                                                    <th>Detalles</th>
                                                                </tr>
                                                            </thead

                                                            <tbody>
                                                                <%
                                                                    if (genoma != null) {

                                                                        for (ArchivoObj aobj : genoma.getArchivos()) {

                                                                %>

                                                                <tr style="border-top:none; background-color: #f9f9f9;" id="detalle<%= aobj.getIdArchivo()%>" >
                                                                    <td style="padding:10px; word-break: break-all;"><em><%= aobj.getNombre()%></em></td>
                                                                    <td style="padding:10px;"><em><%= aobj.getDescription()%></em></td>
                                                                    <td style="padding:10px;"><button  class="fa fa-save"></button></td>
                                                                    <td style="padding:10px;"><button value="0" class="fa fa-plus-circle" onclick="desplegar(<%= aobj.getIdArchivo()%>)" id="icono<%= aobj.getIdArchivo()%>"></button></td>
                                                                </tr> 
                                                                <tr style="display:none;" id="<%= aobj.getIdArchivo()%>">

                                                                    <td>
                                                                        <table width="100%" class="table table-striped table-bordered table-hover">
                                                                            <tbody>
                                                                                <tr>
                                                                                    <td style="padding:10px;"><em><b>Fecha:</b></em></td>
                                                                                    <td style="padding:10px;"><em><%= aobj.getDate().getFecha()%></em></td>
                                                                                </tr>  
                                                                                <tr>
                                                                                    <td style="padding:10px;"><em><b>Alcance:</b></em></td>
                                                                                    <td style="padding:10px;"><em><%= aobj.getAlcance()%></em></td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td style="padding:10px;"><em><b>Editor:</b></em></td>
                                                                                    <td style="padding:10px; word-wrap:break-word;"><em><%= aobj.getEditor()%></em></td>
                                                                                </tr> 
                                                                                <tr>
                                                                                    <td style="padding:10px;"><em><b>Derechos:</b></em></td>
                                                                                    <td style="padding:10px;"><em><%= aobj.getDerechos()%></em></td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td style="padding:10px;"><em><b>Etiquetas:</b></em></td>
                                                                                    <td style="padding:10px;"><em><%=aobj.getTags()%></em></td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td style="padding:10px;"><em><b>Tipo de archivo:</b></em></td>
                                                                                    <td style="padding:10px;"><em><%= aobj.getNombreTipo()%></em></td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td style="padding:10px;"><em><b>Cheksum:</b></em></td>
                                                                                    <td style="padding:10px; word-break: break-all;"><em><%= aobj.getChecksum()%></em></td>
                                                                                </tr> 
                                                                                <tr>
                                                                                    <td style="padding:10px;"><em><b>Tama�o:</b></em></td>
                                                                                    <td style="padding:10px;"><em><%= aobj.getSize()%></em></td>
                                                                                </tr>
                                                                            </tbody>
                                                                        </table>
                                                                    </td>

                                                                    <td colspan="3">
                                                                        <table width="100%" class="table table-striped table-bordered table-hover">
                                                                            <tbody>
                                                                                <tr>
                                                                                    <td style="padding:10px; text-align: center;"><em><b>Usuarios:</b></em></td>
                                                                                </tr> 
                                                                                <tr>
                                                                                    <td>
                                                                                        <table width="100%" class="table table-striped table-bordered table-hover" >
                                                                                            <thead>
                                                                                                <tr>
                                                                                                    <th>Usuario</th>
                                                                                                    <th>Relaci�n</th>
                                                                                                    <th>Comentarios</th>
                                                                                                </tr>
                                                                                            </thead>
                                                                                            <tbody>
                                                                                                <%
                                                                                                    ArrayList<Usuario> usu = aobj.getUsuarios();
                                                                                                    if (aobj.getUsuarios() != null) {
                                                                                                        for (Usuario u : aobj.getUsuarios()) {
                                                                                                %>
                                                                                                <tr>

                                                                                                    <td><%= u.getNombres() + " " + u.getApellidos()%></td>
                                                                                                    <td><%= u.getAcciones()%></td>
                                                                                                    <td><%= u.getComentarios()%></td>                                                    
                                                                                                </tr>
                                                                                                <%
                                                                                                        }
                                                                                                    }

                                                                                                %>
                                                                                            </tbody>

                                                                                        </table>                                                           
                                                                                    </td>
                                                                                </tr> 
                                                                            </tbody>
                                                                        </table>                                                                        
                                                                    </td>
                                                                    <td>                                                                       
                                                                    </td>
                                                                    <td>                                                                       
                                                                    </td>
                                                                </tr>                                                                
                                                                <%     }
                                                                    }
                                                                %>

                                                            </tbody>

                                                        </table>
                                                    </div>
                                                    <!-- /.table-responsive -->
                                                </div>
                                                <!-- /.panel-body -->
                                            </div> 

                                        </div>
                                    </div>
                                    <!--<div class="tab-pane fade" id="krona2">
                                        <br>
                                        <div id="resultadokrona">


                                        </div>
                                    </div>      -->
                                    <div class="tab-pane fade" id="krona3">
                                        <br>
                                        <div id="resultadokrona3">


                                        </div>
                                    </div>      

                                    <div class="tab-pane fade" id="matriz">
                                        <br>
                                        <h4>Crear Matriz de abundancia</h4> 
                                        <br> 
                                        <div class="row">                         
                                            <div class="col-lg-2">

                                                <label>Nivel Taxon�mico</label>
                                                <select id="nivelTaxo" name="nivel" class="form-control">
                                                    <option value="" disabled selected>Select...</option>                                                
                                                    <option value="kingdom">Reino</option>
                                                    <option value="phylum">Filum</option>
                                                    <option value="class">Clase</option>
                                                    <option value="orden">Orden</option>
                                                    <option value="family">Familia</option>
                                                    <option value="genus">G�nero</option>
                                                    <option value="species">Especie</option>                                                
                                                </select>   
                                                <div class="form-group" id="mGeneros" style="display:none;">

                                                    <div class="radio">
                                                        <label>
                                                            <input type="checkbox" value="norm" name = "degradadores"  id="degradadores">Degradadores de hidrocarburos
                                                        </label>
                                                    </div>

                                                    <div class="radio">
                                                        <label>
                                                            <a href="#popup" id="organismos" >Lista de organismos </a>
                                                        </label>
                                                    </div>
                                                    <div class="modal-wrapper" id="popup">
                                                        <div class="popup-contenedor">
                                                            <div class="panel panel-default">
                                                                <div class="panel-heading">
                                                                    <center><h3><b style="color:#d9534f;">G�neros Degradadores</b></h3></center>
                                                                </div>
                                                                <%
                                                                   // Object tabla = request.getAttribute("tabla");
                                                                   // String tablaHTML = tabla != null ? (String) tabla : null;
                                                                %>
                                                                <!-- /.panel-heading -->
                                                                <div class="panel-body" >
                                                                    <div class="dataTable_wrapper">
                                                                        <%
                                                                           // if (tablaHTML != null) {
                                                                        %>
                                                                        <%//= tablaHTML%>

                                                                        <%

                                                                           // }
                                                                        %>
                                                                    </div>
                                                                </div>
                                                                <!-- /.panel-body -->
                                                            </div>                               

                                                            <a class="popup-cerrar" href="#">X</a>
                                                        </div>
                                                    </div>


                                                </div>   
                                            </div>
                                            <div class="col-lg-1" >
                                                <br>

                                                <!-- <input type="checkbox" id="nombres" checked> <label>Nombres Cortos</label>-->
                                                <input type="checkbox" id="toFile">
                                                <span style="margin-right:5px; cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, '', {position: 0});"></span> 
                                                <label>Archivo</label>

                                            </div>
                                            <div class="col-lg-1" >
                                                <br>

                                                <input type="checkbox" id="cabecera" > 
                                                <span style="margin-right:5px; cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, '', {position: 0});"></span>
                                                <label>Incluir cabecera</label>
                                            </div>       
                                            <div class="col-lg-3">
                                                <label>Nombre del Organismo</label>
                                                <span style="margin-right:5px; cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, '', {position: 0});"></span>
                                                <div class="panel panel-default">
                                                    <div class="panel-body">
                                                        <div class="form-group">
                                                            <!--<label>Radio Buttons</label>-->
                                                            <div class="radio">
                                                                <label>
                                                                    <input type="radio" name = "orgName" value = "taxon" id="taxon" checked>�ltimo nivel taxon�mico
                                                                </label>
                                                            </div>

                                                            <div class="radio">
                                                                <label>
                                                                    <input type="radio" value = "filo" name = "orgName" id="filognia" >Filogenia
                                                                </label>
                                                            </div>

                                                            <div class="radio">
                                                                <label>
                                                                    <input type="radio" name = "orgName" value ="ncbi" id="ncbi">NCBI TAX ID
                                                                </label>
                                                            </div>	

                                                        </div>
                                                    </div>
                                                </div>
                                            </div>    
                                            <div class="col-lg-3">
                                                <label>Conteos</label>
                                                <span style="margin-right:5px; cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, '', {position: 0});"></span>
                                                <div class="panel panel-default">
                                                    <div class="panel-body">
                                                        <div class="form-group">
                                                            <!--<label>Radio Buttons</label>-->
                                                            <div class="radio">
                                                                <label>
                                                                    <input type="radio" value="norm" name = "conteos" checked id="normalizar">Normalizadas
                                                                </label>
                                                            </div>

                                                            <div class="radio">
                                                                <label>
                                                                    <input type="radio" name = "conteos" value = "crudo" id="crudos" >Cuentas crudas
                                                                </label>
                                                            </div>


                                                        </div>
                                                    </div>
                                                </div>
                                            </div>                        
                                            <div class="col-lg-1">
                                                <br>

                                                <button  class="fa fa-gear" onclick="buscaMatriz(<%//=marcador.getIdMarcador()%>)" > Generar</button>
                                            </div>
                                        </div>


                                        <div id="loading"></div>
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
<script>
    $(document).ready(function () {
        $("select[name=nivel]").change(function () {
            var niveles = $('select[name=nivel]').val();
            if (niveles == "genus") {

                $("#mGeneros").css("display", " block");
            } else {
                $("#mGeneros").css("display", "none");
            }

        });
    });
</script>
<script>
    $(document).ready(function () {
        $('#lista-organismo').DataTable({
            responsive: true
        });
    });
</script>

</body>
</html>

