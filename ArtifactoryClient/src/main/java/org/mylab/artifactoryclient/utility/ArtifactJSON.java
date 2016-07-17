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
public class ArtifactJSON {
    //{  "repo" : "parsers",  "path" : ".",  "name" : "batstop.tsk",  "type" : "file",  "size" : 83545704,  "created" : "2016-06-15T15:41:58.627-04:00",  "created_by" : "feedsci",  "modified" : "2016-06-15T15:41:57.605-04:00",  "modified_by" : "feedsci",  "updated" : "2016-06-15T15:41:57.605-04:00"}

    @JsonProperty("repo")
    private String repo;
    @JsonProperty("path")
    private String path;
    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("size")
    private double size;
    @JsonProperty("created")
    private String created;
    @JsonProperty("created_by")
    private String createdBy;
    @JsonProperty("modified")
    private String modified;
    @JsonProperty("modified_by")
    private String modifiedBy;
    @JsonProperty("updated")
    private String updated;

    ArtifactJSON() {

    }

    ArtifactJSON(String repo, String path, String name, String type, double size, String created, String createdBy, String modified, String modifiedBy, String updated) {
        this.repo = repo;
        this.path = path;
        this.name = name;
        this.type = type;
        this.size = size;
        this.created = created;
        this.createdBy = createdBy;
        this.modified = modified;
        this.modifiedBy = modifiedBy;
        this.updated = updated;
    }

    /**
     * @return the repo
     */
    public String getRepo() {
        return repo;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the size
     */
    public double getSize() {
        return size;
    }

    /**
     * @return the created
     */
    public String getCreated() {
        return created;
    }

    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @return the modified
     */
    public String getModified() {
        return modified;
    }

    /**
     * @return the modifiedBy
     */
    public String getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @return the updated
     */
    public String getUpdated() {
        return updated;
    }
    
    @Override
    public String toString() {
        return "repo: " + repo 
                + ",path: " + path
                + ",name: " + name
                + ",type: " + type
                + ",size: " + size
                + ",created: " + created
                + ",created by: " + createdBy
                + ",modified: " + modified
                + ",modified by: " + modifiedBy
                + ",updated: " + updated;
    }
}
