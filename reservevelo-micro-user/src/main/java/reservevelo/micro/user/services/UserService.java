package reservevelo.micro.user.services;

import java.util.List;

import reservevelo.micro.user.models.Role;
import reservevelo.micro.user.models.User;

public interface UserService {

    User saveUser (User user );
    Role saveRole (Role role);
    void assignRoleToUser (String userName,String roleName);
    User getUser (String username);
    List<User> getAllUsers();
    
    
 }
