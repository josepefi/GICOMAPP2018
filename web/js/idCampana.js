/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {
    //obtener el id de la campaña  
    $("select[name=campanas]").change(function() {
        //alert($('select[name=campanas]').val());

        var valorSelect = $('select[name=campanas]').val();

        var form = document.createElement("form");
        form.setAttribute("method", "post");
        form.setAttribute("action", "homeCamp");

        var dataIDS = document.createElement("input");
        dataIDS.setAttribute("type", "hidden");
        dataIDS.setAttribute("name", "idCampana");
        dataIDS.setAttribute("value", valorSelect);
        form.appendChild(dataIDS);
      
        var puntos =   $('input[name=puntosXestacion]:checked').val();
        var dataPuntos = document.createElement("input");
        dataPuntos.setAttribute("type", "hidden");
        dataPuntos.setAttribute("name", "estacion");
        dataPuntos.setAttribute("value", puntos);
        form.appendChild(dataPuntos);

        document.body.appendChild(form);
        form.submit();
        //   document.getElementById('loadingPanel').style.display = "none";
        return true;
    });
});

