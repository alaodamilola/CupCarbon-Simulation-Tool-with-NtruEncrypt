package cupcarbon;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import user.User;
import user.UserList;


/**
 * This is the Controller Class for New User creation Window
 * @author Bang Tran
 *
 */
public class NewUserWindowController  implements Initializable {

	@FXML
	private TextField txtUserName;
	@FXML
	private Button buttonDone;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	@FXML
	public void save() {
		boolean success = false;
		String name = txtUserName.getText();

		if(!name.trim().equals("")) {
			success = true;

			User u = new User(name);
			UserList.users.add(u);
		}

		if(success) {
		    Stage stage = (Stage) buttonDone.getScene().getWindow();
		    stage.close();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Can not create the user.");
			alert.showAndWait();
		}
	}
}
