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
package org.mylab.sonarpress;

import org.mylab.artifactoryclient.ArtifactoryClient;
import org.mylab.artifactoryclient.ArtifactoryClient.ArtifactoryClientBuilder;
import org.mylab.artifactoryclient.utility.ArtifactJSON;
import org.mylab.artifactoryclient.utility.ArtifactJSONResults;
import org.mylab.consulclient.ConsulClient;
import org.mylab.consulclient.ConsulClient.ConsulClientBuilder;
import org.mylab.consulclient.utility.ConsulKey;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dimitrios <dimitrios.work@outlook.com>
 */
public class MainClass {

    public static void main(String argv[]) {
        Logger logger = LoggerFactory.getLogger(MainClass.class);
        ConsulClient cc = new ConsulClientBuilder().endpoint("http://localhost:8085/v1/kv/")
                .logger(logger).build();
        System.out.println(cc.listAllKeys());
        System.out.println("test/testKey=" + cc.getKey("test/testKey"));

        ConsulKey key;
        try {
            key = cc.getKeyDetailed("test/testKey");
            System.out.println("our key.Value is:" + key.getValue());
        } catch (IOException ex) {
            logger.error("IO Exception trying to retrieve test/testKey", ex);
        }
        
        ArtifactoryClient arClient = new ArtifactoryClientBuilder()
                .baseURL("http://localhost:8081/artifactory")
                .username("feedsci")
                .password("feeds&23")
                .build();
        
        System.out.println("Artifactory isAlive? = " + (arClient.isAlive()? "true" : "false"));
        //System.out.println("Artifactory, get all parsers: " + arClient.listAllParserArtifacts());
        Optional<ArtifactJSONResults> arJSONResults = arClient.listAllParserArtifacts();
        arJSONResults.ifPresent(result -> prettyPrintAllArtifacts(result.getResults()));
        arJSONResults.map(result -> result.getResults()).orElseThrow(IllegalArgumentException::new);
        //JAVA 9 - tested in jshell:
        //arJSONResults.ifPresentOrElse(result -> prettyPrintAllArtifacts(result.getResults()), () -> {raise blah});
        //ArtifactJSON[] arrArray = arJSONResults.orElseThrow();
        ArtifactJSON[] artifactsArray = arClient.listAllParserArtifacts().getResults();
        System.out.println("artifactsArray: " + artifactsArray.toString());
        System.out.println("artifactsArray[0]: " + artifactsArray[0].toString());
        System.out.println("artifactsArray[0].getName: " + artifactsArray[0].getName());
        
        artifactsArray = arClient.listParser("amers", "batstop").getResults();
        System.out.println("listing artifacts for amers/batstop : ");
        for (ArtifactJSON artifact : artifactsArray){
            System.out.println("artifact:" + artifact.toString());
        }
        
        System.out.println("listing artifact tags for amer/batstop : ");
        artifactsArray = arClient.listTags("amers", "batstop").getResults();
        for (ArtifactJSON artifact : artifactsArray) {
            System.out.println("tag: " + artifact.toString());
        }
        
        System.out.println("saving artifact for amer/batstop locally : ");
        arClient.storeArtifact("http://localhost:8081/artifactory/parsers/blah_test/test/test-tag-36/blah_test.bin", "/home/ddimas/blah_test.bin");
    }
    
    static void prettyPrintAllArtifacts(ArtifactJSON[] artJSONArr) {
        System.out.println("the whole array \n" + artJSONArr.toString());
        System.out.println("the first element of the array: " + artJSONArr[0].toString());
        System.out.println("the name field of the 1st element of the array: " + artJSONArr[0].getName());
        
        System.out.println("pretty printing all elements: ");
        
        Arrays.asList(artJSONArr).forEach(element -> {
            System.out.println("element name: " + element.getName()
                + ", " + "element type: " + element.getType()
                + ", " + "element path: " + element.getPath()
                + ", " + "created at: " + element.getUpdated());
        });
    }
}
