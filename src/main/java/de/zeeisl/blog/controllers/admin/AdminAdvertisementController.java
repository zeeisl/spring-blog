package de.zeeisl.blog.controllers.admin;

import java.util.List;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.zeeisl.blog.entities.Advertisement;
import de.zeeisl.blog.repositories.AdvertisementRepository;
import de.zeeisl.blog.repositories.TagRepository;
import de.zeeisl.blog.services.StorageService;
import de.zeeisl.blog.transitonObjects.advertisement.CreateAdvertisementForm;

@Controller
@RequestMapping("/admin/ads")
public class AdminAdvertisementController {

    @Autowired
    AdvertisementRepository advertisementRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    StorageService storageService;

    @GetMapping("")
    String index(Model model) {
        List<Advertisement> ads = advertisementRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("ads", ads);
        return "admin/ads/index";
    }

    @GetMapping("/create")
    String create(Model model, CreateAdvertisementForm createAdvertisementForm) {
        return "admin/ads/create";
    }

    @PostMapping("/create")
    String store(@Valid CreateAdvertisementForm createAdvertisementForm, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (createAdvertisementForm.getImage() == null && createAdvertisementForm.getImageFile().isEmpty()) {
            // no ad-image set
            bindingResult.rejectValue("image", "error.createAdvertisementForm", "Die Werbung muss ein Bild enhalten.");
        } else if (!createAdvertisementForm.getImageFile().isEmpty()) {
            // ad-image set
            try {
                BufferedImage imgFileBuffer = ImageIO.read(createAdvertisementForm.getImageFile().getInputStream());
                if (imgFileBuffer.getWidth() != 320 || imgFileBuffer.getHeight() != 50) {
                    // wrong ad-image size
                    bindingResult.rejectValue("image", "error.createAdvertisementForm",
                            "Das Werbebild muss 320x50 Pixel groß sein.");
                } else {
                    // valid ad-image
                    String path = storageService.store(createAdvertisementForm.getImageFile());
                    createAdvertisementForm.setImage(path);
                }
            } catch (IOException e) {
                bindingResult.rejectValue("image", "error.createAdvertisementForm",
                        "Bild kann nicht verarbeitet werden.");
            }

        }

        if (bindingResult.hasErrors()) {
            return "admin/ads/create";
        }

        Advertisement ad = createAdvertisementForm.toEntity();
        tagRepository.saveAll(ad.getTags());
        advertisementRepository.save(ad);

        redirectAttributes.addFlashAttribute("success", "Werbung erfolgreich gespeichert.");
        return "redirect:/admin/ads";
    }
}
