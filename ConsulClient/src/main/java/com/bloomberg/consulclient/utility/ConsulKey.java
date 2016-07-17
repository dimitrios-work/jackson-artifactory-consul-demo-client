/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mylab.consulclient.utility;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Base64;

/**
 *
 * @author ddimas
 */

//{LockIndex":0,"Key":"cmdb/","Flags":0,"Value":null,"CreateIndex":82093,"ModifyIndex":82093}
public class ConsulKey {

    @JsonProperty("LockIndex")
    private int lockIndex;
    @JsonProperty("Key")
    private String key;
    @JsonProperty("Flags")
    private int flags;
    @JsonProperty("Value")
    private String value;
    @JsonProperty("CreateIndex")
    private int createIndex;
    @JsonProperty("ModifyIndex")
    private int modifyIndex;

    public ConsulKey() {

    }

    public ConsulKey(int lockIndex, String key, int flags, String value, int createIndex, int modifyIndex) {
        this.lockIndex = lockIndex;
        this.key = key;
        this.flags = flags;
        this.value = value;
        this.createIndex = createIndex;
        this.modifyIndex = modifyIndex;
    }

    public int getLockIndex() {
        return this.lockIndex;
    }

    public String getKey() {
        return this.key;
    }

    public int getFlags() {
        return this.flags;
    }

    public String getValue() {
        return new String(Base64.getDecoder().decode(this.value));
    }

    public int getCreateIndex() {
        return this.createIndex;
    }

    public int getModifyIndex() {
        return this.modifyIndex;
    }
}
