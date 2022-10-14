package imnu.jumpserver.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import imnu.jumpserver.model.Host;
import imnu.jumpserver.model.Role;
import imnu.jumpserver.model.User;
import imnu.jumpserver.repository.RoleRepository;
import imnu.jumpserver.repository.UserRepository;
import imnu.jumpserver.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("user")
public class UserController {

    public static final String DEFAULT_PASSWORD = "123456";

    @Resource
    RoleRepository roleRepository;

    @Resource
    UserRepository userRepository;

    @Resource
    UserService userService;

    @Value("${upload.dir}")
    private String uploadDir;

    @RequestMapping("list")
    public String list(ModelMap map) {
        map.put("allUsers", userRepository.findAll());
        return "user/list";
    }

    @GetMapping("save")
    public String saveForm(ModelMap map, @RequestParam(value = "id", required=false) Long id) {
        User user = id != null ? this.userRepository.findById(id).get() : new User();
        map.put("user", user);
        return "user/save";
    }

    @PostMapping("save")
    public String save(User user, @RequestParam("roleId") Long roleId) {
        User savedUser = null;
        if (user.getId() != null) {
            // 更新的逻辑：先从数据库中查找到已保持的对象，再更加表单提交的值，去更新此对象，最后保存
            savedUser = this.userRepository.findById(user.getId()).get();
        } else {
            // 新建用户
            savedUser = new User();
            // 为新用户设置默认密码
            savedUser.setPassword(this.getDefaultHashedPassword());
        }

        savedUser.setName(user.getName());

        Role role = this.roleRepository.findById(roleId).get();
        savedUser.setRole(role);

        this.userRepository.save(savedUser);

        return "redirect:/user/list";
    }

    @GetMapping("del")
    public String del(Long id) {
        this.userRepository.deleteById(id);
        return "redirect:/user/list";
    }

    @GetMapping("resetPassword")
    public String resetPassword(Long id) {
        User savedUser = this.userRepository.findById(id).get();
        savedUser.setPassword(this.getDefaultHashedPassword());
        this.userRepository.save(savedUser);
        return "redirect:/user/list";
    }

    @GetMapping("changeProfile")
    public String changeProfileForm(ModelMap map) {
        User user = this.userService.getCurrentUser();
        map.put("user", user);
        return "user/changeProfile";
    }

    @PostMapping("changeProfile")
    public String changeProfile(User newUserInfo, @RequestPart("avatarFile") MultipartFile avatarFile) throws IOException {
        User currentUser = this.userService.getCurrentUser();
        currentUser.setFullName(newUserInfo.getFullName());
        currentUser.setEmail(newUserInfo.getEmail());
        currentUser.setGender(newUserInfo.isGender());

        avatarFile.transferTo(this.getUserAvatarFile());

        this.userService.save(currentUser);

        return "redirect:/user/changeProfile";
    }

    // 读取用户头像文件，并返回给客户端浏览器
    @GetMapping("avatar.png")
    public void avatarFile(HttpServletResponse response) throws IOException {
        File file = this.getUserAvatarFile();
        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        FileInputStream fin = new FileInputStream(file);
        StreamUtils.copy(fin, response.getOutputStream());
        fin.close();
        response.getOutputStream().flush();
    }

    private String getDefaultHashedPassword() {
        return BCrypt.withDefaults().hashToString(12, DEFAULT_PASSWORD.toCharArray());
    }

    private File getUserAvatarFile() {
        return new File(this.uploadDir, StpUtil.getLoginIdAsString()+".a.png");
    }

}
