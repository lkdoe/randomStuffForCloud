package com.fdmgroup.soloproject2.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.soloproject2.model.Article;
import com.fdmgroup.soloproject2.model.ArticleText;
import com.fdmgroup.soloproject2.model.HobbyGroup;
import com.fdmgroup.soloproject2.model.Project;
import com.fdmgroup.soloproject2.model.User;
import com.fdmgroup.soloproject2.repository.ArticleRepository;
import com.fdmgroup.soloproject2.repository.GroupRepository;
import com.fdmgroup.soloproject2.repository.ProjectRepository;
import com.fdmgroup.soloproject2.repository.UserRepository;

@Service
public class ProjectServiceImp implements ProjectService{

	private ProjectRepository projectRepository;
	private GroupRepository groupRepository;
	private ArticleRepository articleRepository;
	private UserRepository userRepository;
	
	@Autowired
	public ProjectServiceImp(ProjectRepository projectRepository, 
					GroupRepository groupRepository,
					ArticleRepository articleRepository,
					UserRepository userRepository) {
		this.projectRepository = projectRepository;
		this.groupRepository = groupRepository;
		this.articleRepository = articleRepository;
		this.userRepository = userRepository;
	}
	
	@Override
	public Collection<Project> recommended(User user) {
		Set<Project> recommendations = new HashSet<>();
		List<Project> favourites = user.getFavourites();
//		System.out.println("Fav Proj: " + favourites.toString());
		List<String> favTopics = favourites.stream()
				.map(p -> p.getProjectTopic())
				.collect(Collectors.toCollection(ArrayList::new));
//		System.out.println("Fav tops: " + favTopics);
		Map<String, Long> topicCount = frequencyMap(favTopics.stream());
//		System.out.println("Top-count: " + topicCount);
//		System.out.println("_----------_");
		while(!topicCount.isEmpty()) {
			String nextSearchTerm = keyForMaxValueOfMap(topicCount);
			recommendations.addAll(search(nextSearchTerm));
			topicCount.remove(nextSearchTerm);
		}
		return recommendations; 
	}

	@Override
	public List<Project> newest() { 
		List<Project> newest = new ArrayList<>();
		newest.addAll(projectRepository.findNewestProjects());
		return newest; 
	}

	@Override
	public void startProject(Project project, HobbyGroup group) {
		group.addProject(project);
		groupRepository.save(group);
	}

	@Override
	public Project findByProjectName(String projectname) { 
		return projectRepository.findByProjectName(projectname); 
	}

	@Override
	public Boolean isProjectOwner(Project project, User user) {
		HobbyGroup group = groupRepository.findByProjects(project);
		Boolean userIsInGroup= (group.getGroupMods().contains(user) 
				|| group.getNormalMembers().contains(user));
		return userIsInGroup; 
	}

	@Override
	public Project findByProjectId(Integer projectId) {
		return projectRepository.findById(projectId).orElse(null); 
	}

	@Override
	public Project updateProject(Integer projectId, String projectName, String projectTopic, String projectDescription) {
		Project project = findByProjectId(projectId);
		project.setProjectName(projectName);
		project.setProjectTopic(projectTopic);
		project.setProjectDescription(projectDescription);
		return projectRepository.saveAndFlush(project);
	}

	@Override
	public ArticleText writeTextArticle(Integer projectId, String articleTitle, String articleText) {
		Project project = projectRepository.findById(projectId).orElse(null);
		ArticleText article = new ArticleText(articleTitle);
		article.setArticleText(articleText);
		Integer idInteger = articleRepository.save(article).getArticleId();
		Article articleAfterSave = articleRepository.findById(idInteger).orElse(article);
		project.addArticle(articleAfterSave);
		projectRepository.saveAndFlush(project);
		return article;
	}

	@Override
	public void rememberArticleImage(ArticleText article, String imageName) {
		article.setImage(imageName);
		saveArticle(article);
	}
	
	@Override
	public void saveArticle(Article article) {
		articleRepository.save(article);
	}

	@Override
	public Collection<Project> search(String searchterm) {
		Set<Project> searchResults = new HashSet<>();
		searchResults.addAll(projectRepository.findByProjectNameContaining(searchterm));
		searchResults.addAll(projectRepository.findByProjectDescriptionContaining(searchterm));
		searchResults.addAll(projectRepository.findByProjectTopicContaining(searchterm));
		return searchResults; 
	}

	@Override
	public Collection<String> getAllTopics() {
		return projectRepository.findAllTopics(); 
	}
	
	@Override
	public String deleteProjectById(Integer projectId, User user) {
		Project project = projectRepository.findById(projectId).orElse(null);
		if(isProjectOwner(project, user)) {
			List<User> whoHasFavourite = userRepository.findByFavourites(project);
			for(User u : whoHasFavourite) {
				u.removeFavourite(project);
				userRepository.save(u);
			}
			HobbyGroup group = groupRepository.findByProjects(project);
			group.removeProject(project);
			groupRepository.save(group);
			projectRepository.delete(project);
			return "/";
		}
		return "/project/" + project.getProjectName(); 
	}

	public <K> Map<K, Long> frequencyMap(Stream<K> elements){
		return elements.collect(
				Collectors.groupingBy(
						Function.identity(),
						HashMap::new,
						Collectors.counting()
						)
				);
	}
	
	public <K, V extends Comparable<V>> K keyForMaxValueOfMap(Map<K, V> map) {
		Optional<Entry<K, V>> maxEntry = map.entrySet()
				.stream()
				.max((Entry<K, V> e1, Entry<K, V> e2) -> e1.getValue().compareTo(e2.getValue())
						);
		return maxEntry.get().getKey();
	}
}
