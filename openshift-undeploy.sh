#!/bin/bash

PROJECT_NAMESPACE=microservices-dev
APP_NAME=blueprint-sso
OCP_APP_DOMAIN=apps.sememeve.com

oc delete all -lapp=${APP_NAME} -n ${PROJECT_NAMESPACE}
