<%-- 
    Document   : matriz
    Created on : 26/03/2017, 07:49:03 PM
    Author     : Alejandro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <style>
            .dataTables_wrapper .dataTables_length {
                float: right;
            }
            .dataTables_wrapper .dataTables_filter {
                float: left;
                text-align: left;
                margin-left:-100%;
            }
        </style>       
        <script>
            $(document).ready(function () {
                $('#tabla-taxo').DataTable({
                    responsive: false,
                    paging: false
                });
            });
        </script> 
        <title>Matriz de Abundancia</title>
    </head>
    <%
        Object tabla = request.getAttribute("tabla");
        String tablaHTML = tabla != null ? (String) tabla : null;
    %>
    <body>

        <div class="row">
            <form style="padding-left: 25px; padding-top: 25px;">  
                <input class="fa fa-arrow-left" type="button" value="Regresar"  onclick="history.back()" />        
            </form>  
            <h3 class="page-header" style="padding-left: 25px; color:#d9534f;">Matriz de Abundancia</h3>
        </div>
        <div class="row">

            <!-- /.panel-heading -->
            <div class="panel-body" style="padding-left: 25px;">                  
                <br>
                <div class="dataTable_wrapper">
                    <%
                        if (tablaHTML != null) {
                    %>
                    <table width="100%" class="table table-striped table-bordered table-hover" id="tabla-taxo">
                        <%=tablaHTML%>
                        <!-- <thead>
                            <tr>
                                <th></th>
                                <th>Nombre</th>
                                <th>Muestra</th>
                                <th>Estación</th>
                            </tr>
                        </thead>
                        <tbody id="metas">

                            <tr style="text-align: left;" class="meta">
                               
                                <td>MET-01-E03-SED_0-10</td>
                                <td>Sedimento</td>
                                <td>E03</td>
                            </tr>

                            <tr style="text-align: left;" class="meta">
                               
                                <td>MMF-01-A04-MIL</td>
                                <td>Agua</td>
                                <td>A04</td>
                            </tr>

                            <tr style="text-align: left;" class="meta">
                              
                                <td>MMF-01-A04-SED</td>
                                <td>Sedimento</td>
                                <td>A04</td>
                            </tr>

                            <tr style="text-align: left;" class="meta">                                  
                                <td>MMF-01-D18-MAX</td>
                                <td>Agua</td>
                                <td>D18</td>
                            </tr>

                            <tr style="text-align: left;" class="meta">
                                <td><input type="checkbox" value="6" name="checkMG"></td>
                                <td>MMF-01-D18-SED</td>
                                <td>Sedimento</td>
                                <td>A04</td>
                            </tr>


                        </tbody>-->
                    </table>
                    <% } %>
                </div>
            </div>
        <script>
            $(document).ready(function () {

                var table = $('#tabla-taxo').DataTable();
              
                $('#estacion').on('keyup', function () {
                    table
                            .columns(0)
                            .search(this.value)
                            .draw();
                });
            });
        </script>

    </body>
</html>
