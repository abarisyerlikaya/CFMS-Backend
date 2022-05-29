package tr.edu.yildiz.cfms.business.concretes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tr.edu.yildiz.cfms.business.abstracts.UserService;
import tr.edu.yildiz.cfms.business.repository.UserRepository;

import java.util.ArrayList;

@Service
public class UserManager implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) throw new UsernameNotFoundException("User not found!");
        var user = optionalUser.get();
        String _username = user.getUsername();
        String _password = user.getPassword();
        return new User(_username, _password, new ArrayList<>());
    }
}
