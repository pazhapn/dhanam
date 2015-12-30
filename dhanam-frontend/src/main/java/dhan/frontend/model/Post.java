package dhan.frontend.model;

public class Post {
	private String id;
	private String postDate;
	private String authorId;
	private String title;
	private String status;
	private String tags;
	private String initialParas;
	private String remainingParas;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPostDate() {
		return postDate;
	}
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getInitialParas() {
		return initialParas;
	}
	public void setInitialParas(String initialParas) {
		this.initialParas = initialParas;
	}
	public String getRemainingParas() {
		return remainingParas;
	}
	public void setRemainingParas(String remainingParas) {
		this.remainingParas = remainingParas;
	}
	
}
