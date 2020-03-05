package com.jfrao.web;

import com.jfrao.domain.User;
import com.jfrao.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    //登录
    @PostMapping("/userlogin")
    public String login(HttpServletRequest request, Model model, HttpSession session){
        System.out.println("----web");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = loginService.login(username, password);
        //登陆失败
        if(user == null){
            model.addAttribute("msg","用户名或密码错误");
            return "login";
        }else{
            //登陆成功，判断用户是否停用
            if(user.getStatus() == 0){
                model.addAttribute("msg","对不起，用户已停用");
                return "login";
            }
            session.setAttribute("user",user);
            //判断用户角色
            if("admin".equals(user.getRole())){
                return "redirect:/admin";
            }else{
                return "hello";
            }
        }
    }

    //退出
    @RequestMapping("/quit")
    public String quit(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/login";
    }

}
