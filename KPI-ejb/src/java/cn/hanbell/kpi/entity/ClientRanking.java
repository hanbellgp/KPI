/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author C1879 此对象用于查询销售统计表数据，数据库无此表
 */
public class ClientRanking implements Serializable,Comparable {

    private static long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //客户代号
    private String cusno;

    //客户名称
    private String cusna;

    //产品别
    private String n_code_DA;

    //本年、月的销售数量
    private String nowshpqy1;

    //本年、月的销售金额
    private String nowshpamts;

    //本年、月的客户排名
    private String nowrank;

    //去年同期销售数量
    private String pastshpqy1;

    //去年同期销售金额
    private String pastshpamts;

    //去年同期客户排名
    private String pastrank;

    //差异值
    private String differencevalue;

    //金额同比
    private String growthrate;

    //字体颜色
    private String paststyle;

    //字体颜色
    private String ultstyle;

    //上月销售数量
    private String ultshpqy1;

    //上月销售金额
    private String ultshpamts;

    //数量环比
    private String shpqy1chainrate;

    //金额环比
    private String shpamtschainrate;

    //数量同比
    private String shpqy1growthrate;

    public ClientRanking() {
    }

    
    public ClientRanking(String cusno, String cusna, String n_code_DA) {
        this.cusno = cusno;
        this.cusna = cusna;
        this.n_code_DA = n_code_DA;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.cusno);
        hash = 79 * hash + Objects.hashCode(this.n_code_DA);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClientRanking other = (ClientRanking) obj;
        if (!Objects.equals(this.cusno, other.cusno)) {
            return false;
        }
        if (!Objects.equals(this.cusna, other.cusna)) {
            return false;
        }
        if (!Objects.equals(this.n_code_DA, other.n_code_DA)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.ClientTable[ id=" + getId() + " ]";
    }

    /**
     * @return the cusno
     */
    public String getCusno() {
        return cusno;
    }

    /**
     * @param cusno the cusno to set
     */
    public void setCusno(String cusno) {
        this.cusno = cusno;
    }

    /**
     * @return the cusna
     */
    public String getCusna() {
        return cusna;
    }

    /**
     * @param cusna the cusna to set
     */
    public void setCusna(String cusna) {
        this.cusna = cusna;
    }

    /**
     * @return the nowshpqy1
     */
    public String getNowshpqy1() {
        return nowshpqy1;
    }

    /**
     * @param nowshpqy1 the nowshpqy1 to set
     */
    public void setNowshpqy1(String nowshpqy1) {
        this.nowshpqy1 = nowshpqy1;
    }

    /**
     * @return the nowshpamts
     */
    public String getNowshpamts() {
        return nowshpamts;
    }

    /**
     * @param nowshpamts the nowshpamts to set
     */
    public void setNowshpamts(String nowshpamts) {
        this.nowshpamts = nowshpamts;
    }

    /**
     * @return the nowrank
     */
    public String getNowrank() {
        return nowrank;
    }

    /**
     * @param nowrank the nowrank to set
     */
    public void setNowrank(String nowrank) {
        this.nowrank = nowrank;
    }

    /**
     * @return the pastshpqy1
     */
    public String getPastshpqy1() {
        return pastshpqy1;
    }

    /**
     * @param pastshpqy1 the pastshpqy1 to set
     */
    public void setPastshpqy1(String pastshpqy1) {
        this.pastshpqy1 = pastshpqy1;
    }

    /**
     * @return the pastshpamts
     */
    public String getPastshpamts() {
        return pastshpamts;
    }

    /**
     * @param pastshpamts the pastshpamts to set
     */
    public void setPastshpamts(String pastshpamts) {
        this.pastshpamts = pastshpamts;
    }

    /**
     * @return the pastrank
     */
    public String getPastrank() {
        return pastrank;
    }

    /**
     * @param pastrank the pastrank to set
     */
    public void setPastrank(String pastrank) {
        this.pastrank = pastrank;
    }

