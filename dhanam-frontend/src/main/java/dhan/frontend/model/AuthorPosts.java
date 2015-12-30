package dhan.frontend.model;

import java.util.List;

public class AuthorPosts {
	private String authorId;
	private List<String> drafts;
	private List<String> posts;
	
	public AuthorPosts(){}
	
	public AuthorPosts(String authorId, List<String> drafts, List<String> posts){
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
	public List<String> getDrafts() {
		return drafts;
	}
	public void setDrafts(List<String> drafts) {
		this.drafts = drafts;
	}
	public List<String> getPosts() {
		return posts;
	}
	public void setPosts(List<String> posts) {
		this.posts = posts;
	}
	
}
