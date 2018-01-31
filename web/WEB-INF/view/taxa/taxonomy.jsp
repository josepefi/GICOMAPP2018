<%-- 
    Document   : buscadores
    Created on : 27/01/2017, 01:51:05 PM
    Author     : Jose Pefi
--%>

<%@page import="bobjects.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession sesion = request.getSession();
  ///  response.setHeader("Cache-Control", "no-cache");
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

        <!-- Custom Fonts -->
        <link href="css/ventanaModal.css" rel="stylesheet" type="text/css">


        <!-- DataTables CSS -->
        <link href="bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.css" rel="stylesheet">
        <!-- DataTables Responsive CSS -->
        <link href="bower_components/datatables-responsive/css/dataTables.responsive.css" rel="stylesheet">        

        <!-- Ventana Modal -->
        <link href="css/ventanaModal.css" rel="stylesheet" type="text/css">
        
        <!-- jQuery -->
        <script src="bower_components/jquery/dist/jquery.min.js"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

        <!-- Metis Menu Plugin JavaScript -->
        <script src="bower_components/metisMenu/dist/metisMenu.min.js"></script>
        
         <!-- Servir secuencias -->
        <script src="js/taxoSequenceServer.js"></script>

        <!---TOlTIPS---->
        <link href="themes/1/tooltip.css" rel="stylesheet" type="text/css">
        <script src="themes/1/tooltip.js" type="text/javascript"></script> 

        <!-- Custom Theme JavaScript -->
        <script src="dist/js/sb-admin-2.js"></script>

        <!-- DataTables JavaScript -->
        <script src="bower_components/datatables/media/js/jquery.dataTables.min.js"></script>
        <script src="bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.min.js"></script>
        <script src="bower_components/datatables-responsive/js/dataTables.responsive.js"></script>  


        <script type="text/javascript">
            var palabraAnterior;
            $(document).ready(function() {
                //var globalTimeout = null;  
                //hacemos focus al campo de búsqueda
                $("#search").focus();
                //comprobamos si se pulsa una tecla
                $("#search").keyup(function(e) {
                    //obtenemos el texto introducido en el campo de búsqueda
                    var consulta = $("#search").val();

                    //var evento= (window.event)?event.keyCode:evt.which;

                    // var isWordCharacter = event.key.length === 1;
                    //backspace || deelte
                    // var isBackspaceOrDelete = (event.keyCode == 8 || event.keyCode == 46);
                    var limite = 2;
                    if ($.isNumeric(consulta)) {
                        limite = 0;
                    }
                    if ((palabraAnterior !== $.trim(consulta)) && consulta.length > limite) {
                        palabraAnterior = $.trim(consulta);
                        clearTimeout($.data(this, 'timer'));
                        var wait = setTimeout(peticion, 750);
                        $(this).data('timer', wait);
                    } else if (consulta.length < 1) {
                        clearTimeout($.data(this, 'timer'));
                        var wait = setTimeout(vacio, 200);
                        $(this).data('timer', wait);
                    }

                    function peticion() {
                        $("#resultado").css("display", "block");
                        $.ajax({
                            type: "POST",
                            url: "buscaPalabra",
                            data: {
                                palabra: consulta
                            },
                            dataType: "html",
                            beforeSend: function() {
                                //imagen de carga
                                $("#resultado").html("<p align='center'><img src='http://form.cenp.com.br/img/carregando.gif' /></p>");
                            },
                            error: function() {
                                //  alert("error petición ajax");
                            },
                            success: function(data) {
                                $("#resultado").empty();
                                $("#resultado").append(data);
                            }
                        });
                    }
                    function vacio() {
                        // alert("ya pasaon 2segundos y esta vacio");
                        $("#resultado").css("display", "none");
                    }

                });
            });
        </script>
        <script type="text/javascript">
            function capturar(id) {
                //alert(id);
                //optener
                var divSeleccionado = $("#" + id).text();
                //Asignar
                $("#search").val(divSeleccionado);
                //ocultar
                $("#resultado").css("display", "none");

            }
        </script>
        <script type="text/javascript">

            function buscar() {
                //optener valor del input
                var search = $("#search").val();
                var grafica = "false";
                if ($("#graficas").is(':checked')) {
                    grafica = "true";
                }
                var source = $('input:radio[name=grupo1]:checked').val();
                
                //quitamos los espacios en blanco, inicio-final
                //  var search2 = $.trim(search);
                //sacamos el primer espacio en blanco 
                //  var search3 = search2.indexOf(" ");
                //sacamos el id, que enviaremos como parametro
                //  var search4 = search2.substring(0, search3);
                //alert(search4);
                $.ajax({
                    type: "POST",
                    url: "searchTaxa",
                    data: {
                        taxa: search,
                        grafica: grafica,
                        source: source
                    },
                    dataType: "html",
                    beforeSend: function() {
                        //imagen de carga
                         $('#buscador').removeClass('btn btn-default');//
                         $('#buscador').addClass('btn btn-default disabled');//
                         $("#detalles").html("<p align='center'><img src='http://form.cenp.com.br/img/carregando.gif' /></p>");
                    },
                    error: function() {
                        alert("error petición ajax");
                        //borrar(ocultar loading div
                    },
                    success: function(data) {
                        $('#buscador').removeClass('btn btn-default disabled');//
                         $('#buscador').addClass('btn btn-default');//
                        $("#detalles").empty();
                        $("#detalles").append(data);
                         
                    }
                });

            }
        </script>


        <title>TAXA</title>

    </head>
    <body >
        <div id="wrapper">

            <!-- Navigation -->
            <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0; padding-top:10px; padding-left:0px; padding-right:15px; background-color:#ffffff; z-index:0;">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <span class="b1">
                        <span class="b1"><img id="logos" src="images/logotipo4.png" alt="logo" width="85%" height="100px" style="padding-left:10px;" /></span>
                    </span>
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
                        <!-- <h1 class="page-header">ADMINISTRADOR</h1>-->
                    </div>
                    <!-- /.col-lg-12 -->
                </div>
                <br>
                <!-- /.row -->
                <div class="row">

                    <!--<div class="col-lg-12">
                        <button  class="btn btn-default" id="cargar">Cargar</button>
                    </div>-->
                    <div class="col-lg-12" id="contenido">

                        <div class="col-lg-6">
                            <div class="panel" style="border:none;">
                                <label>Buscar Taxón:</label>
                                <div class="input-group custom-search-form" style="z-index:0;">
                                    <input type="text" class="form-control" placeholder="Buscar..." id="search" name="search">
                                    <span class="input-group-btn">
                                        <button class="btn btn-default" type="button" id="buscador" onclick="buscar()">
                                            <i class="fa fa-search"></i>
                                        </button>
                                    </span>

                                </div>
                                <!-- /.panel-heading -->
                                <div id="resultado" style="position:absolute; z-index:2; background-color:#ffffff;">

                                </div>
                                <!-- .panel-body -->
                            </div>
                        </div>
                        <div class="col-lg-2" >
                            <br>
                            <input type="checkbox" id="graficas"> <label>Generar gráficas</label>


                        </div>
                        <div class="col-lg-2">
                            <label>Datos</label>
                            <span style="margin-right:5px; cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, 'Seleccionar la fuente de los datos.<br>La opción amplicones busca taxones dentro de las asignaciones realizadas sobre librerías de genes marcadores (16S).<br>La opción Shotgun, buscará taxones sobre las predicciones taxonómicas realizadas en los metagenomas mediante espectro de kámeros (Kraken).', {position: 0});"></span>
                            <div class="panel panel-default">
                                <div class="panel-body">
                                    <div class="form-group">
                                        <!--<label>Radio Buttons</label>-->
                                        <div class="radio" style="margin-bottom:2px; margin-top: 2px;">
                                            <label>
                                                <input type="radio" name="grupo1" id="optionsRadios2" checked value="AMP" >Amplicones
                                            </label>
                                        </div>

                                        <div class="radio" style="margin-bottom:2px; margin-top: 2px;">
                                            <label>
                                                <input type="radio" name="grupo1" id="optionsRadios1" value="SHOT" >Shotgun
                                            </label>
                                        </div>


                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-12">
                        <div class="panel panel-default" >  

                            <!-- /.panel-heading -->
                            <div class="panel-body" style="position:relative; z-index:1;" id="detalles" style="background-color:#ddd;">


                            </div>

                        </div>
                    </div>



                </div>
                <div class="panel panel-default" style="padding:15px; position:relative; z-index: 1;">
                    <div class="panel-heading" style="background-color:#eee;">
                        <b style="color:#d9534f;">Filtros</b> <p class="fa fa-toggle-down" id="filtros-bt" style="cursor:pointer;"></p>
                    </div>
                    <br>
                    <div class="row" id="filtros-row" style="display:none;">

                        <div class="col-md-4">                      
                            <div class="panel panel-default" >
                                <div class="panel-heading">
                                    <center><b style="color:#d9534f;">Tipo de profundidad</b></center>
                                </div>
                                <!-- /.panel-heading -->
                                <div class="panel-body" >
                                                    <div class="dataTable_wrapper">
                                                        <table width="100%" class="table table-striped table-bordered table-hover" id="profundidad">
                                                            <thead>
                                                                <tr style="display:none;">
                                                                    <th></th>
                                                                    <th></th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>

                                                                
                         			<tr style="border-top:none;">
                                                    <td style="padding:10px;"><b>Mínimo de Oxigeno</b></td>
                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><input type="checkbox" value="MIN" ></td>                                                                   
                                                </tr>
                                                <tr>
                                                    <td style="padding:10px;"><b>Máximo de Fluorescencia</b></td>
                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><input type="checkbox" value="MAX"  ></td>
                                                </tr>
                                                <tr>
                                                    <td style="padding:10px;"><b>Mil metros</b></td>
                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><input type="checkbox" value="MIL"  ></td>
                                                </tr>
                                                <tr>
                                                    <td style="padding:10px;"><b>Fondo</b></td>
                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><input type="checkbox" value="FON"  ></td>
                                                </tr>   
                                                <tr>
                                                    <td style="padding:10px;"><b>Sedimento</b></td>
                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><input type="checkbox" value="SED"  ></td>
                                                </tr>                                            

   

                                                            </tbody>
                                                        </table>
                                                    </div>
                                    <!-- /.table-responsive -->
                                </div>
                                <!-- /.panel-body -->
                            </div>                               

                        </div>
                        <div class="col-md-4"> 
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <center><b style="color:#d9534f;">Campañas</b></center>
                                </div>
                                <!-- /.panel-heading -->
                                <div class="panel-body" >
                                                    <div class="dataTable_wrapper">
                                                        <table width="100%" class="table table-striped table-bordered table-hover" id="campanas">
                                                            <thead style="display:none;">
                                                                <tr>
                                                                    <th></th>
                                                                    <th></th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>

                                                                
						<tr style="border-top:none;">
                                                    <td style="padding:10px;"><b>Metagenómica I</b></td>
                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><input type="checkbox" value="1" id="grafica" ></td>                                                                   
                                                </tr>
                                                <tr>
                                                    <td style="padding:10px;"><b>Met. y Malla Fina I</b></td>
                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><input type="checkbox" value="2" id="grafica" ></td>
                                                </tr>
                                                <tr>
                                                    <td style="padding:10px;"><b>SoGOM I</b></td>
                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><input type="checkbox" value="3" id="grafica" ></td>
                                                </tr>
                                                <tr>
                                                    <td style="padding:10px;"><b>SoGOM II</b></td>
                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><input type="checkbox" value="4" id="grafica" ></td>
                                                </tr> 
                                                <tr>
                                                    <td style="padding:10px;"><b>XIXIMI IV</b></td>
                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><input type="checkbox" value="5" id="grafica" ></td>
                                                </tr>  
                                                <tr>
                                                    <td style="padding:10px;"><b>XIXIMI V</b></td>
                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><input type="checkbox" value="6" id="grafica" ></td>
                                                </tr>                                           
                                                <tr>
                                                    <td style="padding:10px;"><b>GOMEX IV</b></td>
                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><input type="checkbox" value="7" id="grafica" ></td>
                                                </tr>                                           

   

                                                            </tbody>
                                                        </table>
                                                    </div>
                                </div>
                            </div>
                        </div>  
                        <div class="col-md-4"> 
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <center><span style="margin-right:5px; cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, '#zonasinfo',{position: 0});"></span><b style="color:#d9534f;">Zonas</b></center>
                                </div>
                                            <div style="display:none;">
                                                <div id="zonasinfo">
                                                    <center><img src="images/regiones.gif" style="margin-left:10px;width:450px;height:372px;" alt="zonas info" /></center>
                                                    
                                                    <p style="text-align:justify;">La descripción de los rasgos geomorfológicos del golfo, se explica a partir de 7 provincias establecidas por Antoine (1972), con base en los cambios de dirección de la plataforma continental en seis de ellas, y en la séptima que caracteriza a la porción central de la Cuenca del Golfo.</p>
                                                </div>
                                            </div>
                                <!-- /.panel-heading -->
                                <div class="panel-body" >
                                                    <div class="dataTable_wrapper">
                                                        <table width="100%" class="table table-striped table-bordered table-hover" id="provincias">
                                                            <thead>
                                                                <tr style="display:none;">
                                                                    <th></th>
                                                                    <th></th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>

                                                                
                                                <tr style="border-top:none;">
                                                    <td style="padding:10px;"><span style="margin-right: 5px; cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, 'Bordea la plataforma de la costa occidental de Florida hasta los 84° de longitud oeste, que tiene una dirección ligeramente noroeste; es aún más amplia que la Península de Yucatán, excediendo los 260 km. Hacia el sur de la provincia la inclinación de su pendiente aumenta bruscamente y va de 100 a 1 000 m de profundidad. Está pendiente constituye el Escarpe de Florida, el cual bordea el Estrecho y la plataforma oeste de Florida.', {position: 0})"></span><b>Primera Provincia</b></td>
                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><input type="checkbox" value="1" id="provincia" ></td>                                                                   
                                                </tr>

                                            <tr>
                                                <td style="padding:10px;"><span style="margin-right:5px; cursor:pointer; " class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, 'Surge a partir de un cambio de dirección de la plataforma al suroeste, la cual es angosta, menor a los 80 km, y se estrecha aún más conforme se acerca al delta del Río Mississippi. En los 28° de latitud norte y 88° longitud oeste, el Escarpe de Florida y el abanico o Cono del Mississippi, constituyen una zona de elevaciones y depresiones denominada Cañón De Soto, el cual se ubica por debajo de la isobata de 300 m y alcanza profundidades hasta de 2 000 m.', {position: 0})"></span><b>Segunda Provincia</b></td>                                                    
                                                <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><input type="checkbox" value="2" id="provincia" ></td>
                                            </tr>
                                            <tr>
                                                <td style="padding:10px;"><span style="margin-right:5px; cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, 'Una de las más amplias, y comprende la parte occidental del Cono del Mississippi continuándose hasta el límite de la zona económica de México ubicada hacia el paralelo 26. La plataforma tiene aproximadamente 100 km de amplitud, flexionándose hacia el suroeste donde se reduce a 82 km; su pendiente es ligera hasta la isobata de 200 m donde ésta aumenta para alcanzar los 1 000 m de profundidad.', {position: 0})"></span><b>Tercera Provincia</b></td>
                                                <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><input type="checkbox" value="3" id="provincia" ></td>
                                            </tr>
                                            <tr>
                                                <td style="padding:10px;"><span style="margin-right:5px; cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, 'Se inicia en donde la plataforma se orienta al oeste y ocupa desde el delta del Río Bravo con 70-82 km de amplitud, resultado del aporte de sedimentos, hasta el paralelo 20, donde ésta cambia de orientación para dirigirse hacia la Sonda de Campeche. La plataforma se va angostando en su trayectoria hacia el sur hasta alcanzar 33-37 km en el paralelo 23 formando una ladera de poca disección con pendiente media entre 1 y 2°, y su talud encuentra la base a una profundidad próxima de 3 000 m. A partir de este paralelo la plataforma nuevamente se flexiona al sureste y llega a la zona volcánica de San Andrés Tuxtla en Veracruz, para alcanzar su mínima amplitud de 6 a 16 km, donde incrementa su pendiente 2° y en áreas muy localizadas hasta 15. En la porción comprendida entre el paralelo 20 y el 23, el talud es disectado por valles submarinos que configuran un relieve irregular, el cual se denomina Cordillera Ordoñez (Cserna, 1984) ubicada dentro de la séptima provincia', {position: 0})"></span><b>Cuarta Provincia</b></td>
                                                <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><input type="checkbox" value="4" id="provincia" ></td>
                                            </tr> 
                                            <tr>
                                                <td style="padding:10px;"><span style="margin-right:5px; cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, 'Comprende la plataforma y talud continental del sur de Veracruz y parte de Campeche. La primera se hace amplia en dirección a la Península de Yucatán con una extensión de 110-130 km frente a Punta Frontera, para después flexionarse y rodear la península. Presenta una débil pendiente de 1 a 5°, donde existen predominantemente sedimentos carbonatados de origen biogénico que convierten a esta zona en una terraza acumulativa. El talud continental de esta provincia tiene un relieve comparable al del noroeste de la cuenca (Provincia 3), donde los domos salinos aquí existentes se orientan en dirección al centro del Golfo de México, a través de sinuosidades a manera de cordones alineados.', {position: 0})"></span><b>Quinta Provincia</b></td>
                                                <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><input type="checkbox" value="5" id="provincia" ></td>
                                            </tr>                                             
                                            <tr>
                                                <td style="padding:10px;"><span style="margin-right:5px; cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, 'En su primera parte, el Cañón de Campeche es un rasgo sobresaliente del talud continental el cual se limita por el escarpe del mismo nombre y cuyo origen puede estar relacionado con la evolución tectónica de esta zona. El Escarpe de Campeche se extiende a profundidades de 2 400 a 2 600 m bordeando por el occidente y noroeste a la plataforma de la península con una pendiente mayor de 45°. La segunda flexión de la plataforma ocurre a partir del paralelo 22. Aquí el escarpe se separa gradualmente del borde de la plataforma y queda entre ambas estructuras una zona a manera de planicie denominada \'Planicie de Lomeríos, marginal a la plataforma continental\'. Esta zona es muy variable en su amplitud y pendiente, pero en general tiene una inclinación débil de 1.5 a 2.5°.', {position: 0})"></span><b>Sexta Provincia</b></td>
                                                <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><input type="checkbox" value="6" id="provincia" ></td>
                                            </tr> 
                                            <tr>
                                                <td style="padding:10px;"><span style="margin-right:5px; cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, 'Corresponde a la parte central del Golfo de México. Comprende la Cuenca o Llanura Abisal de Sigsbee, llamada así por tener las mayores profundidades y funcionar como captadora de sedimentos. Está limitada totalmente por la isobata de 3 600 m y presenta dos zonas aún más profundas, una de ellas a los 3 735 m y la otra a los 3 741 m, donde se localizan algunas colinas de hasta 200 y 300 m de altura. Esta cuenca sirve como frontera al Escarpe de Campeche y constituye, en el noroeste del Golfo, el Escarpe de Sigsbee.', {position: 0})"></span><b>Séptima Provincia</b></td>
                                                <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><input type="checkbox" value="7" id="provincia" ></td>
                                            </tr>                                           

   

                                                            </tbody>
                                                        </table>
                                                    </div>
                                </div>
                            </div>
                        </div>                       

                    </div>

                </div>

            </div>

            <!-- DataTables JavaScript -->
            <script src="bower_components/datatables/media/js/jquery.dataTables.min.js"></script>
            <script src="bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.min.js"></script>
            <script src="bower_components/datatables-responsive/js/dataTables.responsive.js"></script>


            <!-- Bootstrap Core JavaScript -->
            <script src="bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

            <!-- Metis Menu Plugin JavaScript -->
            <script src="bower_components/metisMenu/dist/metisMenu.min.js"></script>

            <!-- Custom Theme JavaScript -->
            <script src="dist/js/sb-admin-2.js"></script>
                       <!--SCRIPT PARA OCULTAR DIV DE MIS BUSQUEDAS-->
            <script>
                $(document).ready(function () {
                    var clic = 1;
                    $("#filtros-bt").on("click", function () {
                        if (clic == 1) {
                            $('#filtros-row').show(); //muestro
                            $('#filtros-bt').removeClass('fa fa-toggle-down');//elimina clse del icono down
                            $('#filtros-bt').addClass('fa fa-toggle-up');//agrega la clase del icono up
                            clic = clic + 1;
                        } else {
                            $('#filtros-row').hide(); //oculto
                            $('#filtros-bt').removeClass('fa fa-toggle-up');//elimina clse del icono up
                            $('#filtros-bt').addClass('fa fa-toggle-down');//agrega la clase del icono down
                            clic = 1;
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

            <script>
                $(document).ready(function () {
                    $('#campanas').DataTable({
                        responsive: true,
                        bFilter: false, 
                        bInfo: false
                    });
                });
            </script>
            <script>
                $(document).ready(function () {
                    $('#profundidad').DataTable({
                        responsive: true,
                        bFilter: false, 
                        bInfo: false
                    });
                });
            </script>
            <script>
                $(document).ready(function () {
                    $('#provincias').DataTable({
                        responsive: true,
                        bFilter: false, 
                        bInfo: false
                    });
                });
            </script>

    </body>
</html>
