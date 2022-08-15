package cupcarbon;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Yiwei Yao
 * 
 * 
 *create a window with result
 */
public class ResultWindow {
	
	public ResultWindow() throws IOException {
		Stage stage = new Stage();
		stage.setTitle("Result");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(SenScriptWindow.class.getResource("result.fxml"));
		BorderPane panneau = (BorderPane) loader.load();
		Scene scene = new Scene(panneau);
		stage.setScene(scene);		
		//stage.initOwner(CupCarbon.stage);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}
}
