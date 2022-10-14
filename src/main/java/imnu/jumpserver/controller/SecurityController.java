package imnu.jumpserver.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import imnu.jumpserver.model.User;
import imnu.jumpserver.repository.UserRepository;
import imnu.jumpserver.service.OperationService;
import imnu.jumpserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("auth")
public class SecurityController {
    //声明一个Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityController.class);

    @Resource
    Environment environment;

    @Resource
    UserService userService;

    @Resource
    OperationService operationService;

    @GetMapping("login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            ModelMap map) {
        if (error != null) {
            map.put("error", "用户名与密码不匹配！");
        }

        map.put("isDev", this.isDev());

        return "login";
    }

    @PostMapping("login")
    public String login(@RequestParam("name") String name, @RequestParam("password") String password) {
        // 处理用户登录请求
        User user = userService.checkPassword(name, password);
        if (user == null) {

            return "redirect:/auth/login?error=1";
        }
        // 用户输入的用户名与密码匹配，登录成功
        StpUtil.login(user.getId());
        this.operationService.record("security", "用户登录: " + user.getName());
        return "redirect:/";
    }

    @GetMapping("changePassword")
    public String changePasswordPage() {
        return "changePassword";
    }

    @PostMapping("changePassword")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword1") String newPassword1,
                                 @RequestParam("newPassword2") String newPassword2) {
       this.userService.changeCurrentUserPassword(currentPassword, newPassword1, newPassword2);
       if (StpUtil.isLogin()) {
           return "redirect:/auth/changePassword";
       } else {
           return "redirect:/auth/login";
       }
    }

    @GetMapping("logout")
    public String logout() {
        StpUtil.logout();
        return "redirect:/auth/login";
    }

    @GetMapping("autologin")
    public String autologin() {
        if (this.isDev()) {
            StpUtil.login(15);
        }
        return "redirect:/";
    }

    private boolean isDev() {
        return environment.getActiveProfiles().length > 0 && environment.getActiveProfiles()[0].equals("dev");
    }

}