    /**
     * @return the differencevalue
     */
    public String getDifferencevalue() {
        return differencevalue;
    }

    /**
     * @param differencevalue the differencevalue to set
     */
    public void setDifferencevalue(String differencevalue) {
        this.differencevalue = differencevalue;
    }

    /**
     * @return the growthrate
     */
    public String getGrowthrate() {
        return growthrate;
    }

    /**
     * @param growthrate the growthrate to set
     */
    public void setGrowthrate(String growthrate) {
        this.growthrate = growthrate;
    }

    /**
     * @return the ultshpqy1
     */
    public String getUltshpqy1() {
        return ultshpqy1;
    }

    /**
     * @param ultshpqy1 the ultshpqy1 to set
     */
    public void setUltshpqy1(String ultshpqy1) {
        this.ultshpqy1 = ultshpqy1;
    }

    /**
     * @return the ultshpamts
     */
    public String getUltshpamts() {
        return ultshpamts;
    }

    /**
     * @param ultshpamts the ultshpamts to set
     */
    public void setUltshpamts(String ultshpamts) {
        this.ultshpamts = ultshpamts;
    }

    /**
     * @return the shpqy1chainrate
     */
    public String getShpqy1chainrate() {
        return shpqy1chainrate;
    }

    /**
     * @param shpqy1chainrate the shpqy1chainrate to set
     */
    public void setShpqy1chainrate(String shpqy1chainrate) {
        this.shpqy1chainrate = shpqy1chainrate;
    }

    /**
     * @return the shpamtschainrate
     */
    public String getShpamtschainrate() {
        return shpamtschainrate;
    }

    /**
     * @param shpamtschainrate the shpamtschainrate to set
     */
    public void setShpamtschainrate(String shpamtschainrate) {
        this.shpamtschainrate = shpamtschainrate;
    }

    /**
     * @return the shpqy1growthrate
     */
    public String getShpqy1growthrate() {
        return shpqy1growthrate;
    }

    /**
     * @param shpqy1growthrate the shpqy1growthrate to set
     */
    public void setShpqy1growthrate(String shpqy1growthrate) {
        this.shpqy1growthrate = shpqy1growthrate;
    }

    /**
     * @return the paststyle
     */
    public String getPaststyle() {
        return paststyle;
    }

    /**
     * @param paststyle the paststyle to set
     */
    public void setPaststyle(String paststyle) {
        this.paststyle = paststyle;
    }

    /**
     * @return the ultstyle
     */
    public String getUltstyle() {
        return ultstyle;
    }

    /**
     * @param ultstyle the ultstyle to set
     */
    public void setUltstyle(String ultstyle) {
        this.ultstyle = ultstyle;
    }

    /**
     * @return the n_code_DA
     */
    public String getN_code_DA() {
        return n_code_DA;
    }

    /**
     * @param n_code_DA the n_code_DA to set
     */
    public void setN_code_DA(String n_code_DA) {
        this.n_code_DA = n_code_DA;
    }

    //
    @Override
    public int compareTo(Object o1) {
        ClientRanking o2 = (ClientRanking) o1;
        int thisrank = Integer.valueOf(this.getNowrank());
        int o2rank = Integer.valueOf(o2.getNowrank());
        if (thisrank == o2rank) {
            double thisUlshpamts = Math.abs(Double.valueOf(this.getUltshpamts()));
            double o2Ulshpamts = Math.abs(Double.valueOf(o2.getUltshpamts()));
            if (thisUlshpamts == o2Ulshpamts) {
                return Double.valueOf(this.getPastrank()) - Double.valueOf(o2.getPastrank()) >= 1 ? 1 : -1;
            } else {
                return thisUlshpamts - o2Ulshpamts >= 1 ? -1 : 1;
            }
        } else {
            return thisrank - o2rank >= 1 ? 1 : -1;
        }
    }
}

