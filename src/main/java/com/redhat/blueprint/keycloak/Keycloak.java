/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.blueprint.keycloak;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Keycloak {

    @JsonProperty("realm")
    private String realm;

    @JsonProperty("auth-server-url")
    private String authServerUrl;

    @JsonProperty("no-check-certificate")
    private Boolean noCheckCertificate;

    @JsonProperty("resource")
    private String resource;

    @JsonProperty("enable-cors")
    private Boolean cors;

    @JsonProperty("ssl-required")
    private String sslRequired;

    @JsonProperty("use-resource-role-mappings")
    private Boolean resourceRoleMappings;

    public Keycloak() { }

    public Keycloak(String realm, String authServerUrl, Boolean noCheckCertificate, String resource, Boolean cors, String sslRequired, Boolean resourceRoleMappings) {
        this.realm = realm;
        this.authServerUrl = authServerUrl;
        this.noCheckCertificate = noCheckCertificate;
        this.resource = resource;
        this.cors = cors;
        this.sslRequired = sslRequired;
        this.resourceRoleMappings = resourceRoleMappings;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getAuthServerUrl() {
        return authServerUrl;
    }

    public void setAuthServerUrl(String authServerUrl) {
        this.authServerUrl = authServerUrl;
    }

    public Boolean getNoCheckCertificate() {
        return noCheckCertificate;
    }

    public void setNoCheckCertificate(Boolean noCheckCertificate) {
        this.noCheckCertificate = noCheckCertificate;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Boolean getCors() {
        return cors;
    }

    public void setCors(Boolean cors) {
        this.cors = cors;
    }

    public String getSslRequired() {
        return sslRequired;
    }

    public void setSslRequired(String sslRequired) {
        this.sslRequired = sslRequired;
    }

    public Boolean getResourceRoleMappings() {
        return resourceRoleMappings;
    }

    public void setResourceRoleMappings(Boolean resourceRoleMappings) {
        this.resourceRoleMappings = resourceRoleMappings;
    }
}
