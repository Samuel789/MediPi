/*
 Copyright 2016  Richard Robinson @ HSCIC <rrobinson@hscic.gov.uk, rrobinson@nhs.net>

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.medipi.messaging.rest;

import java.io.FileInputStream;
import java.net.ConnectException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.medipi.MediPiMessageBox;
import org.medipi.MediPiProperties;
import org.medipi.logging.MediPiLogger;

/**
 * This Message engine class manages the SSL context for the mutual
 * authentication of the data in transit and allows a common interface for
 * calling the restful verbs GET, PUT and POST
 *
 *
 * @author rick@robinsonhq.com
 */
public class RESTfulMessagingEngine {

    private static final String MEDIPITRANSMITKEYSTORE = "medipi.device.cert.location";
    private static final String MEDIPITRANSMITTRUSTSTORELOCATION = "medipi.transmit.truststore.location";
    private static final String MEDIPITRANSMITTRUSTSTOREPASSWORD = "medipi.transmit.truststore.password";
    private final WebTarget trackingTarget;

    /**
     * Creates the SSL context and constructs foundations for RESTful messaging
     *
     * @param urlPath of the RESTful interface
     * @param params to be added to the base target
     * @throws Exception
     */
    public RESTfulMessagingEngine(String urlPath, String[] params) throws Exception {
        ClientBuilder builder = ClientBuilder.newBuilder();

        SSLContext sslContext = createSSLContext();
        builder.sslContext(sslContext);
        builder.hostnameVerifier(new javax.net.ssl.HostnameVerifier() {

            public boolean verify(String hostname,
                    javax.net.ssl.SSLSession sslSession) {
                // verify is not necessary
                return true;
            }
        });

        Client client = builder.build();

        WebTarget baseTarget = client.target(urlPath);
        String paramCat = "";
        if (params != null && params.length > 0) {
            paramCat = String.join("/", params);
            trackingTarget = baseTarget.path(paramCat);
        } else {
            trackingTarget = baseTarget;
        }

    }

    private SSLContext createSSLContext() throws Exception {
        String truststoreLocation = MediPiProperties.getInstance().getProperties().getProperty(MEDIPITRANSMITTRUSTSTORELOCATION);
        if (truststoreLocation == null || truststoreLocation.trim().equals("")) {
            MediPiLogger.getInstance().log(RESTfulMessagingEngine.class.getName() + "constructor", "MediPi truststore is not set");
            MediPiMessageBox.getInstance().makeErrorMessage("MediPi truststore is not set and secure transmission of data will not work", null);
        }
        String truststorePass = MediPiProperties.getInstance().getProperties().getProperty(MEDIPITRANSMITTRUSTSTOREPASSWORD);
        if (truststorePass == null || truststorePass.trim().equals("")) {
            MediPiLogger.getInstance().log(RESTfulMessagingEngine.class.getName() + "constructor", "MediPi truststore password is not set");
            MediPiMessageBox.getInstance().makeErrorMessage("MediPi truststore password is not set and secure transmission of data will not work", null);
        }
        String keystoreLocation = MediPiProperties.getInstance().getProperties().getProperty(MEDIPITRANSMITKEYSTORE);
        if (keystoreLocation == null || keystoreLocation.trim().equals("")) {
            MediPiLogger.getInstance().log(RESTfulMessagingEngine.class.getName() + "constructor", "MediPi keystore is not set");
            MediPiMessageBox.getInstance().makeErrorMessage("MediPi keystore is not set and secure transmission of data will not work", null);
        }
        String keystorePass = System.getProperty("medipi.device.macaddress");

        KeyStore trustStore = loadStore(truststoreLocation, truststorePass);

        TrustManagerFactory tmf
                = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        KeyStore keyStore = loadStore(keystoreLocation, keystorePass);

        KeyManagerFactory kmf
                = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, keystorePass.toCharArray());
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

