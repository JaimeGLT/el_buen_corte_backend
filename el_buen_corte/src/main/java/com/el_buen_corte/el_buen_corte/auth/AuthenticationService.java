package com.el_buen_corte.el_buen_corte.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.el_buen_corte.el_buen_corte.config.JwtService;
import com.el_buen_corte.el_buen_corte.user.Role;
import com.el_buen_corte.el_buen_corte.user.User;
import com.el_buen_corte.el_buen_corte.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .hairdresserRole(request.getHairdresserRole())
            .email(request.getEmail())
            .phoneNumber(request.getPhoneNumber())
            .workingHoursStart(request.getWorkingHoursStart())
            .workingHoursFinish(request.getWorkingHoursFinish())
            .specialties(request.getSpecialties())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.ESTILISTA)
            .build();

        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );
        var user = repository.findByEmail(request.getEmail())
                    .orElseThrow();
                    
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }
    
}
