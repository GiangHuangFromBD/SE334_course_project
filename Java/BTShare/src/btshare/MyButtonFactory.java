package btshare;
import java.io.File;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.bluetooth.LocalDevice;
public class MyButtonFactory extends AbstractButtonFactory {    
    private Stage win;
    public MyButtonFactory()  { win = BluetoothJavaFXApplication.getPrimaryStage();  }
    @Override
    public Button createButton(BTDevice device){
       Button result = createDeviceItemMyButton(device);
    return result;
    }
    private MyButton createDeviceItemMyButton(BTDevice device) {
            MyButton b = new MyButton(device);
            b.updateDisplay();
            ContextMenu contextMenu = new ContextMenu();
            // Create MenuItems
            MenuItem item1 = new MenuItem("Gửi file");
            MenuItem item2 = new MenuItem("Đổi tên");
            MenuItem item3 = new MenuItem("Xóa thiết bị");
            item1.setOnAction(ev -> {
                if ( !ProcessorController.isBusy()) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Chọn file để gửi");
                File selectedFile = fileChooser.showOpenDialog(win);
                if (selectedFile != null) {   
                    System.out.println("File selected: " + selectedFile.getAbsolutePath());   
                    boolean validsize; if (selectedFile.length() <= 70000000) validsize = true; else validsize = false;
                    if (validsize ) sendFile(device, selectedFile.getAbsolutePath()); else AlertDialogWorker.showAlertDialog("toobig", win);
                }                
                } else AlertDialogWorker.showAlertDialog("busy", win);                          
            });
            item2.setOnAction(ev -> {
                showRenameDialog(win, b);            
            });                     
            item3.setOnAction( ev ->  {
                Platform.runLater( () ->{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xóa thiết bị?");
            alert.setHeaderText("Bạn muốn xóa thiết bị này?");            
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            alert.initModality(Modality.WINDOW_MODAL);

            alert.initOwner(win );
            ButtonType acceptButton = new ButtonType("Xóa");
            ButtonType declineButton = new ButtonType("Hủy");
            alert.getButtonTypes().setAll(acceptButton, declineButton);   
                alert.showAndWait().ifPresent(response -> {
                if (response == acceptButton) {   ( new Thread( () -> DatabaseWorker.deleteDevice(b.getBTDevice().getDeviceID())     )  ).start();
              VBox parent =(VBox) b.getParent(); parent.getChildren().remove(b);     
                }                 
            }); 

            }) ;             
            });           
            contextMenu.getItems().addAll(item1, item2, item3);
            b.setContextMenu(contextMenu);
                    b.setOnMouseClicked(ev -> {
                        b.getContextMenu().show(b,ev.getScreenX(),ev.getScreenY() );
                    });
                    b.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-border-style: solid; -fx-font-weight: bold;");
                    b.setAlignment(Pos.CENTER_LEFT);
                    b.setMaxWidth(Double.MAX_VALUE);
                    b.setMinWidth(550.0);
                    b.setMaxHeight(Double.MAX_VALUE);
                    b.setPadding(new Insets(5,0,0,0));
                    
            return b;
        }
    private void sendFile(BTDevice partner, String filePath) {            
            if(LocalDevice.isPowerOn())     ( new Thread( new MasterSender(partner, filePath) ) ).start();        
            else AlertDialogWorker.showAlertDialog("off", win);
        }   
    private void showRenameDialog(Window owner, Button bbutton) {
        MyButton button = (MyButton) bbutton;    
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Đổi tên thiết bị");
        dialog.setHeaderText("Nhập tên mới:");
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);

        dialog.showAndWait().ifPresent(result -> {
            System.out.println("Entered name: " + result);
            if (isValidName(result)) {
            button.getBTDevice().setHfName(result);  button.updateDisplay();
            ( new Thread( () -> DatabaseWorker.rename(button.getBTDevice().getDeviceID(), result)  ) ).start();           
            }  });
        }
        private boolean isValidName(String name) {
        if (name.toLowerCase().contains("delete")) return false;
        if (name.toLowerCase().contains("select")) return false;
        if (name.contains("*") || name.contains("=") || name.contains("-") ) return false;
        if(name.equals((""))) return false;
        return true;
        }
}
