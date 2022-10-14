package imnu.jumpserver.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import imnu.jumpserver.model.User;
import imnu.jumpserver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    UserRepository userRepository;

    @Resource
    OperationService operationService;

    /**
     * 判断用户是否提供了正确的密码
     * @param username 登录用户名
     * @param submittedPassword 用户提交的密码
     * @return 如果密码匹配，返回user对象；否则，返回null
     */
    public User checkPassword(String username, String submittedPassword) {
        User user = userRepository.findByName(username);
        if (user == null) {
            return null;
        }
        BCrypt.Result result = this.verifyBCrptPassword(submittedPassword, user.getPassword());
        return result.verified ? user : null;
    }

    public void changeCurrentUserPassword(String currentPassword, String newPassword1, String newPassword2) {
        User user = this.getCurrentUser();
        BCrypt.Result result = this.verifyBCrptPassword(currentPassword, user.getPassword());
        if (!result.verified) {
            StpUtil.logout();
            return;
        }

        if (!newPassword1.equals(newPassword2)) {
            return;
        }

        String hashedPassword = BCrypt.withDefaults().hashToString(12, newPassword1.toCharArray());
        user.setPassword(hashedPassword);
        this.userRepository.save(user);
        this.operationService.record("security", "修改本人密码: " + user.getId());
        StpUtil.logout();
    }

    private BCrypt.Result verifyBCrptPassword(String currentPassword, String submitedPassword) {
        return BCrypt.verifyer().verify(currentPassword.toCharArray(), submitedPassword);
    }

    public User getCurrentUser() {
        Long userId = StpUtil.getLoginIdAsLong();
        return this.userRepository.findById(userId).get();
    }

    public User save(User currentUser) {
        return this.userRepository.save(currentUser);
    }
}
