<%-- 
    Document   : metabolismo
    Created on : 4/08/2017, 01:36:01 PM
    Author     : Jose Pefi
--%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="bobjects.Usuario"%>
<%
    HttpSession sesion = request.getSession();
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    if (session == null) {
        response.sendRedirect("index.jsp");
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

        <!---TOlTIPS---->
        <link href="themes/1/tooltip.css" rel="stylesheet" type="text/css">
        <script src="themes/1/tooltip.js" type="text/javascript"></script> 

        <!-- Custom Theme JavaScript -->
        <script src="dist/js/sb-admin-2.js"></script>
        <!-- Ventana Modal -->
        <link href="css/ventanaModal.css" rel="stylesheet" type="text/css">

        <!--Graficas en canvas--->
        <script src="js/canvasjs.min.js"></script>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"></script>

        <!--ALERTAS-->
        <script src="alerta/dist/sweetalert-dev.js"></script>
        <link rel="stylesheet" href="alerta/dist/sweetalert.css">
        <!-- DataTables JavaScript -->
        <script src="bower_components/datatables/media/js/jquery.dataTables.min.js"></script>
        <script src="bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.min.js"></script>
        <script src="bower_components/datatables-responsive/js/dataTables.responsive.js"></script>

        <script>

            $(document).ready(function () {
                $('#tabla-resultados').DataTable({
                    responsive: true,
                    pageLength: 5
                });
            });

            $(document).ready(function () {

                //segundo nivel
                $("select[name=nivel1]").change(function () {
                    var nivel = $('select[name=nivel1]').val();
                    $.ajax({
                        type: "POST",
                        url: "nivel2",
                        data: {
                            clave: nivel
                        },
                        dataType: "html",
                        beforeSend: function () {
                            //imagen de carga
                            $("#resultadoNivel2").html("<p align='center'><img src='http://form.cenp.com.br/img/carregando.gif' /></p>");
                        },
                        error: function () {
                            alert("error en la petición");
                        },
                        success: function (data) {
                            $("#resultadoNivel2").empty();
                            $("#resultadoNivel2").append(data);
                        }
                    });
                });

                //Tercer nivel
                $("#boton").click(function () {
                    var nivel1 = $('select[name=nivel1]').val();
                    var nivel2 = $('select[name=nivel2]').val();
                    var metodo = $('select[name=metodo]').val();

                    //var metagenomaValor = $('input:radio[name=metagenomas]:checked').val();


                    var metagenomaValor = [];
                    var i = 0;
                    $('#metagenomas tbody tr [type="checkbox"]').each(function (index) {
                        if ($(this).is(':checked')) {
                            metagenomaValor[i] = $(this).val();
                            i++;
                        }
                    });

                    var metaResultados = metagenomaValor.join(',');


                    //alert("El las columnas son:"+tablaResultados);
                    //alert ("El tamaño de los metagenomas es:"+metagenomaValor.length);
                    //var myJSON = JSON.stringify(metagenomaValor);
                    //alert("El metagenoma original es:"+metagenomaValor);
                    //alert("El metagenoma secundario es"+myJSON);

                    if (nivel1 == "-1") {
                        swal({
                            title: "<span style='color:#red'>Selecciona el primer nivel!</span>",
                            imageUrl: "images/error_busqueda.png",
                            html: true
                        });
                    } else if (nivel2 == "-1") {
                        swal({
                            title: "<span style='color:#red'>Selecciona el segundo nivel!</span>",
                            imageUrl: "images/error_busqueda.png",
                            html: true
                        });
                    } else if (metodo == "-1") {
                        swal({
                            title: "<span style='color:#red'>Selecciona un mètodo!</span>",
                            imageUrl: "images/error_busqueda.png",
                            html: true
                        });
                    } else if (metagenomaValor == undefined) {
                        swal({
                            title: "<span style='color:#red'>Selecciona un metagenoma!</span>",
                            imageUrl: "images/error_busqueda.png",
                            html: true
                        });
                    } else if (metodo == 1 && metagenomaValor.length > 1) {
                        swal({
                            title: "<span style='color:#red'>No puedes seleccionar mas de un metagenoma para este metodo!</span>",
                            imageUrl: "images/error_busqueda.png",
                            html: true
                        });
                    } else {
                        $.ajax({
                            type: "POST",
                            url: "generarVia",
                            data: {
                                nivel2: nivel2,
                                metagenoma: metaResultados,
                                metodo: metodo
                            },
                            dataType: "html",
                            beforeSend: function () {
                                //imagen de carga
                                $("#resultadoVia").html("<p align='center'><img src='http://form.cenp.com.br/img/carregando.gif' /></p>");
                            },
                            error: function () {
                                alert("error en la petición");
                            },
                            success: function (data) {
                                $("#resultadoVia").empty();
                                $("#resultadoVia").append(data);
                            }
                        });

                    }
                    //$("#mapa").css("display", "block");
                });

            });
        </script>
        <style>
            .dataTables_paginate, .paging_simple_numbers{
                margin-left:-100%;
            }

        </style>
        <style>
            canvas {
                max-width: 100%;
                height: auto;
            }   
            img {
                display: block;
                max-width: 100%;
                height: auto;
            }


        </style>
        <title>METABOLISMO</title>

    </head>

    <body onLoad="img_canvas()">


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
                    <h2 class="page-header" style="color:#337ab7; float:left;">Metabolismo <!--<span style="margin-right:5px; " class="glyphicon glyphicon-info-sign" class="tooltip" data-toggle="modal" data-target="#myModal" style="cursor:pointer;" ></span>--></h2>

                </div>

                <!-- Modal -->
                <div class="modal fade" id="myModal" role="dialog">
                    <div class="modal-dialog">

                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <!--<h4 class="modal-title">Modal Header</h4>-->
                            </div>
                            <div class="modal-body">
                                <iframe src="http://libros.metabiblioteca.org/bitstream/001/591/1/004%20Desarrollo%20de%20aplicaciones%20web.pdf" style=" zoom: 0.85; width:100%; height:700px; " frameborder="0"> </iframe>
                            </div>
                            <div class="modal-footer">
                                <!--<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>-->
                            </div>
                        </div>

                    </div>
                </div>
                <!-- /.col-lg-12 -->
            </div>

            <br>
            <div class="panel panel-default" style="padding:15px;">
                <!--<div class="panel-heading" style="background-color:#eee;">
                    <b style="color:#d9534f;"></b>  <p class="fa fa-toggle-up" id="filtros-bt" style="cursor:pointer;"></p>
                </div>-->
                <br>
                <div class="row">
                    <div class="col-lg-3">

                        <div class="form-group">
                            <label>Modulos Metabolicos</label>
                            <select class="form-control" name="nivel1">
                                <option value="-1">Seleccionar...</option>
                                <option value="1">1.1 Carbohydrate metabolism </option>
                                <option value="17">1.2 Energy metabolism</option>
                                <option value="26">1.3 Lipid metabolism </option>
                                <option value="44">1.4 Nucleotide metabolism</option>
                                <option value="47">1.5 Amino acid metabolism</option>
                                <option value="62">1.6 Metabolism of other amino acids</option>
                                <option value="72">1.7 Glycan biosynthesis and metabolism</option>
                                <option value="184">1.8 Metabolism of cofactors and vitamins</option>
                                <option value="197">1.9 Metabolism of terpenoids and polyketides</option>
                                <option value="259">1.10 Biosynthesis of other secondary metabolites</option>
                                <option value="420">1.11 Xenobiotics biodegradation and metabolism</option>
                                <option value="443">1.12 Chemical structure transformation maps</option>

                            </select>
                        </div>

                        <div id="resultadoNivel2">
                        </div>

                        <div class="form-group">
                            <label>Mètodo</label>
                            <select class="form-control" name="metodo">
                                <option value="-1">Seleccionar...</option>
                                <option value="2">Trinotate</option>
                                <option value="3">Ghost Koala</option>
                                <option value="1">Ambos</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <center><b style="color:#d9534f;">METAGENOMAS/GENOMAS</b></center>
                                </div>
                                <!-- /.panel-heading -->
                                <div class="panel-body" id="tablaMeta" >

                                    <div class="dataTable_wrapper">
                                        <table width="100%" class="table table-striped table-bordered table-hover" id="metagenomas">
                                            <thead>
                                                <tr>
                                                    <th>Clave de acceso</th>
                                                    <th>Marcar</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <%
                                                    ArrayList<ArrayList> listas = null;
                                                    Object listasObj = request.getAttribute("listaGenomas");

                                                    if (listasObj != null) {
                                                        listas = (ArrayList<ArrayList>) listasObj;
                                                    }
                                                    if (listas != null) {
                                                        for (int li = 0; li < listas.size(); li++) {

                                                %> 
                                                <tr style="text-align: left;" class="meta">
                                                    <td>
                                                        <span style="margin-right:5px; cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, '<%= listas.get(li).get(2)%>', {position: 0});"></span>
                                                        <a href="<%= listas.get(li).get(0)%>" target="_blank"><%= listas.get(li).get(1)%></a>

                                                    </td>
                                                    <td style="text-align:center; "><input type="checkbox" name="metagenomas" id="" value="<%= listas.get(li).get(1)%>"></td>

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

                                                <button id="boton" class="btn btn-info btn-lg" <!--onmouseover="tooltip.pop(this, 'HOLA ESTE EL MENSAJE', {position: 0});"-->Generar Via <!--<span style="margin-right:5px; " class="glyphicon glyphicon-info-sign" class="tooltip" ></span>--> </button>

                    </div>


                    <div class="col-lg-9" id="resultados">
                        <div id="resultadoVia">
                        </div> 
                    </div>
                    <div class="col-lg-3">

                    </div>
                    <div class="col-lg-9" id="">


                    </div>


                    </body>
                    </html>
