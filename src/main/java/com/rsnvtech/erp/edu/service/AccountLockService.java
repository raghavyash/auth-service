package com.rsnvtech.erp.edu.service;

import com.rsnvtech.erp.edu.entity.User;
import com.rsnvtech.erp.edu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountLockService {
    private final UserRepository userRepository;

    private static final int MAX_FAILED_ATTEMPTS = 5;

    private static final long LOCK_DURATION_MINUTES = 15;

    public void increaseFailedAttempts(User user) {

        int attempts = user.getFailedAttempts() + 1;

        user.setFailedAttempts(attempts);

        if (attempts >= MAX_FAILED_ATTEMPTS) {

            user.setAccountNonLocked(false);

            user.setLockTime(LocalDateTime.now());
        }

        userRepository.save(user);
    }

    public void resetFailedAttempts(User user) {

        user.setFailedAttempts(0);

        user.setAccountNonLocked(true);

        user.setLockTime(null);

        userRepository.save(user);
    }

    public boolean unlockWhenTimeExpired(User user) {

        if (user.getLockTime() == null) {
            return false;
        }

        LocalDateTime unlockTime =
                user.getLockTime()
                        .plusMinutes(LOCK_DURATION_MINUTES);

        if (LocalDateTime.now().isAfter(unlockTime)) {

            user.setAccountNonLocked(true);

            user.setFailedAttempts(0);

            user.setLockTime(null);

            userRepository.save(user);

            return true;
        }

        return false;
    }

}
