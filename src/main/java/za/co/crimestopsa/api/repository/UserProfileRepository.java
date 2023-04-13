package za.co.crimestopsa.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import za.co.crimestopsa.api.entity.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long>, JpaSpecificationExecutor<UserProfile> {
    UserProfile findByEmail(String email);

    UserProfile findByUsername(String username);
}
