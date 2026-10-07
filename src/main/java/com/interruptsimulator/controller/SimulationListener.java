package com.interruptsimulator.controller;

/**
 * ช่องทางที่ SimulationController แจ้ง View (GUI)
 * Controller จึงไม่ต้องรู้จัก Swing เลย
 */
public interface SimulationListener {

    /** มีข้อความ log ใหม่ */
    void onLog(String message);

    /** state ของระบบเปลี่ยน ให้ View วาดใหม่ */
    void onStateChanged();

    /** ระบบถูก reset แล้ว */
    void onReset();
}
