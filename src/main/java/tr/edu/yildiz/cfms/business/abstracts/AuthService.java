package tr.edu.yildiz.cfms.business.abstracts;

import org.springframework.stereotype.Service;
import tr.edu.yildiz.cfms.api.models.LoginRequest;
import tr.edu.yildiz.cfms.core.response_types.Response;

@Service
public interface AuthService {
    Response login(LoginRequest request);

    Response logout(String token);
}
