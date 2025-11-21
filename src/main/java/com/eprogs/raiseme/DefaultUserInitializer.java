package com.eprogs.raiseme;

import com.eprogs.raiseme.entity.SystemUser;
import com.eprogs.raiseme.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DefaultUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {

        if (userRepository.count() == 0) {

            SystemUser defaultUser = SystemUser.builder()
                    .email("admin@admin.com")
                    .firstName("Abdelrhman ")
                    .lastName("Salah")
                    .phoneNumber("01154633193")
                    .password("$2a$10$yO6mtgTg2EI5mKOt1i7ydeQjsTPqEyK2AOXhvFBT5wu7W1vsxyN0e")
                    .accountSuspended(false)
                    .failedLoginAttempts(0)
                    .isFirstLogin(false)
                    .createdDate(new java.util.Date())
                    .isLocked(Boolean.FALSE)
                    .build();

            userRepository.save(defaultUser);

            System.out.println("Default admin user created: admin@admin.com/admin");

        } else {
            System.out.println("Users already exist, skipping default user creation.");
        }
    }
}
