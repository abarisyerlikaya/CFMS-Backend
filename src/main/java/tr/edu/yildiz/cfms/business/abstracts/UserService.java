package tr.edu.yildiz.cfms.business.abstracts;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tr.edu.yildiz.cfms.entities.concretes.hibernate.User;

@Service
public interface UserService extends UserDetailsService {
    User getByUsername(String username) throws UsernameNotFoundException;

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
