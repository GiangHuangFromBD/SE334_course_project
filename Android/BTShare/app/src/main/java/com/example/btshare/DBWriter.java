package com.example.btshare;

public abstract class DBWriter {                       // fileName, fileSize, length = 2
    final public void writeToDB(Object input,String[] args){
        BTDevice partner = getPartner(input);
        createDBRecord(partner,args);
    }
    protected abstract BTDevice getPartner(Object input);
    protected abstract void createDBRecord(BTDevice partner, String[] args1 );
}
