package org.fperspective.academicblogapi.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.fperspective.academicblogapi.model.Credential;
import org.fperspective.academicblogapi.service.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@Tag(name = "Credential", description = "Credential Management API")
@RequestMapping("/api/v1/user")
@CrossOrigin
@ApiResponses(value = {
    @ApiResponse (responseCode = "200", content = { @Content(schema = @Schema(implementation = Credential.class), mediaType = "application/json") }),
    @ApiResponse (responseCode = "404", content = { @Content(schema = @Schema()) }),
    @ApiResponse (responseCode = "500", content = { @Content(schema = @Schema()) }) })
public class CredentialController {

    @Autowired
    // @Lazy
    private CredentialService credentialService;

    @Hidden
    @RequestMapping("/")
    @CrossOrigin
    public void redirect(HttpServletResponse response) throws IOException{
        response.sendRedirect("/swagger-ui.html");
    }

    @GetMapping("/show")
    @CrossOrigin
    public Collection<Credential> get(){
        return credentialService.get();
    }

    @GetMapping("/show/{userId}")
    @CrossOrigin
    
    public Credential get(@PathVariable String userId) {
        Credential user = credentialService.get(userId);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return user;
    }

    @GetMapping("/search/{text}")
    @CrossOrigin
    public List<Credential> search(@PathVariable String text) {
        List<Credential> users = credentialService.search(text);
        if (users == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return users;
    }

    @GetMapping("/search/{campus}")
    @CrossOrigin
    public List<Credential> searchByCampus(@PathVariable String campus) {
        List<Credential> users = credentialService.searchByCampus(campus);
        if (users == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return users;
    }

    @DeleteMapping("/delete/{userId}")
    @CrossOrigin
    public void delete(@PathVariable String userId) {
        credentialService.remove(userId);
    }

    @PostMapping("/show")
    @CrossOrigin
    public Credential save(@RequestBody Credential credential){
        return credentialService.saveUser(credential);
    }
}