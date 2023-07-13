package com.tai.paysafe.service;

import com.tai.paysafe.dto.response.RegistrationRequest;
import com.tai.paysafe.entities.User;
import com.tai.paysafe.entities.UserBank;

import java.util.List;

public interface UserService {
      User create(RegistrationRequest request, String role);
      void deleteUserBank (Long userId) throws Exception;

      User findById(Long id);
      List<UserBank> getUserBanks(Long userId);
}
