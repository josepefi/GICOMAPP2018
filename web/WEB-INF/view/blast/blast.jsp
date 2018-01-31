<%-- 
    Document   : blast
    Created on : 24/10/2016, 01:47:02 PM
    Author     : Jose Pefi
--%>

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
    //response.setHeader("Pragma", "no-cache");
    //response.setDateHeader("Expires", 0);

    if (session == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    Usuario usuario = (Usuario) sesion.getAttribute("userObj");
    String nombreCompleto = usuario.getNombres() + " " + usuario.getApellidos();
%>
<!DOCTYPE html>
<html>
    <head>
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

        <!-- Mandar form con archivo  -->
        <script src="js/blastForm.js"></script>

        <!--ALERTAS-->
        <script src="alerta/dist/sweetalert-dev.js"></script>
        <link rel="stylesheet" href="alerta/dist/sweetalert.css">

        <!--ESCRIPT QUE ACTIVA EL BUSCADOR EN LA TABLA MIS BUSQUEDAS-->
        <script>
            $(document).ready(function () {
                $('#tabla-misbusquedas').DataTable({
                    responsive: true,
                    pageLength: 5,
                    order: [[2, "desc"]]
                });
            });</script>       

        <!--ESCRIPT QUE ACTIVA EL BUSCADOR EN LA TABLA METAGENOMAS-->
        <script>
            $(document).ready(function () {
                $('#tabla-metagenomas').DataTable({
                    responsive: true,
                    pageLength: 5
                });
            });</script>

        <!--ESCRIPT QUE ACTIVA EL BUSCADOR EN LA TABLA GENOMAS-->
        <script>
            $(document).ready(function () {
                $('#tabla-genomas').DataTable({
                    responsive: true,
                    pageLength: 5
                });
            });</script>
        <!--ESCRIPT PARA OBTENER LOS VALORES DEL FORMULARIO-->
        <script type="text/javascript">
            $(document).ready(function () {
                $("#ObtenerVal").click(function (event) {
                    var table = $("#tabla-metagenomas").DataTable();
                    var rows = table.rows().nodes();
                    var resultMG = [];
                    var resultG = [];
                    var i = 0;
                    $('input[type="checkbox"]', rows).each(function (index) {
                        if ($(this).is(':checked')) {
                            resultMG[i] = $(this).val();
                            i++;
                        }
                    });
                    var tableG = $("#tabla-genomas").DataTable();
                    var rowsG = tableG.rows().nodes();
                    i = 0;
                    $('input[type="checkbox"]', rowsG).each(function (index) {
                        if ($(this).is(':checked')) {
                            resultG[i] = $(this).val();
                            i++;
                        }
                    });
                    var metagenomas = resultMG.join(',');
                    var genomas = resultG.join(',');

                    uploadXHTML(genomas, metagenomas);
                    event.preventDefault(); // Stop the submit here!

                });
            });

        </script>

        <!--FUNCIÓN PARA ELIMINAR UN BLASTJOB--->

        <script>
            function eliminar(id, url) {
                swal({
                    title: "Estas seguro de elimar esta busqueda?",
                    //text: url+"---"+id,
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#2274ba",
                    confirmButtonText: "Si, Acepto!",
                    cancelButtonColor: "#2274ba",
                    cancelButtonText: "Cancelar",
                    closeOnConfirm: true,
                    closeOnCancel: true
                },
                        function (isConfirm) {
                            if (isConfirm) {

                                $(function () {
                                    $('#' + id).hide(); //ocultamos el registro    

                                    //parametros enviados al controlador 'deleteJob'
                                    var params = {
                                        id: id,
                                        url: url
                                    };
                                    $.post('deleteJob', params, function (data) {
                                        //
                                    });
                                });

                            }
                        });
            }
        </script>      

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>BLAST</title>
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
                    <div class="col-lg-12">
                        <h2 class="page-header" style="color:#337ab7;">BLAST</h2>
                    </div>
                    <!-- /.col-lg-12 -->
                </div>
                <br>
                <!-- /.row -->
                <div class="row">

                    <div class="col-lg-12">
                        <div class="panel panel-default" >
                            <div class="panel-heading" style="background-color:#eee;">
                                <b style="color:#d9534f;"> Mis búsquedas</b>  <p class="fa fa-toggle-up" id="mis-busquedas" style="cursor:pointer;"></p>
                            </div>
                            <div class="panel-body"  id="mi-busquedablast">

                                <div class="col-lg-12">
                                    <div class="panel panel-default">
                                        <!-- <div class="panel-heading">
                                             <center>Resultado mis busquedas</center>
                                         </div>-->
                                        <!-- /.panel-heading -->
                                        <div class="panel-body" >
                                            <div class="dataTable_wrapper">
                                                <table width="100%" class="table table-striped table-bordered table-hover" id="tabla-misbusquedas">
                                                    <thead>
                                                        <tr>
                                                            <th>Nombre</th>
                                                            <th>Estatus</th>
                                                            <th>Inicio</th>
                                                            <th>Fin</th>
                                                            <th></th>
                                                        </tr>
                                                    </thead>
                                                    <tbody id="metas">
                                                        <%
                                                            Object jobsObj = request.getAttribute("jobs");
                                                            ArrayList<Job> jobs = null;

                                                            if (jobsObj != null) {
                                                                jobs = (ArrayList<Job>) jobsObj;
                                                            }
                                                            if (jobs != null) {
                                                                for (Job jb : jobs) {

                                                        %> 
                                                        <tr style="text-align: left;" class="meta" id="<%= jb.getId_job()%>" width="100%" >
                                                            <td><a href="showJob?jobURL=<%= jb.getURL()%>"><%= jb.getJob_name()%></a></td>
                                                            <td><%= jb.getStatus()%></td>
                                                            <td><%= jb.getStart_date()%></td>
                                                            <td><%= jb.getEnd_date()%></td>
                                                            <td style="text-align:center;"><button onclick="eliminar('<%= jb.getId_job()%>', '<%= jb.getURL()%>')" class="glyphicon glyphicon-trash" id="eliminar"></button></td>
                                                        </tr>
                                                        <%
                                                                }
                                                            }
                                                        %>

                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                        <!-- /.panel-body -->
                                    </div>
                                </div>                                  
                            </div>                           
                            <div class="panel-heading" style="background-color:#eee;">
                                <b style="color:#d9534f;">Crear nueva búsqueda Blast</b>  <p class="fa fa-toggle-up" id="nueva-busqueda" style="cursor:pointer;"></p>
                            </div>
                            <div class="panel-body" id="busquedablast">
                                <div class="row">
                                    <form rol="form">
                                        <div class="col-lg-8">

                                            <div class="form-group">
                                                <label>Nombre</label>
                                                <input class="form-control" placeholder="" id="nombre">
                                            </div>
                                            <div class="form-group">
                                                <label>Introduzca el número de acceso (s), gi (s), o secuencia (s) FASTA</label>
                                                <textarea class="form-control" rows="4" id="secuencia"></textarea>
                                            </div>
                                            <div class="form-group">
                                                <label>O, subir archivo</label>
                                                <input id="uploadedFasta" type="file" value="upload">
                                                <progress style="display: none" id="progressBar" max="100" value="0"></progress>
                                                <span  id="percentageCalc" style="display: none"></span>
                                            </div>    
                                        </div>

                                        <div class="col-lg-2">
                                            <label>Titulo de opción</label>
                                            <div class="panel panel-default">
                                                <div class="panel-body">
                                                    <div class="form-group">
                                                        <!--<label>Radio Buttons</label>-->
                                                        <div class="radio">
                                                            <label>
                                                                <input type="radio" name="algoritmo" id="optionsRadios1" value="BLASTN" >BLAST N
                                                            </label>
                                                        </div>
                                                        <div class="radio">
                                                            <label>
                                                                <input type="radio" name="algoritmo" id="optionsRadios2" value="BLASTP">BLAST P
                                                            </label>
                                                        </div>
                                                        <div class="radio">
                                                            <label>
                                                                <input type="radio" name="algoritmo" id="optionsRadios2" value="BLASTX">BLAST X
                                                            </label>
                                                        </div>	
                                                        <div class="form-group">
                                                            <label>e. value:</label>
                                                            <input class="form-control" id="eval" value="10">
                                                        </div>

                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-lg-6">
                                            <div class="panel panel-default">
                                                <div class="panel-heading">
                                                    <center><b style="color:#d9534f;">Metagenomas</b></center>
                                                </div>
                                                <!-- /.panel-heading -->
                                                <div class="panel-body">
                                                    Marcar todos
                                                    <input type="checkbox" name="marcarTodo" id="marcarTodoMG" /> 
                                                    <br>
                                                    <br>
                                                    <div class="dataTable_wrapper">
                                                        <table width="100%" class="table table-striped table-bordered table-hover" id="tabla-metagenomas">
                                                            <thead>
                                                                <tr>
                                                                    <th></th>
                                                                    <th>Nombre</th>
                                                                    <th>Muestra</th>
                                                                    <th>Estación</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody id="metas">
                                                                <%
                                                                    ArrayList<ArrayList<String>> metagenomas = null;
                                                                    Object metagenomasOobj = request.getAttribute("metagenomas");

                                                                    if (metagenomasOobj != null) {
                                                                        metagenomas = (ArrayList<ArrayList<String>>) metagenomasOobj;
                                                                    }
                                                                    if (metagenomas != null) {
                                                                        for (int mg = 0; mg < metagenomas.size(); mg++) {

                                                                %> 
                                                                <tr style="text-align: left;" class="meta">
                                                                    <td><input type="checkbox" value="<%= metagenomas.get(mg).get(0)%>" name="checkMG"></td>
                                                                    <td><%= metagenomas.get(mg).get(1)%></td>
                                                                    <td><%= metagenomas.get(mg).get(2)%></td>
                                                                    <td><%= metagenomas.get(mg).get(3)%></td>
                                                                </tr>
                                                                <%
                                                                        }
                                                                    }
                                                                %>

                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <!-- /.panel-body -->
                                            </div>

                                        </div>
                                        <div class="col-lg-6">

                                            <div class="panel panel-default">
                                                <div class="panel-heading">
                                                    <center><b style="color:#d9534f;">Genomas</b></center>
                                                </div>
                                                <!-- /.panel-heading -->
                                                <div class="panel-body">
                                                    Marcar todos
                                                    <input type="checkbox" name="marcarTodo" id="marcarTodoG" /> 
                                                    <br>
                                                    <br>
                                                    <div class="dataTable_wrapper">
                                                        <table width="100%" class="table table-striped table-bordered table-hover" id="tabla-genomas">
                                                            <thead>
                                                                <tr>
                                                                    <th></th>
                                                                    <th>Nombre</th>
                                                                    <th>Taxa</th>
                                                                    <th>Estación</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody id="tgenomas">
                                                                <%
                                                                    ArrayList<ArrayList<String>> genomas = null;
                                                                    // int c = request.getAttribute("campanaid");
                                                                    Object genomasOobj = request.getAttribute("genomas");

                                                                    if (genomasOobj != null) {
                                                                        genomas = (ArrayList<ArrayList<String>>) genomasOobj;
                                                                    }
                                                                    if (genomas != null) {
                                                                        for (int g = 0; g < genomas.size(); g++) {

                                                                %>   

                                                                <tr style="text-align: center;" class="genomas" id="genoma">
                                                                    <td><input type="checkbox" value="<%= genomas.get(g).get(0)%>" class="boton" name="checkG"></td>
                                                                    <td><%= genomas.get(g).get(1)%></td>
                                                                    <td><%= genomas.get(g).get(2)%></td>
                                                                    <td><%= genomas.get(g).get(3)%></td>
                                                                </tr>

                                                                <%
                                                                        }
                                                                    }
                                                                %>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>   
                                    <!-- /.col-lg-6 (nested) -->

                                    <!-- /.col-lg-4 (nested) -->
                                </div>                                                          
                                <!-- /.row (nested) -->
                                           
                                          <!-- <div class="col-lg-12">
                                           <p class="fa fa-plus-circle" id="masOpciones"> Opciones Avanzadas</p>
                                           </div>-->
                                            <!--div opciones avanzadas-->
                                          <!--  <div class="col-lg-12" id="opcionesAvanzadas" style="display:none;">
                                            <div class="form-group">
                                            <label>Max target sequences</label>
                                            <select class="form-control">
                                                <option>1</option>
                                                <option>2</option>
                                            </select>
                                            </div>
                                            <label>Short queries</label>
                                            <div class="radio">
                                                <label>
                                                    <input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" checked>Radio 1
                                                </label>
                                            </div>
                                         <div class="form-group">
                                            <label>Expect threshold</label>
                                            <input class="form-control">
                                        </div>   
                                            </div> -->
                                            <div class="col-lg-1">
                                                <button type="reset" class="btn btn-default">Limpiar</button>
                                            </div>
                                    </form>

                                    <div class="col-lg-1">
                                        <button  class="btn btn-default" id="ObtenerVal">Buscar</button>
                                    </div>
                                     <!---->                                
                            </div>
                            <!-- /.panel-body -->
                        </div>
                    </div>

                </div>

            </div>
            <!-- /#page-wrapper -->

        </div>
        <!--SCRIPT PARA MARCAR Y DESMARCAR LOS CHECKS DE LA TABLA METAGENOMAS-->
        <script>
            $("#marcarTodoMG").change(function () {
                var table = $("#tabla-metagenomas").DataTable();
                var rows = table.rows().nodes();
                $('input[type="checkbox"]', rows).prop('checked', this.checked);

            });
        </script>
        <!--16 de nov 0155 50155136-->

        <!--SCRIPT PARA MARCAR Y DESMARCAR LOS CHECKS DE LA TABLA GENOMAS-->

        <script>
            $("#marcarTodoG").change(function () {
                var table = $("#tabla-genomas").DataTable();
                var rows = table.rows().nodes();
                //ya no es necesario validar "$(this).is(':checked')" ya que es el vallor que se usa implicitamente
                $('input[type="checkbox"]', rows).prop('checked', this.checked);


            });
        </script>

        <!--SCRIPT PARA OCULTAR DIV DE MIS BUSQUEDAS-->
        <script>
            $(document).ready(function () {
                var clic = 1;
                $("#mis-busquedas").on("click", function () {
                    if (clic == 1) {
                        $('#mi-busquedablast').hide(); //oculto
                        $('#mis-busquedas').removeClass('fa fa-toggle-up');//elimina clse del icono up
                        $('#mis-busquedas').addClass('fa fa-toggle-down');//agrega la clase del icono down
                        clic = clic + 1;
                    } else {
                        $('#mi-busquedablast').show(); //muestro
                        $('#mis-busquedas').removeClass('fa fa-toggle-down');//elimina clse del icono down
                        $('#mis-busquedas').addClass('fa fa-toggle-up');//agrega la clase del icono up
                        clic = 1;
                    }
                });
            });
        </script>
        <!--SCRIPT PARA OCULTAR DIV DE BUSQUEDABLAST-->
        <script>
            $(document).ready(function () {
                var clic = 1;
                $("#nueva-busqueda").on("click", function () {
                    if (clic == 1) {
                        $('#busquedablast').hide(); //oculto
                        $('#nueva-busqueda').removeClass('fa fa-toggle-up');//elimina clse del icono up
                        $('#nueva-busqueda').addClass('fa fa-toggle-down');//agrega la clase del icono down
                        clic = clic + 1;
                    } else {
                        $('#busquedablast').show(); //muestro
                        $('#nueva-busqueda').removeClass('fa fa-toggle-down');//elimina clse del icono down
                        $('#nueva-busqueda').addClass('fa fa-toggle-up');//agrega la clase del icono up
                        clic = 1;
                    }

                });
            });
        </script>
        <!--SCRIPT PARA OCULTAR DIV DE OPCIONES AVANZADAS-->
        <script>
            $(document).ready(function () {
                var clic = 1;
                $("#masOpciones").on("click", function () {
                if (clic == 1) {
                    $('#opcionesAvanzadas').show(); //muestro
                    $('#masOpciones').removeClass('fa fa-plus-circle');//elimina clse del fa fa-plus-circle
                    $('#masOpciones').addClass('fa fa-minus-circle');//agrega la clase del icono fa fa-minus-circle
                    clic = clic + 1;
                } else {
                    $('#opcionesAvanzadas').hide(); //oculto
                    $('#masOpciones').removeClass('fa fa-minus-circle');//elimina clase fa-minus-circle
                    $('#masOpciones').addClass('fa fa-plus-circle');//agrega la clase fa fa-plus-circle
                    clic = 1;
                }


                });
            });
        </script>
    </body>
</html>
