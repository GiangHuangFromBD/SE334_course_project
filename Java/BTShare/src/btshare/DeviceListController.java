package btshare;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Window;
import javax.bluetooth.LocalDevice;
import javafx.scene.control.Button;
public class DeviceListController implements Initializable {
    @FXML
    ScrollPane devicesSPane;
    private List<BTDevice> deviceList;
    private AbstractButtonFactory bf;    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bf = new MyButtonFactory();
        deviceList = loadBTDeviceList();        
        VBox vb =(VBox) devicesSPane.getContent();               
          
                for (int i = 0; i < deviceList.size(); i++) {
                    Button b = bf.createButton(deviceList.get(i));                            
                    vb.getChildren().add(b);
                }
    }           
     private List<BTDevice> loadBTDeviceList() {         
         AtomicReference < List<BTDevice >> atomicRefDeviceList = new AtomicReference<>() ;   
         
         CountDownLatch latch = new CountDownLatch(1);
         Runnable loadJob = () ->  {      
         atomicRefDeviceList.set( DatabaseWorker.loadAllDevices() );    
         latch.countDown();  
     };
         ( new Thread(loadJob) ).start();      
      try { latch.await(); } catch (InterruptedException ie) { return atomicRefDeviceList.get();  }
      
         System.out.println("BTDevice list loaded successfully...");
         return atomicRefDeviceList.get();
     }         
}
