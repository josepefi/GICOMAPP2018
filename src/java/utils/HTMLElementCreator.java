/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Alejandro
 */
public class HTMLElementCreator {

    /**
     * Crea la lista de opciones para una lista de seleccion html
     *
     * @param data el resultado de un query en transacciones el primer elmento
     * (0) tiene que ser el valor value de la opcion y el segundo (1) el valor a
     * desplegar
     * @param htmlClass si se requiere que el elemento option tenga una clase en
     * particular si no se desea este parametr4o tiene que ser ""
     * @param displayWithID si se require qiue el id se despliegue en ña ñista
     * ID - VALOR
     * @return <option>elemento 1</option>,....n
     */
    public String createSelectionList(ArrayList<ArrayList> data, String htmlClass, boolean displayWithID) {
        StringBuilder selectionList = new StringBuilder();
        //SELECT id_type, type_name from sample_type
        selectionList.append("<option value=\"-1\" class=\"").append(htmlClass).append("\"> </option>");

        for (ArrayList<String> listElement : data) {

            if (htmlClass.length() > 0) {
                selectionList.append("<option value=\"").append(listElement.get(0)).append("\" class=\"").append(htmlClass).append("\">");
            } else {
                selectionList.append("<option value=\"").append(listElement.get(0)).append("\">");
            }
            if (displayWithID) {
                selectionList.append(listElement.get(0)).append(" - ").append(listElement.get(1));
            } else {
                selectionList.append(listElement.get(1));
            }
            selectionList.append("</option>\n");
        }
        return selectionList.toString();
    }

    /**
     * Crea la lista de opciones para una lista de seleccion html
     *
     * @param data el resultado de un query en transacciones el primer elmento
     * (0) tiene que ser el valor value de la opcion y el segundo (1) el valor a
     * desplegar
     * @param htmlClass si se requiere que el elemento option tenga una clase en
     * particular si no se desea este parametr4o tiene que ser ""
     * @param displayWithID si se require qiue el id se despliegue en ña ñista
     * ID - VALOR
     * @param idToSelect si se quiere seleccionar un elemento de la lista en
     * particular, se requiere el id esperado a seleccionar
     * @param isFirstEmpty si el primer elemento es -1 vacio
     * @param emptyString si es vacio y quiere algo diferente a vacion como un *
     * para all
     * @return <option>elemento 1</option>,....n
     */
    public String createSelectionListSelectElement(ArrayList<ArrayList<String>> data, String htmlClass, boolean displayWithID, String idToSelect, boolean isFirstEmpty, String emptyString) {
        StringBuilder selectionList = new StringBuilder();
        //SELECT id_type, type_name from sample_type

        if (isFirstEmpty) {
            selectionList.append("<option value=\"-1\" class=\"").append(htmlClass).append("\">").append(emptyString).append("</option>");;
        }
        for (ArrayList<String> listElement : data) {

            if (htmlClass.length() > 0) {
                if (listElement.get(0).equals(idToSelect)) {
                    selectionList.append("<option value=\"").append(listElement.get(0)).append("\" class=\"").append(htmlClass).append("\" selected>");
                } else {
                    selectionList.append("<option value=\"").append(listElement.get(0)).append("\" class=\"").append(htmlClass).append("\">");
                }
            } else {
                selectionList.append("<option value=\"").append(listElement.get(0)).append("\">");
            }
            if (displayWithID) {
                selectionList.append(listElement.get(0)).append(" - ").append(listElement.get(1));
            } else {
                selectionList.append(listElement.get(1));
            }
            selectionList.append("</option>\n");
        }
        return selectionList.toString();
    }

