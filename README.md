# Interrupt Handling Simulator

ระบบจำลองการจัดการ Interrupt ของระบบปฏิบัติการ  
**Operating Systems Final Project**

---

## Project Information

**Project Name:** Interrupt Handling Simulator

**Course:** Operating Systems

**Project Type:** Operating System Simulation

**Programming Language:** Java

**GUI Framework:** Java Swing

**Build Tool:** Apache Maven

**Java Version:** Java 17

---

## Project Members

| Name | Student ID |
|---|---|
| นายพิชญพงษ์ ทองแม้น | 673380595-7 |
| นายศุภวัทน์ แสนเรียน | 673380604-2 |
| นายอนุชา ประมาระตา | 673380607-6 |

---

# 1. Project Description

**Interrupt Handling Simulator** เป็นโปรแกรมจำลองการทำงานของระบบจัดการ Interrupt ภายในระบบปฏิบัติการ โดยออกแบบให้สามารถมองเห็นลำดับการทำงานของ CPU, Process, Interrupt Queue, Interrupt Vector Table, Interrupt Service Routine (ISR) และการ Context Switching ผ่าน Graphical User Interface (GUI)

โปรแกรมจะแสดงกระบวนการตั้งแต่ Process กำลังทำงานอยู่บน CPU จนกระทั่งมี Interrupt เข้ามา จากนั้นระบบจะบันทึก Context ของ Process, หยุด Process ชั่วคราว, ค้นหา Interrupt Handler, ทำงานใน ISR และ Restore Context เพื่อให้ Process กลับมาทำงานต่อ

ระบบถูกออกแบบเพื่อช่วยให้ผู้เรียนสามารถเข้าใจการทำงานของ Interrupt Handling ในระบบปฏิบัติการผ่านการจำลองแบบ Interactive และ Visualization

---

# 2. Main Concept

การทำงานหลักของระบบสามารถอธิบายได้ดังนี้

```text
CPU
 |
 v
Process is RUNNING
 |
 | Interrupt occurs
 v
Interrupt Controller
 |
 v
Interrupt Queue
 |
 | Select highest priority interrupt
 v
Save CPU Context
 |
 v
PCB
 |
 v
Process becomes INTERRUPTED
 |
 v
Interrupt Vector Table
 |
 v
Interrupt Service Routine (ISR)
 |
 v
Restore CPU Context
 |
 v
Process RESUMED
 |
 v
Process RUNNING
```

---

# 3. Interrupt Handling Flow

ระบบจำลองกระบวนการจัดการ Interrupt ตามลำดับดังนี้

```text
RUNNING
   |
   v
INTERRUPT RECEIVED
   |
   v
SAVING CONTEXT
   |
   v
INTERRUPTED
   |
   v
LOOKUP HANDLER
   |
   v
ISR EXECUTING
   |
   v
RESTORING CONTEXT
   |
   v
RESUMED
   |
   v
RUNNING
```

---

# 4. Simulation States

ระบบมีสถานะของการจำลองทั้งหมดดังนี้

| State | Description |
|---|---|
| READY | ระบบพร้อมเริ่มการทำงาน |
| RUNNING | Process กำลังทำงานบน CPU |
| INTERRUPT_RECEIVED | ระบบได้รับ Interrupt |
| SAVING_CONTEXT | กำลังบันทึก CPU Context |
| INTERRUPTED | Process ถูก Interrupt |
| LOOKUP_HANDLER | กำลังค้นหา Interrupt Handler |
| ISR_EXECUTING | กำลังทำงานใน Interrupt Service Routine |
| RESTORING_CONTEXT | กำลังกู้คืน CPU Context |
| RESUMED | Process กลับมาทำงานต่อ |

---

# 5. Process State

Process ในระบบมีสถานะหลัก 3 สถานะ (`ProcessState`)

```text
READY
  |
  v
RUNNING
  |
  | Interrupt
  v
INTERRUPTED
  |
  | Restore Context
  v
RUNNING
```

### READY

Process พร้อมที่จะทำงาน แต่ยังไม่ได้ทำงานบน CPU

### RUNNING

Process กำลังทำงานอยู่บน CPU

### INTERRUPTED

Process ถูกขัดจังหวะจาก Interrupt และ CPU Context ถูกบันทึกไว้ใน PCB

หลังจาก Interrupt Handler ทำงานเสร็จ ระบบจะ Restore Context และให้ Process กลับมาเป็น `RUNNING`

---

# 6. Interrupt Types

ระบบรองรับ Interrupt หลายประเภท ได้แก่

