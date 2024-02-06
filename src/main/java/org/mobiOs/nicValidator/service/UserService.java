package org.mobiOs.nicValidator.service;

import org.mobiOs.nicValidator.dto.LoginDTO;
import org.mobiOs.nicValidator.dto.UserDTO;
import org.mobiOs.nicValidator.util.LoginResponse;

public interface UserService {
    String addUser(UserDTO userDTO);

    LoginResponse loginUser(LoginDTO loginDTO);

    String forgotPassword(String email);

    String validateOTP(String email, int otp);

    String setNewPassword(String email, String newPassword);

}
