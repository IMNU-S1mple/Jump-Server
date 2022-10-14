package imnu.jumpserver.controller;

import cn.dev33.satoken.stp.StpUtil;
import imnu.jumpserver.model.User;
import imnu.jumpserver.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Map;

@Controller
public class HomeController {

    @RequestMapping(value = {"/","index"})
    public String index() {
        return "index";
    }

}