| Interrupt Type | Priority | ISR |
|---|---:|---|
| Timer | 1 | `TimerInterruptHandler` |
| Disk | 1 | `DiskInterruptHandler` |
| Keyboard | 2 | `KeyboardInterruptHandler` |
| Network | 3 | `NetworkInterruptHandler` |

ค่าตัวเลข Priority ที่น้อยกว่า หมายถึง Priority ที่สูงกว่า  
หาก Priority เท่ากัน จะจัดการตามลำดับที่เกิดก่อน (Interrupt ID น้อยกว่าก่อน)

---

# 7. Architecture

โปรเจกต์แบ่งโค้ดตามหลัก **MVC** เพื่อแยก Logic ออกจากหน้าจอ

```text
ผู้ใช้กดปุ่ม
    |
    v
view/SimulatorGUI  ------>  controller/SimulationController
    ^                              |
    |                              | ควบคุม Timer และ STEP 1-9
    |                              v
    |                       core/ + model/
    |                       (CPU, Process, PCB, Queue, Vector Table, ISR)
    |                              |
    +---- SimulationListener ------+
          (onLog, onStateChanged, onReset)
```

- **Model / Core** เป็น Logic ล้วน ไม่รู้จักหน้าจอ จึงเขียน Unit Test ได้โดยไม่ต้องเปิด GUI
- **Controller** ควบคุมลำดับการจำลอง (STEP 1-9) และแจ้ง View ผ่าน `SimulationListener`
- **View** แสดงผลและส่งคำสั่งของผู้ใช้ไปให้ Controller

### ลำดับการจำลอง (STEP)

| STEP | Simulation State | การทำงาน |
|---:|---|---|
| 1 | `INTERRUPT_RECEIVED` | รับ Interrupt เข้า Queue |
| 2 | `SAVING_CONTEXT` | บันทึก PC และ Register ลง PCB |
| 3 | `INTERRUPTED` | Process เปลี่ยนจาก RUNNING เป็น INTERRUPTED |
| 4 | `LOOKUP_HANDLER` | ดึง Interrupt ที่ Priority สูงสุด และค้นหา ISR จาก Vector Table |
| 5 | `ISR_EXECUTING` | ทำงานใน ISR |
| 6 | `RESTORING_CONTEXT` | Restore Context จาก PCB |
| 7 | `RESUMED` | Process กลับมาทำงานต่อ |
| 8 | `RUNNING` | กลับสู่สถานะปกติ |
| 9 | - | จบรอบ หากเป็น Auto Simulation และยังมี Interrupt ค้างอยู่ จะเริ่มรอบใหม่ |

---

# 8. Project Structure

```text
interrupt-handling-simulator/
│
├── README.md
├── pom.xml
├── .gitignore
│
├── รายงานโครงงานรายวิชาos.pdf
│
└── src/
    ├── main/java/com/interruptsimulator/
    │   ├── Main.java
    │   │
    │   ├── model/                      # ข้อมูลพื้นฐาน
    │   │   ├── Interrupt.java
    │   │   ├── PCB.java
    │   │   ├── Process.java
    │   │   ├── ProcessState.java
    │   │   └── SimulationState.java
    │   │
    │   ├── core/                       # กลไกที่จำลองฮาร์ดแวร์
    │   │   ├── CPU.java
    │   │   ├── InterruptHandler.java   # interface ของ ISR
    │   │   ├── InterruptQueue.java
    │   │   ├── InterruptVectorTable.java
    │   │   └── handler/                # ISR แต่ละชนิด
    │   │       ├── BaseInterruptHandler.java
    │   │       ├── TimerInterruptHandler.java
    │   │       ├── KeyboardInterruptHandler.java
    │   │       ├── DiskInterruptHandler.java
    │   │       └── NetworkInterruptHandler.java
    │   │
    │   ├── controller/                 # ตัวควบคุม
    │   │   ├── InterruptController.java
    │   │   ├── SimulationController.java
    │   │   └── SimulationListener.java
    │   │
    │   ├── view/                       # หน้าจอ (Swing)
    │   │   ├── SimulatorGUI.java
    │   │   └── panel/
    │   │       ├── StateDiagramPanel.java
    │   │       ├── InterruptIllustrationPanel.java
    │   │       └── InterruptQueuePanel.java
    │   │
    │   └── util/
    │       └── Constants.java
    │
    └── test/java/com/interruptsimulator/   # Unit Test (JUnit 5)
        ├── model/
        ├── core/
        └── controller/
```

> หมายเหตุ: `target/` เป็นโฟลเดอร์ที่ Maven สร้างขึ้นอัตโนมัติ และไม่ถูก Commit ขึ้น Git Repository (ระบุไว้ใน `.gitignore`)

### หน้าที่ของแต่ละส่วน

