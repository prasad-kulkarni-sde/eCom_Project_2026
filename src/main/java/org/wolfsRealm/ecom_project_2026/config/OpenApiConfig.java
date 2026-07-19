package org.wolfsRealm.ecom_project_2026.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;



@OpenAPIDefinition(
        info = @Info(title = "Spring Boot eCom Project 2026 API",
                version = "6.9",
                description = "This is Spring Boot eCommerce 2026 Project",
                contact= @Contact(
                        name = "Prasad Kulkarni",
                        email = "prasadkulkarni2409@gmail.com",
                        url = "https://github.com/prasad-kulkarni-sde"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )

        ),
        externalDocs = @ExternalDocumentation(
                description = ("Project Documentation"),
                url = "https://github.com/prasad-kulkarni-sde"

        ),
        security = @SecurityRequirement(name = "jwtCookieAuth")
)
@SecurityScheme(
        name = "jwtCookieAuth",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.COOKIE,
        paramName = "springBootEcomProject2026"
)
public class OpenApiConfig {
}