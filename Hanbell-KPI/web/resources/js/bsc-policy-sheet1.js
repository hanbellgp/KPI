/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    //指标说明
    var sc = document.getElementById("formOne:sc");
    if (sc !== null) {
        var h = "formOne:hdnSummary";
        var d = "formOne:summary";
        var hdnSummary = document.getElementById(h);
        var displaySummary = document.getElementById(d);
        //alert($(hdnSummary).val());
        $(displaySummary).html($(hdnSummary).val());
    }
    //对策说明
    var sc = document.getElementById("formOne:ac");
    if (sc !== null) {
        var hf = "formOne:hdnfactor";
        var f = "formOne:factor";
        var hc = "formOne:hdncountermeasure";
        var c = "formOne:countermeasure";
        var displayFactor = document.getElementById(f);
        var hdnFactor = document.getElementById(hf);
        var displayCountermeasure = document.getElementById(c);
        var hdnCountermeasure = document.getElementById(hc);
        $(displayFactor).html($(hdnFactor).val());
        $(displayCountermeasure).html($(hdnCountermeasure).val());
    }
});