| Package | หน้าที่ |
|---|---|
| `model` | ข้อมูลของระบบ เช่น Interrupt, Process, PCB และสถานะต่างๆ |
| `core` | CPU, Interrupt Queue, Interrupt Vector Table และ ISR ของแต่ละชนิด |
| `controller` | Interrupt Controller และ Simulation Controller ที่ควบคุม STEP 1-9 |
| `view` | หน้าต่างหลักและ Panel แสดงผล (State Diagram, Illustration, Queue) |
| `util` | ค่าคงที่ เช่น ระยะเวลาของแต่ละ STEP |

---

# 9. Technologies

โปรเจกต์นี้ใช้เทคโนโลยีดังต่อไปนี้

| Technology | Purpose |
|---|---|
| Java 17 | Programming Language |
| Java Swing | Graphical User Interface |
| Apache Maven | Build Management |
| JUnit 5 | Unit Testing |
| PriorityQueue | Interrupt Priority Management |
| OOP | Software Design |
| Timer | GUI Animation |
| Git / GitHub | Version Control |

---

# 10. Build Project

ใช้คำสั่ง (รันที่โฟลเดอร์ที่มี `pom.xml`)

```bash
mvn clean compile
```

หาก Build สำเร็จ จะเห็นข้อความประมาณ

```text
BUILD SUCCESS
```

---

# 11. Run Project

หลังจาก Build แล้ว สามารถรันได้ด้วยคำสั่ง

```bash
java -cp target/classes com.interruptsimulator.Main
```

หรือ Run `Main.java` จาก IDE เช่น IntelliJ IDEA, Eclipse หรือ Visual Studio Code

### ปุ่มควบคุมในโปรแกรม

| ปุ่ม | การทำงาน |
|---|---|
| Execute Process | ให้ CPU ทำงานกับ Process หนึ่งรอบ (PC +10, A +5, B +2) |
| Timer / Keyboard / Disk / Network Interrupt | สร้าง Interrupt เข้า Queue |
| Handle Interrupt | เริ่มจัดการ Interrupt ที่ค้างใน Queue ทีละ STEP |
| Pause | หยุด/ทำต่อ Animation |
| Auto Simulation | สร้าง Interrupt ทั้ง 4 ชนิดและจัดการต่อเนื่องจนครบ |
| Reset | เริ่มระบบใหม่ |

---

# 12. Testing

โปรเจกต์มี Unit Test (JUnit 5) สำหรับ `model`, `core` และ `controller`

```bash
mvn clean test
```

ผลลัพธ์ที่ถูกต้องจะแสดงประมาณ

```text
Tests run: 32, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

| Test Class | ตรวจสอบ |
|---|---|
| `PCBTest` | PCB เก็บค่า Context ณ เวลาที่บันทึก |
| `InterruptQueueTest` | ลำดับ Priority และการเรียงตามลำดับที่เกิดก่อน |
| `CPUTest` | execute, save context และ restore context |
| `InterruptVectorTableTest` | การค้นหา ISR และ Log ของแต่ละชนิด |
| `InterruptControllerTest` | การจัดการ Interrupt ที่รู้จักและไม่รู้จัก |
| `SimulationControllerTest` | State เริ่มต้น, การสร้าง Interrupt และ Reset |

---

# Summary

**Interrupt Handling Simulator** เป็นโปรเจกต์จำลองการจัดการ Interrupt ของระบบปฏิบัติการ โดยจำลองตั้งแต่ Process ทำงานบน CPU จนเกิด Interrupt และเข้าสู่กระบวนการ Interrupt Handling

กระบวนการหลักคือ

```text
Process Running
       |
       v
Interrupt Received
       |
       v
Save Context
       |
       v
Process Interrupted
       |
       v
Lookup Interrupt Handler
       |
       v
Execute ISR
       |
       v
Restore Context
       |
       v
Process Resumed
       |
       v
Process Running
```

ระบบประกอบด้วย CPU Simulation, Process, PCB, Interrupt Queue, Interrupt Controller, Interrupt Vector Table, Interrupt Handler (ISR) และ GUI Visualization

การใช้ Java Swing ทำให้สามารถแสดงการเปลี่ยนแปลงของ Process State, Interrupt Pipeline, Interrupt Queue, Statistics และ Event Timeline ได้แบบ Interactive

---

# Project Repository

GitHub Repository:

```text
https://github.com/suphawat1/Interrupt-Handling-Simulator
```

---

# Authors

**Operating Systems Final Project**

### นายพิชญพงษ์ ทองแม้น
Student ID: `673380595-7`

### นายศุภวัทน์ แสนเรียน
Student ID: `673380604-2`

### นายอนุชา ประมาระตา
Student ID: `673380607-6`

---

**Operating Systems Final Project**
