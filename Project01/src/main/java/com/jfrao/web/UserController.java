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

import java.util.List;
import java.util.UUID;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    //超级用户
    @GetMapping("/admin")
    public String userList(Model model){
        List<User> list = userService.getAll();
        model.addAttribute("userList",list);
        System.out.println(list);
        return "admin";
    }

    //停用用户
    @RequestMapping("/stopUser/{id}")
    public String stopUser(@PathVariable("id") String id){
        User user = userService.getOneById(id);
        user.setStatus(0);
        userService.update(user);
        return "redirect:/admin";
    }

    //启用用户
    @RequestMapping("/startUser/{id}")
    public String startUser(@PathVariable("id") String id){
        User user = userService.getOneById(id);
        user.setStatus(1);
        userService.update(user);
        return "redirect:/admin";
    }

    //添加用户
    @PostMapping("/add")
    public String add(User user){
        String id = UUID.randomUUID().toString();
        user.setUserid(id);
        user.setRole("user");
        user.setStatus(1);
        userService.insert(user);
        return "redirect:/admin";
    }

    //删除用户
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id,Model model){
        userService.delete(id);
        return "redirect:/admin";
    }

}
