#!/bin/bash

oc import-image openjdk/openjdk-8-rhel8 --from=registry.redhat.io/openjdk/openjdk-8-rhel8 -n openshift --confirm
