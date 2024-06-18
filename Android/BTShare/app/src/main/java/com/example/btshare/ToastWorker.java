package com.example.btshare;

import android.content.Context;

public class ToastWorker {
    private static ToastMaker instance;
public static void initialize() {
    if (instance == null) instance = new ToastMakerImpl();
}
public static void showToast(String code, Context ct) {   instance.showToast(code,ct);   }
}
