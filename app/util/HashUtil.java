package util;

import java.util.Base64;

import com.google.inject.Singleton;


@Singleton
public class HashUtil {
	
	public String enocode(String...args){
		String input = "";
		for(String st : args){
			if(input.length() == 0){
				input = st;
			}else{
				input += "@" + st;
			}
		}
		 return Base64.getEncoder().withoutPadding().encodeToString(input.getBytes());
	}
	public String enocode(String id){
		 return Base64.getEncoder().withoutPadding().encodeToString(id.getBytes());
	}
	public String decode(String input, int part){
		String decoded = new String(Base64.getDecoder().decode(input));
		return decoded.split("@")[part];
	}

}
