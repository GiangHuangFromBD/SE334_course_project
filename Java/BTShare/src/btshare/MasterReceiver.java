package btshare;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

public class MasterReceiver implements Runnable {

    private String remoteAddress;
    private String filename;
    private int receivedSize; //// fileSize
    private String[] uuids;
    private DBWriter writer;
    public  MasterReceiver (String rA, String[] uu, String fN, int fS){
        remoteAddress = rA; filename = fN;
            receivedSize = fS;
            uuids = uu;  writer = new DBWriterImpl();
    }
@Override
public void run() {
    byte[] array = new byte[receivedSize];
    int cores = uuids.length;
    ProcessorController.occupied();

    ArrayList <Thread> myWorker = new ArrayList<Thread>();
    int segSize = receivedSize / cores;
    PBarUpdater up = new PBarUpdater(receivedSize);
    try {
    for (int i = 0; i < cores; i++) {
        Thread mW;
        if ( i < cores - 1 )
            mW = new Thread( new Receiver(remoteAddress, UUID.fromString(uuids[i]),
            i*segSize, segSize, array ,up ));
        else
            mW = new Thread( new Receiver(remoteAddress, UUID.fromString(uuids[i]),
            i*segSize, receivedSize - i * segSize, array, up ));
        mW.start(); myWorker.add(mW);
    }
        for (int i = 0; i < cores; i++) myWorker.get(i).join();
    } catch (InterruptedException ie) {
        ProcessorController.cancelTransferring();
        return; }
    /////  WAIT UNTIL THOSE SINGLE RECEIVERS FINISH THEIR JOBS
    if ( !ProcessorController.hasError()) {
        File file = checkDuplicateFile();
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(array, 0, receivedSize);
            fos.flush();
            fos.close();
            
            String[] args = new String[] { filename , Integer.toString(receivedSize), "0" } ;            
            writer.writeToDB(remoteAddress,args);
            HistoryTabPaneUpdater.checkHistoryPane("Nhận");
        } catch (IOException ie) {
            ie.getMessage();
        }
        }
        ProcessorController.finishTransferring();
}
private File checkDuplicateFile() {
    String folderString = "D:/BTShare";    
    File myfolder = new File (folderString);
    if ( !myfolder.exists()) myfolder.mkdir();

    File file = new File(folderString + "/" + filename);
    while (file.exists()) {
        filename ="(1)" + filename;
        file = new File(folderString + "/" + filename);
    }
    return file;
}
}//END CLASS