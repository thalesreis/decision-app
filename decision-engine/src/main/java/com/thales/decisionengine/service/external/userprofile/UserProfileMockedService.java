package com.thales.decisionengine.service.external.userprofile;

import com.thales.decisionengine.enums.Segment;
import com.thales.decisionengine.model.response.external.userprofile.UserProfileResponse;
import org.springframework.stereotype.Service;

/**
 * The UserProfileMockedService class mocked class is to simulate an external service responsible to
 * provide the user's segment
 */
@Service
public class UserProfileMockedService {
  /**
   * Return an UserProfileResponse based on user personalCode. The result is based on the last digit
   * of personalCode: <br>
   * 6 -> segment 1, hasDebt = false <br>
   * 7 -> segment 2, hasDebt = false <br>
   * 8 -> segment 3, hasDebt = false <br>
   * N != 6, 7, 8 -> no segment, hasDebt = true
   *
   * @param personalCode the user personal code
   * @return UserProfileResponse instance with Segment - ENUM, creditModifier and if it has debts or
   *     not
   */
  public UserProfileResponse retrieveUserProfileByPersonalCode(String personalCode) {

    final var lastDigit = personalCode.charAt(personalCode.length() - 1);

    return switch (lastDigit) {
      default -> new UserProfileResponse(Segment.UNDEFINED, 0, true);

      case '6' -> new UserProfileResponse(Segment.SEGMENT_1, 100, false);
      case '7' -> new UserProfileResponse(Segment.SEGMENT_2, 300, false);
      case '8' -> new UserProfileResponse(Segment.SEGMENT_3, 1000, false);
    };
  }
}
