package com.dadskitchen.controller;

import com.dadskitchen.model.Rating;
import com.dadskitchen.repository.RatingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin
public class RatingController {

    private final RatingRepository ratingRepository;

    public RatingController(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @GetMapping
    public List<Rating> getAll() {
        return ratingRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rating> getRatingById(@PathVariable Long id){
        return ratingRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Rating addRating(@RequestBody Rating rating) {
        return ratingRepository.save(rating);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rating> updateRating(@PathVariable Long id, @RequestBody Rating updated){
        return ratingRepository.findById(id)
                .map(existing ->
                {existing.setStars(updated.getStars());
                existing.setComments(updated.getComments());
                return ResponseEntity.ok(ratingRepository.save(existing));})
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Rating> deleteRating(@PathVariable Long id){
        ratingRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
