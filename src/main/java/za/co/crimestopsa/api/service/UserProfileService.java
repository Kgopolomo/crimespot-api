package za.co.crimestopsa.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import za.co.crimestopsa.api.dto.UserProfileDto;
import za.co.crimestopsa.api.entity.UserDevice;
import za.co.crimestopsa.api.entity.UserProfile;
import za.co.crimestopsa.api.repository.UserDeviceRepository;
import za.co.crimestopsa.api.repository.UserProfileRepository;
import za.co.crimestopsa.api.request.LoginRequest;
import za.co.crimestopsa.api.response.JwtAuthenticationResponse;
import za.co.crimestopsa.api.util.JWTUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService implements UserDetailsService {
    @Autowired private UserProfileRepository userProfileRepository;

    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired private UserDeviceRepository userDeviceRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    public UserProfile updateProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public UserProfile getProfile(Long userId) {
        Optional<UserProfile> userProfile = userProfileRepository.findById(userId);
        return userProfile.orElse(null);
    }

    public List<UserProfile> getAllProfiles() {
        return userProfileRepository.findAll();
    }

    public void deleteProfile(Long userId) {
        userProfileRepository.deleteById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserProfile userProfile = userProfileRepository.findByUsername(username);
        if (userProfile == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(userProfile.getUsername(), userProfile.getPassword(),
                new ArrayList<>());
    }

    public UserProfile createProfile(UserProfile userProfile) {
        userProfile.setPassword(bCryptPasswordEncoder.encode(userProfile.getPassword()));
        for (UserDevice device : userProfile.getDevices()) {
            device.setUserProfile(userProfile);
        }
        return userProfileRepository.save(userProfile);
    }


    public JwtAuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = loadUserByUsername(loginRequest.getUsername());
        UserProfile userProfile = userProfileRepository.findByUsername(userDetails.getUsername());
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setUsername(userProfile.getUsername());
        userProfileDto.setEmail(userProfile.getEmail());
        userProfileDto.setFirstName(userProfile.getFirstName());
        userProfileDto.setLastName(userProfile.getLastName());

        String jwt = jwtUtil.generateToken(userDetails);
        JwtAuthenticationResponse token = new JwtAuthenticationResponse(jwt, "Bearer", userProfileDto);
        return token;
    }

    public UserProfile getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserProfile profile = userProfileRepository.findByUsername(userDetails.getUsername());
        return profile;
    }
}
