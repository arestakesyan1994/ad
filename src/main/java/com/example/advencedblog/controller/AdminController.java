package com.example.advencedblog.controller;

import com.example.advencedblog.model.Ad;
import com.example.advencedblog.model.Category;
import com.example.advencedblog.model.User;
import com.example.advencedblog.model.UserType;
import com.example.advencedblog.repository.AdRepository;
import com.example.advencedblog.repository.CategoryRepository;
import com.example.advencedblog.repository.UserRepository;
import com.example.advencedblog.security.CurrentUser;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class AdminController {

    @Value("${image.folder}")
    String imageUploadDir;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admin")
    public String mainAdmin(ModelMap modelMap) {
        modelMap.addAttribute("ad", new Ad());
        modelMap.addAttribute("category", new Category());

        modelMap.addAttribute("categories", categoryRepository.findAll());
        modelMap.addAttribute("ads", adRepository.findAll());
        return "admin";
    }

    @GetMapping("/addAd")
    public String addAd(ModelMap modelMap) {
        modelMap.addAttribute("ads", new Ad());
        modelMap.addAttribute("allCategory", categoryRepository.findAll());
        return "addAd";
    }

    @GetMapping("/getAD")
    public String getBlogs(ModelMap map, @RequestParam("id") int id) {
        List<Ad> listBlog = adRepository.findAdByCategory(categoryRepository.getOne(id));
        map.addAttribute("ListBlog", listBlog);
        map.addAttribute("allCategory", categoryRepository.findAll());
        return "allAd";
    }

    @GetMapping("/addCategory")
    public String addCategory(ModelMap modelMap) {
        modelMap.addAttribute("category", new Category());
        return "addCategory";
    }

    @GetMapping("/allCategory")
    public String allCategory(ModelMap modelMap) {
        modelMap.addAttribute("category", categoryRepository.findAll());
        return "allCategory";
    }

    @GetMapping("/allAd")
    public String allAd(ModelMap modelMap) {
        modelMap.addAttribute("ad", adRepository.findAll());
        return "allAd";
    }

    @PostMapping("/addAd")
    public String addAd(@ModelAttribute(name = "product") Ad ad,
                        @RequestParam("picture") MultipartFile multipartFile) throws IOException {
        String pictureName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        File imageDir = new File(imageUploadDir);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
        File file = new File(imageUploadDir + pictureName);
        multipartFile.transferTo(file);
        ad.setPicUrl(pictureName);
        adRepository.save(ad);
        return "redirect:/addAd";
    }

    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public void getImageAsByteArray(HttpServletResponse response, @RequestParam("fileName") String fileName) throws IOException {
        InputStream in = new FileInputStream(imageUploadDir + fileName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }


    @PostMapping("/addCategory")
    public String addCategory(@ModelAttribute("category") Category category) {
        categoryRepository.save(category);
        return "redirect:/addCategory";
    }

    @GetMapping("/deleteAd/{id}")
    public String deleteAd(@PathVariable("id") int id) {
        System.out.println(id);
        return "redirect:/allAd";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable("id") int id) {
        System.out.println(id);
        return "redirect:/allCategory";
    }
}
