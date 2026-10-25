package com.gcare.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gcare.entity.ServiceItem;
import com.gcare.repository.ServiceItemRepository;

@Service
public class ServiceItemService {

    private final ServiceItemRepository repo;
    private final FileStorageService storage;     // the same upload class used by Sliders

    public ServiceItemService(ServiceItemRepository repo, FileStorageService storage) {
        this.repo = repo;
        this.storage = storage;
    }

    public List<ServiceItem> getAll()      { return repo.findAll(); }
    public List<ServiceItem> getActive()   { return repo.findByStatusTrue(); }
    public ServiceItem getById(Long id)    { return repo.findById(id).orElseThrow(); }

    public void save(ServiceItem item, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String newName = storage.save(file);   // save the new image first
            storage.delete(item.getImage());       // then remove the old one
            item.setImage(newName);
        }
        repo.save(item);                           // no new file: old image name is kept
    }

    public void toggleStatus(Long id) {
        ServiceItem s = getById(id);
        s.setStatus(!s.isStatus());
        repo.save(s);
    }

    public void delete(Long id) {
        ServiceItem s = getById(id);
        storage.delete(s.getImage());
        repo.deleteById(id);
    }
}