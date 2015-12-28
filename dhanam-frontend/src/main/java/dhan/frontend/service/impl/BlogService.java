package dhan.frontend.service.impl;

import java.io.File;
import java.io.IOException;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import dhan.frontend.model.Author;
import dhan.frontend.model.LoginResult;
import dhan.frontend.util.JSONUtil;

public class BlogService {

	private String authorDir;
	private String postDir;
	
	public BlogService(String authorDir, String postDir){
		this.authorDir = authorDir;
		this.postDir = postDir;				
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
	
	/*
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
            for (Path path : directoryStream) {
                fileNames.add(path.toString());
            }
        } catch (IOException ex) {}
	}
	*/
}
