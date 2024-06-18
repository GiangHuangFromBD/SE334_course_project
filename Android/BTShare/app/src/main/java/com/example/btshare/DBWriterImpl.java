package com.example.btshare;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class DBWriterImpl extends DBWriter{
    @Override
    protected BTDevice getPartner(Object input) {
        if ( input instanceof  BTDevice) { BTDevice de = (BTDevice) input; return de;    }
        if ( input instanceof  String  ) {
            BTDevice partner = DatabaseWorker.getDevice((String) input  );
            return partner;
        }
        return null;
    }
    @Override
    protected void createDBRecord(BTDevice partner, String[] args) {
        BTTask task = new BTTask();
        int taskNumber = DatabaseWorker.getMaxTaskNumber() + 1;
        String taskID = "";
        if (taskNumber < 10) taskID = "000" + taskNumber;
        else if (taskNumber < 100) taskID = "00" + taskNumber;
        else taskID = "0" + taskNumber;

        String btaddr = partner.getBtAddr();
        String hfName = partner.getHfName();
        int size = Integer.parseInt(args[1]);
        String time = getDateTimeString();

        task.setTaskID(taskID); task.setSend( Integer.parseInt(args[2])  == 1 );
        task.setBtAddr(btaddr); task.setHfName(hfName);
        task.setFileName(args[0]); task.setSize(size);
        assert time != null;
        task.setTime(time);
        DatabaseWorker.insertNewTask(task);
    }
    private String getDateTimeString() {
        LocalDateTime now = null;
        DateTimeFormatter formatter = null;
        now = LocalDateTime.now();
        formatter = DateTimeFormatter.ofPattern("yyyy MM dd HH:mm");
        return now.format(formatter);
    }
}