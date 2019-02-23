package controller;

import java.io.IOException;
import java.net.URL;

import gateway.DataSource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

// CS 4743 Assignment 4 by Devin Nguyen

public class Launcher extends Application {
	
	@Override
	public void start(Stage stage) throws IOException {
		stage.setTitle("Book Inventory");
		URL viewURL = this.getClass().getResource("/view/MainView.fxml");
		FXMLLoader loader = new FXMLLoader(viewURL);
		DataSource.getDs().setURL("jdbc:mysql://easel2.fulgentcorp.com:3306/jgl270");
		DataSource.getDs().setUser("jgl270");
		DataSource.getDs().setPassword("tJegLCknkPA7AAgPZ86U");
		loader.setController(new MenuViewController());
		BorderPane rootBorderPane = loader.load();
		ViewSwitcher.getInstance().setMenuBorderPane(rootBorderPane);
		Scene scene = new Scene(rootBorderPane);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
