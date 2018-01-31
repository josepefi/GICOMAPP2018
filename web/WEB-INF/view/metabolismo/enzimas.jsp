<%-- 
    Document   : enzimas
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

                $("#generaImagen").click(function () {

                    var nColumnas = $("#tabla-resultados tr:last td").length;
                    var noMeta = nColumnas - 2;
                    var listaTaxones = [];
                    var i = 0;
                    $("#tabla-resultados tbody tr").find('td:eq(0)').each(function () {

                        listaTaxones[i] = $(this).text();
                        i++;

                    });

                    var valores = listaTaxones.join(',');

                    alert("El numero de metagenomas es:" + noMeta);
                    alert("Los valores de la tabla son:" + valores);

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
                                <li>
                                    <a href="enzimas">Enzimas degradadoras</a>
                                </li>
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
                    <h2 class="page-header" style="color:#337ab7;">Enzimas Degradadoras</h2>
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

                        <button id="boton" class="btn btn-info btn-lg">Generar</button>

                    </div>


                    <div class="col-lg-9" id="resultadoVia" >

                    </div>

                    </body
                    </html>
