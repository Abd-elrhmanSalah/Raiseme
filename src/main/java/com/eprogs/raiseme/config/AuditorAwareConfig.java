package com.eprogs.raiseme.config;

import com.eprogs.raiseme.dto.authDTOS.SystemUserDetails;
import com.eprogs.raiseme.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Slf4j
public class AuditorAwareConfig implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        log.info("getCurrentAuditor called");
        Long userId = null;
        try {
            SystemUserDetails principal = Utils.getCurrentUser();


            userId = principal.getUserId();
            log.info("userId: " + userId);
        } catch (Exception e) {
            log.error("Error fetching user ID: ", e);
        }
        return Optional.ofNullable(userId);
    }


}