    /**
     * Crea una fila de html con estructura tr td div /div /td /tr metodo usado
     * en anilisisBio.jsp
     *
     * @param chain la cadena a convertir
     * @param delim delimitador para partir la cadena
     * @param divClass la clase para las celdas
     * @param fDivClass la clase para la primera de celda en caso de que aplique
     * @return la fila en html.
     */
    public String createStringToTableRow(String chain, String delim, String divClass, String fDivClass) {
        StringTokenizer st = new StringTokenizer(chain, delim);
        StringBuilder row = new StringBuilder();
        row.append("<tr>");
        int tokenNum = 0;
        while (st.hasMoreTokens()) {
            if (tokenNum == 0) {
                row.append("<td><div class='").append(fDivClass).append("'>").append(st.nextToken().trim()).append("</div></td>");
                tokenNum++;
            } else {
                row.append("<td><div class='").append(divClass).append("'>").append(st.nextToken().trim()).append("</div></td>");
            }
        }
        row.append("</tr>");
        return row.toString();
    }

    /**
     * Crea las filas html de una tabla dado un set de datos (data)
     *
     * @param data los datos a poner en la tabla
     * @param trExtra cualqquier modificador extra dentro del html de caa <trow
     * trExtra>
     * @param cellCSSClass la clase para cada celda
     * @param isTheFirstThevalue true si el primer elemento dentro de data es el
     * valor (como un ID) que tiene que venir dentro de un td "<td><div
     * values="">"
     * @param withSelectionbox si la primera fila va a tener una casilla de
     * seleccion, como ser un checkbox o radiobutton, esto será definido por el
     * parametro selectionType. En esos casos se esperaria que el primer
     * elemento del set de datos sea un id "representativo de la fila"
     * @param selectionboxHTMLType Si el anterior paràetro = true -> que tipo de
     * selectionBos radiobuton checkbox, etx
     * @param selectionboxHTMLName en caso de que tenga selectionBox el nombre
     * de este (para hacer scripts con getElementsByName y cosas asi)
     * @param selectionboxCSSClass en caso de que tenga selectionbos la clase
     * css de este si aplica
     * @return
     */
    public String createHTMLBodyTable(ArrayList<ArrayList<String>> data, String trExtra, String cellCSSClass, boolean isTheFirstThevalue, boolean withSelectionbox, String selectionboxHTMLType, String selectionboxCSSClass, String selectionboxHTMLName) {
        StringBuilder htmlTable = new StringBuilder();
        if (data != null && data.size() > 0) {
            int fila = 1;
            int i = 0;
            for (ArrayList row : data) {
                i = 0;
                if (fila % 2 == 0) {
                    htmlTable.append("\t\t\t<tr class=\"even\" ").append(trExtra).append(" >");
                } else {
                    htmlTable.append("\t\t\t<tr class=\"odd\" ").append(trExtra).append(" >");
                }

                fila++;

                for (Object dato : row) {
                    if (i == 0 && withSelectionbox) {
                        htmlTable.append("\n\t<td><div class=\"").append(selectionboxCSSClass).append("\"><input type=\"").append(selectionboxHTMLType).append("\" name=\"").append(selectionboxHTMLName).append("\" value =\"").append(dato).append("\" /></div></td>");
                    } else if (i == 0 && !withSelectionbox && isTheFirstThevalue) {
                        //no hace nada, solo no escribe la primera columna de data ya que es el value que va en la primera celda
                    } else {
                        //se usa tambien && !withSelectionbox por q si ese es true a fuerzas isThefirstThevalue no tiene q 
                        //actuar asi que asi se "neutraliza" y siempre prevalece el withselectionBox 
                        if (i == 1 && isTheFirstThevalue && !withSelectionbox) {
                            htmlTable.append("\n\t<td><div class=\"").append(cellCSSClass).append("\" value=\"").append(row.get(0)).append("\">").append(dato).append("</div></td>");
                        } else {
                            htmlTable.append("\n\t<td><div class=\"").append(cellCSSClass).append("\">").append(dato).append("</div></td>");
                        }
                    }
                    i++;
                }

                htmlTable.append("\n\t\t\t</tr>");
            }
        }
        return htmlTable.toString();
    }

