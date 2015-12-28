package dhan.frontend.util;

import java.io.File;
import java.io.FileWriter;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import dhan.frontend.model.Author;
import dhan.frontend.server.WebConfig;

public class DataCreatorUtil {

	public static void createAuthor(String propsFilePath, String authorId, String password) throws Exception{
		String authorFolder = WebConfig.getProperties(propsFilePath).getProperty("authorFolder");
		Author author = new Author();
		author.setAuthorId(authorId);
		author.setPassword(password);
		String authFileName = authorFolder+authorId+".json";
		Files.write(JSONUtil.write(author), new File(authFileName), Charsets.UTF_8);
	}
	public static void main(String[] args) {
		try{
			DataCreatorUtil.createAuthor(args[0], "paz", "noneed");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
