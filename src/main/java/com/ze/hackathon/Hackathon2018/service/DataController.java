package com.ze.hackathon.Hackathon2018.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by handy.kestury on 10/15/2018.
 */
@Controller
public class DataController {

  private DataService dataService;

  @Autowired
  public void set(DataService dataService) {
    this.dataService = dataService;
  }

  @GetMapping("/api/getWeatherData")
  public ResponseEntity getBalance(){
    String result = dataService.getWeather();
    return ResponseEntity.ok(result);
  }

}
