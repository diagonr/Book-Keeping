package confirm;

import java.awt.Toolkit;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import model.Book;

public class ExitConfirm {
	private static Alert alert = new Alert(AlertType.CONFIRMATION);
	
	public static ConfirmType show(Book book) {
		alert.setTitle("Alert");
		alert.setHeaderText("Exit");
		alert.setContentText("Are you sure you want to exit?");
		
		Runnable runnable = (Runnable)Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.default");
		if(runnable != null)
			runnable.run();
		
		ButtonType yesButton = new ButtonType("Yes", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(yesButton, cancelButton);

		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.get() == yesButton) {
			return ConfirmType.YES;
		} else {	//User chooses cancel
			return ConfirmType.CANCEL;
		}
	}
}
