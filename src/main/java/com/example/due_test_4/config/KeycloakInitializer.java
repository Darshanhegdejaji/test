package com.example.due_test_4.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Configuration
public class KeycloakInitializer implements CommandLineRunner {

    private final Keycloak keycloakAdmin;

    @Value("${keycloak.realm}")
    private String realm;

     @Value("${keycloak.default-role}")
     private String keycloakDefaultRole;

      @Value("${keycloak.admin.client-id}")
         private String adminClientId;

    public KeycloakInitializer(Keycloak keycloakAdmin) {
        this.keycloakAdmin = keycloakAdmin;
    }

    @Override
    public void run(String... args) {
        try {
            RealmResource realmResource = keycloakAdmin.realm(realm);

            // Find client
            List<ClientRepresentation> clients =
            realmResource.clients().findByClientId(adminClientId);

             if (clients.isEmpty()) {
                       throw new RuntimeException("Client not found: " + adminClientId);
                   }

               // Internal UUID
               String clientUuid = clients.get(0).getId();

               // Client resource
               ClientResource clientResource =
                           realmResource.clients().get(clientUuid);

               // Client roles resource
               RolesResource rolesResource = clientResource.roles();

            List<String> existingRoles = rolesResource.list().stream()
                                .map(RoleRepresentation::getName)
                                .toList();

            // Create roles defined in the application

         createRoleIfNotExists(rolesResource, existingRoles, "STUDENT");


         createRoleIfNotExists(rolesResource, existingRoles, "DEPARTMENT ADMIN");


         createRoleIfNotExists(rolesResource, existingRoles, "SUPER ADMIN");


            // Set default role for the realm
            try {
                 RoleRepresentation defaultRole = rolesResource.get(keycloakDefaultRole).toRepresentation();
                               // Add the default role to the realm's default roles
                               realmResource.roles().get("default-roles-" + realm.toLowerCase()).addComposites(
                                       List.of(defaultRole)
                               );
                               log.info("Set default realm role: {}", keycloakDefaultRole);
            } catch (Exception e) {
                log.warn("Could not set default role 'STUDENT': {}", e.getMessage());
            }

            log.info("Keycloak role initialization complete.");
        } catch (Exception e) {
            log.error("Failed to initialize Keycloak roles. Ensure Keycloak is running and admin credentials are correct.", e);
        }
    }

    private void createRoleIfNotExists(
                RolesResource rolesResource,
                List<String> existingRoles,
                String roleName
        ) {

            if (!existingRoles.contains(roleName)) {

                RoleRepresentation role = new RoleRepresentation();

                role.setName(roleName);
                role.setDescription("Client Role: " + roleName);

                rolesResource.create(role);

                log.info("Created client role: {}", roleName);

            } else {
                log.info("Client role already exists: {}", roleName);
            }
        }
}
