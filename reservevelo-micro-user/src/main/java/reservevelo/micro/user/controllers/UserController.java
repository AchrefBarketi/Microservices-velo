package reservevelo.micro.user.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reservevelo.micro.user.models.Role;
import reservevelo.micro.user.models.User;
import reservevelo.micro.user.services.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<User>addUser(@RequestBody User user){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(("api/user/save")).toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }
    @PostMapping("/role/save")
    public ResponseEntity<Role>addRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(("api/role/save")).toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }
    @PostMapping("/role/assign")
    public ResponseEntity<Role>assignRoleToUser(@RequestBody ModelUserForm form){
        userService.assignRoleToUser(form.getUserName(),form.getRoleName());
        return ResponseEntity.ok().build();
    }
    
   
    
    
    
    @GetMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorisationHeader = request.getHeader(AUTHORIZATION);
        if (authorisationHeader != null && authorisationHeader.startsWith("Bearer ")){
            try {
                String token = authorisationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);

                String refresh_token = JWT.create()
                        .withSubject(user.getUserName())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",user.getRoles().stream().map(Role::getName ).collect(Collectors.toList()))
                        .sign(algorithm);
                

                Map<String,String> tokens = new HashMap<>();
                tokens.put("refresh token",refresh_token);
                
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            }catch (Exception e){
                log.error("Error logging in : {}",e.getMessage());
                response.setHeader("error",e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String,String> error = new HashMap<>();
                error.put("error_message",e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }

        }else {
            throw new RuntimeException ("Refresh token is missing");
        }
    }
}
@Data @AllArgsConstructor @NoArgsConstructor
class ModelUserForm{
    private String userName ;
    private String roleName ;
}
