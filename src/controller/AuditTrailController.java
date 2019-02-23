package controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import model.AuditTrailEntry;
import model.Book;

public class AuditTrailController {
	
	

    @FXML
    private TitledPane titledPaneAudit;
    
	@FXML
	private ListView<AuditTrailEntry> auditList;
	
	@FXML
	private Button backButton;
	
	@FXML
	private static Logger logger = LogManager.getLogger(MenuViewController.class);
	
	ObservableList<AuditTrailEntry> auditTrailEntries = FXCollections.observableArrayList();
	
	public AuditTrailController(ObservableList<AuditTrailEntry> auditTrailEntries) {
		this.auditTrailEntries = auditTrailEntries;
	}
	@FXML
	public void initialize() {
		auditList.getItems().addAll(auditTrailEntries);
		this.titledPaneAudit.setCollapsible(false);
		logger.info("Books printed: " + auditTrailEntries);
	}
    @FXML
    void itemClick(MouseEvent event) {
    	if (!auditList.getSelectionModel().isEmpty() && event.getClickCount() == 2) {
	    	logger.info("Audit " + auditList.getSelectionModel().getSelectedItem() + " Selected. Details Shown.");
	    	ViewSwitcher.setAudit(auditList.getSelectionModel().getSelectedItem());
    		auditList.getSelectionModel().clearSelection();
    		ViewSwitcher.getInstance().switchView(3);
    	} 
    }
    @FXML
    void onBackClick(MouseEvent event) {
    	ViewSwitcher.getInstance().switchView(2);
    }
}
