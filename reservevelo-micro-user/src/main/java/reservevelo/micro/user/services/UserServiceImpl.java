package reservevelo.micro.user.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reservevelo.micro.user.models.Role;
import reservevelo.micro.user.models.User;
import reservevelo.micro.user.repos.RoleRepo;
import reservevelo.micro.user.repos.UserRepo;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService , UserDetailsService {
    private final UserRepo userRepo ;
    private final RoleRepo roleRepo ;
    private final PasswordEncoder passwordEncoder ;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findAppUserByUserName(username);
        if(user == null){
            log.error("user not found in db");
            throw new UsernameNotFoundException("user not found exception");

        }else{
            log.info("user found in db : {}",username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUserName(),user.getPassword(),authorities);
    }


    @Override
    public User saveUser(User user) {
        log.info("saving user :{} to db",user.getUserName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("saving role :{} to db",role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void assignRoleToUser(String userName, String roleName) {
        log.info("assigning role {} to user {} to database",roleName,userName);
        User user = userRepo.findAppUserByUserName(userName);
        Role role = roleRepo.findByName(roleName);
        user.getRoles().add(role);
        userRepo.save(user);
    }

    @Override
    public User getUser(String username) {
        log.info("getting user {}",username);
        return userRepo.findAppUserByUserName(username);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("getting all users");
        return userRepo.findAll();
    }

}
