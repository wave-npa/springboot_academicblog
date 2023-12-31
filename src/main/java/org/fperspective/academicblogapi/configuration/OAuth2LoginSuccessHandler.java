package org.fperspective.academicblogapi.configuration;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fperspective.academicblogapi.model.Credential;
import org.fperspective.academicblogapi.model.LoginProvider;
import org.fperspective.academicblogapi.model.Role;
import org.fperspective.academicblogapi.service.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    
    @Value("${FRONT_END_URL:default}")
    private String frontendUrl;

    private String[] adminList = {"annpse172989@fpt.edu.vn"};

    @Autowired
    @Lazy
    private CredentialService credentialService;

    @Override
    // @PostConstruct
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException{
        
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        boolean checkLogin = true;

        if("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())){
            
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            String organization = attributes.getOrDefault("hd", "").toString();
            if("fpt.edu.vn".equals(organization) || "fe.edu.vn".equals(organization)){
            String email = attributes.getOrDefault("email", "").toString();
            String pattern = "([a-zA-Z]{2})(\\d+)";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(email);
            // ObjectId userID = new ObjectId();
            String result = null;
            if(m.find()){
                result = m.group(1).toLowerCase();
            }
            
            String category = result.toUpperCase();
            String fullName = attributes.getOrDefault("given_name", "").toString();
            String userName = attributes.getOrDefault("name", "").toString();
            String avatar_url = attributes.getOrDefault("picture", "").toString();
            
            String input = attributes.getOrDefault("family_name", "").toString();
            String[] parts = input.replace("(", "").replace(")", "").split(" ");
            String term = parts[0];
            String campus = parts[1];
            // Integer num = (Integer) attributes.getOrDefault("exp", "");
            Date currentDate = new Date();
            // SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        // Date date = new Date();
            // System.out.println(formatter.format(date));
            // Collection<? extends GrantedAuthority> authority = authentication.getAuthorities();
            
           
            credentialService.findByEmail(email)
                             .ifPresentOrElse(user -> {
                                DefaultOAuth2User newUser = new DefaultOAuth2User(List.of(new SimpleGrantedAuthority(user.getRole().name())), attributes, "name");
                                Authentication securityAuth = new OAuth2AuthenticationToken(newUser, List.of(new SimpleGrantedAuthority(user.getRole().name())), oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                                SecurityContextHolder.getContext().setAuthentication(securityAuth);
                             }, () -> {
                                
                                Credential credential = new Credential();
                                Boolean check = false;
                                for (String element : adminList) {
                                    if (element.equals(email)) {
                                        check = true;
                                        break; // No need to continue searching once found
                                    }
                                }
                                if("fpt.edu.vn".equals(organization) && check.equals(false)){
                                    credential.setRole(Role.ROLE_USER);
                                }
                                else if("fe.edu.vn".equals(organization)){
                                    credential.setRole(Role.ROLE_TEACHER);
                                }
                                else{
                                    credential.setRole(Role.ROLE_ADMIN);
                                }
                                credential.setEmail(email);
                                credential.setBio(null);
                                credential.setUsername(userName);
                                credential.setFullName(fullName);
                                credential.setLoginProvider(LoginProvider.GOOGLE);
                                credential.setAvatarUrl(avatar_url);
                                if("fpt.edu.vn".equals(organization)){
                                    credential.setCategory(category);
                                    credential.setTerm(term);
                                    credential.setCampus(campus);
                                }
                                else{
                                    credential.setCategory(null);
                                    credential.setTerm(null);
                                    credential.setCampus(null);
                                }

                                try {
                                    credential.setCreatedDate(currentDate);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                // credential.setAttributes(attributes);
                                // credential.setAuthorities(authority);
                                credential.setStatus(true);
                                credentialService.save(credential);
                                DefaultOAuth2User newUser = new DefaultOAuth2User(List.of(new SimpleGrantedAuthority(credential.getRole().name())), attributes, "name");
                                Authentication securityAuth = new OAuth2AuthenticationToken(newUser, List.of(new SimpleGrantedAuthority(credential.getRole().name())), oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                                SecurityContextHolder.getContext().setAuthentication(securityAuth);
                             });
            }
            else{
                checkLogin = false;
            }
        }
        if(checkLogin){
            this.setAlwaysUseDefaultTargetUrl(true);
        //Redirect to front end page
        // this.setDefaultTargetUrl("http://localhost:5173");
        this.setDefaultTargetUrl(frontendUrl);
        super.onAuthenticationSuccess(request, response, authentication);
        }
        else{
            this.setAlwaysUseDefaultTargetUrl(true);
        //Redirect to front end page
        // this.setDefaultTargetUrl("http://localhost:5173");
        this.setDefaultTargetUrl(frontendUrl+"/login");
        super.onAuthenticationSuccess(request, response, null);
        }
    }
}
