/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */

package cn.hanson.kpi.evaluation;

/**
 *
 * @author C0160
 */
public class CastingProductionHanyoungResinSand extends CastingProduction {

    public CastingProductionHanyoungResinSand() {
        super();
        queryParams.put("facno", "Y");
        queryParams.put("line", " LIKE '%树脂砂%' ");
        queryParams.put("step", " = '造型' ");
    }

}
