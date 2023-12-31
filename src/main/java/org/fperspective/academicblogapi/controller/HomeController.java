package org.fperspective.academicblogapi.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Home", description = "Home Management API")
// @RequestMapping("/api/v1")
@CrossOrigin
public class HomeController {

    @GetMapping("/")
    @CrossOrigin
    public String home(@RequestParam(name = "logout", required = false, defaultValue = "true") boolean logout) {
        return "index";
    }

    @GetMapping("/login")
    @CrossOrigin
    String login() {
        return "app-user/login";
    }

    @GetMapping("/token")
    public String getAccessToken(@AuthenticationPrincipal OidcUser principal) {
        OidcIdToken idToken = principal.getIdToken();
        String idTokenValue = idToken.getTokenValue();

        return idTokenValue ;

    }
}
