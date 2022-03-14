package com.fdmgroup.soloproject2.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@DiscriminatorValue("text")
public class ArticleText extends Article {

	@Id
	@GeneratedValue
	@Column(name = "article_id")
	private Integer articleId;
	@Column(columnDefinition = "TEXT")
	private String articleText;
	private String image;
	
	public ArticleText() {}

	public ArticleText(String articleTitle) { 
		super(articleTitle);
		this.image = "";
	}

	public Integer getArticleId() { return articleId; }
	public String getArticleText() { return articleText; }
	public String getImage() { return image; }
	public void setArticleId(Integer articleId) { this.articleId = articleId; }
	public void setArticleText(String articleText) { this.articleText = articleText; }
	public void setImage(String image) { this.image = image; }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(articleText, image);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!super.equals(obj)) { return false; }
		ArticleText other = (ArticleText) obj;
		return Objects.equals(articleText, other.articleText) && Objects.equals(image, other.image);
	}
	
}