        return sslContext;
    }

    private KeyStore loadStore(String trustStoreFile, String password) throws Exception {
        KeyStore store = KeyStore.getInstance("JKS");
        store.load(new FileInputStream(trustStoreFile), password.toCharArray());
        return store;
    }

    /**
     * Common interface for executing RESTful GET requests
     *
     * @param params hashmap of parameters to be added to the target URL
     * @return Response
     */
    public Response executeGet(HashMap<String, Object> params) {
        try {
            WebTarget wt;
            if (params != null) {
                wt = trackingTarget
                        .resolveTemplates(params);
            } else {
                wt = trackingTarget;
            }

            Response listResponse = wt
                    .request(MediaType.APPLICATION_JSON)
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .get();

            return listResponse;
        } catch (Exception ex) {
            if (ex.getCause().getClass() == ConnectException.class) {

                MediPiLogger.getInstance().log(RESTfulMessagingEngine.class.getName() + ".error", "Error - The Concentrator host is not reachable - detected when trying to GET: " + trackingTarget.getUri() + " message: " + ex.getMessage());
                MediPiMessageBox.getInstance().makeErrorMessage("Error detected - The Concentrator host is not reachable - " + ex.getLocalizedMessage(), ex);
            } else {
                MediPiLogger.getInstance().log(RESTfulMessagingEngine.class.getName() + ".error", "Error detected when trying to GET: " + trackingTarget.getUri() + " message: " + ex.getMessage());
                MediPiMessageBox.getInstance().makeErrorMessage("Error detected - " + ex.getLocalizedMessage(), ex);
            }
        }
        return null;
    }

    /**
     *
     * Common interface for executing RESTful POST requests
     *
     * @param params hashmap of parameters to be added to the target URL
     * @param e Entity representation of the payload
     * @return Response
     */
    public Response executePost(HashMap<String, Object> params, Entity<?> e) throws Exception {
        try {
            WebTarget wt;
            if (params != null) {
                wt = trackingTarget
                        .resolveTemplates(params);
            } else {
                wt = trackingTarget;
            }

            Response listResponse = wt
                    .request(MediaType.APPLICATION_JSON)
                    .post(e);
            return listResponse;
        } catch (Exception ex) {
            if (ex.getCause().getClass() == ConnectException.class) {

                MediPiLogger.getInstance().log(RESTfulMessagingEngine.class.getName() + ".error", "Error - The Concentrator host is not reachable - detected when trying to POST: " + trackingTarget.getUri() + " message: " + ex.getMessage());
                MediPiMessageBox.getInstance().makeErrorMessage("Error detected - The Concentrator host is not reachable - " + ex.getLocalizedMessage(), ex);
            } else {
                MediPiLogger.getInstance().log(RESTfulMessagingEngine.class.getName() + ".error", "Error detected when trying to POST: " + trackingTarget.getUri() + " message: " + ex.getMessage());
                MediPiMessageBox.getInstance().makeErrorMessage("Error detected - " + ex.getLocalizedMessage(), ex);
            }
        }
        return null;
    }

    /**
     *
     * Common interface for executing RESTful POST requests
     *
     * @param params hashmap of parameters to be added to the target URL
     * @param e Entity representation of the payload
     * @param header hashmap representation of bespoke header name and value to
     * be added to the message
     * @return Response
     * @throws Exception
     */
    public Response executePut(HashMap<String, Object> params, Entity<?> e, HashMap<String, String> header) throws Exception {
        try {
            WebTarget wt;
            if (params != null) {
                wt = trackingTarget
                        .resolveTemplates(params);
            } else {
                wt = trackingTarget;
            }

            Builder b = wt.request(MediaType.APPLICATION_JSON);
            if (header != null || !header.isEmpty()) {
                Iterator it = header.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    b.header(pair.getKey().toString(), pair.getValue());
                    it.remove();
                }
            }
            Response listResponse = b.put(e);
            return listResponse;
        } catch (Exception ex) {
            if (ex.getCause().getClass() == ConnectException.class) {

                MediPiLogger.getInstance().log(RESTfulMessagingEngine.class.getName() + ".error", "Error - The Concentrator host is not reachable - detected when trying to PUT: " + trackingTarget.getUri() + " message: " + ex.getMessage());
                MediPiMessageBox.getInstance().makeErrorMessage("Error detected - The Concentrator host is not reachable - " + ex.getLocalizedMessage(), ex);
            } else {
                MediPiLogger.getInstance().log(RESTfulMessagingEngine.class.getName() + ".error", "Error detected when trying to PUT: " + trackingTarget.getUri() + " message: " + ex.getMessage());
                MediPiMessageBox.getInstance().makeErrorMessage("Error detected - " + ex.getLocalizedMessage(), ex);
            }
        }
        return null;
    }
}
