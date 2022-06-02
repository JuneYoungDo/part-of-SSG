package com.promotion.ssg_assignment1.user;

import com.promotion.ssg_assignment1.Config.BaseException;
import com.promotion.ssg_assignment1.Config.BaseResponseStatus;
import com.promotion.ssg_assignment1.user.Dto.CreateUserReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    public void createUser(String name, String type) {
        User user = User.builder()
                .name(name)
                .type(type)
                .deleted(false)
                .build();
        save(user);
    }

    public void newUser(CreateUserReq createUserReq) throws BaseException {
        if(createUserReq.getName().length() == 0 || createUserReq.getName().equals(" "))
            throw new BaseException(BaseResponseStatus.EMPTY_NAME);
        String type = "일반";
        if(createUserReq.getType().equals("기업회원")) type = "기업회원";
        createUser(createUserReq.getName(), type);
    }
}
