package slidebuilder.util;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import slidebuilder.Main;
import slidebuilder.data.DataManager;

public class FileSaverUtil {
	
	public File exportProject() {
		FileChooser fc = new FileChooser();
		
		//Get the Campaign Name, if not found set it as New Campaign
		String fileName = "New Campaign";
		if (!DataManager.getDataCampaign().getCampaignName().isEmpty()){
			fileName = DataManager.getDataCampaign().getCampaignName();
		}
		
		fc.setInitialFileName(fileName);
		
		//Find automatically user's AoE2:DE local mods folder and set it as the export output folder
		String localModsPath = UserDirectoryUtil.getAoe2LocalModsPath();
		
		//Path found
		if(localModsPath != null) {
			fc.setInitialDirectory(new File(localModsPath));
		}
		//Path not found, instead set the default export path as last user output
		else {
			fc.setInitialDirectory(DataManager.getDataFolderLocation().getLastOutput());
		}
		
		File file = fc.showSaveDialog(Main.getStage());
		
		if (file == null)
			return null;
		
		return file;
	}
	
	public File saveProjectFile() {
		FileChooser fc = new FileChooser();
		
		String fileName = "New Campaign";
		
		//Get the last used name of the project file
		if(DataManager.getDataFolderLocation().getProjectPath() != null && FileUtil.fileExists(DataManager.getDataFolderLocation().getProjectPath().getAbsolutePath())) {
			fileName = DataManager.getDataFolderLocation().getProjectPath().getName();
		}
		//Get the Campaign Name
		else if (!DataManager.getDataCampaign().getCampaignName().isEmpty()){
			fileName = DataManager.getDataCampaign().getCampaignName();
		}
		
		fc.setInitialFileName(fileName);
		fc.getExtensionFilters().add(new ExtensionFilter(FileFormats.FILE_FORMAT_PROJECT, FileFormats.FILE_EXTENSION_PROJECT));
		fc.setInitialDirectory(DataManager.getDataFolderLocation().getLastOutput());
		
		System.out.println("FILENAME: "+fileName);
		System.out.println("Output: "+DataManager.getDataFolderLocation().getLastOutput());
		
		File file = fc.showSaveDialog(Main.getStage());
		
		if (file == null) 
			return null;
		
		//Save project and output path
		DataManager.getDataFolderLocation().setProjectPath(file);
		DataManager.getDataFolderLocation().setLastOutput(file.getParentFile());
		
		return file;
	}
}
