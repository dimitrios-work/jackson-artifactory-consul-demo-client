/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mylab.artifactoryclient.utility;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author ddimas
 */
public class ArtifactJSONResults {
    @JsonProperty("results")
    private ArtifactJSON[] artifactsArray;
    @JsonProperty("range")
    private ArtifactJSONRange artifactsRange;
    
    ArtifactJSONResults() {
        
    }
    
    ArtifactJSONResults(ArtifactJSON[] artifactsArray, ArtifactJSONRange artifactsRange) {
        this.artifactsArray = artifactsArray;
        this.artifactsRange = artifactsRange;
    }
    
    public ArtifactJSON[] getResults() {
        return artifactsArray;
    }
    
    public ArtifactJSONRange getRange() {
        return artifactsRange;
    }
}
