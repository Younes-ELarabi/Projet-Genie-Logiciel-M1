package jwt;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.DatatypeConverter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;


public class JwtToken {
	
	private final  String KEY = "C6DFA0B215B2CF24EF04794F718A3FC8ZZC6DFA0B215B2CF24EF04794F718A3FC8";
	private byte[] signInKey = null; 
	
	public String createJWT(String id, String arg1, String arg2, long ttlMillis) throws InvalidKeyException, UnsupportedEncodingException {
	    
	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);
	    //We will sign our JWT with our ApiKey secret
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(KEY);
	    signInKey = apiKeySecretBytes;
	    //Let's set the JWT Claims)
	    Map<String,Object> infos = new HashMap<String,Object>();
	    infos.put("id", id);
	    infos.put("userName",arg1);
	    infos.put("role",arg2);
		JwtBuilder builder = Jwts.builder().setClaims(infos)
	                                .setIssuedAt(now)
	                                .signWith(Keys.hmacShaKeyFor(apiKeySecretBytes));
	    //if it has been specified, let's add the expiration
	    if (ttlMillis >= 0) {
	    long expMillis = nowMillis + ttlMillis;
	        Date exp = new Date(expMillis);
	        builder.setExpiration(exp);
	    }
	    //Builds the JWT and serializes it to a compact, URL-safe string
	    return builder.compact();
	}
	
	@SuppressWarnings("restriction")
	public Claims parseJWT(String jwt) {
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(KEY);
	    signInKey = apiKeySecretBytes;
	    //This line will throw an exception if it is not a signed JWS (as expected)
	    Claims claims = Jwts.parser()         
	       .setSigningKey(signInKey)
	       .parseClaimsJws(jwt).getBody();
	    System.out.println("ID: " + claims.get("id"));
	    System.out.println("userName: " + claims.get("userName"));
	    System.out.println("role: " + claims.get("role"));
	    System.out.println("Expiration: " + claims.getExpiration());
	    return claims;
	}
	
}

