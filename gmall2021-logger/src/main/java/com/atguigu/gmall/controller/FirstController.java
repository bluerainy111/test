package com.atguigu.gmall.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//将FirstC类交给spring容器管理,并接收请求
public class FirstController {
    //通过requestMapping匹配请求并交给方法处理
    @RequestMapping("first")
    public String test (String username, String password){
        System.out.println(username+":::"+password);
        return "success";
    }

}
