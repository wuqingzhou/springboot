package com.wqz.example.springboot.controller;

import com.wqz.example.springboot.config.AppConfig;
import com.wqz.example.springboot.mapper.TbUserMapper;
import com.wqz.example.springboot.model.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/test")
public class TestController {

    @Value("${cusp2.cusProperty1:自定义属性default1}")
    private String cusp2_cusProperty1;

    @Value("${cusp2.cusProperty2_date:2020/05/04}")
    private String cusp2_cusProperty2Date;

    @Value("${cusp2.cusProperty3.name:自定义属性default3}")
    private String cusp2_cusProperty3Name;

    @Value("${cusp2.cusProperty4_arr:d_aa,d_bb,d_cc}")
    private String[] cusp2_cusProperty4Arr;

    @Value("${cusp2.cusProperty5_list:d_dd,d_ee,d_ff}")
    private List<String> cusp2_cusProperty5List;

    @Value("#{${cusp2.cusProperty6_map:{\"a\":\"d_a1\",\"b\":\"d_b1\",\"c\":\"d_c1\"}}}")
    private Map<String, String> cusp2_cusProperty6Map;

    @GetMapping("/sayHello")
    public String sayHello() {
        return "hello !";
    }

    @Autowired
    TbUserMapper userMapper;

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

    @GetMapping("/test222")
    public  List<TbUser> test222222() {
        List<TbUser> userList = userMapper.selectAll();
        List<TbUser> userList2 = userMapper.getListTest();
        List<Map<String, String>> userList3 = userMapper.getListTest2();
        return userList2;
    }

    @GetMapping("/测试插入数据")
    public  List<TbUser> testInsert1(@RequestParam String txt) {
        TbUser tbUser = new TbUser();
        tbUser.setId(UUID.randomUUID().toString());
        tbUser.setName(txt);
        tbUser.setOldName(txt);
        userMapper.insertSelective(tbUser);

        List<TbUser> userList = userMapper.selectAll();
        return userList;
    }
}
