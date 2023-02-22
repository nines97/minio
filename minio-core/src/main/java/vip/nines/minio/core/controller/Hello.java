package vip.nines.minio.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tanyujie
 * @classname Hello
 * @description hello
 * @date 2022/11/19 21:22
 * @since 1.0
 */
@RestController
@RequestMapping("/")
public class Hello {

    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }

}
