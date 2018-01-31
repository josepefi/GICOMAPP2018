<%-- 
    Document   : enconstruccion
    Created on : 31/03/2017, 12:09:19 PM
    Author     : Jose Pefi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EN CONSTRUCCION</title>
    </head>
    <body>
        <div id="page-wrapper">
            <div class="row">
                <br>
                <div class="col-lg-12">
                    <div class="col-lg-4">
                    </div>
                    <div class="col-lg-4">
                        <center>
                            <img src="images/cons.png" width="35%">
                            <br>
                            <h3 style="color:#EC1010; font-size:90%;">
                                <%
                                    Object mensaje = request.getAttribute("msg");
                                    if (mensaje != null) {
                                %>
                                <b><%=mensaje.toString()%></b>
                                <% }
                                %>
                                <!--<b>Grafica de taxonomía no disponible</b>-->
                            </h3> 
                        </center>
                    </div>
                    <div class="col-lg-4">
                    </div>                        
                </div>
                <!-- /.col-lg-12 -->
            </div>

        </div>
    </body>
</html>
