package slidebuilder.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class WWiseConverter {
	
	private static ArrayList<String> temp_list = new ArrayList<>();
	
	public static String convert(ArrayList<String> file_list, String output) throws InterruptedException {
			
			//Check that WWise is installed
			if(System.getenv("WWISEROOT") == null) {
				Popup.showError("Cannot locate WWise, is it installed in your computer?");
				popupFail();
				return null;
			}
		
			//Create temp WWise project that will be used for conversion
			String new_project_path = output+"/project/project.wproj";
			
			//Check that all the files exists, if not abort
			for(String s : file_list) {
				if(!FileUtil.fileExists(s)) {
					Popup.showError("File in location '"+s+"' doesn't exists.");
					popupFail();
					return null;
				}
			}
			
			try {
			
				String temp_path = output;
				
				File temp_file = new File(temp_path);
				
				if(!temp_file.exists()) {
					temp_file.mkdir();
				}
				
				
				//Copy the audio files to the output folder, when they are all in one place they can be easily converted
				for(String s : file_list) {
					File f = new File(s);
					FileUtil.copyFile(f, new File(temp_path+f.getName()));
					temp_list.add(f.getName());
				}
				
				writeXML(temp_path);
				temp_list.clear();
				
				//Add paths inside quotation marks, otherwise if the path contains spaces ' ' the command will not work
				String cmdProjectPath = "\""+new_project_path+"\"";
				String cmdTemp_path = "\""+temp_path+"\"";
				
				//Open WWISE cmd
				String s1 = "cd %WWISEROOT%/Authoring/x64/Release/bin";
				//Create a new temp project
				String s2 = "wwisecli.exe "+cmdProjectPath+" -CreateNewProject -Platform Windows";
				//Convert the source files
				String s3 = "wwisecli.exe "+cmdProjectPath+" -ConvertExternalSources "+cmdTemp_path+"/aoe2slidebuilder.wsources -ExternalSourcesOutput "+cmdTemp_path+"/";
				
				Process p1 = Runtime.getRuntime().exec("cmd /c cmd.exe /K \""+s1+" && "+s2+" && exit\"");
				p1.waitFor();
				
				Process p2 = Runtime.getRuntime().exec("cmd /c cmd.exe /K \""+s1+" && "+s3+" && exit\"");
				p2.waitFor();
				
				System.out.println("conversion finished");
				
				//Delete the copied audio files as they are not needed anymore
				File[] contents = temp_file.listFiles();
			    if (contents != null) {
			        for (File f : contents) {
			            f.delete();
			        }
			    }
			    //Delete the folder where the copied audio files were
			    temp_file.delete();
			    
			    //Delete the WWise project folder
			    File project_folder = new File(new_project_path);
			    deleteFolder(project_folder.getParentFile());
				
				return temp_path;
			}
			catch (IOException e) {
				e.printStackTrace();
				Popup.showError("File conversion failed.");
				return null;
			}
		
	}
	
	private static void deleteFolder(File dir) {
	    for (File file: dir.listFiles()) {
	        if (file.isDirectory())
	        	deleteFolder(file);
	        file.delete();
	    }
	    dir.delete();
	}
	
	private static void popupFail() {
		Popup.showError("Audio conversion failed, the exported project will not include any audio files.");
	}
	
	private static void writeXML(String output_path) {
		
		
		try {
			File file = new File(output_path+"aoe2slidebuilder.wsources");
			
			if(!file.exists()) {
				file.createNewFile();
			}
			
			PrintWriter pw = new PrintWriter(file);
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			pw.println("<ExternalSourcesList SchemaVersion=\"1\" Root=\""+output_path+"\">");
			
			for(String name : temp_list) {
				pw.println("<Source Path=\""+name+"\" Conversion=\"Vorbis Quality High\" />");
			}

			pw.println("</ExternalSourcesList>");
			pw.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