    /**
     * Crea las filas html de una tabla dado un set de datos (data) con una
     * primera columna definida por selectionboxHTMLType la cual puede pre
     * seleccionar las casillas del selectionbox comparando el primer elemento
     * del set de datos (generalmente el id) contra una lista de ids
     * (toCoompare)
     *
     * @param data los datos a poner en la tabla
     * @param toCompare lista de ids
     * @param selectKeyWord si el selectionboxHTMLType es checkbox o radio
     * button, puede ser selected o checked
     * @param trExtra codigo html extra para cada tr de datos
     * @param cellCSSClass la clase para cada celda
     * @param isTheFirstThevalue si es true el valor 0 de cada row en data se
     * despliega de lo contrario es solo un value (valor) de la casilla de
     * seleccion
     * @param withSelectionbox si la primera fila va a tener una casilla de
     * seleccion, como ser un checkbox o radiobutton, esto será definido por el
     * parametro selectionType. En esos casos se esperaria que el primer
     * elemento del set de datos sea un id "representativo de la fila"
     * @param selectionboxHTMLType el tipo de casilla radiobutton o checkbox
     * @param selectionboxCSSClass clase html de la casilla de seleccion
     * @param selectionboxHTMLName nombre de la casilla html de seleccion
     * @return
     */
    public String createHTMLBodyTablePreSelect(ArrayList<ArrayList> data, ArrayList<String> toCompare, String selectKeyWord, String trExtra, String cellCSSClass, boolean isTheFirstThevalue, boolean withSelectionbox, String selectionboxHTMLType, String selectionboxCSSClass, String selectionboxHTMLName) {
        StringBuilder htmlTable = new StringBuilder();
        if (data != null && data.size() > 0) {
            int fila = 1;
            int i = 0;
            String select;
            for (ArrayList row : data) {
                i = 0;
                if (fila % 2 == 0) {
                    htmlTable.append("\t\t\t<tr class=\"even\" ").append(trExtra).append(" >");
                } else {
                    htmlTable.append("\t\t\t<tr class=\"odd\" ").append(trExtra).append(" >");
                }
                if (toCompare != null && toCompare.contains(row.get(0).toString()) && withSelectionbox) {
                    select = " " + selectKeyWord + " "; //selected o checked dependiendo del selectionboxHTMLType
                } else {
                    select = "";
                }
                fila++;

                for (Object dato : row) {
                    if (i == 0 && withSelectionbox) {
                        htmlTable.append("\n\t<td><div class=\"").append(selectionboxCSSClass).append("\"><input type=\"").append(selectionboxHTMLType).append("\" name=\"").append(selectionboxHTMLName).append("\" value =\"").append(dato).append("\"").append(select).append("/></div></td>");
                    } else if (i == 0 && !withSelectionbox && isTheFirstThevalue) {
                        //no hace nada, solo no escribe la primera columna de data ya que es el value que va en la primera celda
                    } else {
                        //se usa tambien && !withSelectionbox por q si ese es true a fuerzas isThefirstThevalue no tiene q 
                        //actuar asi que asi se "neutraliza" y siempre prevalece el withselectionBox 
                        if (i == 1 && isTheFirstThevalue && !withSelectionbox) {
                            htmlTable.append("\n\t<td><div class=\"").append(cellCSSClass).append("\" value=\"").append(row.get(0)).append("\">").append(dato).append("</div></td>");
                        } else {
                            htmlTable.append("\n\t<td><div class=\"").append(cellCSSClass).append("\">").append(dato).append("</div></td>");
                        }
                    }
                    i++;
                }

                htmlTable.append("\n\t\t\t</tr>");
            }
        }
        return htmlTable.toString();
    }

