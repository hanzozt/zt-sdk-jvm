echo Creating and enrolling identities
zt edge -i postgresql create identity device databaseTunneler -o databaseTunneler.jwt -a databases
zt edge -i postgresql create identity device databaseClient -o databaseClient.jwt -a clients

zt edge enroll --jwt databaseTunneler.jwt
zt edge enroll --jwt databaseClient.jwt

echo Creating postgres service configs
zt edge -i postgresql create config postgres-intercept.v1 intercept.v1 '{"protocols":["tcp"],"addresses":["postgres-server.demo"], "portRanges":[{"low":5432, "high":5432}]}'
zt edge -i postgresql create config postgres-host.v1 host.v1 '{"protocol":"tcp", "address":"postgres-db","port":5432 }'
zt edge -i postgresql create service postgres --configs postgres-intercept.v1,postgres-host.v1 -a "postgres-services"

echo Creating postgres service access policies
zt edge -i postgresql create service-policy postgres-dial-policy Dial --identity-roles '#clients' --service-roles '#postgres-services'
zt edge -i postgresql create service-policy postgres-bind-policy Bind --identity-roles '#databases' --service-roles '#postgres-services'

echo Creating postgres network access policies
zt edge -i postgresql create edge-router-policy public-router-client-access --identity-roles "#clients" --edge-router-roles "#all"
zt edge -i postgresql create edge-router-policy public-router-database-access --identity-roles "#databases" --edge-router-roles "#all"
zt edge -i postgresql create service-edge-router-policy public-router-access --service-roles "#postgres-services" --edge-router-roles "#all"
