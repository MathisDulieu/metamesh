package com.example.metamesh.service;

import com.example.metamesh.dao.UserDao;
import com.example.metamesh.model.User;
import com.example.metamesh.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final JwtTokenService jwtToken;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    public ResponseEntity<Void> register(User user) {
        if(isNull(user.getEmail()) || !isValidEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(isNull(user.getUsername()) ||user.getUsername().length() < 3 || userDao.isUserInDatabase(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(isNull(user.getPassword()) ||!isStrongPassword(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        user.setSubscribers(emptyList());
        user.setSubscriptions(emptyList());
        user.setPrivate(true);
        user.setAdmin(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(UUID.randomUUID().toString());
        userDao.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public String login(LoginRequest login) {
        User user = userDao.findByEmail(login.getEmail());

        if (user == null || !passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return jwtToken.generateToken(user.getId());
    }

    public ResponseEntity<Void> setPrivateAccount(String userId, boolean isPrivate) {
        User tokenPeople = jwtToken.resolveTokenFromRequest();
        if(tokenPeople == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(!tokenPeople.isAdmin()) {
            if(!tokenPeople.getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }

        if(!tokenPeople.isPrivate() == isPrivate) {
            tokenPeople.setPrivate(isPrivate);
            userDao.save(tokenPeople);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<Void> subscribeToUser(String userId) {
        User tokenPeople = jwtToken.resolveTokenFromRequest();
        if (tokenPeople == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User targetUser = userDao.findById(userId);
        if (isNull(targetUser)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (Objects.equals(tokenPeople.getId(), userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (tokenPeople.getSubscriptions().contains(targetUser.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        tokenPeople.getSubscriptions().add(Map.of(targetUser.getId(), targetUser.getUsername()));
        userDao.save(tokenPeople);

        targetUser.getSubscribers().add(Map.of(tokenPeople.getId(), tokenPeople.getUsername()));
        userDao.save(targetUser);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<Void> unsubscribeFromUser(String userId) {
        User tokenPeople = jwtToken.resolveTokenFromRequest();
        if (tokenPeople == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User targetUser = userDao.findById(userId);
        if (isNull(targetUser)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (Objects.equals(tokenPeople.getId(), userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (!tokenPeople.getSubscriptions().contains(Map.of(targetUser.getId(), targetUser.getUsername()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        tokenPeople.getSubscriptions().remove(Map.of(targetUser.getId(), targetUser.getUsername()));
        userDao.save(tokenPeople);

        targetUser.getSubscribers().remove(Map.of(tokenPeople.getId(), tokenPeople.getUsername()));
        userDao.save(targetUser);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public User getUserInformations() {
        User tokenPeople = jwtToken.resolveTokenFromRequest();
        if (tokenPeople == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return tokenPeople;
    }

    private static boolean isValidEmail(String email) {
        return email != null && Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    private boolean isStrongPassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }
}
