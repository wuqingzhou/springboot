package com.wqz.example.springboot.controller;

import com.wqz.example.springboot.config.AppConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Value("${cusp2.cusProperty1}")
    private String cusp2_cusProperty1;

    @Value("${cusp2.cusProperty2_date}")
    private String cusp2_cusProperty2Date;

    @Value("${cusp2.cusProperty3.name}")
    private String cusp2_cusProperty3Name;

    @Value("${cusp2.cusProperty4_arr}")
    private String[] cusp2_cusProperty4Arr;

    @Value("${cusp2.cusProperty5_list}")
    private List<String> cusp2_cusProperty5List;

    @Value("#{${cusp2.cusProperty6_map}}")
    private Map<String, String> cusp2_cusProperty6Map;

    @GetMapping("/sayHello")
    public String sayHello() {
        return "hello !";
    }

    @GetMapping("/cusProperty")
    public void cusProperty() {
        System.out.println("cusp2.cusProperty1=" + cusp2_cusProperty1);
        System.out.println("cusp2.cusProperty2_date=" + cusp2_cusProperty2Date);
        System.out.println("cusp2.cusProperty3.name=" + cusp2_cusProperty3Name);
        for (int i = 0; i < cusp2_cusProperty4Arr.length; i++) {
            System.out.println("cusp2.cusProperty4_arr[" + i + "]=" + cusp2_cusProperty4Arr[i]);
        }
        for (int i = 0; i < cusp2_cusProperty5List.size(); i++) {
            System.out.println("cusp2.cusProperty5List.get(" + i + ")=" + cusp2_cusProperty5List.get(i));
        }
        cusp2_cusProperty6Map.forEach((k, v) -> System.out.println("k = " + k + "     v = " + v));
    }

    @GetMapping("/cusProperty2")
    public void cusProperty2() {
        System.out.println(AppConfig.getName());
        System.out.println(AppConfig.getDesc());
        System.out.println(AppConfig.getId());
    }
}
