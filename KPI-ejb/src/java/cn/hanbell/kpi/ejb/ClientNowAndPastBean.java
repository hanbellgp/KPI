/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.ClientRanking;
import cn.hanbell.kpi.entity.ClientTable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class ClientNowAndPastBean extends ClientRanking implements Serializable {

    //当前
    public List<ClientTable> getNowClient(int y, int m, LinkedHashMap<String, String> map) {
        String facno = map.get("facno") != null ? map.get("facno") : "";
        //查询当前值并赋予排名
        String[] arr = facno.split(",");
        //过渡list
        List<ClientTable> list;
        //汇总返回list
        List<ClientTable> returnlist = new ArrayList<>();
        ClientTable ct;
        boolean aa;
        try {
            for (String arr1 : arr) {
                list = new ArrayList<>();
                List result = getClient(y, m, arr1, map);
                if (result != null && !result.isEmpty()) {
                    for (int i = 0; i < result.size(); i++) {
                        ct = new ClientTable();
                        Object[] row = (Object[]) result.get(i);
                        ct.setCusno(row[0].toString());
                        ct.setCusna(row[1].toString());
                        ct.setNowshpqy1(RTshpqy1(row[2].toString()));
                        ct.setNowshpamts(row[3].toString());
                        list.add(ct);
                    }
                    //汇总
                    //循环list数据往returnlist合并数据               
                    if (result == null && returnlist.isEmpty()) {
                        returnlist = list;
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            aa = true;
                            for (int j = 0; j < returnlist.size(); j++) {
                                //list客户与returnlist客户相同 则台数与金额 returnlist = returnlist + list
                                if (list.get(i).getCusna().equals(returnlist.get(j).getCusna())) {
                                    returnlist.get(j).setNowshpqy1(RTshpqy1(returnlist.get(j).getNowshpqy1(), list.get(i).getNowshpqy1()));
                                    returnlist.get(j).setNowshpamts(RTshpamts(returnlist.get(j).getNowshpamts(), list.get(i).getNowshpamts()));
                                    aa = false;
                                }
                            }
                            if (aa) {
                                returnlist.add(list.get(i));
                            }
                        }
                    }
                }
            }
            return returnlist;
        } catch (Exception e) {
            return null;
        }
    }

    //去年同期
    public List<ClientTable> getPastClient(int y, int m, LinkedHashMap<String, String> map) {
        String facno = map.get("facno") != null ? map.get("facno") : "";
        //查询去年同期值并赋予排名
        String[] arr = facno.split(",");
        //过渡list
        List<ClientTable> list;
        //汇总返回list
        List<ClientTable> returnlist = new ArrayList<>();
        ClientTable ct;
        boolean aa;
        try {
            for (String arr1 : arr) {
                list = new ArrayList<>();
                List result = getClient(y - 1, m, arr1, map);
                if (result != null && !result.isEmpty()) {
                    for (int i = 0; i < result.size(); i++) {
                        ct = new ClientTable();
                        Object[] row = (Object[]) result.get(i);
                        ct.setCusno(row[0].toString());
                        ct.setCusna(row[1].toString());
                        ct.setPastshpqy1(RTshpqy1(row[2].toString()));
                        ct.setPastshpamts(row[3].toString());
                        list.add(ct);
                    }
                    //汇总
                    //循环list数据往returnlist合并数据                
                    if (result == null && returnlist.isEmpty()) {
                        returnlist = list;
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            aa = true;
                            for (int j = 0; j < returnlist.size(); j++) {
                                //list客户与returnlist客户相同 则台数与金额 returnlist = returnlist + list
                                if (list.get(i).getCusna().equals(returnlist.get(j).getCusna())) {
                                    returnlist.get(j).setPastshpqy1(RTshpqy1(returnlist.get(j).getPastshpqy1(), list.get(i).getPastshpqy1()));
                                    returnlist.get(j).setPastshpamts(RTshpamts(returnlist.get(j).getPastshpamts(), list.get(i).getPastshpamts()));
                                    aa = false;
                                }
                            }
                            if (aa) {
                                returnlist.add(list.get(i));
                            }
                        }
                    }
                }
            }
            return returnlist;
        } catch (Exception e) {
            return null;
        }
    }

    public List<ClientTable> getClientListNowAndPast(int y, int m, LinkedHashMap<String, String> map) {
        List<ClientTable> list = new LinkedList<>();
        //得到已经有排名的list
        //NowClient       
        List<ClientTable> nowList = RTlist(getNowClient(y, m, map), "now");
        //PastClient
        List<ClientTable> pastList = RTlist(getPastClient(y, m, map), "past");
        //循环nowList 并与 pastList 合并客户
        //其他值
        int nowothershpqy1, pastothershpqy1;
        Double nowothershpamts, pastothershpamts;
        //TOP20总计
        int top20nowshpqy1 = 0;
        Double top20nowshpamts = 0.0;
        int top20pastshpqy1 = 0;
        Double top20pastshpamts = 0.0;
        if (nowList != null && !nowList.isEmpty()) {
            ClientTable now, past;
            boolean aa, bb;
            for (int i = 0; i < nowList.size(); i++) {
                now = nowList.get(i);
                aa = true;
                bb = false;
                //前20项台数金额累加 以计算其他值
                if (Integer.parseInt(now.getNowrank()) <= 20) {
                    ClientTable ct = new ClientTable();
                    if (pastList != null && !pastList.isEmpty()) {
                        for (int j = 0; j < pastList.size(); j++) {
                            past = pastList.get(j);
                            if (now.getCusna().equals(past.getCusna())) {
                                ct.setCusno(now.getCusno());
                                ct.setCusna(now.getCusna());
                                ct.setNowrank(now.getNowrank());
                                ct.setNowshpqy1(now.getNowshpqy1());
                                ct.setNowshpamts(df.format(now.getNowshpamts()));
                                ct.setPastrank(past.getPastrank());
                                ct.setPastshpqy1(past.getPastshpqy1());
                                ct.setPastshpamts(df.format(past.getPastshpamts()));
                                ct.setDifferencevalue(RTdifferencevalue(now.getNowshpamts(), past.getPastshpamts()));
                                ct.setGrowthrate(RTgrowthrate(now.getNowshpamts(), past.getPastshpamts()));
                                //合计TOP20
                                top20nowshpqy1 += Integer.parseInt(now.getNowshpqy1());
                                top20nowshpamts += Double.parseDouble(now.getNowshpamts());
                                top20pastshpqy1 += Integer.parseInt(past.getPastshpqy1());
                                top20pastshpamts += Double.parseDouble(past.getPastshpamts());
                                if ((Double.parseDouble(now.getNowshpamts()) - Double.parseDouble(past.getPastshpamts())) < 0) {
                                    ct.setStyle("red");
                                }
                                aa = false;
                                if (now.getCusna().equals("总计")) {
                                    bb = true;
                                }
                                list.add(ct);
                            }
                        }
                        if (aa) {
                            top20nowshpqy1 += Integer.parseInt(now.getNowshpqy1());
                            top20nowshpamts += Double.parseDouble(now.getNowshpamts());
                            ct.setCusno(now.getCusno());
                            ct.setCusna(now.getCusna());
                            ct.setNowrank(now.getNowrank());
                            ct.setNowshpqy1(now.getNowshpqy1());
                            ct.setNowshpamts(df.format(now.getNowshpamts()));
                            ct.setPastshpqy1("0");
                            ct.setPastshpamts("0");
                            ct.setDifferencevalue(df.format(now.getNowshpamts()));
                            ct.setGrowthrate("100");
                            list.add(ct);
                        }

                    }
                }
                //如果排名大于20 则有其他项 
                if (Integer.parseInt(now.getNowrank()) > 20 && now.getCusna().equals("总计")) {
                    for (int j = 0; j < pastList.size(); j++) {
                        if (pastList.get(j).getCusna().equals("总计")) {
                            nowothershpqy1 = Integer.parseInt(now.getNowshpqy1()) - top20nowshpqy1;
                            pastothershpqy1 = Integer.parseInt(pastList.get(j).getPastshpqy1()) - top20pastshpqy1;
                            nowothershpamts = Double.parseDouble(now.getNowshpamts()) - top20nowshpamts;
                            pastothershpamts = Double.parseDouble(pastList.get(j).getPastshpamts()) - top20pastshpamts;
                            ClientTable ct = new ClientTable();
                            ct.setCusna("其他");
                            ct.setNowshpqy1(String.valueOf(nowothershpqy1));
                            ct.setNowshpamts(df.format(nowothershpamts));
                            ct.setPastshpqy1(String.valueOf(pastothershpqy1));
                            ct.setPastshpamts(df.format(pastothershpamts));
                            if ((nowothershpamts - pastothershpamts) < 0) {
                                ct.setStyle("red");
                            }
                            if (Double.parseDouble(pastList.get(j).getPastshpamts()) < 0) {
                                ct.setDifferencevalue("0");
                                ct.setGrowthrate("0");
                            } else {
                                ct.setDifferencevalue(RTdifferencevalue(String.valueOf(nowothershpamts), String.valueOf(pastothershpamts)));
                                ct.setGrowthrate(RTgrowthrate(String.valueOf(nowothershpamts), String.valueOf(pastothershpamts)));

                            }
                            list.add(ct);
                            ClientTable ctsum = new ClientTable();
                            ctsum.setCusna(now.getCusna());
                            ctsum.setNowshpqy1(now.getNowshpqy1());
                            ctsum.setNowshpamts(df.format(now.getNowshpamts()));
                            ctsum.setPastshpqy1(pastList.get(j).getPastshpqy1());
                            ctsum.setPastshpamts(df.format(pastList.get(j).getPastshpamts()));
                            ctsum.setDifferencevalue(RTdifferencevalue(now.getNowshpamts(), pastList.get(j).getPastshpamts()));
                            ctsum.setGrowthrate(RTgrowthrate(now.getNowshpamts(), pastList.get(j).getPastshpamts()));
                            if ((Double.parseDouble(now.getNowshpamts()) - Double.parseDouble(pastList.get(j).getPastshpamts())) < 0) {
                                ct.setStyle("red");
                            }
                            if (Double.parseDouble(pastList.get(j).getPastshpamts()) < 0) {
                                ct.setDifferencevalue("0");
                                ct.setGrowthrate("0");
                            } else {
                                ct.setDifferencevalue(RTdifferencevalue(now.getNowshpamts(), pastList.get(j).getPastshpamts()));
                                ct.setGrowthrate(RTgrowthrate(now.getNowshpamts(), pastList.get(j).getPastshpamts()));
                            }

                        }

                    }
                }
            }
            return new LinkedList<>();
        }
        return null;
    }

}
