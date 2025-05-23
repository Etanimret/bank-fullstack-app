package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.service.SampleService;

@RestController
public class SampleController {

    @Autowired
    private SampleService sampleService;

    @GetMapping("/sample-data1")
    public String getSampleData() {
        return sampleService.fetchSampleData();
    }
}