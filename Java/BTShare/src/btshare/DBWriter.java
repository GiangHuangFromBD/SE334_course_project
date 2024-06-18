/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package btshare;

public abstract class DBWriter {                       // fileName, fileSize, length = 2
final public void writeToDB(Object input,String[] args){
    BTDevice partner = getPartner(input);
    createDBRecord(partner,args);
}
protected abstract BTDevice getPartner(Object input);
protected abstract void createDBRecord(BTDevice partner, String[] args1 );    
}
