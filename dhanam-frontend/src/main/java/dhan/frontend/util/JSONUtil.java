package dhan.frontend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

public class JSONUtil {
	private static final Logger log = LoggerFactory.getLogger(JSONUtil.class);
	private static final ObjectMapper mapper;
	
	static{
		mapper = new ObjectMapper();
		mapper.registerModule(new AfterburnerModule());
	}
	public static <T> T read(String content, Class<T> c) throws Exception{
		return mapper.readValue(content, c);
	}
	
	public static <T> String write(T object){
		try {
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
