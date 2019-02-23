package alert;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.awt.Toolkit;

import model.Book;

public class ModifiedBookAlert {
	
	private static Alert alert = new Alert(AlertType.ERROR);
	
	public static void show(Book book) {
		alert.setTitle("Error");
		alert.setHeaderText("Book was Modified");
		alert.setContentText("The current book was modified. Changes were discarded. \nSelect the book again to see changed details.");
		Runnable runnable = (Runnable)Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.default");
		if(runnable != null)
			runnable.run();
		alert.showAndWait();

	}
	

	
}
