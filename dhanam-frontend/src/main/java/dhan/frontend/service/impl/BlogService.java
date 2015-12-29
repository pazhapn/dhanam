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
import dhan.frontend.model.LoginResult;
import dhan.frontend.model.Post;
import dhan.frontend.util.JSONUtil;

public class BlogService {
	private static final Logger log = LoggerFactory.getLogger(BlogService.class);
	
	private String authorDir;
	private String postDir;
	private String draftDir;
	
	public BlogService(String authorDir, String postDir, String draftDir){
		this.authorDir = authorDir;
		this.postDir = postDir;			
		this.draftDir = draftDir;
	}

	public LoginResult checkAuthor(Author author) throws Exception {
		LoginResult result = new LoginResult();
		Author authorFound = JSONUtil.getObject(Files.toString(new File(authorDir+author.getAuthorId()+".json"), Charsets.UTF_8), Author.class);//authorDao.getUserbyUsername(author.getAuthorId());
		if(authorFound == null) {
			result.setError("Invalid authorname");
		} else if(!author.getPassword().equals(authorFound.getPassword())) {
			result.setError("Invalid password");
		} else {
			result.setAuthor(authorFound);
		}
		return result;
	}
	
	public List<String> getDrafts(Author author){
		List<String> drafts = new ArrayList<>();
		try (DirectoryStream<Path> directoryStream = java.nio.file.Files.newDirectoryStream(Paths.get(this.draftDir+author.getAuthorId()+File.separator))) {
			for (Path path : directoryStream) {
				drafts.add(path.getFileName().toString());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return drafts;
	}
		
	public Post getDraft(String authorId, String title) throws Exception{
		try{
			if(!title.equals("new")){
				return JSONUtil.getObject(Files.toString(new File(this.draftDir+authorId+File.separator+title+".json"), Charsets.UTF_8), Post.class);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new Post();
	}
	/*
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
            for (Path path : directoryStream) {
                fileNames.add(path.toString());
            }
        } catch (IOException ex) {}
	}
	*/
}
