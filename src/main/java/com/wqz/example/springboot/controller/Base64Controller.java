package com.wqz.example.springboot.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@RestController
@RequestMapping("/base64")
public class Base64Controller {
    final Base64.Decoder decoder = Base64.getDecoder();
    final Base64.Encoder encoder = Base64.getEncoder();

    @ApiOperation("加密")
    @GetMapping("/myEncoder")
    public String myEncoder(@RequestParam String str) throws UnsupportedEncodingException {
        return encoder.encodeToString(str.getBytes("UTF-8"));
    }

    @ApiOperation("解密")
    @GetMapping("/myDecoder")
    public String myDecoder(@RequestParam String base64Str) throws UnsupportedEncodingException {
        return new String(decoder.decode(base64Str),"UTF-8");
    }
}
