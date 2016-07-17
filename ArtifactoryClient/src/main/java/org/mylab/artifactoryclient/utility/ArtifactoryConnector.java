/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mylab.artifactoryclient.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Base64;

/**
 *
 * @author ddimas
 */
public class ArtifactoryConnector {

    String baseURL;
    String basicAuth;

    public ArtifactoryConnector(String baseURL, String username, String password) {
        this.baseURL = baseURL;
        if (username != null && password != null) {
            this.basicAuth = "Basic " + new String(Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));
        }
    }

    public String get(String endpoint) throws IOException {
        URLConnection connection = new URL(this.baseURL + endpoint).openConnection();

        if (this.basicAuth != null) {
            connection.setRequestProperty("Authorization", this.basicAuth);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }

        return response.toString();
    }

    public ReadableByteChannel binaryGet(String url) throws MalformedURLException, IOException {
        URL artifact = new URL(url);
        return Channels.newChannel(artifact.openStream());
    }

    public String post(String endpoint, String body) throws IOException {
        final String CHARSET = java.nio.charset.StandardCharsets.UTF_8.name();

        HttpURLConnection connection = (HttpURLConnection) new URL(this.baseURL + endpoint).openConnection();

        if (this.basicAuth != null) {
            connection.setRequestProperty("Authorization", this.basicAuth);
        }

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", java.nio.charset.StandardCharsets.UTF_8.name());
        connection.setRequestProperty("Content-Type", "plain/text;charset=" + CHARSET);
        connection.setDoOutput(true);

        try (OutputStream output = connection.getOutputStream()) {
            output.write(body.getBytes());
        }

        HttpURLConnection httpConnection = (HttpURLConnection) connection;
        int rc = httpConnection.getResponseCode();

        InputStream is;
        if (rc < 400) {
            is = httpConnection.getInputStream();
        } else {
            is = httpConnection.getErrorStream();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }

        if (rc > 400) {
            throw new IOException("artifactory returned an http error code of: "
                    + rc + ", the error message was: "
                    + response.toString());
        } else {
            return response.toString();
        }
    }
}
