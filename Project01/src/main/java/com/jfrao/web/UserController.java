package com.jfrao.web;

import com.jfrao.domain.User;
import com.jfrao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@Controller
public class UserController {


    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public String login(HttpServletRequest request, Model model, HttpSession session){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = userService.login(username, password);
        if(user == null){
            model.addAttribute("msg","用户名或密码错误");
            return "login";
        }else{
            if(user.getStatus() == 0){
                model.addAttribute("msg","用户已停用");
                return "login";
            }
            session.setAttribute("user",user);
            if("admin".equals(user.getRole())){
                return "redirect:/admin";
            }else{
                return "hello";
            }
        }
    }

    @GetMapping("/admin")
    public String userList(Model model){
        List<User> list = userService.getAll();
        model.addAttribute("userList",list);
        System.out.println(list);
        return "admin";
    }

    @RequestMapping("/stopUser/{id}")
    public String stopUser(@PathVariable("id") String id){
        User user = userService.getOneById(id);
        user.setStatus(0);
        userService.update(user);
        return "redirect:/admin";
    }

    @RequestMapping("/startUser/{id}")
    public String startUser(@PathVariable("id") String id){
        User user = userService.getOneById(id);
        user.setStatus(1);
        userService.update(user);
        return "redirect:/admin";
    }

    @PostMapping("/add")
    public String add(User user){
        String id = UUID.randomUUID().toString();
        user.setUserid(id);
        user.setRole("user");
        user.setStatus(1);
        userService.insert(user);
        return "redirect:/admin";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id,Model model){
        userService.delete(id);
        return "redirect:/admin";
    }

}
