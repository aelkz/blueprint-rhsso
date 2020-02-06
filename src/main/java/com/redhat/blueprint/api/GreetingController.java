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
package com.redhat.blueprint.api;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @Autowired
    private GreetingProperties properties;
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/api/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name, Principal principal) {

        final Principal userPrincipal = principal;

        if (userPrincipal instanceof KeycloakPrincipal) {

            KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) userPrincipal;

            String realm = kp.getKeycloakSecurityContext().getRealm();

            System.out.println(realm);

            AccessToken accessToken = kp.getKeycloakSecurityContext().getToken();

            Map<String,Object> otherClaims = accessToken.getOtherClaims();

            for (Map.Entry<String, Object> claim : otherClaims.entrySet()) {
                System.out.println(claim.getKey() + " : " + claim.getValue());
            }

        } else {
            throw new RuntimeException("UserPrincipal object not inherits from KeycloakSecurityContext");
        }

        return new Greeting(counter.incrementAndGet(), String.format(properties.getMessage(), name));
    }
}
