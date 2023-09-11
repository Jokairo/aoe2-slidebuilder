package slidebuilder.util;

import java.io.File;

public class UserDirectoryUtil {
	
	public static String getAoE2UserDirectory() {
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
		
		//If no UserId folder is found, return null
		if (userIdDirectory == null)
			return null;

		//Find local mods folder
		String localMods = "/mods/local/";
		
		//Check if local mods folder exists
		if (!FileUtil.fileExists(userIdDirectory + localMods))
			return null;
		
		//User local mods folder exists, return it
		return userIdDirectory + localMods;
	}
}
