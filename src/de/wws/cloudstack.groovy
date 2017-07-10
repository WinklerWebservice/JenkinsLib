import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;
import java.util.*
import org.apache.commons.codec.binary.Hex

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

public String[] createSignature(Map<String, String> map, String key) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
		String fullcommand = ''
		Integer c = 0;
		for (Map.Entry<String, String> entry : map){
			if (c != 0) { fullcommand = fullcommand + '&' }
			fullcommand = fullcommand + entry.getKey() + "=" + entry.getValue()
			c++
		}
		
		// Get an hmac_sha1 key from the raw key bytes
		byte[] keyBytes = key.getBytes();           
		SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

		// Get an hmac_sha1 Mac instance and initialize with the signing key
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signingKey);

		// Compute the hmac on input data bytes
		byte[] rawHmac = mac.doFinal(fullcommand.toLowerCase().getBytes());

		// Create readable result
		String result = new String(rawHmac)
		
		// Generate URLDecoder
		result = new String(Base64.getEncoder().encode(rawHmac), "UTF-8")
		
		//  Covert array of Hex bytes to a String
		return [result, fullcommand]
}