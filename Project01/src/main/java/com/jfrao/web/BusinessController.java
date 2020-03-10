package com.jfrao.web;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.jfrao.domain.Customer;
import com.jfrao.domain.Queue_Number;
import com.jfrao.service.NumberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
public class BusinessController {

    private static List<Customer> list = new ArrayList<>();
    private static Integer index = 1;
    private static Object lock = new Object();

    @Autowired
    private NumberService numberService;

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
            customer.setNumid(UUID.randomUUID().toString());
            customer.setBusiness(business);
            customer.setNum(num);
            list.add(customer);
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
    public void create(@PathVariable("business") String business, HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        Customer customer = add(business);

        //生成排队号码记录，存入数据库中
        Queue_Number number = new Queue_Number();
        number.setNum_id(customer.getNumid());
        number.setCreate_time(new Date());
        number.setNum(customer.getNum());
        log.debug("queue_number",number);
        //存入数据库
        numberService.addNumber(number);

        //记录办理业务操作日志
        request.setAttribute("customer", customer);
        request.getRequestDispatcher("/Log/CreateBusiness").forward(request, response);

    }

    //小窗口申请分派业务
    @GetMapping("/getCustomer")
    public String getCustomer(Model model){
        Customer customer = get();
        model.addAttribute("run_customer",customer);

        //插入叫号时间记录
        numberService.addCallTime(customer.getNumid(),new Date());

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
                        log.debug("播报结束");
                        break;
                    }
                }

                //叫号超时，插入超时标记
                if(!Thread.currentThread().isInterrupted()){
                    numberService.overtime(customer.getNumid());
                }

                xj.safeRelease();
                sap.safeRelease();
            }
        });
        thread.start();
        model.addAttribute("threadId",thread.getId());
        return "little_LED";
    }

    //客户到达
    @GetMapping("/arrive/{threadId}")
    @ResponseBody
    public String arrive(@PathVariable("threadId") Long threadId, @RequestParam(name = "numid") String numid){
        log.debug("threadID",threadId);
        log.debug(numid);
        Thread thread = findThread(threadId);
        log.debug("thread",thread);
        if(thread != null){
            thread.interrupt();
            numberService.addStartTime(numid,new Date());
        }
        return null;

    }


}
