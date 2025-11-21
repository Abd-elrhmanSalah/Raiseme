package com.eprogs.raiseme.service;

import com.eprogs.raiseme.dto.ErrorResponseDTO;
import com.eprogs.raiseme.dto.authDTOS.SystemUserDetails;
import com.eprogs.raiseme.entity.SystemUser;
import com.eprogs.raiseme.exception.ApplicationException;
import com.eprogs.raiseme.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.eprogs.raiseme.constant.ErrorMessageEnum.ERROR_EMAIL_ALREADY_EXIST;
import static com.eprogs.raiseme.constant.ErrorMessageEnum.ERROR_ITEM_NOT_FOUND;
import static com.eprogs.raiseme.constant.ErrorMessageEnum.ERROR_USER_LOCKED;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        username = username.toLowerCase();

        SystemUser systemUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (systemUser.getIsLocked())
            throw new ApplicationException(new ErrorResponseDTO(HttpStatus.BAD_REQUEST,
                    ERROR_USER_LOCKED.getMessageEN(), ERROR_USER_LOCKED.getMessageAR(), LocalDateTime.now()));

//        if (systemUser.isFirstLogin())
//            throw new ApplicationException(new ErrorResponseDTO(HttpStatus.BAD_REQUEST,
//                    ERROR_USER_NOT_ACTIVE.getMessageEN(), ERROR_USER_NOT_ACTIVE.getMessageAR(), LocalDateTime.now()));

        return new SystemUserDetails(systemUser);
    }

    public SystemUser saveUser(SystemUser systemUser) {
        return userRepository.save(systemUser);
    }

    public void updateUserEmail(String oldEmail, String newEmail) {
        oldEmail = oldEmail.trim().toLowerCase();
        newEmail = newEmail.trim().toLowerCase();

        if (validateExistingUserEmailWithoutException(newEmail))
            return;

        SystemUser user = findByEmail(oldEmail);
        if (user != null) {
            user.setEmail(newEmail);
            userRepository.save(user);
        }
    }

    public SystemUser findByEmail(String email) {
        email = email.toLowerCase();
        Optional<SystemUser> user = userRepository.findByEmail(email);
        if (!user.isPresent())
            throw new ApplicationException(
                    new ErrorResponseDTO(HttpStatus.BAD_REQUEST, ERROR_ITEM_NOT_FOUND.getMessageEN(), LocalDateTime.now()));

        return user.get();
    }

    public Optional<SystemUser> findByEmailWithoutException(String email) {
        return userRepository.findByEmail(email);
    }

    public SystemUser findByToken(String token) {
        Optional<SystemUser> user = userRepository.findByToken(token);
        if (!user.isPresent())
            throw new ApplicationException(
                    new ErrorResponseDTO(HttpStatus.BAD_REQUEST, ERROR_ITEM_NOT_FOUND.getMessageEN(), LocalDateTime.now()));
        return user.get();
    }

    public void lockUser(String email, Boolean isLocked) {
        email = email.toLowerCase();
        SystemUser user = findByEmail(email);
        user.setIsLocked(isLocked);
        userRepository.save(user);
    }

    public void validateExistingUserEmail(String userEmail) {
        userEmail = userEmail.toLowerCase();
        if (userRepository.existsByEmail(userEmail))
            throw new ApplicationException(
                    new ErrorResponseDTO(HttpStatus.BAD_REQUEST, ERROR_EMAIL_ALREADY_EXIST.getMessageEN(), LocalDateTime.now()));

    }

    public Boolean validateExistingUserEmailWithoutException(String userEmail) {
        userEmail = userEmail.toLowerCase();
        return userRepository.existsByEmail(userEmail);
    }

    public SystemUser validateExistingUserById(Long userId) {
        Optional<SystemUser> user = userRepository.findById(userId);
        if (!user.isPresent())
            throw new ApplicationException(
                    new ErrorResponseDTO(HttpStatus.BAD_REQUEST, ERROR_ITEM_NOT_FOUND.getMessageEN(), LocalDateTime.now()));

        return user.get();
    }

    public void loginAtAndAddToken(String email, String token) {
        email = email.toLowerCase();
        SystemUser user = findByEmail(email);
        user.setToken(token);
        userRepository.save(user);
    }

    public void loginOutAndRemoveToken(String email) {
        email = email.toLowerCase();
        SystemUser user = findByEmail(email);
        user.setToken(null);
        userRepository.save(user);
    }

    public void deleteUserByEmail(String email) {
        email = email.toLowerCase();
        SystemUser user = findByEmail(email);
        userRepository.save(user);

    }

}
