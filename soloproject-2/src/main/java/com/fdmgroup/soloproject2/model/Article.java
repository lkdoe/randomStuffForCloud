package com.fdmgroup.soloproject2.model;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "article_type")
public abstract class Article {
	
	@Id
	@GeneratedValue
	@Column(name = "article_id")
	private Integer articleId;
	private String articleTitle;
	private LocalDate dateOfWriting;
	
	public Article() {}
	public Article(String articleTitle) { 
		this.articleTitle = articleTitle;
		this.dateOfWriting = LocalDate.now();
	}
	
	public Integer getArticleId() { return articleId; }
	public String getArticleTitle() { return articleTitle; }
	public LocalDate getDateOfWriting() {return dateOfWriting;}
	public void setArticleId(Integer articleId) { this.articleId = articleId; }
	public void setArticleTitle(String articleTitle) { this.articleTitle = articleTitle; }
	public void setDateOfWriting(LocalDate date) {this.dateOfWriting = date;}
	@Override
	public int hashCode() { return Objects.hash(articleTitle); }
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Article other = (Article) obj;
		return Objects.equals(articleTitle, other.articleTitle);
	} 
	
	
}
