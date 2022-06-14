package de.zeeisl.blog.controllers.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.zeeisl.blog.entities.Advertisement;
import de.zeeisl.blog.repositories.AdvertisementRepository;

@Controller
@RequestMapping("/admin/ads")
public class AdminAdvertisementController {

    @Autowired
    AdvertisementRepository advertisementRepository;

    @GetMapping("")
    String index(Model model) {
        List<Advertisement> ads = advertisementRepository.findAll();
        model.addAttribute("ads", ads);
        return "admin/ads/index";
    }

    @GetMapping("/create")
    String create(Model model) {
        return "admin/ads/create";
    }
}
