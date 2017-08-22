package com.siebre.util;

public class IdCardUtil {
	static final int[] wi = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2,1};
	
	// verify digit 
	static final int[] vi = {1,0,'X',9,8,7,6,5,4,3,2};
	
	//verify 
	public static boolean verifySocialSecurityCard(String idcard) {
		if (idcard.length() == 15) {
			idcard = uptoeighteen(idcard);
		}
		if (idcard.length() != 18) {
			return false;
		}
		String verify = idcard.substring(17, 18).toUpperCase();
		if (verify.equals(getVerify18CardNum(idcard))) {
			return true;
		}
		return false;
	}
	
	//get verify 
	public static String getVerify18CardNum(String eightcardid) {
		int[] ai = new int[18]; 
		int remaining = 0;
		if (eightcardid.length() == 18) {
			eightcardid = eightcardid.substring(0, 17);
		}
		if (eightcardid.length() == 17) {
			int sum = 0;
			for (int i = 0; i < 17; i++) {
				String k = eightcardid.substring(i, i + 1);
				ai[i] = Integer.parseInt(k);
			}
			for (int i = 0; i < 17; i++) {
				sum = sum + wi[i] * ai[i];
			}
			remaining = sum % 11;
		}
		return remaining == 2 ? "X" : String.valueOf(vi[remaining]);
	}
	
	//15 update to 18 
	public static String uptoeighteen(String fifteencardid) {
		String eightcardid = fifteencardid.substring(0, 6);
		eightcardid = eightcardid + "19";
		eightcardid = eightcardid + fifteencardid.substring(6, 15);
		eightcardid = eightcardid + getVerify18CardNum(eightcardid);
		return eightcardid;
	}
	
	public static String[] splitIdNum(String sId) {
		if (verifySocialSecurityCard(sId)) {
			String sBirthday = "";
			Integer gender = 0;
			String sGender = "";
			
			if (sId.length() == 18) {
				sBirthday = sId.substring(6, 10) + "-" + sId.substring(10, 12) + "-" + sId.substring(12, 14);
				gender = Integer.valueOf(sId.substring(16, 17));
				sGender = (gender % 2 != 0) ? "MALE" : "FEMALE";
			} else if (sId.length() == 15) {
				sBirthday = "19" + sId.substring(6, 8) + "-" + sId.substring(8, 10) + "-" + sId.substring(10, 12);
				gender = Integer.valueOf(sId.substring(14, 15));
				sGender = (gender % 2 != 0) ? "MALE" : "FEMALE";
			}
			return new String[] { sGender, sBirthday };
		} else {
			return null;
		}
	}
	
}
