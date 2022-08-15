package cupcarbon;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This is the Window Class for New User creation Window
 * @author Bang Tran
 *
 */
public class NewUserWindow {
	public NewUserWindow() throws IOException {
		Stage stage = new Stage();
		stage.setTitle("Create new user");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(SenScriptWindow.class.getResource("newuser.fxml"));
		BorderPane panneau = (BorderPane) loader.load();
		Scene scene = new Scene(panneau);
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}
}
