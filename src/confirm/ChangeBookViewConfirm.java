package confirm;

import java.awt.Toolkit;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import model.Book;

public class ChangeBookViewConfirm {
	private static Alert alert = new Alert(AlertType.CONFIRMATION);
	
	public static ConfirmType show(Book book) {
		alert.setTitle("Alert");
		alert.setHeaderText("Leaving Book");
		alert.setContentText("Do you want to save?");
		
		Runnable runnable = (Runnable)Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.default");
		if(runnable != null)
			runnable.run();
		
		ButtonType yesButton = new ButtonType("Yes");
		ButtonType noButton = new ButtonType("No");
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);

		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.get() == yesButton) {
			return ConfirmType.YES;
		} else if (result.get() == noButton) {
			return ConfirmType.NO;
		} else {	//User chooses cancel
			return ConfirmType.CANCEL;
		}
	}
}
