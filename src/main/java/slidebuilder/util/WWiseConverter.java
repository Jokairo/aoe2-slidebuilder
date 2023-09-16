package slidebuilder.util;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class WWiseConverter {
	
	private static ArrayList<String> temp_list = new ArrayList<>();
	
	public static String convert(ArrayList<String> file_list, String output) throws InterruptedException {

			String wwiseRoot = System.getenv("WWISEROOT");

			//Check that WWise is installed
			if(wwiseRoot == null) {
				popupFail("Cannot locate WWise, is it installed on your computer?");
				return null;
			}
		
			//Create temp WWise project that will be used for conversion
			String new_project_path = output+"/project/project.wproj";
			
			//Check that all the files exists, if not abort
			for(String s : file_list) {
				if(!FileUtil.fileExists(s)) {
					popupFail("File in location '" + s + "' doesn't exists.");
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

				//Write XML file that is needed for file conversions. Files in temp_list will be written to the XML.
				writeXML(temp_path);
				//Temp list not needed anymore, clear it
				temp_list.clear();
				
				//Add paths inside quotation marks, otherwise if the path contains spaces ' ' the command will not work
				String cmdProjectPath = "\""+new_project_path+"\"";
				String cmdTemp_path = "\""+temp_path+"\"";

				//Check which version of WWise the user has
				String path = wwiseRoot + "/Authoring/x64/Release/bin/WwiseConsole.exe";
				boolean newVersion  = FileUtil.fileExistsCaseInsensitive(path);

				//If user doens't have new CLI, check that they have the old version
				if(!newVersion) {
					path = wwiseRoot + "/Authoring/x64/Release/bin/wwisecli.exe";
					boolean hasWwise  = FileUtil.fileExistsCaseInsensitive(path);
					//Can't find old Wwise CLI, abort
					if(!hasWwise) {
						popupFail("Cannot locate WWise CLI. Make sure you have installed WWise with Authoring package and set the correct WWise environment variable.");
						return null;
					}
				}
				
				//Open WWISE cmd
				String s1 = "cd %WWISEROOT%/Authoring/x64/Release/bin";

				//Create a new temp project
				String s2 = "wwisecli.exe "+cmdProjectPath+" -CreateNewProject -Platform Windows";
				if(newVersion)
					s2 = "WwiseConsole create-new-project "+cmdProjectPath;

				//Convert the source files
				String s3 = "wwisecli.exe "+cmdProjectPath+" -ConvertExternalSources "+cmdTemp_path+"/aoe2slidebuilder.wsources -ExternalSourcesOutput "+cmdTemp_path+"/";
				if(newVersion)
					s3 = "WwiseConsole convert-external-source "+cmdProjectPath+" --source-file "+cmdTemp_path+"/aoe2slidebuilder.wsources --output "+cmdTemp_path+"/";

				Process p1 = Runtime.getRuntime().exec("cmd /c cmd.exe /K \""+s1+" && "+s2+" && exit\"");

				BufferedReader reader = new BufferedReader(new InputStreamReader(p1.getInputStream()));

				boolean isError = false;

				while (p1.isAlive()) {
					String line;
					while ((line = reader.readLine()) != null) {
						System.out.println(line);

						//Find specific WWise error, if the error happens then stop the process, otherwise the process will run forever
						if(line.contains("The provided project is not located in a folder of the same name")) {
							System.out.println("Process1 terminated");
							p1.destroy();
							isError = true;
							break;
						}
					}
				}

				//If process1 error, don't do process2
				if(!isError) {
					Process p2 = Runtime.getRuntime().exec("cmd /c cmd.exe /K \""+s1+" && "+s3+" && exit\"");

					while (p2.isAlive()) {

						String line;
						while ((line = reader.readLine()) != null) {
							System.out.println(line);

							//Find specific WWise error, if the error happens then stop the process, otherwise the process will run forever
							if(line.contains("The provided project is not located in a folder of the same name")) {
								System.out.println("Process2 terminated");
								p2.destroy();
								isError = true;
								break;
							}
						}
					}
				}
				
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

				if(isError)
					popupFail(null);

				return temp_path;
			}
			catch (IOException e) {
				e.printStackTrace();
				popupFail("File conversion failed.");
				return null;
			}
		
	}
	
	private static void deleteFolder(File dir) {

		//Cant delete if it doesn't exist
		if(!dir.exists()) return;

	    for (File file: dir.listFiles()) {
	        if (file.isDirectory())
	        	deleteFolder(file);
	        file.delete();
	    }
	    dir.delete();
	}
	
	private static void popupFail(String error) {
		UpdateUIFromOtherThread.call(() -> {
			if(error != null)
				Popup.showError(error);
			Popup.showError("Audio conversion failed, the exported project will not include any audio files.");
		});
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
