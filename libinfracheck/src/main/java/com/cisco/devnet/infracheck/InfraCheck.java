package com.cisco.devnet.infracheck;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.json.JSONObject;
import org.json.JSONString;

import javax.net.ssl.SSLContext;

public class InfraCheck {

    String BASE_URL = "https://sandboxapic.cisco.com/api";
    String APICEM_AUTH = BASE_URL + "/v1/ticket";
    String APICEM_PATHTRACE = BASE_URL + "/v1/flow-analysis";
//    String APICEM_AUTH = "http://localhost:8080";

    public HttpResponse<JsonNode> getHealth() throws java.security.NoSuchAlgorithmException,
            java.security.KeyStoreException, java.security.KeyManagementException, UnirestException {
        return login();
    }

    public HttpResponse<JsonNode> login() {

        try {
            SSLContext sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                    .build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            Unirest.setHttpClient(httpclient);


            HttpResponse<JsonNode> jsonResponse = Unirest.post(APICEM_AUTH)
                    .header("Content-Type", "application/json")
                    .body("{\"username\": \"devnetuser\", \"password\": \"Cisco123!\"}")
                    .asJson();

            return jsonResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<JsonNode> pathCheck(String apicemTicket) {
        //https://sandboxapic.cisco.com/apic/api/v1/flow-analysis/7ce5d5e4-98f3-4ae3-ba90-b3d81502fb90
        try {
            SSLContext sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                    .build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            Unirest.setHttpClient(httpclient);

            HttpResponse<JsonNode> jsonResponse = Unirest.get(APICEM_PATHTRACE + "7ce5d5e4-98f3-4ae3-ba90-b3d81502fb90")
                    .header("Content-Type", "application/json")
                    .header("X-Auth-Token", apicemTicket)
                    .asJson();

            return jsonResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
