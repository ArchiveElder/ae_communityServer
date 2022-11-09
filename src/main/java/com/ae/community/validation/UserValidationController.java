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
    public CommunityUser validateuser(Long userIdx) {
        Optional<CommunityUser> user = communityUserService.findByUserIdx(userIdx);
        if (user == Optional.<CommunityUser>empty()) throw new chaebbiException(EMPTY_USER);
        else return user.get();

    }
}
