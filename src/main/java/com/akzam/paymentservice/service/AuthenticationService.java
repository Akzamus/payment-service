package com.akzam.paymentservice.service;

import com.akzam.paymentservice.DTO.auth.AuthenticationRequest;
import com.akzam.paymentservice.DTO.auth.AuthenticationResponse;
import com.akzam.paymentservice.DTO.auth.RegisterRequest;
import com.akzam.paymentservice.exception.InvalidTokenException;
import com.akzam.paymentservice.exception.user.UserAlreadyExistsException;
import com.akzam.paymentservice.exception.user.UserNotFoundException;
import com.akzam.paymentservice.jwt.JwtFactory;
import com.akzam.paymentservice.jwt.JwtParser;
import com.akzam.paymentservice.jwt.JwtValidator;
import com.akzam.paymentservice.model.Role;
import com.akzam.paymentservice.model.User;
import com.akzam.paymentservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtFactory jwtFactory;
    private final JwtValidator jwtValidator;
    private final JwtParser jwtParser;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(
            UserRepository userRepository,
            JwtFactory jwtFactory,
            JwtValidator jwtValidator,
            JwtParser jwtParser,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.jwtFactory = jwtFactory;
        this.jwtValidator = jwtValidator;
        this.jwtParser = jwtParser;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        userRepository.findByUsername(request.username())
                .ifPresent(foundUser -> {
                    throw new UserAlreadyExistsException(
                            "User with the username " + foundUser.getUsername() + " already exists."
                    );
                });

        User user = User.builder()
                .fullName(request.fullName())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String accessToken = jwtFactory.generateAccessToken(user);
        String refreshToken = jwtFactory.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );


        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UserNotFoundException("User with this username was not found"));


        String accessToken = jwtFactory.generateAccessToken(user);
        String refreshToken = jwtFactory.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse refreshToken(String authHeader) {
        String refreshToken = authHeader.substring(7);

        String username = jwtParser.extractUsername(refreshToken)
                .orElseThrow(() -> new InvalidTokenException("Username cannot be null"));


        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with this username was not found"));

        if (!jwtValidator.isTokenValid(refreshToken, user)) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        return AuthenticationResponse.builder()
                        .accessToken(jwtFactory.generateAccessToken(user))
                        .refreshToken(refreshToken)
                        .build();
    }
}
