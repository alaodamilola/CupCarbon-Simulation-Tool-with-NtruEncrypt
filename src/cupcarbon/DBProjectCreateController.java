package cupcarbon;

import java.net.URL;
import java.util.ResourceBundle;

import database.ImportToDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.Project;

/**
 * DBProjectCreateController control the windows of dbprojectcreate.fxml
 * @author Yiwei Yao
 * @deprecated It is deprecated, database now does not support muti-projects.
 */
public class DBProjectCreateController implements Initializable {

	@FXML
	private TextField projectName;

	@FXML
	private Button done;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	/**
	 * if the project is not set.
	 * for example, the project is not created or imported
	 * then create the project and set project path to database mode and save to db
	 * otherwise save to db directly.
	 */
	@FXML
	public void save() {
		boolean success = false;
		if(!projectName.getText().equals("")) {
			String name = projectName.getText();
			name = name.trim();
			name = name.replaceAll(" ", "");
			Project.projectName = name;
			Project.projectPath = "DataBase Mode";
			CupCarbon.stage.setTitle("CupCarbon " + CupCarbonVersion.VERSION + " [" + Project.projectPath + "]" + " (" + Project.DBFilePath + ")");
			ImportToDB.saveProjectToDB();
			success = true;
		}
		if(success) {
		    Stage stage = (Stage) done.getScene().getWindow();
		    stage.close();
		    Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning!");
			alert.setHeaderText(null);
			alert.setContentText("Operation is successed!");
			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning!");
			alert.setHeaderText(null);
			alert.setContentText("Operation is not successed!");
			alert.showAndWait();
		}
	}

}
