#!/bin/bash

#
# Removes all of the Hanzo ZT network artifacts created by network-setup.sh
#
echo Deleting identities
zt edge delete identity private-service
zt edge delete identity client
#zt edge delete identity database

echo Deleting demo-service
zt edge delete service demo-service
zt edge delete config demo-service-config

echo Deleting database service
zt edge delete service private-postgres
zt edge delete config private-postgres-intercept.v1
zt edge delete config private-postgres-host.v1

echo Deleting identity service policies
zt edge delete service-policy service-bind-policy
zt edge delete service-policy service-dial-policy
zt edge delete service-policy database-bind-policy
zt edge delete service-policy database-dial-policy
