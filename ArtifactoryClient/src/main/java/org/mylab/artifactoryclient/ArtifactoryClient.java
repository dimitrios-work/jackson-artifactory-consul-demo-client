/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mylab.artifactoryclient;

import org.mylab.artifactoryclient.utility.ArtifactJSONResults;
import org.mylab.artifactoryclient.utility.ArtifactoryConnector;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.util.Optional;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ddimas
 */
public class ArtifactoryClient {

    private Logger logger;
    private ArtifactoryConnector arConnector;

    public static class ArtifactoryClientBuilder {

        private Logger logger;
        private String baseURL;
        private String username;
        private String password;

        public ArtifactoryClientBuilder baseURL(String baseURL) {
            this.baseURL = baseURL;
            return this;
        }

        public ArtifactoryClientBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public ArtifactoryClientBuilder username(String username) {
            this.username = username;
            return this;
        }

        public ArtifactoryClientBuilder password(String password) {
            this.password = password;
            return this;
        }

        public ArtifactoryClient build() {
            if (logger == null) {
                logger = LoggerFactory.getLogger(ArtifactoryClientBuilder.class);
            }

            if (baseURL == null) {
                this.baseURL = "http://localhost:8500/v1/kv/";
            }

            logger.info("builder is returning an ArtifactoryClient object with the following information, baseURL: " + this.baseURL
                    + ", username: " + this.username
                    + ", password: " + this.password
                    + ", logger: " + this.logger.toString());
            return new ArtifactoryClient(this.logger, this.baseURL, this.username, this.password);
        }
    }

    private ArtifactoryClient(Logger logger, String baseURL, String username, String password) {
        this.logger = logger;
        this.arConnector = new ArtifactoryConnector(baseURL, username, password);
    }

    public Optional<ArtifactJSONResults> listAllParserArtifacts() {
        String jsonResponse = invokePost("/api/search/aql", "items.find({\"repo\":{\"$eq\":\"parsers\"}})");
        
        ObjectMapper mapper = new ObjectMapper();
        ArtifactJSONResults results = null;
        try {
            results = mapper.readValue(jsonResponse, ArtifactJSONResults.class);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ArtifactoryClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Optional.ofNullable(results);
    }
    
    public Optional<ArtifactJSONResults> listParser(String region, String parser){
        String jsonResponse = invokePost("/api/search/aql", "items.find({\"repo\":{\"$eq\":\"parsers\"}, \"$and\":[{\"path\":{\"$eq\":\"amers/batstop\"}}]})");
        
        ObjectMapper mapper = new ObjectMapper();
        ArtifactJSONResults results = null;
        try {
            results = mapper.readValue(jsonResponse, ArtifactJSONResults.class);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ArtifactoryClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Optional.ofNullable(results);
    }
    
    public ArtifactJSONResults listTags(String region, String parser) {
        String jsonResponse = invokePost("/api/search/aql", "items.find({\"repo\":{\"$eq\":\"parsers\"}, \"$and\":[{\"path\":{\"$eq\":\"" + region + "/" + parser +"\"}}], \"$and\":[{\"type\":{\"$eq\":\"folder\"}}]})");
        
        ObjectMapper mapper = new ObjectMapper();
        ArtifactJSONResults results = null;
        try {
            results = mapper.readValue(jsonResponse, ArtifactJSONResults.class);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ArtifactoryClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

    public boolean isAlive() {
        String response =  invokeGet("/api/system/ping");
        return response == null ? null : response.contains("OK");
    }
    
    public void storeArtifact(String url, String destination) {
        //FileOutputStream fos = invokeBinaryGet(url, destination);
        //fos.getChannel().transferFrom(arConnector.binaryGet(url), 0, Long.MAX_VALUE);
        long bytesWritten = saveArtifactToDisk(url, destination);
        logger.info("bytes written: " + bytesWritten);
    }

    private long invokeBinaryGet(String url, String destination) throws FileNotFoundException, IOException {
            ReadableByteChannel rbc = arConnector.binaryGet(url);
            return new FileOutputStream(destination).getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
    
    private Long saveArtifactToDisk(String url, String destination) {
        Long bytesWritten = null;
        try {
            bytesWritten = invokeBinaryGet(url, destination);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ArtifactoryClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return bytesWritten;
    }
    
    private String invokeGet(String endpoint) {
        try {            
            return arConnector.get(endpoint);
        } catch (IOException ex) {
            this.logger.error("couldn't connect to the artifactory server", ex);
        }

        return null;
    }
    
    private String invokePost(String endpoint, String body) {
        try {
            return arConnector.post(endpoint, body);
        } catch (IOException ex) {
            this.logger.error("got an IO Exception trying to post to artifactory:", ex);
        }
        
        return null;
    }
}
