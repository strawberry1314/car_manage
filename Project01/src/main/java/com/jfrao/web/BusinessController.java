package com.jfrao.web;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.jfrao.domain.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BusinessController {

    private static List<Customer> list = new ArrayList<>();
    private static Integer index = 1;
    private static Object lock = new Object();

    //生成业务
    public Customer add(String business){
        synchronized (lock){
            String str = index.toString();
            String num = "A";
            for(int i = str.length(); i < 4; i++){
                num += 0;
            }
            num += str;
            index++;
            Customer customer = new Customer();
            customer.setBusiness(business);
            customer.setNum(num);
            list.add(customer);
            System.out.println("集合：--------" + list);
            System.out.println("当前对象：---------" + this);
            lock.notifyAll();
            return customer;
        }
    }

    //分派业务
    public Customer get(){
        synchronized (lock){
            if(list.size() == 0){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Customer customer = list.get(0);
            list.remove(0);
            return customer;
        }
    }

    //寻找播报线程
    public static Thread findThread(long threadId) {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        while(group != null) {
            Thread[] threads = new Thread[(int)(group.activeCount() * 1.2)];
            int count = group.enumerate(threads, true);
            for(int i = 0; i < count; i++) {
                if(threadId == threads[i].getId()) {
                    return threads[i];
                }
            }
            group = group.getParent();
        }
        return null;
    }

    //大屏幕显示当前等待客户
    @GetMapping("/getList")
    public String getList(Model model){
        model.addAttribute("list",list);
        return "list";
    }

    //办理业务
    @GetMapping("/create/{business}")
    public String create(@PathVariable("business") String business, Model model){
        Customer customer = add(business);
        model.addAttribute("wait_customer",customer);
        return "business";
    }

    //小窗口申请分派业务
    @GetMapping("/getCustomer")
    public String getCustomer(Model model){
        Customer customer = get();
        model.addAttribute("run_customer",customer);
        //广播叫号
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ActiveXComponent sap = new ActiveXComponent("sapi.SpVoice");
                String content = "请" + customer.getNum() + "到" + "6" + "号窗口办理业务";

                // 调节语速 音量大小
                sap.setProperty("Volume", new Variant(100));
                sap.setProperty("Rate", new Variant(-4));

                Dispatch xj = sap.getObject();
                // 执行朗读 没有读完就继续读

                for (int i = 0; i < 3; i++) {
                    Dispatch.call(xj, "Speak", new Variant(content));
                    try {
                        Thread.sleep(2 * 1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    }
                    //判断customer是否来到
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("播报结束");
                        break;
                    }
                }
                xj.safeRelease();
                sap.safeRelease();
            }
        });
        thread.start();
        System.out.println(thread+"---"+thread.getName()+"---"+thread.getId());
        model.addAttribute("threadId",thread.getId());
        return "little_LED";
    }

    //客户到达
    @GetMapping("/arrive/{threadId}")
    @ResponseBody
    public String arrive(@PathVariable("threadId") Long threadId){
        System.out.println(threadId);
        Thread thread = findThread(threadId);
        System.out.println(thread);
        if(thread != null){
            thread.interrupt();
        }
        return null;

    }


}
