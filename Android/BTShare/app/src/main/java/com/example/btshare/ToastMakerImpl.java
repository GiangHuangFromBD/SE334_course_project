package com.example.btshare;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
public class ToastMakerImpl implements ToastMaker {
@Override
public void showToast(String code, Context ct) {
    if (code.equals("invalidqr")) invalidQRToast(ct);
    if (code.equals("unbonded")) unbondedDeviceToast(ct);
    if (code.equals("busy")) busyDeviceToast(ct);
    if (code.equals("notadded")) notAddedDeviceToast(ct);
    if (code.equals("off")) bluetoothOffToast(ct);
    if (code.equals("toobig")) fileTooBigToast(ct);
}
    private void bluetoothOffToast(Context ct) {
        Toast.makeText(ct,"Bluetooth đang tắt!",Toast.LENGTH_SHORT).show();
    }
    private void invalidQRToast(Context ct) {
        Toast.makeText(ct,
                "QR không hợp lệ!",
                Toast.LENGTH_LONG).show();
    }

    private void unbondedDeviceToast(Context ct) {
        Toast.makeText(ct,
                "Thiết bị này chưa được ghép đôi.",
                Toast.LENGTH_LONG).show();
    }
    private void busyDeviceToast(Context ct) {
        Toast.makeText(ct,
                "Thiết bị đang bận, không thể nhận file!",
                Toast.LENGTH_LONG).show();
    }
    private void notAddedDeviceToast(Context ct) {
        Toast.makeText(ct ,
                "Thiết bị này chưa được thêm vào danh sách\n quét QR để thêm thiết bị.",
                Toast.LENGTH_LONG).show();
    }
    private void fileTooBigToast(Context ct) {
        Toast.makeText(ct, "File vượt quá kích cỡ\ncho phép là 70MB!",
                Toast.LENGTH_SHORT).show();
    }
}