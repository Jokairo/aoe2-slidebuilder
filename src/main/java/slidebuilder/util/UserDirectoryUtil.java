package slidebuilder.util;

import java.io.File;

public class UserDirectoryUtil {
	
	private static String getAoE2UserDirectoryPath() {
		String userDirectory = System.getProperty("user.home");
		String aoe2folderName = "/Games/Age of Empires 2 DE";
		
		String aoe2Directory = userDirectory + aoe2folderName;
		
		//Check if aoe2 directory exists
		if (!FileUtil.fileExists(aoe2Directory))
			return null;
		
		//Find UserId folder
		File aoe2File = new File(aoe2Directory);
		String userIdDirectory = null;
		for (File file : aoe2File.listFiles()) {
			if (file.isDirectory()) {
				String fileName = file.getName();
				
				//Find UserId folder, it only contains numbers 
				if (fileName.matches("[0-9]+") && fileName.length() > 2) {
					userIdDirectory = file.getAbsolutePath();
					break;
				}
			}
		}
		
		//If no UserId folder is found, return it, otherwise it returns null
		return userIdDirectory;
	}

	public static String getAoe2LocalModsPath() {
		//Get AoE2 directory
		String aoe2d = getAoE2UserDirectoryPath();

		if (aoe2d == null) return null;

		//Find local mods folder
		String localMods = aoe2d + "/mods/local/";

		//Check if local mods folder exists
		if (!FileUtil.fileExists(localMods)) return null;

		//User local mods folder exists, return it
		return localMods;
	}

	public static String getAoe2UserCampaignsPath() {
		//Get AoE2 directory
		String aoe2d = getAoE2UserDirectoryPath();

		if (aoe2d == null) return null;

		//Find campaigns folder
		String userCampaigns = aoe2d + "/resources/_common/campaign/";

		//Check if user campaigns folder exists
		if (!FileUtil.fileExists(userCampaigns)) return null;

		//User campaigns folder exists, return it
		return userCampaigns;
	}
}