    /**
     * Crea las filas html de una tabla dado un set de datos (data)
     *
     * @param data los datos a poner en la tabla
     * @param cellCSSClass la clase para cada celda
     * @param isFirstThevalue si es true el valor a desplegar
     * @param withSelectionbox si la primera fila va a tener una casilla de
     * seleccion, como ser un checkbox o radiobutton, esto será definido por el
     * parametro selectionType. En esos casos se esperaria que el primer
     * elemento del set de datos sea un id "representativo de la fila"
     * @param selectionboxHTMLType
     * @param chkBoxName en caso de que tenga checkbox el nombre de este
     * @param cssCheckboxClass en caso de que tenga checkbox la clase css de
     * este
     * @return
     */
    public String createHTMLBodyTableWithColumnExtra(ArrayList<ArrayList> data, String trExtra, String cellCSSClass, boolean isTheFirstThevalue, boolean withSelectionbox, String selectionboxHTMLType, String selectionboxCSSClass, String selectionboxHTMLName, ArrayList extraColumn, int rowIndex) {
        StringBuilder htmlTable = new StringBuilder();
        if (data != null && data.size() > 0) {
            int fila = 1;
            int i = 0;
            for (ArrayList row : data) {
                i = 0;
                if (fila % 2 == 0) {
                    htmlTable.append("\t\t\t<tr class=\"even\" ").append(trExtra).append(" >");
                } else {
                    htmlTable.append("\t\t\t<tr class=\"odd\" ").append(trExtra).append(" >");
                }

                //como la columna extra puede ser la ultima, nunca entraría en el ciclo
                //para comparar el rowIndex == i, por lo que se usa esta bandera
                boolean addExtra = false;

                for (Object dato : row) {
                    if (i == 0 && withSelectionbox) {
                        // htmlTable.append("\n\t<td><div class=\"").append(selectionboxCSSClass).append("\"><input type=\"").append(selectionboxHTMLType).append("\" name=\"").append(selectionboxHTMLName).append("\" value =\"").append(dato).append("\" /></div></td>");
                        htmlTable.append("\n\t<td><input type=\"").append(selectionboxHTMLType).append("\" name=\"").append(selectionboxHTMLName).append("\" value =\"").append(dato).append("\" /></td>");

                    } else if (i == 0 && !withSelectionbox && isTheFirstThevalue) {
                        //no hace nada, solo no escribe la primera columna de data ya que es el value que va en la primera celda
                    } else {
                        //se usa tambien && !withSelectionbox por q si ese es true a fuerzas isThefirstThevalue no tiene q 
                        //actuar asi que asi se "neutraliza" y siempre prevalece el withselectionBox 
                        if (i == 1 && isTheFirstThevalue && !withSelectionbox) {
                            htmlTable.append("\n\t<td><div class=\"").append(cellCSSClass).append("\" value=\"").append(row.get(0)).append("\">").append(dato).append("</div></td>");
                        } else if (rowIndex == i) {
                            htmlTable.append("\n\t<td><div class=\"").append(cellCSSClass).append("\">").append(extraColumn.get(fila - 1)).append("</div></td>");
                            addExtra = true;
                        } else {
                            htmlTable.append("\n\t<td><div class=\"").append(cellCSSClass).append("\">").append(dato).append("</div></td>");
                        }

                    }
                    i++;
                }
                if (!addExtra) {
                    htmlTable.append("\n\t<td><div class=\"").append(cellCSSClass).append("\">").append(extraColumn.get(fila - 1)).append("</div></td>");
                }

                fila++;
                htmlTable.append("\n\t\t\t</tr>");
            }
        }
        return htmlTable.toString();
    }

