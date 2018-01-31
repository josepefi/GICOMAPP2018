/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function buscaMatriz(idSrc, src) {


    // para cada checkbox "chequeado"
    var nivelTaxo = $('select[name=nivel]').val();
    var header = "false";
    if ($("#cabecera").is(':checked')) {
        header = "true";
    }
    var toFile = "false";
    if ($("#toFile").is(':checked')) {
        toFile = "true";
    }
    var orgsName = $('input[name=orgName]:checked').val();
    var conteos = $('input[name=conteos]:checked').val();

    var degradadores = "false";
    if ($("#degradadores").is(':checked')) {
        degradadores = "true";
    }
    if (nivelTaxo === null) {
        swal({
            title: "<span style='color:#red'>Seleccionar Nivel Taxonómico!</span>",
            imageUrl: "images/error_busqueda.png",
            html: true
        })
    } else {
        try {
            var form = document.createElement("form");
            form.setAttribute("method", "post");
            form.setAttribute("action", "creaMatriz");

            var dataDegradadores = document.createElement("input");
            dataDegradadores.setAttribute("type", "hidden");
            dataDegradadores.setAttribute("name", "degradadores");
            dataDegradadores.setAttribute("value", degradadores);
            form.appendChild(dataDegradadores);

            var source = document.createElement("input");
            source.setAttribute("type", "hidden");
            source.setAttribute("name", "source");
            source.setAttribute("value", src);
            form.appendChild(source);

            var nivel = document.createElement("input");
            nivel.setAttribute("type", "hidden");
            nivel.setAttribute("name", "nivel");
            nivel.setAttribute("value", nivelTaxo);
            form.appendChild(nivel);

            var toFileData = document.createElement("input");
            toFileData.setAttribute("type", "hidden");
            toFileData.setAttribute("name", "toFile");
            toFileData.setAttribute("value", toFile);
            form.appendChild(toFileData);

            var marcador = document.createElement("input");
            marcador.setAttribute("type", "hidden");
            marcador.setAttribute("name", "idSrc");
            marcador.setAttribute("value", idSrc);
            form.appendChild(marcador);

            var withHeader = document.createElement("input");
            withHeader.setAttribute("type", "hidden");
            withHeader.setAttribute("name", "cabecera");
            withHeader.setAttribute("value", header);
            form.appendChild(withHeader);

            var orgName = document.createElement("input");
            orgName.setAttribute("type", "hidden");
            orgName.setAttribute("name", "orgName");
            orgName.setAttribute("value", orgsName);
            form.appendChild(orgName);

            var counts = document.createElement("input");
            counts.setAttribute("type", "hidden");
            counts.setAttribute("name", "norm");
            counts.setAttribute("value", conteos);
            form.appendChild(counts);

            document.body.appendChild(form);
            form.submit();
            //   document.getElementById('loadingPanel').style.display = "none";
            //return true;
        } catch (err) {
            var txt = "Error al crear matriz";
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
function buscaMatrizTotal() {
    // para cada checkbox "chequeado"
    var nivelTaxo = $('select[name=nivel2]').val();
    var trim = "false";
    if ($("#trimName").is(':checked')) {
        trim = "true";
    }

    try {
        var form = document.createElement("form");
        form.setAttribute("method", "post");
        form.setAttribute("action", "matrizFactory");

        var nivel = document.createElement("input");
        nivel.setAttribute("type", "hidden");
        nivel.setAttribute("name", "nivel");
        nivel.setAttribute("value", nivelTaxo);
        form.appendChild(nivel);


        var withHeader = document.createElement("input");
        withHeader.setAttribute("type", "hidden");
        withHeader.setAttribute("name", "trim");
        withHeader.setAttribute("value", trim);
        form.appendChild(withHeader);

        document.body.appendChild(form);
        form.submit();
        //   document.getElementById('loadingPanel').style.display = "none";
        //return true;
    } catch (err) {
        var txt = "Error al crear matriz";
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
