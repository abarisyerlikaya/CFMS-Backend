package tr.edu.yildiz.cfms.business.concretes;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tr.edu.yildiz.cfms.business.abstracts.UserService;

import java.util.ArrayList;

@Service
public class UserManager implements UserService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User("admin", "1234", new ArrayList<>());
    }
}
