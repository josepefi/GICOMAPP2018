/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var oReq;
var isFile = false;
/**
 *Este metodo manda los archivos para guardar via XHR- AJAX
 *
 */
function uploadXHTML(genomas, metagenomas) {
    try {
        //analysis type
        var jobName = document.getElementById("nombre").value;
        //data base
        var secuencia = document.getElementById("secuencia").value;
        var eval = document.getElementById("eval").value;
        //exSecName
        // var fileInput = document.getElementById("uploadFasta");
        var fileInputTxt = document.getElementById("uploadedFasta");
        // var userInput = document.getElementById("userInput").value;

        var radioOption = document.getElementsByName("algoritmo");
        var blastOption = "";
        for (var i = 0; i < radioOption.length; i++) {
            if (radioOption[i].checked) {
                blastOption = radioOption[i].value;
                break;
            }
        }
        if (blastOption === null || blastOption.length < 1) {
            //alert("Seleccionar algoritmo de búsqueda");
            swal({
                title: "<span style='color:#red'>Seleccionar algoritmo de búsqueda!</span>",
                imageUrl: "images/error_busqueda.png",
                html: true
            });
            document.getElementById("nombre").focus();
            return false;
        }
        if (jobName === null || jobName.length < 1) {
            //alert("Ingresa un nombre para la búqueda");
            swal({
                title: "<span style='color:#red'>Ingresa un nombre para la búqueda!</span>",
                imageUrl: "images/error_busqueda.png",
                html: true
            });            
            document.getElementById("nombre").focus();
            return false;
        }
        var e;
        if (eval.length < 1 || isNaN(eval)) {
            //alert("Ingrese un número entero entre  0 y 100" + "\n" + "3 default -> 0.001");
            swal({
                title: "<span style='color:#red'>Ingrese un número entero entre  0 y 100" + "\n" + "3 default -> 0.001!</span>",
                imageUrl: "images/error_busqueda.png",
                html: true
            });             
            document.getElementById("eval").focus();
            return false;
        } else {
            try {
                e = parseInt(eval);
                if (e < 0 || e > 100) {
                    //alert("Ingrese un número entero entre  0 y 100" + "\n" + "3 default -> 0.001");
            swal({
                title: "<span style='color:#red'>Ingrese un número entero entre  0 y 100" + "\n" + "3 default -> 0.001!</span>",
                imageUrl: "images/error_busqueda.png",
                html: true
            });                    
                    document.getElementById("evalue").focus();
                    return false;
                }
            } catch (err) {
                //alert("Ingrese un número entero entre  0 y 100" + "\n" + "3 default -> 0.001");
            swal({
                title: "<span style='color:#red'>Ingrese un número entero entre  0 y 100" + "\n" + "3 default -> 0.001!</span>",
                imageUrl: "images/error_busqueda.png",
                html: true
            });                 
                document.getElementById("evalue").focus();
                return false;
            }
        }

        var oMyForm = new FormData();
        oMyForm.append("algoritmo", blastOption);
        oMyForm.append("evalue", eval);
        oMyForm.append("name", jobName);


        if (fileInputTxt.value !== null && fileInputTxt.value.length > 3 && secuencia.length < 10) {
            var file;
            if (fileInputTxt.files) {
                file = fileInputTxt.files[0];
            } else {
                file = fileInputTxt.value;
            }
            //se agregar la parte/variable req a la forma
            oMyForm.append("seqFile", file);
            oMyForm.append("upType", 'XMLHttp');
            oMyForm.append("inputType", 'file');
            isFile = true;
        } else if (secuencia !== null && secuencia.length > 10) {
            oMyForm.append("seq", secuencia);
            oMyForm.append("inputType", 'sequence');
            isFile = false;
        } else {
            //alert("Ingrese una secuencia o seleccione un archivo");
             swal({
                title: "<span style='color:#red'>Ingrese una secuencia o seleccione un archivo!</span>",
                imageUrl: "images/error_busqueda.png",
                html: true
            });            
            document.getElementById('secuencia').focus();
            return false;
        }


        //RADIO BUTTONS MetaDBs
        /*  var radioMetaDB = document.getElementsByName("checkMG");
         var metaDBs = "";
         for (var i = 0; i < radioMetaDB.length; i++) {
         if (radioMetaDB[i].checked) {
         if (metaDBs.length > 0) {
         metaDBs += "," + radioMetaDB[i].value;
         } else {
         metaDBs = radioMetaDB[i].value;
         }
         }
         }*/
        oMyForm.append("idmetagenomas", metagenomas);
        //RADIO BUTTONS GenoDBs
        /* var radioGenoDB = document.getElementsByName("checkG");
         var genoDBs = "";
         for (var i = 0; i < radioGenoDB.length; i++) {
         if (radioGenoDB[i].checked) {
         if (genoDBs.length > 0) {
         genoDBs += "," + radioGenoDB[i].value;
         } else {
         genoDBs = radioGenoDB[i].value;
         }
         }
         }*/
        oMyForm.append("idgenomas", genomas);
        if ((metagenomas.length + genomas.length) === 0) {
            //alert("Seleccione por lo menos una base de datos");
            swal({
                title: "<span style='color:#red'>Seleccione por lo menos una base de datos!</span>",
                imageUrl: "images/error_busqueda.png",
                html: true
            });  
            return false;
        }
        oReq = getXmlHttpObject();
        oReq.upload.addEventListener("loadstart", loadStartFunction, false);
        oReq.upload.addEventListener("progress", progressFunction, false);
        oReq.upload.addEventListener("load", transferCompleteFunction, true);
        oReq.onreadystatechange = handleStateChangeFiles;
        oReq.open("POST", "blastSearch", true);
        oReq.send(oMyForm);
    } catch (err) {
        //alert("Error al crear la petición Blast\n" + err.message);
               swal({
                title: "Error al crear la petición Blast\n!"+ err.message,
                imageUrl: "images/error_busqueda.png",
                html: true
            }); 
        return false;
    }
}
function getXmlHttpObject() {
    var xmlHttp;
    try {
        // Firefox, Opera 8.0+, Safari, Chrome
        xmlHttp = new XMLHttpRequest();
    } catch (e) {
        // Internet Explorer
        try {
            xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
    }
    if (!xmlHttp) {
        //alert("Your browser does not support AJAX!");
               swal({
                title: "Your browser does not support AJAX!!",
                imageUrl: "images/error_busqueda.png",
                html: true
            });        

    }
    return xmlHttp;
}
function handleStateChangeFiles()
{
    if (oReq.readyState == 4)
    {
        if (oReq.status == 200) {

            var response = new String(oReq.responseText);

            if (response.toString().toLowerCase().indexOf("error") == -1) {
                window.location.href = "showJob?jobURL=" + response;

                // window.location.href = "/WEB-INF/selections/functiontable.jsp";
            } else {
                //alert(response);
                swal({
                title: response,
                imageUrl: "images/error_busqueda.png",
                html: true
            });    
            }

        } else
        {
            //alert("Error creando blast job " + oReq.status + ":" + oReq.statusText);
            swal({
                title: "Error creando blast job" + oReq.status + ":" + oReq.statusText,
                imageUrl: "images/error_busqueda.png",
                html: true
            }); 
        }
    }
}
function progressFunction(evt) {
    var progressBar = document.getElementById("progressBar");
    var percentageDiv = document.getElementById("percentageCalc");
    if (isFile) {
        progressBar.style.display = '';
        percentageDiv.style.display = '';
        if (evt.lengthComputable) {
            progressBar.max = evt.total;
            progressBar.value = evt.loaded;
            percentageDiv.innerHTML = "Subiendo archivo: " + Math.round(evt.loaded / evt.total * 100) + "%";
        }
    }
}
function loadStartFunction(evt) {
// alert('Load start event');
}
function transferCompleteFunction(evt) {
    var response = new String(oReq.responseText);
    var percentageDiv = document.getElementById("percentageCalc");
    if (response.toLowerCase().indexOf("error") === -1) {
        // document.getElementById("msgExpediente").innerHTML = "Expediente #:&nbsp;" + response;
        if (isFile) {
            percentageDiv.innerHTML = "Archivo listo";
        }
        // alert("Expediente creado correctamente ID: " + response.toString());
    } else {
        //alert(response);
                swal({
                title: response,
                imageUrl: "images/error_busqueda.png",
                html: true
            });        
    }

}
