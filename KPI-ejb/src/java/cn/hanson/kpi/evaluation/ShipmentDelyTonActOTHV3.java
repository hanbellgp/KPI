/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 汉声 OTHER其他 实际催货量（吨）
 */
public class ShipmentDelyTonActOTHV3 extends ShipmentDelyTon {

    public ShipmentDelyTonActOTHV3() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("cdrcus", "OTHER");
        queryParams.put("material", "OTH");
        queryParams.put("houtsta", "not in ('W')");
    }
}