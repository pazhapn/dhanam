package dhan.frontend.service.impl;

import dhan.frontend.model.Author;
import dhan.frontend.model.LoginResult;

public class BlogService {

	public BlogService(String directory){
	}

	public LoginResult checkAuthor(Author author) {
		LoginResult result = new LoginResult();
		Author authorFound = null;//authorDao.getUserbyUsername(author.getAuthorId());
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
