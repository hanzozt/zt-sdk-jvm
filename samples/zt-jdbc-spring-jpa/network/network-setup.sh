#!/bin/bash

echo Creating identities
zt edge create identity device private-service -o /hanzozt/network-setup/private-service.jwt -a "services"
zt edge create identity device client -o /hanzozt/network-setup/client.jwt -a "clients"
#zt edge create identity device database -o /hanzozt/network-setup/database.jwt -a "databases"

echo Enrolling identities
zt edge enroll -j /hanzozt/network-setup/private-service.jwt
zt edge enroll -j /hanzozt/network-setup/client.jwt
#zt edge enroll -j /hanzozt/network-setup/database.jwt

echo Adding databases attribute to identity for postgres router
zt edge update identity zt-edge-router -a databases

echo Creating demo-service
zt edge create config demo-service-config zt-tunneler-client.v1 '{"hostname": "example.web","port": 8080}'
zt edge create service demo-service --configs demo-service-config -a "demo-service"

echo Creating database service
zt edge create config private-postgres-intercept.v1 intercept.v1 '{"protocols":["tcp"],"addresses":["private-postgres-server.demo"], "portRanges":[{"low":5432, "high":5432}]}'
zt edge create config private-postgres-host.v1 host.v1 '{"protocol":"tcp", "address":"postgres-db","port":5432 }'
zt edge create service private-postgres --configs private-postgres-intercept.v1,private-postgres-host.v1 -a "private-postgres-services"

echo Creating identity service policies
zt edge create service-policy service-bind-policy Bind --identity-roles "#services" --service-roles "#demo-service"
zt edge create service-policy service-dial-policy Dial --identity-roles "#clients" --service-roles "#demo-service"
zt edge create service-policy database-bind-policy Bind --identity-roles "#databases" --service-roles "#private-postgres-services"
zt edge create service-policy database-dial-policy Dial --identity-roles "#services" --service-roles "#private-postgres-services"
