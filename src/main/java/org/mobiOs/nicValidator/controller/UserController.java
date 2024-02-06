package org.mobiOs.nicValidator.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.mobiOs.nicValidator.dto.LoginDTO;
import org.mobiOs.nicValidator.dto.UserDTO;
import org.mobiOs.nicValidator.service.UserService;
import org.mobiOs.nicValidator.util.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping(path = "/save")
    public ResponseEntity<String> saveUser(@RequestBody UserDTO userDTO) {
        String response = userService.addUser(userDTO);
        if("Username already exits".equals(response)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }

     @PostMapping(path = "/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO){
        LoginResponse loginResponse=userService.loginUser(loginDTO);
        return ResponseEntity.ok(loginResponse);
     }

     @PostMapping(path = "/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email){
        String response=userService.forgotPassword(email);
        if("Email sent with OTP".equals(response)){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
     }

     @PostMapping(path = "/validate-otp")
    public ResponseEntity<String> validateOTP(@RequestParam String email,@RequestParam int otp){
        String response=userService.validateOTP(email,otp);
         if("OTP Validated".equals(response)){
             return ResponseEntity.ok(response);
         }else{
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
         }
     }
    @PatchMapping(path = "/set-new-password")
    public ResponseEntity<String> setNewPassword(@RequestParam String email, @RequestParam String newPassword){
        String response = userService.setNewPassword(email, newPassword);
        log.info(email);
        if ("Password updated successfully".equals(response)) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


}
