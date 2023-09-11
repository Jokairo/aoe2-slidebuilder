package slidebuilder.data;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

public class DataFolderLocation {
	
	private File lastInputFolder;
	private File lastOutputFolder;
	private File projectPath;
	
	public DataFolderLocation() {
		//By default File Chooser opens 'User Documents' folder
		lastInputFolder = FileSystemView.getFileSystemView().getDefaultDirectory();
		lastOutputFolder = lastInputFolder;
		
		//Project path null by default (new project doesn't have a location where it will be saved)
		projectPath = null;
	}
	
	public File getLastOutput() {
		return lastOutputFolder;
	}
	
	public File getLastInput() {
		return lastInputFolder;
	}
	
	public File getProjectPath() {
		return projectPath;
	}
	
	public void setLastOutput(File f) {
		lastOutputFolder = f;
	}
	
	public void setLastInput(File f) {
		lastInputFolder = f;
	}
	
	public void setProjectPath(File f) {
		projectPath = f;
	}
}
