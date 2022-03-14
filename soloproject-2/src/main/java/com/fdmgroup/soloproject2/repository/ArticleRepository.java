package com.fdmgroup.soloproject2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.soloproject2.model.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

}
