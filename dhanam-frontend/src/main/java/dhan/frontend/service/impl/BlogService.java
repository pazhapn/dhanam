package dhan.frontend.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import dhan.frontend.model.Author;
import dhan.frontend.model.AuthorPosts;
import dhan.frontend.model.LoginResult;
import dhan.frontend.model.Post;
import dhan.frontend.util.JSONUtil;
import spark.utils.StringUtils;

public class BlogService {
	private static final Logger log = LoggerFactory.getLogger(BlogService.class);
	private static final String DATE_FORMAT = "yyyyMMddHHmmss";
	
	private final int pageSize = 10;
	private final String authorFolder;
	private final String postFolder;	
	private DB db;
	private ConcurrentNavigableMap<String, Post> posts;
	private ConcurrentNavigableMap<String, String> ids;
	private TreeSet<String> tags;
	
	public BlogService(String dataFolder, String createDB) throws Exception{
		this.authorFolder = dataFolder+"author"+File.separator;
		this.postFolder = dataFolder+"post"+File.separator;
		this.db = DBMaker.fileDB(new File(dataFolder+"db"+File.separator+"posts"))
		        .closeOnJvmShutdown().transactionDisable()
		        .fileMmapEnableIfSupported().cacheHashTableEnable()
		        .make();
		this.posts = db.treeMap("posts");
		this.ids = db.treeMap("ids");
		this.tags = new TreeSet<String>();
		int fileCount = new File(postFolder).listFiles().length;
		int postCount = this.posts.size();
		if(createDB.startsWith("t") || fileCount != postCount){
			reCreateDB();
		}
	}

	public List<Post> getPosts(String page, String tag){
		int pageNum = 1;
		try{
			pageNum = Integer.parseInt(page);
		}catch(Exception e){}
		List<Post> list = new ArrayList<Post>();
		int count = 0, recordsCollected = 0, startPos = (pageNum - 1) * pageSize;
		for(String id: ids.descendingKeySet()){
			if(StringUtils.isEmpty(tag) || posts.get(id).getTagsList().contains(tag)){
				count++;
				if(count > startPos){
					recordsCollected++;
					list.add(posts.get(id));
				}
			}
			if(recordsCollected >= pageSize) break;
		}
		return list;
	}
	
	public String getIdForUrl(String url){
		if(posts.containsKey(url)) return posts.get(url).getId();
		else return url;
	}
	
	public LoginResult checkAuthor(Author author) throws Exception {
		LoginResult result = new LoginResult();
		Author authorFound = getAuthor(author.getAuthorId());
		if(authorFound == null) {
			result.setError("Invalid authorname");
		} else if(!author.getPassword().equals(authorFound.getPassword())) {
			result.setError("Invalid password");
		} else {
			result.setAuthor(authorFound);
		}
		return result;
	}

	public AuthorPosts getAuthorPosts(String authorId) throws Exception{
		AuthorPosts aps = new AuthorPosts(authorId, new HashMap<String, String>(), new HashMap<String, String>());
		Post post;
		for(String id: posts.keySet()){
			post = posts.get(id);
			if(post.getAuthorId().equals(authorId)){
				if(post.getStatus() == Post.DRAFT_STATUS) aps.getDrafts().put(post.getId(), post.getUrl());
				else aps.getPosts().put(post.getId(), post.getUrl());
			}
		}
		return aps;
	}

	public void publish(String id) throws Exception{
		Post post = getPost(id);
		post.setStatus(Post.PUBLISHED_STATUS);
		savePost(post);
	}
	
	public void deletePost(String id) throws Exception{
		Post post = getPost(id);
		posts.remove(post.getUrl());
		//java.nio.file.Files.delete();
	}
	
