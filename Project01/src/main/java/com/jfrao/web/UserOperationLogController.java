package com.jfrao.web;


import com.jfrao.domain.Customer;
import com.jfrao.domain.User;
import com.jfrao.service.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;

@Controller
public class UserOperationLogController {
    @Autowired
    private UserLogService userLogService;

    @RequestMapping("/Log/{Operation}")
    public String Log(HttpServletRequest request, Model model, HttpSession session, @PathVariable("Operation") String Operation){
        User user = (User)session.getAttribute("user");
        String ip = getIP(request);
        //登录时进行记录
        if("Login".equals(Operation)){
            userLogService.LogLogin(user, ip, Operation);
            //判断用户角色
            if("admin".equals(user.getRole())){
                return "redirect:/admin";
            }else{
                return "hello";
            }
            //退出登录进行记录
        }else if("Exit".equals(Operation)){
            userLogService.LogLogin(user, ip, Operation);
            session.removeAttribute("user");
            return "redirect:/login";
            //创建业务时进行记录
        }else if("CreateBusiness".equals(Operation)){
            Customer customer = (Customer) request.getAttribute("customer");
            userLogService.LogCreateBusiness(user, ip, customer.getNum());
            model.addAttribute("wait_customer",customer);
            return "business";
        }else {
            return null;
        }
    }

    private String getIP(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length () == 0 || "unknown".equalsIgnoreCase (ip)) {
            ip = request.getHeader ("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length () == 0 || "unknown".equalsIgnoreCase (ip)) {
            ip = request.getRemoteAddr ();
            if (ip.equals ("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost ();
                } catch (Exception e) {
                    e.printStackTrace ();
                }
                ip = inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length () > 15) {
            if (ip.indexOf (",") > 0) {
                ip = ip.substring (0, ip.indexOf (","));
            }
        }
        return ip;
    }
}
