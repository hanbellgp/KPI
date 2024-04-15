/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb.tms;

import cn.hanbell.kpi.entity.tms.Project;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class ProjectBean implements Serializable {

    final String URL = "http://172.16.10.94:54320/api/TMS_CostRegion";

    public ProjectBean() {

    }

    public List<Project> getProjects(String projectSeq,String projectName) {
        StringBuffer params=new StringBuffer("/GetPLMProjectData?projectName=");
        params.append(projectName).append("&");
        params.append("projectSeq=").append(projectSeq);
        CloseableHttpResponse response = get(URL+params.toString(), null, null);
        if (response != null) {
            HttpEntity httpEntity = response.getEntity();
            try {
                JSONObject jor;
                jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                List<Project> list = new ArrayList<Project>();
                String message = jor.getString("Message");
                JSONArray arrays = jor.getJSONArray("Data");
                for (int i = 0; i < arrays.length(); i++) {
                    Project p = new Project();
                    JSONObject data = arrays.getJSONObject(i);
                    p.setId(String.valueOf(i));
                    p.setPMUserId(data.getString("pMUserId"));
                    p.setProjectSeq(data.getInt("projectSeq"));
                    p.setProjectName(data.getString("projectName"));
                    p.setPMUser(data.getString("pMUser"));
                    list.add(p);
                }
                return list;
            } catch (ParseException | JSONException | IOException ex) {

            } finally {
                try {
                    response.close();
                } catch (IOException ex) {
                }
            }

        }
        return null;
    }

    public String findByProjectSeq(String projectSeq) {
        String urlString = URL + "/GetPLMProjectPh?projectSeq=" + projectSeq;
        CloseableHttpResponse response = get(urlString, null, null);
        if (response != null) {
            HttpEntity httpEntity = response.getEntity();
            try {
                JSONObject jor;
                jor = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                String message = jor.getString("Message");
                if ("查询成功".equals(message)) {
                    return jor.getString("Data");
                } else {
                    return null;
                }

            } catch (ParseException | JSONException | IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    response.close();
                } catch (IOException ex) {
                }
            }
        }
        return null;
    }

    public CloseableHttpResponse get(String url, Map<String, String> params, RequestConfig config) {
        try {
            SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
            HostnameVerifier allowAllHosts = new NoopHostnameVerifier();
            SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(connectionFactory).build();
            URIBuilder builder = new URIBuilder(url);
            if (params != null) {
                Iterator var5 = params.keySet().iterator();

                while (var5.hasNext()) {
                    String k = (String) var5.next();
                    builder.addParameter(k, (String) params.get(k));
                }
            }
            URI uri = builder.build();
            HttpGet httpGet = new HttpGet(uri);
            if (config != null) {
                httpGet.setConfig(config);
            }
            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                return response;
            }
        } catch (Exception var9) {
            return null;
        }
        return null;
    }

}
