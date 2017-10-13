package com.cisco.devnet.infracheck;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.util.logging.Logger;

public class InfraCheck {

    private static final Logger log = Logger.getLogger(InfraCheck.class.getName());


//    String BASE_URL = "http://localhost:8080";
    String BASE_URL = "https://sandboxapic.cisco.com/api";
    String APICEM_AUTH = BASE_URL.concat("/v1/ticket");
    String APICEM_PATHTRACE = BASE_URL.concat("/v1/flow-analysis");


    public String getTicket() {

        log.info("Logging into APIC-EM: ".concat(BASE_URL));

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

            String ticket = jsonResponse
                    .getBody()
                    .getObject()
                    .getJSONObject("response")
                    .getString("serviceTicket");

            log.info("Received login token!");
            return ticket;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<JsonNode> pathCheck(String apicemTicket) {
        return pathCheck(apicemTicket, "7ce5d5e4-98f3-4ae3-ba90-b3d81502fb90");
    }

    public HttpResponse<JsonNode> pathCheck(String apicemTicket, String pathId) {
        //https://sandboxapic.cisco.com/api/v1/flow-analysis/7ce5d5e4-98f3-4ae3-ba90-b3d81502fb90
        log.info("Starting path trace");
        log.info("API: ".concat(APICEM_PATHTRACE));

        try {
            SSLContext sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                    .build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            Unirest.setHttpClient(httpclient);

            HttpResponse<JsonNode> res = Unirest.get(APICEM_PATHTRACE.concat("/").concat(pathId))
                    .header("X-Auth-Token", apicemTicket)
                    .asJson();

            log.info("Finished path trace request!");
            log.info("Returning result from APIC-EM");
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
