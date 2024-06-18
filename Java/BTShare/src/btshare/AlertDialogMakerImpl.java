/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package btshare;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class AlertDialogMakerImpl implements AlertDialogMaker  {
    @Override
    public void showAlertDialog(String code, Stage s) {
         if (code.equals("invalidqr") ) showInvalidQRDialog(s);
         if (code.equals("unbonded") ) showUnbondedDeviceDialog(s);
         if (code.equals("busy") ) showBusyDeviceDialog(s);   
         if (code.equals("off") ) showBluetoothOffError(s);
         if (code.equals("notadded") ) showNotAddedDialog(s);
         if (code.equals("toobig") ) showFileTooBigDialog(s);
    }    
    private void showInvalidQRDialog(Stage stage){        
        // Show the dialog
        Platform.runLater( () ->{
            Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi!");
        alert.setHeaderText("QR vừa quét không hợp lệ.");
        // Set modality and owner
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(stage);     
          alert.showAndWait() ;
          stage.close();
         });    
    }
     private void showBluetoothOffError (Window owner) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Bluetooth đang tắt!");
        alert.setHeaderText("Vui lòng bật bluetooth rồi thử lại!");
        // Set modality and owner
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(owner);
        // Show the dialog
        alert.showAndWait();       
    }
    private void showUnbondedDeviceDialog(Stage stage){
        
        // Show the dialog
        Platform.runLater( () ->{ 
            Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Chưa ghép đôi");
        alert.setHeaderText(" Thiết bị này chưa được ghép đôi. ");
        // Set modality and owner
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(stage);
            alert.showAndWait(); 
            stage.close();
                });    
    }
    private void showBusyDeviceDialog(Stage stage){
        
        // Show the dialog
        Platform.runLater( () ->{ 
            Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Không thể nhận!");
        alert.setHeaderText(" Thiết bị đang bận, không thể nhận file! ");
        // Set modality and owner
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(stage);
            alert.showAndWait(); 
            stage.close();
                });    
    }
        private void showNotAddedDialog(Stage stage){        
        // Show the dialog
        Platform.runLater( () ->{ 
            Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Không thể nhận!");
        alert.setHeaderText(" Thiết bị chưa được thêm vào danh sách! ");
        // Set modality and owner
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(stage);
            alert.showAndWait(); 
            stage.close();
                });    
    }
         private void showFileTooBigDialog (Window window) {
         Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("File không hợp lệ!");
        alert.setHeaderText("Kích cỡ file đã chọn quá lớn (vượt 70 MB).");
        // Set modality and owner
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(window);
        // Show the dialog
        alert.showAndWait();        
        }    
}
