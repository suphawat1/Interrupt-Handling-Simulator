package com.interruptsimulator.util;

/**
 * ค่าคงที่ของ simulator (ระยะเวลาของแต่ละ STEP, หน่วย ms)
 */
public final class Constants {

    private Constants() {
    }

    /** STEP ปกติ */
    public static final int NORMAL_STEP_DELAY = 2600;

    /** ให้ ISR ค้างนานขึ้นเพื่อให้เห็นว่ากำลังทำงาน */
    public static final int ISR_STEP_DELAY = 3200;

    /** ให้ RESUMED ค้างไว้ก่อนกลับ RUNNING */
    public static final int RESUME_STEP_DELAY = 2800;
}
