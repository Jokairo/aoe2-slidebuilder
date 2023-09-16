package slidebuilder.controllers;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import slidebuilder.controllers.interfaces.ControllerStageInterface;
import slidebuilder.data.CustomImage;
import slidebuilder.data.DataManager;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.util.FileChooserUtil;
import slidebuilder.util.FileFormats;
import slidebuilder.util.FileUtil;

public class ControllerCreateCustomImage extends ControllerStageInterface {

	@FXML private HBox tableHbox;
	@FXML private TableView<CustomImage> tableView;
	@FXML private TableColumn<CustomImage, String> tableColumnName;
	@FXML private TableColumn<CustomImage, String> tableColumnPath;
	
	private CreatorEnum creatorEnum;
	
	//INIT
	@FXML
	public void initialize() {

		//Auto resize table horizontally and vertically when you resize window
		HBox.setHgrow(tableView, Priority.ALWAYS);
		VBox.setVgrow(tableHbox, Priority.ALWAYS);

		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnPath.setCellValueFactory(new PropertyValueFactory<>("path"));
		tableColumnName.setSortable(false);
		tableColumnPath.setSortable(false);
		
		tableColumnName.impl_setReorderable(false);
		tableColumnPath.impl_setReorderable(false);

	}
	
	public void initData(CreatorEnum ce) {
		//Save which type of images the user is going to add
		creatorEnum = ce;
		
		//Clear the table from possible previous values
		tableView.getItems().clear();
		
		//Add all the current images that the user has added
		if(ce == CreatorEnum.SLIDE_BG) {
			tableView.getItems().addAll(DataManager.getDataCampaign().getCustomImageData().getListCustomSlideshowBackground());
			slide_title_s.setText("Create Slideshow Background");
		} else if(ce == CreatorEnum.CAMPAIGN_BG) {
			tableView.getItems().addAll(DataManager.getDataCampaign().getCustomImageData().getListCustomCampaignBackground());
			slide_title_s.setText("Create Campaign Menu Background");
		} else if(ce == CreatorEnum.SLIDE_IMAGE) {
			tableView.getItems().addAll(DataManager.getDataCampaign().getCustomImageData().getListCustomSlideshowImage());
			slide_title_s.setText("Create Slideshow Image");
		} else {
			tableView.getItems().addAll(DataManager.getDataCampaign().getCustomImageData().getListCustomCampaignButton());
			slide_title_s.setText("Create Campaign Menu Button Image");
		}
	}
	
	private String createImageName(String name) {
		//Start from 2, so if there's already file called 'image.png' this will be called 'image.png2'
		int index = 2;
		String image_name = name;
		
		//Check if there is already a image with the same name, if there is add a number to the end of the name and check again, do until you find available name
		while(DataManager.getDataCampaign().getCustomImageData().getCustomImage(creatorEnum, image_name) != null) {
			image_name = name + index;
			index++;
		}
		return image_name;
	}
	
	private void saveCustomImage(File f) {
		
		int width = 0;
		int height = 0;
		
		//Check what the uncompressed dimension of the image is
		//If we create Image with the actual dimensions, it will take too much memory if the image is large
		try {
			Dimension dim = FileUtil.getImageDimension(f);
			width = (int)dim.getWidth();
			height = (int)dim.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		//Get image path and the name of the image file
		String path = f.getAbsolutePath();
		String name = f.getName();
		
		//Create a custom name for the image that is going to be used for example in the comboboxes where the user can select images
		String image_name = createImageName(name);
		
		CustomImage ci = new CustomImage(image_name, path, width, height, creatorEnum);
		tableView.getItems().add(ci);
		
		//Create the actual image
		ci.setImage();

		DataManager.getDataCampaign().getCustomImageData().saveCustomImage(creatorEnum, ci);
	}
	
	private boolean tableFileExists(File f) {
		for(CustomImage ci : tableView.getItems()) {
			if(ci.getPath().equals(f.getAbsolutePath()))
				return true;
		}
		return false;
	}
	
	private void saveFilesToTable(List<File> list) {
		
		//Place chosen files to the table
		
		for(File f : list) {
			
			//If file path is already in the table, don't add it to avoid duplicates
			if(tableFileExists(f)) continue;
			
			//Create and add the image to the table
			saveCustomImage(f);
		}
	}
	
	@FXML
	private void chooseFile(ActionEvent event) {
		FileChooserUtil fc = new FileChooserUtil();
		String fileFormat = FileFormats.FILE_FORMAT_PNG;
		String[] fileExtensions = {FileFormats.FILE_EXTENSION_PNG};
		List<File> f = fc.openFileMultiple(fileFormat, fileExtensions);

		if (f == null) return;
		
		saveFilesToTable(f);
	}

	
	@FXML
	private void deleteSelected(ActionEvent event) {
		if(tableView.getSelectionModel().getSelectedItem() != null) {
			CustomImage ci = tableView.getSelectionModel().getSelectedItem();
			tableView.getItems().remove(ci);
			
			DataManager.getDataCampaign().getCustomImageData().removeCustomImage(creatorEnum, ci);
		}
	}
}
