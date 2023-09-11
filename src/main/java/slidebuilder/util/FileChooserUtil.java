package slidebuilder.util;

import java.io.File;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import slidebuilder.Main;
import slidebuilder.data.DataManager;

public class FileChooserUtil {
	
	public File loadProject() {
		File f = openFileSingle(FileFormats.FILE_FORMAT_PROJECT, new String[] {FileFormats.FILE_EXTENSION_PROJECT});
		if (f == null)
			return null;
		
		//Set project path
		DataManager.getDataFolderLocation().setProjectPath(f);
		
		return f;
	}
	
	public File openFileSingle(String fileFormat, String[] fileExtensions) {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter(fileFormat, fileExtensions));
		fc.setInitialDirectory(DataManager.getDataFolderLocation().getLastInput());
		
		//Choosing 1 file
		File f = fc.showOpenDialog(Main.getStage());
		
		if (f == null) 
			return null;
		
		//Set input location the same location as the selected file
		DataManager.getDataFolderLocation().setLastInput(f.getParentFile());
		return f;
	}
	
	public List<File> openFileMultiple(String fileFormat, String[] fileExtensions) {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter(fileFormat, fileExtensions));
		fc.setInitialDirectory(DataManager.getDataFolderLocation().getLastInput());
		

		//Choosing multiple files
		List<File> list = fc.showOpenMultipleDialog(Main.getStage());
		
		if (list == null) 
			return null;
		
		//Set input location the same as the location of the last selected file
		DataManager.getDataFolderLocation().setLastInput(list.get(list.size()-1).getParentFile());
		return list;
	}
}
