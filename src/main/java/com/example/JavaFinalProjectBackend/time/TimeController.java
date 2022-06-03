package com.example.JavaFinalProjectBackend.time;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.springframework.web.bind.annotation.*;

@RestController
public class TimeController {
    @GetMapping("time/get")
    public HashMap<String, String> get() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        HashMap<String, String> res = new HashMap<String, String>();
        res.put("time", formatter.format(date));
        return res;
    }
}
