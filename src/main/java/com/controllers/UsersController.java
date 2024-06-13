package com.controllers;



import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dao.UsersDAO;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.model.Users;


@RestController

public class UsersController {
	
	@Autowired
	UsersDAO usersDAO;
	

	
	
	
	@GetMapping("userLogin/{emailId}/{password}")
	public Users userLogin(@PathVariable("emailId") String emailId, @PathVariable("password") String password) {
		return usersDAO.userLogin(emailId, password);
	}
	
	@PostMapping("registerUser")
	public Users registerUser(@RequestBody Users user) {
		return usersDAO.registerUser(user);
	}

	@PutMapping("updateUser/{userId}")
	public Users update(@PathVariable int userId, @RequestBody Users updatedUser) {
	    // Set the ID from the path variable to the updated user object
	    updatedUser.setId(userId);
	    return usersDAO.updateEmp(updatedUser);
	}


	
	@DeleteMapping("deleteUserById/{id}")
	public String deleteUserById(@PathVariable("id") int Id) {
		usersDAO.deleteUserById(Id);
		return "Employee Record Deleted...";
	}


	private Map<String, Integer> otpMap = new HashMap<>();
	@GetMapping("/send-sms/{phonenumber}")
    public boolean sendSms(@PathVariable("phonenumber") String phoneNumber) {
        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            com.google.i18n.phonenumbers.Phonenumber.PhoneNumber parsedPhoneNumber = phoneNumberUtil.parse(phoneNumber, "IN");

            if (phoneNumberUtil.isValidNumber(parsedPhoneNumber)) {
                String formattedPhoneNumber = phoneNumberUtil.format(parsedPhoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                int otp = (int) (Math.random() * 900000) + 100000;
                String message = "Your OTP is: " + otp;
                otpMap.put(formattedPhoneNumber, otp);
                usersDAO.sendSms(formattedPhoneNumber, message);
                return true;
            } else {
                return false;
            }
        } catch (NumberParseException e) {
        	
            return false;
        }
    }
	
	@GetMapping("/verify-otp/{phonenumber}/{enteredotp}")
		    public boolean verifyOtp(@PathVariable("phonenumber") String phoneNumber, @PathVariable("enteredotp") String enteredOtp) {
		        try {
		            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
		            com.google.i18n.phonenumbers.Phonenumber.PhoneNumber parsedPhoneNumber = phoneNumberUtil.parse(phoneNumber, "IN");

		            if (phoneNumberUtil.isValidNumber(parsedPhoneNumber)) {
		                // Convert the phone number to E.164 format
		            	String formattedPhoneNumber = phoneNumberUtil.format(parsedPhoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);

		                // Check if the phone number exists in the OTP map
		                if (otpMap.containsKey(formattedPhoneNumber)) {
		                    int originalOtp = otpMap.get(formattedPhoneNumber);

		                    // Compare the entered OTP with the original OTP
		                    int enteredOtpInt;
		                    try {
		                        enteredOtpInt = Integer.parseInt(enteredOtp);
		                        
		                    } catch (NumberFormatException e) {
		                    	System.out.println("Invalid OTP format");
		                        return false;
		                        
		                    }

		                    if (originalOtp == enteredOtpInt) {
		                        // OTP is valid, remove it from the map
		                        otpMap.remove(formattedPhoneNumber);
		                        System.out.println("OTP is valid");
		                        return true;
		                        
		                    }
		                }
		            }
		            System.out.println("Invalid OTP or phone number");
		            return false;
		        } catch (NumberParseException e) {
		        	System.out.println("Invalid phone number");
		            return false;
		        }
		    }
}
