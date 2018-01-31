/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function descargaSeqsMarcador(rank, valueRank) {
    var idsMarcadores = [];
    var table = $("#taxonomia").DataTable();
    var rows = table.rows().nodes();
    var i = 0;
    $('input[type="checkbox"]', rows).each(function(index) {
        if ($(this).is(':checked')) {
            idsMarcadores[i] = $(this).val();
            i++;
        }
    });
    if (i === 0) {

        swal({
            title: "Error. Se espera por lo menos un registro seleccionada",
            imageUrl: "images/error_busqueda.png",
            html: true
        });


    } else {
        var ids = idsMarcadores.join('-');
        try {
            var form = document.createElement("form");
            form.setAttribute("method", "post");
            form.setAttribute("action", "getSequenceTaxo");

            var dataIDS = document.createElement("input");
            dataIDS.setAttribute("type", "hidden");
            dataIDS.setAttribute("name", "ids");
            dataIDS.setAttribute("value", ids);
            form.appendChild(dataIDS);

            var rankData = document.createElement("input");
            rankData.setAttribute("type", "hidden");
            rankData.setAttribute("name", "rank");
            rankData.setAttribute("value", rank);
            form.appendChild(rankData);

            var valueRankData = document.createElement("input");
            valueRankData.setAttribute("type", "hidden");
            valueRankData.setAttribute("name", "valueRank");
            valueRankData.setAttribute("value", valueRank);
            form.appendChild(valueRankData);

            document.body.appendChild(form);
            form.submit();

        } catch (err) {
            var txt = "Error al solicitar secuencias";
            txt += "Error description: " + err.message + "\n\n";

            //alert(txt);
            swal({
                title: txt,
                imageUrl: "images/error_busqueda.png",
                html: true
            });

            return false;
        }
    }
}