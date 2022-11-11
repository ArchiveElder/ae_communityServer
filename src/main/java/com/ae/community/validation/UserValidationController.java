package com.ae.community.validation;

import com.ae.community.domain.CommunityUser;
import com.ae.community.exception.chaebbiException;
import com.ae.community.service.CommunityUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Optional;

import static com.ae.community.exception.CodeAndMessage.*;

@Controller
@RequiredArgsConstructor
public class UserValidationController {
    private final CommunityUserService communityUserService;
    public CommunityUser validateUser(Long userIdx) {
        Optional<CommunityUser> user = communityUserService.findByUserIdx(userIdx);
        if (user == Optional.<CommunityUser>empty()) throw new chaebbiException(EMPTY_USER);
        else return user.get();

    }

    public void validateUserByJwt(String jwtUserId) {
        if(jwtUserId.equals("INVALID JWT")) throw new chaebbiException(INVALID_JWT);
        if(jwtUserId.equals("anonymousUser")) throw new chaebbiException(EMPTY_JWT);
    }

    public void compareUserIdAndJwt(Long userIdx, String jwtUserId) {
        if(!jwtUserId.equals(userIdx.toString())) throw new chaebbiException(NOT_CORRECT_JWT_AND_PATH_VARIABLE);
    }

    public CommunityUser validateUserByUserIdxAndJwt(Long userIdx, String jwtUserId) {
        if(jwtUserId.equals("INVALID JWT")) throw new chaebbiException(INVALID_JWT);
        if(jwtUserId.equals("anonymousUser")) throw new chaebbiException(EMPTY_JWT);
        if(!jwtUserId.equals(userIdx.toString())) throw new chaebbiException(NOT_CORRECT_JWT_AND_PATH_VARIABLE);
        Optional<CommunityUser> user = communityUserService.findByUserIdx(userIdx);
        if (user == Optional.<CommunityUser>empty()) throw new chaebbiException(EMPTY_USER);
        else return user.get();
    }
}
