package managers;

import helpers.SecuredPassword;

public interface PasswordListener {

    SecuredPassword getSecuredPassFromDB(String nickname);
}
