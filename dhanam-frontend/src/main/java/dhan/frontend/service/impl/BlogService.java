package dhan.frontend.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import dhan.frontend.model.Author;
import dhan.frontend.model.AuthorPosts;
import dhan.frontend.model.LoginResult;
import dhan.frontend.model.Post;
import dhan.frontend.util.JSONUtil;

public class BlogService {
	private static final Logger log = LoggerFactory.getLogger(BlogService.class);
	
	private String authorFolder;
	private String postFolder;
	private String draftFolder;
	private String authorPostsFolder;
	
	public BlogService(String authorFolder, String postFolder, String draftFolder, String authorPostsFolder){
		this.authorFolder = authorFolder;
		this.postFolder = postFolder;			
		this.draftFolder = draftFolder;
		this.authorPostsFolder = authorPostsFolder;
	}

	public LoginResult checkAuthor(Author author) throws Exception {
		LoginResult result = new LoginResult();
		Author authorFound = JSONUtil.read(Files.toString(new File(authorFolder+author.getAuthorId()+".json"), Charsets.UTF_8), Author.class);//authorDao.getUserbyUsername(author.getAuthorId());
		if(authorFound == null) {
			result.setError("Invalid authorname");
		} else if(!author.getPassword().equals(authorFound.getPassword())) {
			result.setError("Invalid password");
		} else {
			result.setAuthor(authorFound);
		}
		return result;
	}
	/*
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

	public AuthorPosts getDraftsAndPosts(Author author) throws Exception{
		File f = new File(this.authorPostsFolder+author.getAuthorId()+".json");
		if(f.exists()) return JSONUtil.read(Files.toString(f, Charsets.UTF_8), AuthorPosts.class);
		else return new AuthorPosts(author.getAuthorId(), new ArrayList<String>(), new ArrayList<String>());
	}
	
	public Post getDraft(String status, String authorId, String id) throws Exception{
		try{
			if(!id.equals("new")){
				if(status.equals("draft")){
					return JSONUtil.read(Files.toString(new File(this.draftFolder+authorId+File.separator+id+".json"), Charsets.UTF_8), Post.class);
				}else{
					return JSONUtil.read(Files.toString(new File(this.postFolder+authorId+File.separator+id+".json"), Charsets.UTF_8), Post.class);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new Post();
	}
	/*
		try (FolderectoryStream<Path> directoryStream = Files.newFolderectoryStream(Paths.get(directory))) {
            for (Path path : directoryStream) {
                fileNames.add(path.toString());
            }
        } catch (IOException ex) {}
	}
	*/
}
