docker run -d
--name keycloak
--network agu-net
-e KEYCLOAK_ADMIN=admin
-e KEYCLOAK_ADMIN_PASSWORD=admin
-e DB_VENDOR=db_vendor
-e DB_ADDR=db_host
-e DB_DATABASE=database
-e DB_USER=db_user
-e DB_PASSWORD=db_password
-v /home/diplom2025/keycloak:/opt/jboss/keycloak/standalone/data
-p 8081:8080
--restart unless-stopped
quay.io/keycloak/keycloak start-dev