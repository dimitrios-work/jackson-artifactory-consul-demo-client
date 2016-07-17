/*
 * Copyright (C) 2016 Dimitrios <dimitrios.work@outlook.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.mylab.consulclient;

import java.io.IOException;
import java.net.MalformedURLException;
import org.mylab.consulclient.utility.ConsulConnector;
import org.mylab.consulclient.utility.ConsulKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dimitrios <dimitrios.work@outlook.com>
 */
public class ConsulClient {

    private Logger logger;
    private String baseURL;
    private String token;

    public static class ConsulClientBuilder {

        private String baseURL;
        private String token;
        private Logger logger;

        public ConsulClientBuilder endpoint(String baseURL) {
            this.baseURL = baseURL;
            return this;
        }

        public ConsulClientBuilder token(String token) {
            this.token = token;
            return this;
        }

        public ConsulClientBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public ConsulClient build() {
            if (logger == null) {
                logger = LoggerFactory.getLogger(ConsulClientBuilder.class);
            }

            if (baseURL == null) {
                this.baseURL = "http://localhost:8500/v1/kv/";
            }

            logger.info("builder is returning a ConsulClient object with the following information, baseURL: " + this.baseURL
                    + ", token: " + this.token
                    + ", logger: " + this.logger.toString());
            return new ConsulClient(this.logger, this.baseURL, this.token);
        }
    }

    ConsulClient(Logger logger, String baseURL, String token) {
        this.logger = logger;
        this.baseURL = baseURL;
        this.token = token;
    }

    public String listAllKeys() {
        logger.debug("listAllKeys called at " + Clock.systemUTC().instant().toString());
        String response = invokeConnector("?recurse");
        logger.debug("listAllKeys returned response: " + response + " at: " + Clock.systemUTC().instant().toString());
        return response;
    }

    public String getKey(String key) {
        logger.debug("getKey called with a key value of: " + key + " at: " + Clock.systemUTC().instant().toString());
        String response = invokeConnector(key + "?raw");
        logger.debug("getKey returned response: " + response + " at: " + Clock.systemUTC().instant().toString());
        return response;
    }

    public ConsulKey getKeyDetailed(String key) throws IOException {
        logger.info("getKey called with a key value of: " + key + " at: " + Clock.systemUTC().instant().toString());
        String response = invokeConnector(key);
        logger.info("getKey returned response: " + response + " at: " + Clock.systemUTC().instant().toString());
        ObjectMapper mapper = new ObjectMapper();
        ConsulKey[] ckKeyArray = mapper.readValue(response, ConsulKey[].class);
        return ckKeyArray[0];
    }

    public String getJenkinsEndpoint() {
        return "";
    }

    public String getArtifactoryEndpoint() {
        return "";
    }

    public String getCiAPIEndpoint() {
        return "";
    }

    public String invokeConnector(String endpoint) {
        String authenticatedEndpoint = this.token == null ? endpoint : (endpoint + "?token=" + this.token);

        try {
            ConsulConnector csConnector = new ConsulConnector(baseURL + authenticatedEndpoint);
            return csConnector.get();
        } catch (MalformedURLException ex) {
            logger.error("invalid url:", ex);
        } catch (IOException ex) {
            logger.error("couldn't connect to the consul server", ex);
        }

        return null;
    }
}
