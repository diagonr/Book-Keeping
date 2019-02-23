package alert;

import java.awt.Toolkit;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Book;

public class DeletedBookAlert {
	private static Alert alert = new Alert(AlertType.ERROR);
	
	public static void show(Book book) {
		alert.setTitle("Error");
		alert.setHeaderText("Book was Deleted");
		alert.setContentText("The current book was deleted. Changes were discarded.");
		Runnable runnable = (Runnable)Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.default");
		if(runnable != null)
			runnable.run();
		alert.showAndWait();

	}
}
