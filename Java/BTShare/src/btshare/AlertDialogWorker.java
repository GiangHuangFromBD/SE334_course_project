/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package btshare;
import javafx.stage.Stage;
public class AlertDialogWorker {
private static AlertDialogMaker instance;
public static void initialize() {
    if ( instance == null) {
        instance = new AlertDialogMakerImpl();
    }   
} 
public static void showAlertDialog(String code, Stage s) {
    instance.showAlertDialog(code, s);
}    
}
