<%-- 
    Document   : gen
    Created on : 5/05/2017, 10:41:51 AM
    Author     : Jose Pefi
--%>
<%@page import="bobjects.GOObj"%>
<%@page import="bobjects.PFAMObj"%>
<%@page import="bobjects.Eggnog"%>
<%@page import="bobjects.GenObj"%>
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
    Object GenObj = request.getAttribute("gen");
    GenObj gen = GenObj != null ? (GenObj) GenObj : null;
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
      
        <title>Gen</title>
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
                <%
                   if (gen != null) {
                %>
                <div class="row">                     
                    <div class="col-lg-12">

                        <h2 style="color:#337ab7;">GEN</h2><h4 class="page-header" style="color:#d9534f; margin-top:0px;"><%= gen.getGenID() %></h4> 
                    </div>
                    <!-- /.col-lg-12 -->
                </div>
                <%
                    }
                %>
                <br>
                <!-- /.row -->
                <div class="row">

                    <div class="col-lg-12" id="contenido">
                        <!--<iframe src="krona.html" width=100%; height=500px; frameborder="0"> </iframe>-->

                        <div class="panel panel-default">
                            <!-- <div class="panel-heading">
                                 Basic Tabs
                             </div>-->
                            <!-- /.panel-heading -->
                            <div class="panel-body">
                                <!-- Nav tabs -->
                                <ul class="nav nav-tabs">
                                    <li class="active"><a href="#origen" data-toggle="tab">Origen</a>
                                    </li>
                                    <li><a href="#anotacion" data-toggle="tab">Anotación</a>
                                    </li>
                                </ul>

                                <!-- Tab panes -->
                                <div class="tab-content">
                                    <div class="tab-pane fade in active " id="origen">
                                        <br>
                                        <div class="col-md-12">
                                            <div class="panel panel-default" style="border:none;">
                                                <!-- /.panel-heading -->
                                                <div class="panel-body">
                                                    <div class="table table-striped table-bordered " width="100%">
                                                        <table class="table table-striped">
                                                            <%
                                                               if (gen != null) {
                                                            %>
                                                           <tbody>
                                                                <tr style="border-top:none;">
                                                                    <td style="padding:10px;"><b>Fuente:</b></td>
                                                                    <script>
                                                                        if("<%= gen.getGen_src() %>" == "GEN"){
                                                                        document.write("<td style=\"padding:10px; text-align:left; color:#777; font-size:87%;\">GENOMA:<em><a href=\"showGenoma?idGenoma=<%= gen.getIdGeMe() %>\" target=\"_blank\"><%= gen.getName()%></a></em></td> ");
                                                                        }else{
                                                                        document.write("<td style=\"padding:10px; text-align:left; color:#777; font-size:87%;\">METAGENOMA:<em><a href=\"showMetagenoma?idMetagenoma=<%= gen.getIdGeMe()%>\" target=\"_blank\"><%= gen.getName()%></a></em></td> ");    
                                                                        }
                                                                    </script>   
                                                                    <td style="padding:10px;"><b>Tipo de Muestra:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><em><%= gen.getTipoMuestra() %></em></td> 
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Muestra:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><em><a href = 'showMuestra?idMuestra=<%=gen.getIdmuestra() %>' target='_blank'><%= gen.getEtiquetaMuestra() %></a></em></td>         
                                                                    <td style="padding:10px;"><b>Profundidad:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><em><%= gen.getProfundidad() %></em></td> 
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
  
                                        <div class="col-md-12">
                                            <div class="panel panel-default" style="border:none;">
                                                <!-- /.panel-heading -->
                                                <div class="panel-body">
                                                    <div class="table table-striped table-bordered " width="100%">
                                                        <table class="table table-striped">
                                                            <thead style="display:none;">
                                                                <tr>
                                                                    <th></th>
                                                                    <th></th>
                                                                </tr>
                                                            </thead>
                                                            <%
                                                               if (gen != null) {
                                                            %>
                                                            <tbody>
                                                                <tr style="border-top:none;">
                                                                    <td style="padding:10px;"><b>Tipo de gen:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><em><%= gen.getGenType() %></em></td>         
                                                                    <td style="padding:10px;"><b>Contig ID:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><em><%= gen.getContig_id() %></em></td> 
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Strand:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><em><%= gen.getGen_strand() %></em></td>         
                                                                    <td style="padding:10px;"><b>Gen contig ID:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><em><%= gen.getContig_gen_id() %></em></td> 
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding:10px;"><b>Longitud:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><em><%= gen.getGen_lenght() %></em></td>         
                                                                    <td style="padding:10px;"><b>Coordenadas contig:</b></td>
                                                                    <td style="padding:10px; text-align:left; color:#777; font-size:87%;"><em><%= gen.getContig_from() %> - <%= gen.getContig_to() %></em></td> 
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
                                        <div class="col-lg-12">
                                            <div class="panel panel-default">
                                                <div class="panel-heading">
                                                    Secuencia
                                                </div>
                                                <!-- /.panel-heading -->
                                                <div class="panel-body">
                                                    <!-- Nav tabs -->
                                                    <ul class="nav nav-tabs">
                                                        <li class="active"><a href="#5p" data-toggle="tab">5P</a>
                                                        </li>
                                                        <li><a href="#nc" data-toggle="tab">NC</a>
                                                        </li>
                                                        <li><a href="#aa" data-toggle="tab">AA</a>
                                                        </li>
                                                        <li><a href="#3p" data-toggle="tab">3P</a>
                                                        </li>
                                                    </ul>

                                                    <!-- Tab panes -->
                                                    <div class="tab-content">
                                                        <div class="tab-pane fade in active" id="5p">
                                                            <br>
                                                                <script>
                                                                    if("<%= gen.getSec5P().getSecuencia()%>" == "null"){
                                                                        document.write("<p>Sin secuencia.</p>");
                                                                    }else{
                                                                        document.write("<p style=\"word-wrap:break-word;\"><%= gen.getSec5P().getSecuencia() %></p>");
                                                                    }
                                                                </script>                                                           
                                                        </div>
                                                        <div class="tab-pane fade" id="nc">
                                                         <br>   
                                                         <script>
                                                                    if("<%= gen.getSecNC().getSecuencia() %>" == "null"){
                                                                        document.write("<p>Sin secuencia.</p>");
                                                                    }else{
                                                                        document.write("<p style=\"word-wrap:break-word;\"><%= gen.getSecNC().getSecuencia() %></p>");
                                                                    }
                                                         </script>
                                                        </div>
                                                        <div class="tab-pane fade" id="aa">
                                                          <br>
                                                         <script>
                                                                    if("<%= gen.getSecAA().getSecuencia() %>" == "null"){
                                                                        document.write("<p>Sin secuencia.</p>");
                                                                    }else{
                                                                        document.write("<p style=\"word-wrap:break-word;\"><%= gen.getSecAA().getSecuencia() %></p>");
                                                                    }
                                                         </script>                                                        
                                                        </div>
                                                        <div class="tab-pane fade" id="3p">
                                                          <br> 
                                                         <script>
                                                                    if("<%= gen.getSec3P().getSecuencia() %>" == "null"){
                                                                        document.write("<p>Sin secuencia.</p>");
                                                                    }else{
                                                                        document.write("<p style=\"word-wrap:break-word;\"><%= gen.getSec3P().getSecuencia() %></p>");
                                                                    }
                                                         </script>                                                            
                                                        </div>
                                                    </div>
                                                </div>
                                                <!-- /.panel-body -->
                                            </div>
                                            <!-- /.panel -->
                                        </div>                                   
                                    </div>

                                    <div class="tab-pane faden " id="anotacion">
                                        <br>                     

                        <div class="col-md-6"> 
                            <div class="panel panel-default" style="border: 0px;">
                                <div class="panel-heading" style="border: 0px;">
                                    <b style="color:#d9534f;">BLAST P</b>
                                </div>
                                <!-- /.panel-heading -->
                                <div class="panel-body" >
                                    <div class="dataTable_wrapper">
                                        <table width="100%" class="table table-striped" id="campanas">
                                            <thead style="display:none;">
                                                <tr>
                                                    <th></th>
                                                    <th></th>
                                                </tr>
                                            </thead>
                                            
                                            <tbody>
                                            
                                                <tr>
                                                    <td style="padding:10px; border: 0px;"><b>Uniprot ID:</b></td>
                                                    <td style="padding:10px; border: 0px;"><%= gen.getBlastP().getUniprotID() %> </td>
                                                </tr>
                                                <tr>
                                                    <td style="padding:10px; border: 0px;"><b>Uniprot ACC:</b></td>
                                                    <td style="padding:10px; border: 0px;"><%= gen.getBlastP().getUniprotACC() %></td>
                                                </tr> 
                                                <tr>
                                                    <td style="padding:10px; border: 0px;"><b>Gen name:</b></td>
                                                    <td style="padding:10px; border: 0px;"><%= gen.getBlastP().getGenName() %> </td>
                                                </tr>
                                                <tr>
                                                    <td style="padding:10px; border: 0px;"><b>Prot name:</b></td>
                                                    <td style="padding:10px; border: 0px;"><%= gen.getBlastP().getProtName() %> </td>
                                                </tr>   
                                                <tr>
                                                    <td style="padding:10px; border: 0px;"><b>Organismo:</b></td>
                                                    <td style="padding:10px; border: 0px;"><%= gen.getBlastP().getTaxID() %> - <%= gen.getBlastP().getTaxon() %> </td>
                                                </tr>                     
                                                <tr>
                                                    <td style="padding:10px; border: 0px;"><b>Eval:</b></td> 
                                                    <td style="padding:10px; border: 0px;"><%= gen.getBlastP().getEval() %></td>
                                                </tr>
                                                <tr>
                                                    <td style="padding:10px; border: 0px;"><b>Identidad:</b></td>
                                                    <td style="padding:10px; border: 0px;"><%= gen.getBlastP().getIdentidad() %></td>
                                                </tr>  
                                                <tr>
                                                    <td style="padding:10px; border: 0px;"><b>Query:</b></td>
                                                    <td style="padding:10px; border: 0px;"><%= gen.getBlastP().getQuery() %></td>
                                                </tr>        
                                                
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div> 
                        <div class="col-md-6"> 
                            <div class="panel panel-default" style="border: 0px;">
                                <div class="panel-heading" style="border: 0px;">
                                    <b style="color:#d9534f;">BLAST X</b>
                                </div>
                                <!-- /.panel-heading -->
                                <div class="panel-body" >
                                    <div class="dataTable_wrapper">
                                        <table width="100%" class="table table-striped" id="">
                                            <thead style="display:none;">
                                                <tr>
                                                    <th></th>
                                                    <th></th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr>
                                                    <td style="padding:10px; border: 0px;"><b>Uniprot ID:</b></td>
                                                    <td style="padding:10px; border: 0px;"><%= gen.getBlastX().getUniprotID() %> </td>
                                                </tr>
                                                <tr>
                                                    <td style="padding:10px; border: 0px;"><b>Uniprot ACC:</b></td>
                                                    <td style="padding:10px; border: 0px;"><%= gen.getBlastX().getUniprotACC() %></td>
                                                </tr> 
                                                <tr>
                                                    <td style="padding:10px; border: 0px;"><b>Gen name:</b></td>
                                                    <td style="padding:10px; border: 0px;"><%= gen.getBlastX().getGenName() %> </td>
                                                </tr>
                                                <tr>
                                                    <td style="padding:10px; border: 0px;"><b>Prot name:</b></td>
                                                    <td style="padding:10px; border: 0px;"><%= gen.getBlastX().getProtName() %> </td>
                                                </tr>   
                                                <tr>
                                                    <td style="padding:10px; border: 0px;"><b>Organismo:</b></td>
                                                    <td style="padding:10px; border: 0px;"><%= gen.getBlastX().getTaxID() %> - <%= gen.getBlastX().getTaxon() %> </td>
                                                </tr>                     
                                                <tr>
                                                    <td style="padding:10px; border: 0px;"><b>Eval:</b></td> 
                                                    <td style="padding:10px; border: 0px;"><%= gen.getBlastX().getEval() %></td>
                                                </tr>
                                                <tr>
                                                    <td style="padding:10px; border: 0px;"><b>Identidad:</b></td>
                                                    <td style="padding:10px; border: 0px;"><%= gen.getBlastX().getIdentidad() %></td>
                                                </tr>  
                                                <tr>
                                                    <td style="padding:10px; border: 0px;"><b>Query:</b></td>
                                                    <td style="padding:10px; border: 0px;"><%= gen.getBlastX().getQuery() %></td>
                                                </tr>  
                                   
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div> 
                        <div class="col-md-12"> 
                            <div class="panel-heading" style="border: 0px; background-color:#f5f5f5;">
                                    <b style="color:#d9534f;">COG/Egg NOG</b>     <p class="fa fa-toggle-down" id="cog-icon" style="cursor:pointer;"></p>
                                </div>
                                                <div class="panel-body" id="cog">
                                                    
                                                    <div class="dataTable_wrapper">
                                                        <table width="100%" class="table table-striped table-bordered" id="">
                                                            <thead>
                                                                <tr style="text-align: center;">
                                                                    <th>ID</th>
                                                                    <th>Descripción</th>
                                                                    <th><span style="margin-right:5px; cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, '', {position: 0});"></span> Proteinas</th>
                                                                    <th><span style="margin-right:5px; cursor:pointer;" class="glyphicon glyphicon-info-sign" class="tooltip" onmouseover="tooltip.pop(this, '', {position: 0});"></span> Especies</th>
                                                                    <th>Eval</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody id="">
                                                                <%
                                                                     if (gen != null) {

                                                                        for (Eggnog egobj : gen.getEggno()) {
                                                                %>   

                                                                <tr style="text-align: center;"  id="">
                                                                    <td><%= egobj.getID() %></td>
                                                                    <td><%= egobj.getDescripcion() %></td>
                                                                    <td><%= egobj.getProteinas() %></td>
                                                                    <td><%= egobj.getEspecies() %></td>
                                                                    <td><%= egobj.getEvalue() %></td>
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
                                                            
                        <div class="col-md-12"> 
                                <div class="panel-heading" style="border: 0px; background-color:#f5f5f5;">
                                    <b style="color:#d9534f;">PFAM</b>    <p class="fa fa-toggle-down" id="pfam-icon" style="cursor:pointer;"></p>
                                </div>                                 
                                                <div class="panel-body" id="pfam">
                                                    
                                                    <div class="dataTable_wrapper">
                                                        <table width="100%" class="table table-striped table-bordered " id="">
                                                            <thead>
                                                                <tr>
                                                                    <th>ACC</th>
                                                                    <th>CLAN_ACC</th>
                                                                    <th>ID PFAM</th>
                                                                    <th>FROM</th>
                                                                    <th>TO</th>
                                                                    <th>Eval</th>
                                                                    <th>Descripción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody id="tgenomas">
                                                                <%
                                                                    if (gen != null) {

                                                                        for (PFAMObj pfam : gen.getPfam()) {
                                                                %>   

                                                                <tr style="text-align: center;" >
                                                                    <td><%= pfam.getAcc() %></td>
                                                                    <td><%= pfam.getClan() %></td>
                                                                    <td><%= pfam.getId_pfam() %></td>
                                                                    <td><%= pfam.getFrom() %></td>
                                                                    <td><%= pfam.getTo() %></td>
                                                                    <td><%= pfam.getEvalue() %></td>
                                                                    <td><%= pfam.getPfam_deff() %></td>
                                                                    
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
                        <div class="col-md-12"> 
                                <div class="panel-heading" style="border: 0px; background-color:#f5f5f5;">
                                    <b style="color:#d9534f;">Gene Ontology</b>    <p class="fa fa-toggle-down" id="ontology-icon" style="cursor:pointer;"></p>
                                </div>                                 
                                                <div class="panel-body" id="ontology">
                                                    
                                                    <div class="dataTable_wrapper">
                                                        <table width="100%" class="table table-striped table-bordered " id="">
                                                            <thead>
                                                                <tr>
                                                                    <th>GO</th>
                                                                    <th>Name Space</th>
                                                                    <th>Name</th>
                                                                    <th>Definición</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody id="tgenomas">
                                                                <%
                                                                    if (gen != null) {

                                                                        for (GOObj go : gen.getGo()) {
                                                                %>   

                                                                <tr style="text-align: justify;" >
                                                                    <td><a href="<%= go.getUrl() %>" target="_blank"><%= go.getId_GO() %></a> </td>
                                                                    <td><%= go.getNamespace() %></td>
                                                                    <td><%= go.getGo_name() %></td>
                                                                    <td><%= go.getDefinition() %></td>
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
            
            <script>
                $(document).ready(function () {
                    var clic = 1;
                    $("#cog-icon").on("click", function () {
                        if (clic == 1) {
                            $('#cog').hide(); //oculto
                            $('#cog-icon').removeClass('fa fa-toggle-up');//elimina clse del icono up
                            $('#cog-icon').addClass('fa fa-toggle-down');//agrega la clase del icono down
                            clic = clic + 1;
                        } else {
                            $('#cog').show(); //muestro
                            $('#cog-icon').removeClass('fa-chevron-down');//elimina clse del icono down
                            $('#cog-icon').addClass('fa-chevron-up');//agrega la clase del icono up
                            clic = 1;
                        }
                    });
                });
            </script> 
            <script>
                $(document).ready(function () {
                    var clic = 1;
                    $("#pfam-icon").on("click", function () {
                        if (clic == 1) {
                            $('#pfam').hide(); //oculto
                            $('#pfam-icon').removeClass('fa fa-toggle-up');//elimina clse del icono up
                            $('#pfam-icon').addClass('fa fa-toggle-down');//agrega la clase del icono down
                            clic = clic + 1;
                        } else {
                            $('#pfam').show(); //muestro
                            $('#pfam-icon').removeClass('fa-chevron-down');//elimina clse del icono down
                            $('#pfam-icon').addClass('fa-chevron-up');//agrega la clase del icono up
                            clic = 1;
                        }
                    });
                });
            </script>
            <script>
                $(document).ready(function () {
                    var clic = 1;
                    $("#ontology-icon").on("click", function () {
                        if (clic == 1) {
                            $('#ontology').hide(); //oculto
                            $('#ontology-icon').removeClass('fa fa-toggle-up');//elimina clse del icono up
                            $('#ontology-icon').addClass('fa fa-toggle-down');//agrega la clase del icono down
                            clic = clic + 1;
                        } else {
                            $('#ontology').show(); //muestro
                            $('#ontology-icon').removeClass('fa-chevron-down');//elimina clse del icono down
                            $('#ontology-icon').addClass('fa-chevron-up');//agrega la clase del icono up
                            clic = 1;
                        }
                    });
                });
            </script>


</body>
</html>

