package dhan.frontend.model;

import java.util.Map;

public class AuthorPosts {
	private String authorId;
	private Map<String, String> drafts;
	private Map<String, String> posts;
	
	public AuthorPosts(){}
	
	public AuthorPosts(String authorId, Map<String, String> drafts, Map<String, String> posts){
		this.authorId = authorId;
		this.drafts = drafts;
		this.posts = posts;
	}
	
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public Map<String, String> getDrafts() {
		return drafts;
	}
	public void setDrafts(Map<String, String> drafts) {
		this.drafts = drafts;
	}
	public Map<String, String> getPosts() {
		return posts;
	}
	public void setPosts(Map<String, String> posts) {
		this.posts = posts;
	}
	
}
