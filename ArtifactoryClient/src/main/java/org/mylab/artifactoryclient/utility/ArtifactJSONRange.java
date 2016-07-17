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
public class ArtifactJSONRange {
    @JsonProperty("start_pos")
    private double startPos;
    @JsonProperty("end_pos")
    private double endPos;
    @JsonProperty("total")
    private double total;
    
    ArtifactJSONRange() {
        
    }
    
    ArtifactJSONRange(double startPos, double endPos, double total) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.total = total;
    }

    /**
     * @return the startPos
     */
    public double getStartPos() {
        return startPos;
    }

    /**
     * @return the endPos
     */
    public double getEndPos() {
        return endPos;
    }

    /**
     * @return the total
     */
    public double getTotal() {
        return total;
    }
    
    
}
