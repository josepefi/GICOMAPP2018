/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function () {

    $("#obtenerdatos-genomas").click(function () {

        // para cada checkbox "chequeado"
        var resultG = [];
        var table = $("#genomas-blast").DataTable();
        var rows = table.rows().nodes();
        var i = 0;
        $('input[type="checkbox"]', rows).each(function (index) {
            if ($(this).is(':checked')) {
                resultG[i] = $(this).val();
                i++;
            }
        });

        if (i === 0) {

            swal({
                title: "Error. Se espera por lo menos un identificador",
                imageUrl: "images/error_busqueda.png",
                html: true
            });


        } else {

            var ids = resultG.join(',');

            var dType = $("#opciones").val();

            var seqType = $("#tipoSecuencia").val();

            if ($('#proteina').prop('checked'))
            {
                var seqHeader = $('input:checkbox[name=proteinas]:checked').val();
            } else
            {
                var seqHeader = "regular";
            }
            try {
                var form = document.createElement("form");
                form.setAttribute("method", "post");
                form.setAttribute("action", "getSequence");

                var dataIDS = document.createElement("input");
                dataIDS.setAttribute("type", "hidden");
                dataIDS.setAttribute("name", "ids");
                dataIDS.setAttribute("value", ids);
                form.appendChild(dataIDS);

                var dataSeqType = document.createElement("input");
                dataSeqType.setAttribute("type", "hidden");
                dataSeqType.setAttribute("name", "seqType");
                dataSeqType.setAttribute("value", seqType);
                form.appendChild(dataSeqType);

                var dataDType = document.createElement("input");
                dataDType.setAttribute("type", "hidden");
                dataDType.setAttribute("name", "dType");
                dataDType.setAttribute("value", dType);
                form.appendChild(dataDType);

                var dataDType = document.createElement("input");
                dataDType.setAttribute("type", "hidden");
                dataDType.setAttribute("name", "seqHeader");
                dataDType.setAttribute("value", seqHeader);
                form.appendChild(dataDType);

                document.body.appendChild(form);
                form.submit();
                //   document.getElementById('loadingPanel').style.display = "none";
                //return true;
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
    });
});
