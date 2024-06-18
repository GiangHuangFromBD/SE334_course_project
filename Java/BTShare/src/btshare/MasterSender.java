package btshare;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

public class MasterSender implements Runnable {
    private static final String serviceName = "RFCOMM";
    private BTDevice partner;
    private String filePath;
    private DBWriter writer;
    public MasterSender(BTDevice p, String fName) { partner = p; filePath =fName; writer = new DBWriterImpl();    }
    @Override
    public void run() {
        ProcessorController.occupied();
        int this_cores = Runtime.getRuntime().availableProcessors() - 3;
        int cores; if (partner.getCores() < this_cores) cores = partner.getCores(); else cores = this_cores;             
        
         ArrayList<UUID> UUIDal = new ArrayList<>();         
           for (int i = 1; i <= cores; i++) UUIDal.add(UUID.randomUUID());            
            String[] uuidString = new String[UUIDal.size()];
            
            for (int i = 0; i < uuidString.length; i++) uuidString[i] = UUIDal.get(i).toString().replace("-", "");
            String thisAddr = getThisAddr();
            String file_name = ( new File(filePath) ).getName();
            int file_size = (int) ( new File(filePath) ).length();            
            
            String sendString = "2::" + thisAddr + "::" + file_name + "::"
                + file_size  ;                        
        for (int i = 0; i < cores; i++) sendString +="::" + UUIDal.get(i).toString();
        ProcessorController.setSendString(sendString);          
            
        Thread[] myWorker = new Thread[cores];
        File file = new File (filePath );
        PBarUpdater updater = new PBarUpdater((int) file.length());
        
        int segSize =(int) file.length() / cores;
        for (int i = 0; i < cores; i++) {
            if ( i < cores - 1 )
                myWorker[i] = new Thread( new Sender(uuidString[i],serviceName,
                        filePath,i * segSize,segSize,updater,partner )  );
            else
                myWorker[i] = new Thread( new Sender(uuidString[i],serviceName,
                        filePath, i * segSize,
                        (int)file.length() - i * segSize,updater, partner  )  );
            myWorker[i].start();
        }               
        try {
            for (int i = 0; i < cores; i++) myWorker[i].join();
        }
        catch (InterruptedException ie) {
            ProcessorController.cancelTransferring();
            ie.printStackTrace();
        }
        if ( !ProcessorController.hasError()) { 
            String[] args = new String[] {  ( new File(filePath) ).getName(),
                Integer.toString( (int) ( new File(filePath) ).length()  ), "1"   } ;
            writer.writeToDB(partner,args);  
            
            HistoryTabPaneUpdater.checkHistoryPane("Gửi");  }      
         ProcessorController.finishTransferring();        
    }
    private String getThisAddr() {    
        String b = "";       
        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();
        b = localDevice.getBluetoothAddress();
        }
        catch (Exception e) { e.printStackTrace();  return b; }
        String standardAddr = "" + b.charAt(0) + b.charAt(1) + ":" + b.charAt(2) + b.charAt(3) + ":"
                + b.charAt(4) + b.charAt(5) + ":" + b.charAt(6) + b.charAt(7) + ":" + b.charAt(8) + b.charAt(9) + ":"
                + b.charAt(10) + b.charAt(11);
         return standardAddr;             
    }         
}
