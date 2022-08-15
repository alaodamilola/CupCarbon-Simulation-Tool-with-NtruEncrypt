package cupcarbon;

import java.io.File;

import action.CupAction;
import action.CupActionBlock;
import action.CupActionModifSensorScriptFile;
import action.CupActionStack;
import device.DeviceList;
import device.SensorNode;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import map.MapLayer;
import project.Project;

public class CupCarbonContextMenu {

	public static boolean drag = false;

	public static void create(SwingNode sn, CupCarbonController controller) {
		ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Add a Sensor Node");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	controller.addSensor();
            }
        });
        MenuItem item2 = new MenuItem("Add a Marker");
        item2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	controller.addMarker();
            }
        });
        MenuItem item3 = new MenuItem("Add a Base Station");
        item3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	controller.addBaseStation();
            }
        });
        MenuItem item4 = new MenuItem("Add a Mobile");
        item4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	controller.addMobile();
            }
        });
        MenuItem item5 = new MenuItem("Route from Markers");
        item5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	controller.routeFromMarkers();
            }
        });

        MenuItem item6 = new MenuItem("SensScript Editor");
        item6.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	controller.openSenScriptWindow();
            }
        });
        MenuItem item7 = new MenuItem("Select All Sensors");
        item7.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	controller.selectAllSensors();
            }
        });


        /**
         * @author Bang tran
         */
        MenuItem item8 = new MenuItem("Save Network to DB");
        item8.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	controller.saveNetworkToDatabase();
            }
        });
        //============ Bang Tran - End

        /**
         * @author Bang tran
         */
        MenuItem item9 = new MenuItem("Reset/Reload from DB");
        item9.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	controller.reloadFromDatabase();
            }
        });

        SeparatorMenuItem menuSep4 = new SeparatorMenuItem();

        //============ Bang Tran - End


        SeparatorMenuItem menuSep1 = new SeparatorMenuItem();
        SeparatorMenuItem menuSep2 = new SeparatorMenuItem();
        SeparatorMenuItem menuSep3 = new SeparatorMenuItem();

        Menu assignScriptMenu = new Menu("Assign Scripts");
        String [] ls = getScriptFileNames();
        if(ls != null) {
        	MenuItem tmpItem = new MenuItem("");
        	tmpItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                	scriptFileApply("");
                	CupCarbon.cupCarbonController.getNodeInformations();
                }
            });
            assignScriptMenu.getItems().add(tmpItem);
	        for(String s : ls) {
	        	tmpItem = new MenuItem(s);
	            tmpItem.setOnAction(new EventHandler<ActionEvent>() {
	                @Override
	                public void handle(ActionEvent event) {
	                	scriptFileApply(s);
	                	CupCarbon.cupCarbonController.getNodeInformations();
	                }
	            });
	            assignScriptMenu.getItems().add(tmpItem);
	        }
        }

        //modified by Bang Tran
        contextMenu.getItems().addAll(item1, item7,
        								menuSep1, assignScriptMenu, item6,
        								menuSep3, item2, item5, menuSep2, item3, item4,
        								menuSep4, item8, item9  //Bang Tran added
        							);


        sn.setOnMouseDragged(new EventHandler<MouseEvent>() {
        	@Override
			public void handle(MouseEvent event) {
				if(event.getButton()==MouseButton.SECONDARY) {
					drag = true;
				}
			}
        });

        sn.setOnMouseReleased(new EventHandler<MouseEvent>() {
        	@Override
            public void handle(MouseEvent event) {
        		if(!drag)
        			if(event.getButton()==MouseButton.SECONDARY) {
        				if(MapLayer.lastKey==0)
        					contextMenu.show(sn, event.getScreenX(), event.getScreenY());
        			}
    				else
    					contextMenu.hide();
        		drag = false;
        		controller.updateLabeLInfos();
            }
        });
	}

	public static void scriptFileApply(String newScriptFileName) {
		CupActionBlock block = new CupActionBlock();
		for (SensorNode sensor : DeviceList.sensors) {
			if (sensor.isSelected()) {
				String currentScriptFileName = sensor.getScriptFileName();
				CupAction action = new CupActionModifSensorScriptFile((SensorNode) sensor, currentScriptFileName, newScriptFileName);
				block.addAction(action);
			}
		}
		CupActionStack.add(block);
		CupActionStack.execute();
		MapLayer.repaint();
	}

	public static String [] getScriptFileNames() {
		File scriptFiles = new File(Project.getProjectScriptPath());
		String [] list = scriptFiles.list();
		if(list!=null) {
			String rList = "";
			for(String s : list) {
				if(!s.startsWith("."))
					rList+= s+" ";
			}
			return rList.split(" ");
		}
		return null;
	}

}
