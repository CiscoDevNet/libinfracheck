package com.cisco.devnet.infracheck;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import java.util.logging.Logger;

public class InfraCheck {

    private static final Logger log = Logger.getLogger(InfraCheck.class.getName());

    private String BASE_URL;
    private String APICEM_AUTH;
    private String APICEM_PATHTRACE;

    public void setConfig(String url) {

        if (!url.isEmpty() && url != null ) {
            this.BASE_URL = url;
            APICEM_AUTH = BASE_URL.concat("/v1/ticket");
            APICEM_PATHTRACE = BASE_URL.concat("/v1/flow-analysis");
        } else {
            throw new IllegalArgumentException("url must not be empty");
        }

    }

    public String getTicket(String username, String password) {

        log.info("Logging into APIC-EM: ".concat(BASE_URL));
        log.info("Credentials: ");
        log.info("Username: ".concat(username));
        log.info("Password: ".concat(password));

        try {
            SSLContext sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                    .build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            Unirest.setHttpClient(httpclient);

            JSONObject jsonLogin = new JSONObject();
            jsonLogin.put("username", username);
            jsonLogin.put("password", password);

            HttpResponse<JsonNode> jsonResponse = Unirest.post(APICEM_AUTH)
                    .header("Content-Type", "application/json")
                    .body(jsonLogin.toString())
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

    /**
     * Returns the response from the /flow-analysis endpoint.
     *
     * @param apicemTicket the APIC-EM authentication ticket
     * @param pathId the ID that represents the path trace (not the taskId)
     * @return
     * HttpResponse&lt;JsonNode&gt;
     */
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

    /*
    POST /flow-analysis
    {
      "protocol": "",
      "periodicRefresh": false,
      "inclusions": [
        ""
      ],
      "sourcePort": "",
      "destIP": "", // Required
      "destPort": "",
      "sourceIP": "" // Required
    }

    returns

    {
      "version": "",
      "response": {
        "flowAnalysisId": "",
        "taskId": "",
        "url": ""
      }
    }
    */

    /**
     *  Returns a path trace ID
     *  For more information: https://sandboxapic.cisco.com/swagger#!/flow-analysis/initiateFlowAnalysis
     *
     * @param apicemTicket API Ticket that is received from the login() method
     * @param sourceIp the Source IP to start the trace from
     * @param destIp the Destination IP for the trace to stop
     * @return
     *
     *  HttpResponse&lt;JsonNode&gt; containing the ID of the path trace created
     */
    public HttpResponse<JsonNode> createPathTrace(String apicemTicket, String sourceIp, String destIp) {

        log.info("Starting create path trace");
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

            String json = "{\"sourceIP\":\"" + sourceIp + "\","
                    + "\"destIP\":\"" + destIp + "\"}";

            HttpResponse<JsonNode> res = Unirest.post(APICEM_PATHTRACE)
                    .header("X-Auth-Token", apicemTicket)
                    .header("Content-Type", "application/json")
                    .body(json)
                    .asJson();

            log.info("Create path trace request!");
            log.info("Returning result");
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