    /**
     * Crea las filas html de una tabla dado un set de datos (data)
     *
     * @param data los datos a poner en la tabla
     * @param cellCSSClass la clase para cada celda
     * @param isFirstThevalue si es true el valor a desplegar
     * @param withSelectionbox si la primera fila va a tener una casilla de
     * seleccion, como ser un checkbox o radiobutton, esto será definido por el
     * parametro selectionType. En esos casos se esperaria que el primer
     * elemento del set de datos sea un id "representativo de la fila"
     * @param selectionboxHTMLType
     * @param chkBoxName en caso de que tenga checkbox el nombre de este
     * @param cssCheckboxClass en caso de que tenga checkbox la clase css de
     * este
     * @param extraColumn el numero de columnas extra
     * @param xtraRowCSSClass arraylist con las clases de cada columna extra
     * @param xtraRowCellsContent arraylist con contenido tipo accion
     * @return
     */
    public String createHTMLBodyTableWithFixedExtraCols(ArrayList<ArrayList> data, String trExtra, String cellCSSClass, boolean isTheFirstThevalue, boolean withSelectionbox, String selectionboxHTMLType, String divCSSClass, String selectionboxHTMLName, String checkBosCssClass, int extraColumn, ArrayList<String> xtraRowsCSSClass, ArrayList<String> xtraRowCellsContent) {
        StringBuilder htmlTable = new StringBuilder();
        if (data != null && data.size() > 0) {
            int fila = 1;
            int i = 0;
            for (ArrayList row : data) {
                i = 0;
                if (fila % 2 == 0) {
                    htmlTable.append("\t\t\t<tr id=\"r").append(row.get(0)).append("\" class=\"even\" ").append(trExtra).append(" >");

                } else {
                    htmlTable.append("\t\t\t<tr id=\"r").append(row.get(0)).append("\" class=\"odd\" ").append(trExtra).append(" >");
                }

                fila++;

                for (Object dato : row) {
                    if (i == 0 && withSelectionbox) {
                        htmlTable.append("\n\t<td><div class=\"").append(divCSSClass).append("\"><input type=\"").append(selectionboxHTMLType).append("\" name=\"").append(selectionboxHTMLName).append("\" value =\"").append(dato).append("\" class=\"").append(checkBosCssClass).append("\" />");
                        //extra rows
                        //     for (int ir = 0; ir < extraColumn; ir++) {
                        //       try {
                        //htmlTable.append("\n\t<button class=\"").append(xtraRowsCSSClass.get(ir)).append("\" ").append(xtraRowCellsContent.get(ir)).append(">");
                        htmlTable.append("\n\t<button class=\"").append("arrowUp16").append("\" ").append("onclick='subir(\"r").append(dato).append("\", \"varsSelected\")'").append(">");
                        htmlTable.append("\n\t<button class=\"").append("arrowDown16").append("\" ").append("onclick='bajar(\"r").append(dato).append("\", \"varsSelected\")'").append(">");

                        //     } catch (Exception e) {//ArrayIndexOutOfBounds muy posible por error humano
                        //       htmlTable.append("\n\t<button class=\"").append("err").append("\" ").append("").append(">");
                        //  }
                        // }
                        htmlTable.append("</div></td>");
                    } else if (i == 0 && !withSelectionbox && isTheFirstThevalue) {
                        //en lso otros metodos no hace nada pero en este tiene que rellenar las columnas extra
                        //extra rows
                        for (int ir = 0; ir < extraColumn; ir++) {
                            try {
                                htmlTable.append("\n\t<td><div class=\"").append(xtraRowsCSSClass.get(ir)).append("\" ").append(xtraRowCellsContent.get(ir)).append("><input type=\"").append(selectionboxHTMLType).append("\" name=\"").append(selectionboxHTMLName).append("\" value =\"").append(dato).append("\" /></div></td>");
                            } catch (Exception e) {//ArrayIndexOutOfBounds muy posible por error humano
                                htmlTable.append("\n\t<td><div><input type=\"").append(selectionboxHTMLType).append("\" name=\"").append(selectionboxHTMLName).append("\" value =\"").append(dato).append("\" /></div></td>");
                            }
                        }
                    } else {
                        //se usa tambien && !withSelectionbox por q si ese es true a fuerzas isThefirstThevalue no tiene q 
                        //actuar asi que asi se "neutraliza" y siempre prevalece el withselectionBox 
                        if (i == 1 && isTheFirstThevalue && !withSelectionbox) {
                            htmlTable.append("\n\t<td><div class=\"").append(cellCSSClass).append("\" value=\"").append(row.get(0)).append("\">").append(dato).append("</div></td>");
                        } else {
                            htmlTable.append("\n\t<td><div class=\"").append(cellCSSClass).append("\">").append(dato).append("</div></td>");
                        }
                    }
                    i++;
                }

                htmlTable.append("\n\t\t\t</tr>");
            }
        }
        return htmlTable.toString();
    }
}
