echo Deleting identities
zt edge delete identity databaseTunneler
zt edge delete identity databaseClient


echo Deleting postgres service configs
zt edge delete config postgres-intercept.v1
zt edge delete config postgres-host.v1
zt edge delete service postgres

echo Deleting postgres service access policies
zt edge delete service-policy postgres-dial-policy
zt edge delete service-policy postgres-bind-policy

echo Deleting postgres network access policies
zt edge delete edge-router-policy public-router-client-access
zt edge delete edge-router-policy public-router-database-access
zt edge delete service-edge-router-policy public-router-access
