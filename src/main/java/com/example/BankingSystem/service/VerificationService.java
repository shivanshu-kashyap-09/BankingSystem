package com.example.BankingSystem.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.BankingSystem.entity.AdminEntity;
import com.example.BankingSystem.entity.CustomerEntity;
import com.example.BankingSystem.repository.AdminRepository;
import com.example.BankingSystem.repository.CustomerRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class VerificationService {

	@Autowired
    private AdminRepository adminRepository;
	
	@Autowired
	private CustomerRepository customerRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    private static final int OTP_VALIDATION = 5;

    private ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>(); 
    private ConcurrentHashMap<String, LocalDateTime> otpTimestampStorage = new ConcurrentHashMap<>(); 
    private ConcurrentHashMap<String, AdminEntity> userDataStorage = new ConcurrentHashMap<>(); 
    private ConcurrentHashMap<String, CustomerEntity> customerDataStorage = new ConcurrentHashMap<>();


    public void generateAndSendOTPEmail(String email) throws MessagingException {
        if (email != null) {
            String emailOtp = generateOtp();
            otpStorage.put(email, emailOtp);
            otpTimestampStorage.put(email, LocalDateTime.now());
            sendEmail(email, "OTP Verification Code", "Your Bank verification OTP is: " + emailOtp);
        }
    }
        public void generateAndSendOTPPhone(String phoneNumber){
        if (phoneNumber != null && phoneNumber.length() == 10) {
            String phoneOtp = generateOtp();
            otpStorage.put(phoneNumber, phoneOtp);
            otpTimestampStorage.put(phoneNumber, LocalDateTime.now());
            sendSms(phoneNumber, "Your phone OTP is: " + phoneOtp);
        }
    }

    public void storeUserData(AdminEntity adminEntity) {
        String email = adminEntity.getEmail(); 
        String phone = adminEntity.getPhoneNumber(); 
        if(email != null){
        	userDataStorage.put(email, adminEntity);
        }else if(phone != null) {
        	userDataStorage.put(phone, adminEntity);
        }
        
    }
    
    public void storeUserData(CustomerEntity customer) {
    	String email = customer.getEmail(); 
    	String phone = customer.getPhoneNumber(); 
    	if(email != null){
    		customerDataStorage.put(email, customer);
    	}else if(phone != null) {
    		customerDataStorage.put(phone, customer);
    	}
    	
    }

    public boolean verifyAndSave(String emailOrPhone, String otp) {
    	System.out.println("s" +emailOrPhone);
		System.out.println(otp);
        String storedOtp = otpStorage.get(emailOrPhone);
        System.out.println(storedOtp);
        LocalDateTime otpGeneratedTime = otpTimestampStorage.get(emailOrPhone);

        if (storedOtp != null && storedOtp.equals(otp) && otpGeneratedTime != null &&
                otpGeneratedTime.isAfter(LocalDateTime.now().minusMinutes(OTP_VALIDATION))) {
            AdminEntity adminEntity = userDataStorage.get(emailOrPhone);
            adminEntity.setVerified(true);
            CustomerEntity customerEntity = customerDataStorage.get(emailOrPhone);
            customerEntity.setVerified(true);
            if (adminEntity != null) {
                if (adminEntity.getRoles() == null) {
                    adminEntity.setRoles(Arrays.asList("EMPLOYEE","ADMIN"));
                }
                
                adminRepository.save(adminEntity);

                otpStorage.remove(emailOrPhone);
                otpTimestampStorage.remove(emailOrPhone);
                userDataStorage.remove(emailOrPhone);

                return true;
            }else if(customerEntity != null) {
            	customerRepository.save(customerEntity);
            }
        }
        return false;
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        javaMailSender.send(message);
    }

    private void sendSms(String toPhoneNumber, String messageBody) {
        Twilio.init(accountSid, authToken);

        Message message = Message.creator(
                new PhoneNumber(toPhoneNumber),
                new PhoneNumber(twilioPhoneNumber),
                messageBody
        ).create();

        System.out.println("SMS sent successfully! Message SID: " + message.getSid());
    }
    
    public boolean verifyForgetPassword(String emailorPhone , String otp) {
    	String storedOtp = otpStorage.get(emailorPhone);
        
        LocalDateTime otpGeneratedTime = otpTimestampStorage.get(emailorPhone);

        if (storedOtp != null && storedOtp.equals(otp) && otpGeneratedTime != null &&
                otpGeneratedTime.isAfter(LocalDateTime.now().minusMinutes(OTP_VALIDATION))) {
        	AdminEntity data = adminRepository.findByEmail(emailorPhone);
        	userDataStorage.put(emailorPhone, data);
        	return true;
        }
        return false;
    }
    
    public ResponseEntity<Object> forgetPassword(String emailorPhone , String newPassword , String confirmPassword) {
    	
    	if(newPassword.equals(confirmPassword)) {
    		AdminEntity data = userDataStorage.get(emailorPhone);
    		data.setPassword(newPassword);
    		adminRepository.save(data);
    		
    		userDataStorage.clear();
    		return new ResponseEntity<>(HttpStatus.OK);
    	}
    	
    	return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
}
