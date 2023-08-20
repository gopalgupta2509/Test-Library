package com.mosambee.notification_sdk.backup_data;

import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMACUtilOld {
	private HMACUtilOld() {}

	public  String calculateHMAC(String signatureKey, String textToHash) throws GeneralSecurityException {
		// Convert Hex string to byte array
		byte[] signatureKeyInByteArray = hexStringToByteArray(signatureKey);

		SecretKeySpec secretKeySpec = new SecretKeySpec(signatureKeyInByteArray, "HmacSHA512");
		Mac mac = Mac.getInstance("HmacSHA512");
		mac.init(secretKeySpec);
//		return DUKPK2009_CBC.parseByte2HexStr(mac.doFinal(textToHash.getBytes()));
		return Base64Old.encodeToString(mac.doFinal(textToHash.getBytes()),false);
	}

	private static byte[] hexStringToByteArray(String str) {


		int len = str.length();
		byte[] data = new byte[len / 2];

		for (int i = 0; i < len-1; i += 2) {
			data[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
		}

		return data;
	}
}