	public String savePost(Post post) throws Exception{
		if(StringUtils.isEmpty(post.getPostDate())) post.setPostDate(getDateForPost());
		if(StringUtils.isEmpty(post.getId())) post.setId(getIdForPost(post));
		if(StringUtils.isEmpty(post.getUrl())) post.setUrl(getUrlForPost(post));
		String postStr = JSONUtil.write(post);
		log.debug("entered post {}", postStr);
		//String authorFolder = this.postFolder+post.getAuthorId()+File.separator;
		File f = new File(postFolder+post.getId()+".json");
		Files.createParentDirs(f);
		Files.write(postStr, f, Charsets.UTF_8);
		
		posts.remove(post.getUrl());
		db.commit();
		addPost(post);
		db.commit();
		db.compact();
		//Files.write(JSONUtil.write(posts), postUrlFile, Charsets.UTF_8);
		return post.getId();
	}

	public Post getPost(String id) throws Exception{
		try{
			if(!id.equals("new")){
				return JSONUtil.read(Files.toString(new File(this.postFolder+File.separator+id+".json"), Charsets.UTF_8), Post.class);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		Post post = new Post();
		post.setPostDate(getDateForPost());
		post.setStatus(Post.DRAFT_STATUS);
		return post;
	}
	
	public TreeSet<String> getTags() {
		return tags;
	}

	public Author getAuthor(String authorId) throws Exception{
		return JSONUtil.read(Files.toString(new File(authorFolder+authorId+".json"), Charsets.UTF_8), Author.class);//authorDao.getUserbyUsername(author.getAuthorId());
	}
	private String getIdForPost(Post post){
		return post.getAuthorId()+"-"+post.getPostDate();
	}
	
	private String getUrlForPost(Post post){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<post.getTitle().length(); i++){
			if(Character.isLetterOrDigit(post.getTitle().charAt(i))){
				sb.append(post.getTitle().charAt(i));
			}else{
				sb.append("-");
			}
		}
		return sb.toString().replaceAll("-{2,}", "-").toLowerCase();
	}
	
	private String getDateForPost(){
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(new Date());
	}
	
	private void reCreateDB() throws Exception{
		if(log.isDebugEnabled()) log.debug("reCreateDb started");
		posts.clear();
		db.commit();
		ids.clear();
		tags.clear();
		for(File f: new File(postFolder).listFiles()){
			addPost(JSONUtil.read(Files.toString(f, Charsets.UTF_8), Post.class));
		}
		db.commit();
		db.compact();
		if(log.isDebugEnabled()) log.debug("reCreateDb finished");
	}
	
	private void addPost(Post post){
		post.setTagsList(Arrays.asList(post.getTags().split("\\s*,\\s*")));
		this.tags.addAll(post.getTagsList());
		this.posts.put(post.getUrl(), post);
		this.ids.put(post.getId(), post.getUrl());
	}
	/*
		
		String htm = Files.toString(new File(this.templates+"post-template.html"), Charsets.UTF_8);
		htm = htm.replaceFirst("~title~", post.getTitle());
		htm = htm.replaceFirst("~cssContent~", post.getCssContent());
		htm = htm.replaceFirst("~scriptContent~", post.getScriptContent());
		htm = htm.replaceFirst("~content~", post.getInitialParas()+"\n"+post.getRemainingParas());
		htm = htm.replaceFirst("~closeToBody~", post.getCloseToBody());
		Files.write(htm, new File(this.templates+"posts"+File.separator+post.getId()+".html"), Charsets.UTF_8);
	
	public void saveAuthorPosts(AuthorPosts ap) throws Exception{
		Files.write(JSONUtil.write(ap),new File(this.authorPostsFolder+ap.getAuthorId()+".json"), Charsets.UTF_8);
	}
		
	public List<String> getDrafts(Author author){
		List<String> drafts = new ArrayList<>();
		try (FolderectoryStream<Path> directoryStream = java.nio.file.Files.newDirectoryStream(Paths.get(this.draftFolder+author.getAuthorId()+File.separator))) {
			for (Path path : directoryStream) {
				drafts.add(path.getFileName().toString());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return drafts;
	}
	*/
	
}
