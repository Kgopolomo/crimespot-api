package za.co.crimestopsa.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import za.co.crimestopsa.api.entity.UserDevice;
import za.co.crimestopsa.api.entity.UserProfile;

import java.util.List;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long>, JpaSpecificationExecutor<UserDevice> {

    List<UserDevice> findByUserProfile(UserProfile userProfile);
}
