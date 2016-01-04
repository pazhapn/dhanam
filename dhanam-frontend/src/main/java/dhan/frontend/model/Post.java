package dhan.frontend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Post {
	public static int DRAFT_STATUS = 0;
	public static int PUBLISHED_STATUS = 1;
	public static int EDIT_STATUS = 2;
	
	private String id;
	private String postDate;
	private String authorId;
	private String authorName;
	private String image;
	private String title;
	private int status;
	private String tags;
	@JsonIgnore
	private List<String> tagsList;
	private String initialParas;
	private String remainingParas;
	private String cssContent;
	private String scriptContent;
	private String closeToBody;
	private String url;
	
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
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
	public String getCssContent() {
		return cssContent;
	}
	public void setCssContent(String cssContent) {
		this.cssContent = cssContent;
	}
	public String getScriptContent() {
		return scriptContent;
	}
	public void setScriptContent(String scriptContent) {
		this.scriptContent = scriptContent;
	}
	public String getCloseToBody() {
		return closeToBody;
	}
	public void setCloseToBody(String closeToBody) {
		this.closeToBody = closeToBody;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public List<String> getTagsList() {
		return tagsList;
	}
	public void setTagsList(List<String> tagsList) {
		this.tagsList = tagsList;
	}
	
}
