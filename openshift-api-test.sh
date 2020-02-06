#!/bin/bash

APP_NAME=blueprint-sso

oc get pod | grep ${APP_NAME} | grep Running | awk '{ print $1 }'

curl -v -k -X GET "http://`oc get route ${APP_NAME} --template {{.spec.host}}`/api/greeting"
