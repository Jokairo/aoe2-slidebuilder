package slidebuilder.components;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import slidebuilder.data.DataManager;
import slidebuilder.util.FileChooserUtil;
import slidebuilder.util.FileFormats;
import slidebuilder.util.FileUtil;

public class TextFieldFile {
	private TextField textField;
	private Button buttonBrowse;
	private Button buttonCancel;
	private Label textFieldTitle;
	private Label error;
	
	private VBox container;
	private HBox errorContainer;
	private String fileFormat = FileFormats.FILE_FORMAT_AUDIO;
	private String[] fileExtensions = {FileFormats.FILE_EXTENSION_MP3};
	
	private ImageView warningImage;
	
	public TextFieldFile() {
		textField = new TextField();
		buttonBrowse = new Button();
		buttonCancel = new Button();
		textFieldTitle = new Label();
		error = new Label();
		
		//Warning image
		Image icon = new Image(getClass().getResource("/images/warning.png").toString(), 25, 25, false, false);
		warningImage = new ImageView(icon);

		//Create containers
		container = new VBox();
		HBox textFieldContainer = new HBox();
		errorContainer = new HBox();
		
		//Add components to containers
		container.getChildren().addAll(textFieldTitle, textFieldContainer, errorContainer);
		textFieldContainer.getChildren().addAll(textField, buttonBrowse, buttonCancel);
		errorContainer.getChildren().addAll(warningImage, error);
		
		//TextField title
		textFieldTitle.setPrefSize(354, 20);
		textFieldTitle.setAlignment(Pos.CENTER);
		
		//Textfield
		textField.setEditable(false);
		textField.setPrefWidth(230.0);
		textField.setPrefHeight(26.0);
		
		//Button browse
		buttonBrowse.setPrefWidth(86.0);
		buttonBrowse.setPrefHeight(27.0);
		buttonBrowse.setText("Browse...");
		buttonBrowse.setMnemonicParsing(false);
		buttonBrowse.setOnAction(e -> {
			chooseFile(e);
		});
		
		//Button cancel
		buttonCancel.setPrefWidth(36.0);
		buttonCancel.setPrefHeight(27.0);
		buttonCancel.setText("✕");
		buttonCancel.setMnemonicParsing(false);
		buttonCancel.setOnAction(e -> {
			removeFile(e);
		});
		
		//Error message
		error.setText("Invalid file path.");
		
		container.setAlignment(Pos.CENTER);
		textFieldContainer.setAlignment(Pos.CENTER);
		textFieldContainer.setSpacing(10);
		errorContainer.setAlignment(Pos.CENTER);
		errorContainer.setSpacing(10);
		
		//Error not visible by default
		errorContainer.setVisible(false);
	}
	
	public void setTitle(String title) {
		textFieldTitle.setText(title);
	}
	
	public void setFileExtensions(String[] stringList) {
		fileExtensions = stringList;
	}
	
	public void setFileFormat(String string) {
		fileFormat = string;
	}
	
	private void chooseFile(ActionEvent event) {
		FileChooserUtil fcu = new FileChooserUtil();
		File f = fcu.openFileSingle(fileFormat, fileExtensions);
		
		if (f == null) return;
		
		String path_text = f.getAbsolutePath();
		setTextFieldString(path_text);
		errorContainer.setVisible(false);
	}
	
	private void removeFile(ActionEvent event) {
		textField.setText(null);
		textField.positionCaret(0);
		errorContainer.setVisible(false);
	}
	
	//Show error message if file path doesn't exist anymore
	public boolean filePathInvalid() {
		if(textField.getText() != null && !textField.getText().isEmpty()) {
			boolean pathValid = FileUtil.fileExists(textField.getText());
			if (pathValid) {
				errorContainer.setVisible(false);
				return false;
			}
			else {
				errorContainer.setVisible(true);
				return true;
			}
		}
		//No file selected, no need to show error
		errorContainer.setVisible(false);
		return false;
	}
	
	public TextField getTextField() {
		return textField;
	}
	
	public String getTextFieldString() {
		return textField.getText();
	}
	
	public void setTextFieldString(String text) {
		textField.setText(text);
		if(text != null)
			textField.positionCaret(text.length());
	}
	
	public void setTextFieldDisabled(boolean b) {
		textField.setDisable(b);
		buttonBrowse.setDisable(b);
		buttonCancel.setDisable(b);
	}
	
	public VBox getContainer() {
		return container;
	}
}
