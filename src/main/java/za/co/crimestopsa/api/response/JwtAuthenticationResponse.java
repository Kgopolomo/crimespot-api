package za.co.crimestopsa.api.response;


import org.springframework.security.core.userdetails.UserDetails;
import za.co.crimestopsa.api.dto.UserProfileDto;


public class JwtAuthenticationResponse {

    private String accessToken;
    private String tokenType;
    private UserProfileDto userDetails;

    public JwtAuthenticationResponse() {
    }

    public JwtAuthenticationResponse(String accessToken, String tokenType, UserProfileDto userDetails) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.userDetails = userDetails;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public UserProfileDto getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserProfileDto userDetails) {
        this.userDetails = userDetails;
    }
}
