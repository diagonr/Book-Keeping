package alert;

import java.awt.Toolkit;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Book;

public class InvalidParametersAlert {
	private static Alert alert = new Alert(AlertType.ERROR);
	
	public static void show(Book book) {
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Parameters");
		alert.setContentText("Book parameters entered were invalid. Please enter valid parameters.");
		Runnable runnable = (Runnable)Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.default");
		if(runnable != null)
			runnable.run();
		alert.showAndWait();

	}	
	public static void show() {
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Parameters");
		alert.setContentText("Parameters entered were invalid. Please enter valid parameters.");
		Runnable runnable = (Runnable)Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.default");
		if(runnable != null)
			runnable.run();
		alert.showAndWait();

	}
}
