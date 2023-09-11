package slidebuilder.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataFileManager {
	public static void saveToFile(String path) {
		File f = new File(path);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(DataManager.getDataCampaign());
			fos.close();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadFromFile(String path) {
		File f = new File(path);
		FileInputStream fis;
		try {
			fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			DataCampaign dc = (DataCampaign) ois.readObject();
			DataManager.setDataCampaign(dc);
			fis.close();
			ois.close();
			System.out.println(DataManager.getDataCampaign().getCampaignName());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
