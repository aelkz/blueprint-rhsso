#!/bin/bash

PROJECT_NAMESPACE=microservices-dev
APP_NAME=blueprint-sso
APP_CONFIGMAP=blueprint-rhsso-env.yml
OCP_APP_DOMAIN=apps.sememeve.com
OCP_SSO_HOST=
SSO_URL=sso73.apps.sememeve.com
SSO_REALM=REDHAT
SSO_REALM_CERT=$SSO_REALM.pem
SSO_REALM_USERNAME=redhat
SSO_REALM_PASSWORD=12345
SSO_CLIENT_ID=blueprint-sso
SSO_CLIENT_SECRET=ba87675e-6c29-4edf-bdd3-9b9fd73dad1c
SSO_AUTH_URL=https://${SSO_URL}/auth
SSO_TOKEN_URL=https://${SSO_URL}/auth/realms/${SSO_REALM}/protocol/openid-connect/token
SSO_REALM_KEYS_URL=https://${SSO_URL}/auth/admin/realms/${SSO_REALM}/keys

oc project ${PROJECT_NAMESPACE}

# deploy the app with previously installed image
oc new-app openjdk-8-rhel8:latest~https://github.com/aelkz/blueprint-rhsso.git --name=${APP_NAME} --context-dir=/ --build-env='MAVEN_MIRROR_URL='${MAVEN_URL} -e MAVEN_MIRROR_URL=${MAVEN_URL} -n ${PROJECT_NAMESPACE}

sleep 5

# configure application port
oc patch svc ${APP_NAME} -p '{"spec":{"ports":[{"name":"http","port":8080,"protocol":"TCP","targetPort":8080}]}}' -n ${PROJECT_NAMESPACE}

oc label svc ${APP_NAME} monitor=springboot2-api -n ${PROJECT_NAMESPACE}

# expose the application to public access
oc create route edge --service=${APP_NAME} --hostname=${APP_NAME}.${OCP_APP_DOMAIN} --insecure-policy='None' --port='8080' -n ${PROJECT_NAMESPACE}

# create a configmap for the application.properties
TKN=$(curl -k -X POST "$SSO_TOKEN_URL" \
 -H "Content-Type: application/x-www-form-urlencoded" \
 -d "username=$SSO_REALM_USERNAME" \
 -d "password=$SSO_REALM_PASSWORD" \
 -d "grant_type=password" \
 -d "client_id=admin-cli" \
 | sed 's/.*access_token":"//g' | sed 's/".*//g')

RSA_PUB_KEY=$(curl -k -X GET "$SSO_REALM_KEYS_URL" \
 -H "Authorization: Bearer $TKN" \
 | jq -r '.keys[]  | select(.type=="RSA") | .publicKey' )

echo "-----BEGIN CERTIFICATE-----" > $SSO_REALM_CERT; echo $RSA_PUB_KEY >> $SSO_REALM_CERT; echo "-----END CERTIFICATE-----" >> $SSO_REALM_CERT

cat > ${APP_CONFIGMAP} <<EOL
apiVersion: v1
kind: ConfigMap
metadata:
  name: blueprint-sso-config
data:
  SSO_REALM_NAME: ${SSO_REALM}
  SSO_REALM_PUBLIC_KEY: ${SSO_REALM_CERT}
  SSO_AUTH_URL: ${SSO_AUTH_URL}
  SSO_CLIENT_ID: ${SSO_CLIENT_ID}
  SSO_CLIENT_SECRET: ${SSO_CLIENT_SECRET}
  APP_CONTEXT_PATH: /${APP_NAME}
EOL

oc create -f ${APP_CONFIGMAP} -n ${PROJECT_NAMESPACE}
oc set env dc/${APP} --from=configmap/blueprint-sso-config
