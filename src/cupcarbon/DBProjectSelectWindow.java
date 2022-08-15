package cupcarbon;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/** DBProjectSelectWindow Link dbprojectselect.fxml and construct the window.
 * @author Yiwei Yao
 * @deprecated It is deprecated, database now does not support muti-projects.
 *
 */
public class DBProjectSelectWindow {

	public DBProjectSelectWindow() throws IOException {
		Stage stage = new Stage();
		stage.setTitle("Select Project Name");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(SenScriptWindow.class.getResource("dbprojectselect.fxml"));
		BorderPane panneau = (BorderPane) loader.load();
		Scene scene = new Scene(panneau);
		stage.setScene(scene);

		//stage.initOwner(CupCarbon.stage);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}
}
