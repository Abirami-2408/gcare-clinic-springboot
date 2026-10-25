package com.gcare.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gcare.entity.Slider;
import com.gcare.repository.SliderRepository;

@Service
public class SliderService {

    private final SliderRepository repo;
    private final FileStorageService storage;

    public SliderService(SliderRepository repo, FileStorageService storage) {
        this.repo = repo;
        this.storage = storage;
    }

    public List<Slider> getAll()     { return repo.findAll(); }
    public List<Slider> getActive()  { return repo.findByStatusTrue(); }
    public Slider getById(Long id)   { return repo.findById(id).orElseThrow(); }

    public void save(Slider slider, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String newName = storage.save(file);       // save the new image first
            storage.delete(slider.getImage());         // then remove the old one
            slider.setImage(newName);
        }
        repo.save(slider);                             // no new file: old image name is kept
    }

    public void toggleStatus(Long id) {
        Slider s = getById(id);
        s.setStatus(!s.isStatus());
        repo.save(s);
    }

    public void delete(Long id) {
        Slider s = getById(id);
        storage.delete(s.getImage());
        repo.deleteById(id);
    }
}