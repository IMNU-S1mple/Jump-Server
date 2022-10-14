package imnu.jumpserver.controller;

import imnu.jumpserver.model.Host;
import imnu.jumpserver.repository.HostRepository;
import imnu.jumpserver.service.OperationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("host")
public class HostController {


    // 标记了@Resource的属性，Springboot会自动注入
    @Resource
    HostRepository hostRepository;

    @Resource
    OperationService operationService;

    @RequestMapping("list")
    public String list(ModelMap map) {
        List<Host> allHosts = this.hostRepository.findAll();
        map.put("allHosts", allHosts);
        return "host/list";
    }

    @GetMapping("save")
    public String saveForm(ModelMap map, @RequestParam(value = "id", required=false) Long id) {
        Host host = id != null ? this.hostRepository.findById(id).get() : new Host();

        map.put("host", host);
        return "host/save";
    }

    // Springboot会将表单中的参数自动组装成Host对象
    @PostMapping("save")
    public String save(Host host) {
        if (host.getId() != null) {
            // 更新的逻辑：先从数据库中查找到已保持的对象，再更加表单提交的值，去更新此对象，最后保存
            Host savedHost = this.hostRepository.findById(host.getId()).get();

            savedHost.setHostName(host.getHostName());
            savedHost.setIp(host.getIp());
            savedHost.setUserName(host.getUserName());
            this.hostRepository.save(savedHost);
            this.operationService.record("host", "更新主机: " + savedHost.getId());

        } else {
            Host savedHost = this.hostRepository.save(host);
            this.operationService.record("host", "新建主机: " + savedHost.getId());

        }
        return "redirect:/host/list";
    }

    @GetMapping("del")
    public String del(Long id) {
        this.hostRepository.deleteById(id);
        this.operationService.record("host", "刪除主机: " + id);

        return "redirect:/host/list";
    }

}
