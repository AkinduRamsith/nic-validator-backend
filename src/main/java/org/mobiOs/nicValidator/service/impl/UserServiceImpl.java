package org.mobiOs.nicValidator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.mobiOs.nicValidator.dao.NICValidatorEntity;
import org.mobiOs.nicValidator.dao.OTPEntity;
import org.mobiOs.nicValidator.dao.UserEntity;
import org.mobiOs.nicValidator.dto.LoginDTO;
import org.mobiOs.nicValidator.dto.UserDTO;
import org.mobiOs.nicValidator.repository.NICValidatorRepository;
import org.mobiOs.nicValidator.repository.OTPRepository;
import org.mobiOs.nicValidator.repository.UserRepository;
import org.mobiOs.nicValidator.service.UserService;
import org.mobiOs.nicValidator.util.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NICValidatorRepository nicValidatorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private OTPRepository otpRepository;

    @Override
    public String addUser(UserDTO userDTO) {
        if (userRepository.existsByUserName(userDTO.getUserName())) {
            return "Username already exits";
        }
        UserEntity userEntity = new UserEntity(
                userDTO.getId(),
                userDTO.getUserName(),
                userDTO.getEmail(),
                this.passwordEncoder.encode(userDTO.getPassword())
        );

        userRepository.save(userEntity);
        return "Sign up success";
    }

    @Override
    public LoginResponse loginUser(LoginDTO loginDTO) {
        UserEntity userEntity = userRepository.findByUserName(loginDTO.getUserName());

        if (null != userEntity) {
            String password = loginDTO.getPassword();
            String encodePassword = userEntity.getPassword();
            Boolean isPwdMatches = passwordEncoder.matches(password, encodePassword);
            if (isPwdMatches) {
                Optional<UserEntity> userEntity1 = userRepository.findOneByUserNameAndPassword(loginDTO.getUserName(), encodePassword);

                if (userEntity1.isPresent()) {

                    return new LoginResponse("Login Success", true);
                } else {
                    return new LoginResponse("Login Failed", false);
                }
            } else {
                return new LoginResponse("Password Not matched", false);
            }
        } else {
            return new LoginResponse("Username not exits", false);
        }
    }

    @Override
    public String forgotPassword(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity != null) {
            String otp = generateOtp();
            saveOtp(email, otp);
            sendOtpByEmail(email, otp);
            return "Email sent with OTP";
        } else {
            return "User not Found";
        }
    }

    @Override
    public String validateOTP(String email, int otp) {

        List<OTPEntity> otpList = otpRepository.findByEmail(email);
        boolean isValidOtp = false;
        for (OTPEntity otpEntity : otpList) {
            if (otpEntity != null && otpEntity.getIs_active() && otpEntity.getOtp() == otp) {

               otpEntity.setIs_active(false);
               otpRepository.save(otpEntity);
               isValidOtp=true;
                  break;



            }
        }
        if(isValidOtp){
            return "OTP Validated";
        }else{
            return "Invalid OTP";
        }



    }

    @Override
    public String setNewPassword(String email, String newPassword) {
        Optional<UserEntity> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));

        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(userEntity);
            return "Password updated successfully";
        } else {
            return "User not found";
        }
    }

    private void saveOtp(String email, String otp) {
        OTPEntity otpEntity = new OTPEntity();
        otpEntity.setEmail(email);
        otpEntity.setOtp(Integer.parseInt(otp));
        otpEntity.setIs_active(true);
        otpRepository.save(otpEntity);
    }

    private void sendOtpByEmail(String email, String otp) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("Password Reset OTP");
        simpleMailMessage.setText("Your OTP for password reset " + otp);
        javaMailSender.send(simpleMailMessage);
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100_000 + random.nextInt(900_000);
        return String.valueOf(otp);
    }
}
