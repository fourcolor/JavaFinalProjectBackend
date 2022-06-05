package com.example.JavaFinalProjectBackend.quiz;

import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
// import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
// import org.springframework.transaction.annotation.Transactional;

@RestController
public class QuizController {

    JSONParser parser = new JSONParser();

    @GetMapping("quiz/getAll")
    public JSONArray getAll() {
        JSONArray arr = null;
        try {
            arr = (JSONArray) parser.parse(new FileReader("src/static/info.json"));
            return arr;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return arr;
    }

    @GetMapping("quiz/get")
    public HashMap<String, String> get(
            @RequestParam("name") String name) {
        String encodedString = null;
        File f = new File("src/static/" + name + "/" + name + ".jpeg");
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(f);
            byte[] bytes = new byte[(int) f.length()];
            fileInputStreamReader.read(bytes);
            encodedString = Base64.getEncoder().withoutPadding().encodeToString(bytes);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HashMap<String, String> res = new HashMap<String, String>();
        res.put("data", encodedString);
        return res;
    }

    @GetMapping("quiz/getMyQuiz")
    public HashMap<String, String> getMyQuiz(
            @RequestParam("name") String name, @RequestParam("uid") String uid, @RequestParam("time") String time) {
        String encodedString = null;
        File f = new File("src/static/" + name + "/" + uid + "/" + time + ".jpeg");
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(f);
            byte[] bytes = new byte[(int) f.length()];
            fileInputStreamReader.read(bytes);
            encodedString = Base64.getEncoder().withoutPadding().encodeToString(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<String, String> res = new HashMap<String, String>();
        res.put("data", encodedString);
        return res;
    }

    @GetMapping("quiz/getMyinfo")
    public String[] getMyinfo(
            @RequestParam("name") String name, @RequestParam("uid") String uid) {
        File f = new File("src/static/" + name + "/" + uid + "/");
        String[] res = f.list();
        for (int i = 0; i < res.length; i++) {
            String[] tmp = res[i].split("\\.");
            res[i] = tmp[0];
        }
        return res;
    }

    @PostMapping("quiz/set")
    public String set(@RequestBody HashMap<String, String> req) {
        try {
            JSONArray arr = (JSONArray) parser.parse(new FileReader("src/static/info.json"));
            boolean Changed = false;
            System.out.println(arr.size());
            for (Object ob : arr) {
                JSONObject job = (JSONObject) ob;
                if (job.get("name").equals(req.get("name"))) {
                    job.put("startAt", req.get("startAt"));
                    job.put("time", req.get("time"));
                    Changed = true;
                    break;
                }
            }
            if (!Changed) {
                JSONObject tjob = new JSONObject();
                tjob.put("name", req.get("name"));
                tjob.put("startAt", req.get("startAt"));
                tjob.put("time", req.get("time"));
                arr.add(tjob);
            }
            try (FileWriter file = new FileWriter("src/static/info.json")) {
                // We can write any JSONArray or JSONObject instance to the file
                file.write(arr.toJSONString());
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] imageByte;
            imageByte = Base64.getDecoder().decode(req.get("data"));
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            BufferedImage bImage2 = ImageIO.read(bis);
            File f = new File("src/static/" + req.get("name") + "/" + req.get("name") + ".jpeg");
            if (!f.getParentFile().exists())
                f.getParentFile().mkdirs();
            if (!f.exists())
                f.createNewFile();
            ImageIO.write(bImage2, "jpeg", f);
            return "Ok";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "Fail";
    }

    @PostMapping("quiz/finish")
    public String finish(@RequestBody HashMap<String, String> req) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date(System.currentTimeMillis());
            byte[] imageByte;
            imageByte = Base64.getDecoder().decode(req.get("data"));
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            BufferedImage bImage2 = ImageIO.read(bis);
            File f = new File(
                    "src/static/" + req.get("name") + "/" + req.get("uid") + "/" + formatter.format(date)
                            + ".jpeg");
            if (!f.getParentFile().exists())
                f.getParentFile().mkdirs();
            if (!f.exists())
                f.createNewFile();
            ImageIO.write(bImage2, "jpeg", f);
            return "Ok";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Fail";
    }
}
