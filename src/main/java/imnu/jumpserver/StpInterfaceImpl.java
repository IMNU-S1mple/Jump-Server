package imnu.jumpserver;

import cn.dev33.satoken.stp.StpInterface;
import imnu.jumpserver.model.User;
import imnu.jumpserver.repository.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    UserRepository userRepository;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询权限
        List<String> list = new ArrayList<String>();
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        User user =this.userRepository.findById(Long.valueOf(loginId.toString())).get();
        List<String> list = new ArrayList<String>();
        list.add(user.getRole().getName());
        return list;
    }

}