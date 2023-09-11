package slidebuilder.controllers.interfaces;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import slidebuilder.data.DataManager;

public abstract class TabControllerInterface extends ControllerDataInterface {
	
	private int tab_size = 1;
	private int selectedTabIndex = 0;
	private int owner_id = -1;

	@FXML private TabPane tab_pane;
	@FXML private Label slide_title;

	
	protected Tab getCurrentTab() {
		return tab_pane.getSelectionModel().getSelectedItem();
	}
	
	protected int getCurrentTabIndex() {
		return tab_pane.getSelectionModel().getSelectedIndex();
	}
	
	public TabPane getTabPane() {
		return tab_pane;
	}
	
	public void setTabSize(int i) {
		tab_size = i;
	}
	
	public int getOwnerId() {
		return owner_id;
	}
	
	public void setOwnerId(int owner_id) {
		this.owner_id = owner_id;
	}
	
	public void initData() {
		selectedTabIndex = 0;
		tab_pane.getTabs().clear();
		
		//Init title
		setTitle();
		
		for(int i=0; i < tab_size; i++) {
			Tab tab = new Tab();
			setTabName(tab, i);
			tab_pane.getTabs().add(tab);

			//Set default values if there arent any
			setTabDefaultValues(i);
			
			tab.setOnSelectionChanged(new EventHandler<Event>() {
	            @Override
	            public void handle(Event t) {
	                if (tab.isSelected()) {
	                	
	                	saveCurrentData(selectedTabIndex);
	                	
	                	//Save changed tab index
	                	selectedTabIndex = getCurrentTabIndex();
	                	
	                	loadData(selectedTabIndex);
	                	
	                	DataManager.globalTabIndex = selectedTabIndex;

	                	//When changing tabs, already load data for subcontroller
	                	//This way the preview will automatically update as you change tabs
	                	if (getSubController() != null) {
	                		getSubController().sceneIn();
	                	}
	                }
	            }
	        });
		}
	}
	
	public void loadData(int index) {
		loadCurrentData(index);
		setTitle();
		setDisabledValues();

    	//already load data for subcontroller
    	//This way the preview will automatically update as you change tabs
    	if (getSubController() != null) {
    		DataManager.globalTabIndex = selectedTabIndex;
    		getSubController().sceneIn();
    	}
	}
	
	@Override
	public void loadData() {}
	
	@Override
	public void saveCurrentData() {
		saveCurrentData(getCurrentTabIndex());
	}
	
	public abstract void loadCurrentData(int index);
	public abstract void saveCurrentData(int index);
	protected abstract void setTabDefaultValues(int index);
	protected abstract void setDisabledValues();
	protected abstract void setTitle();
	protected abstract void setTabName(Tab tab, int index);
}
