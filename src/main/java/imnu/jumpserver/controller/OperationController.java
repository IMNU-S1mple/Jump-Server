package imnu.jumpserver.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import imnu.jumpserver.model.Operation;
import imnu.jumpserver.model.User;
import imnu.jumpserver.repository.OperationRepository;
import imnu.jumpserver.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("operation")
public class OperationController {

    @Resource
    OperationRepository operationRepository;

    @RequestMapping("list")
    @SaCheckRole("admin")
    public String list(HttpServletResponse response) {
        return "operation/list";
    }

    @RequestMapping("list.json")
    @ResponseBody
    @SaCheckRole("admin")
    /**
     * page: 页号
     * limit: 每一页最多放多少个元素
     */
    public Object listJson(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        List<Operation> ops = operationRepository.findAllByOrderByTimeDesc(pageable);
        long count = operationRepository.count();

        Map<String, Object> result = new HashMap<>();
        result.put("data", ops);
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", count);

        return result;
    }

}
