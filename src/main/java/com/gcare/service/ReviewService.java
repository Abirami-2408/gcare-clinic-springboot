package com.gcare.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gcare.entity.Review;
import com.gcare.repository.ReviewRepository;

@Service
public class ReviewService {

    private final ReviewRepository repo;

    public ReviewService(ReviewRepository repo) {
        this.repo = repo;
    }

    public List<Review> getAll()      { return repo.findAll(); }
    public List<Review> getActive()   { return repo.findByStatusTrue(); }
    public Review getById(Long id)    { return repo.findById(id).orElseThrow(); }
    public void save(Review review)   { repo.save(review); }
    public void delete(Long id)       { repo.deleteById(id); }

    public void toggleStatus(Long id) {
        Review r = getById(id);
        r.setStatus(!r.isStatus());                 // flip active/inactive
        repo.save(r);
    }
}