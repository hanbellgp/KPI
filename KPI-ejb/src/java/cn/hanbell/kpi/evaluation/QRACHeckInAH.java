/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 */
public class QRACHeckInAH extends QRACHeckIn{
    public QRACHeckInAH(){
        super();
        queryParams.put("STEPID", "机体入库前检验站");
    }
    
}
