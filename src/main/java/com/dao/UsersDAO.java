package com.dao;


import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;


import com.model.Users;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


@Service
public class UsersDAO {
	
	@Autowired
	UserRepository usersRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private JavaMailSender  mailSender;
	
	 private static final String ACCOUNT_SID = "AC4a5b3f7ab19a9bc62eb8cab7486a1c71";
	    private static final String AUTH_TOKEN ="6df62587966254e159b35071add6f1d8";
	    private static final String TWILIO_PHONE_NUMBER = "+12028732872";
	
		
	
	public Users userLogin(String emailId, String password) {
        Users user = usersRepo.findByEmailId(emailId);

        if (user != null) {
            if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                return user; 
                
            }
        }

        return null; // User not found or passwords do not match
    }

	public Users registerUser(Users user) {	
		//password encryption
		String plainTextPassword = user.getPassword();
		String hashedPassword = bCryptPasswordEncoder.encode(plainTextPassword);
		user.setPassword(hashedPassword);
		
		//
		SimpleMailMessage message=new SimpleMailMessage();
		message.setFrom("journeyglimpses2@gmail.com");//sendermail
		message.setTo(user.getEmailId());//reciever mail
	
		message.setText("Dear "+user.getName()+",\n\n\nWe are thrilled to welcome you to JourneyGlimpses! Thank you for choosing to be a part of our community.\nHere at JourneyGlimpses, we're committed to providing you with an exceptional online experience. As a registered member, you now have access to a wide range of exciting features and benefits, including:\n\n1. Personalized Account: You can customize your profile, update your preferences, and enjoy a tailored experience.\n\n2. Exclusive Content: Stay updated with the latest news, articles, products, or services related to your interests.\n\n3. Community Engagement: Join discussions, connect with like-minded individuals, and share your thoughts with our growing community.\n\n4. Member-Only Discounts: As a registered member, you'll have access to exclusive promotions and discounts on our products/services.\n\nHere are a few tips to make the most of your membership:\n\n- Complete your profile: Let others get to know you by adding a profile picture and some information about yourself.\n\n- Explore our website: Browse our content, products, and services to discover what interests you the most.\n\n- Join the conversation: Participate in discussions, leave comments, and engage with our community.\n\n\nIf you have any questions or need assistance with anything related to JourneyGlimpses , our support team is here to help. Feel free to reply to this email or reach out to our support team at journeyglimpses2@gmail.com\n\nWe're excited to have you as part of our community, and we look forward to serving your needs and interests. Thank you for choosing JourneyGlimpses.\n\n\nBest regards,\nJourneyGlimpses Team");
		message.setSubject("Welcome to JourneyGlimpses");
		mailSender.send(message);
		System.out.println("Mail sent");
		return usersRepo.save(user);
	}

	public Users updateEmp(Users updatedUser) {
	    // Check if the user with the given ID exists
	    Optional<Users> existingUserOptional = usersRepo.findById(updatedUser.getId());

	    if (existingUserOptional.isPresent()) {
	        Users existingUser = existingUserOptional.get();
	        
	        // Update only the fields you want to allow being updated
	        existingUser.setName(updatedUser.getName());
	        existingUser.setCountry(updatedUser.getCountry());
	        existingUser.setGender(updatedUser.getGender());
	        existingUser.setPhonenumber(updatedUser.getPhonenumber());
	        
	        // Save the updated user
	        return usersRepo.save(existingUser);
	    } else {
	        // Handle the case where the user with the given ID is not found
	    	return usersRepo.save(updatedUser);
	    	//throw new EntityNotFoundException("User with ID " + updatedUser.getId() + " not found");
	    }
	}


	
	

	  public void sendSms(String toPhoneNumber, String messageBody) {
	        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	        Message message = Message.creator(
	                new PhoneNumber(toPhoneNumber),  // Recipient's phone number
	                new PhoneNumber(TWILIO_PHONE_NUMBER),  // Your Twilio phone number
	                messageBody)
	                .create();
	        System.out.println("SMS sent with SID: " + message.getSid());
	    }

	  public void deleteUserById(int id) {
		    try {
		        usersRepo.deleteById(id);
		        System.out.println("User with ID " + id + " deleted successfully");
		    } catch (Exception e) {
		        e.printStackTrace();
		        System.err.println("Error deleting user with ID " + id);
		    }
		}



}
