package com.example.advencedblog.repository;

import com.example.advencedblog.model.Ad;
import com.example.advencedblog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Integer> {

    List<Ad> findAllByCategoryId(int id);
    List<Ad> findAdByCategory(Category category);


}
