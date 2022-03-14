package com.fdmgroup.soloproject2.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "projects", uniqueConstraints = @UniqueConstraint(columnNames = {"project_name"}))
public class Project {

	@Id
	@GeneratedValue
	private Integer projectId;
	@Column(name = "project_name")
	private String projectName;
	private String projectTopic;
	private LocalDate dateOfCreation;
	private String projectDescription;
	@OneToMany(cascade = CascadeType.ALL)
	@OrderBy("dateOfWriting DESC")
	@JoinTable(name = "project_articles",
					joinColumns = @JoinColumn(name = "project_id"),
					inverseJoinColumns = @JoinColumn(name = "article_id"))
	private List<Article> articles;
	
	public Project() {}
	public Project(String projectName, String projectTopic) {
		this.projectName = projectName;
		this.projectTopic = projectTopic;
		this.dateOfCreation = LocalDate.now();
	}
	
	public Project(String projectName) { 
		this.projectName = projectName;
		this.dateOfCreation = LocalDate.now();
	}
	
	public Integer getProjectId() { return projectId; }
	public String getProjectName() { return projectName; }
	public String getProjectTopic() { return projectTopic; }
	public String getProjectDescription() { return projectDescription; }
	public List<Article> getArticles() { return articles; }
	public LocalDate getDateOfCreation() {return dateOfCreation; }
	public void setProjectId(Integer projectId) { this.projectId = projectId; }
	public void setProjectName(String projectName) { this.projectName = projectName; }
	public void setProjectTopic(String projectTopic) { this.projectTopic = projectTopic; }
	public void setProjectDescription(String projectDescription) { this.projectDescription = projectDescription; }
	public void setArticles(List<Article> articles) { this.articles = articles; }
	public void setDateOfCreation(LocalDate date) {this.dateOfCreation = date; }
	
	public void addArticle(Article article) {articles.add(article);}
}
