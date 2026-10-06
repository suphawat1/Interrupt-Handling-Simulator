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
| นายอนุชา ประมาระตา | 673380607-? |

---

# 1. Project Description

**Interrupt Handling Simulator** เป็นโปรแกรมจำลองการทำงานของระบบจัดการ Interrupt ภายในระบบปฏิบัติการ โดยออกแบบให้สามารถมองเห็นลำดับการทำงานของ CPU, Process, Interrupt Queue, Interrupt Vector Table, Interrupt Service Routine (ISR) และการ Context Switching ผ่าน Graphical User Interface (GUI)

โปรแกรมจะแสดงกระบวนการตั้งแต่ Process กำลังทำงานอยู่บน CPU จนกระทั่งมี Interrupt เข้ามา จากนั้นระบบจะบันทึก Context ของ Process, หยุด Process ชั่วคราว, ค้นหา Interrupt Handler, ทำงานใน ISR และ Restore Context เพื่อให้ Process กลับมาทำงานต่อ

ระบบถูกออกแบบเพื่อช่วยให้ผู้เรียนสามารถเข้าใจการทำงานของ Interrupt Handling ในระบบปฏิบัติการผ่านการจำลองแบบ Interactive และ Visualization

---

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

Process ในระบบมีสถานะหลัก 3 สถานะ

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

| Interrupt Type | Priority |
|---|---:|
| Timer | 1 |
| Disk | 1 |
| Keyboard | 2 |
| Network | 3 |

ค่าตัวเลข Priority ที่น้อยกว่า หมายถึง Priority ที่สูงกว่า

---

# 7. Project Structure

```text
interrupt-handling-simulator/
│
├── README.md
├── pom.xml
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── interruptsimulator/
│   │               ├── CPU.java
│   │               ├── Interrupt.java
│   │               ├── InterruptController.java
│   │               ├── InterruptHandler.java
│   │               ├── InterruptQueue.java
│   │               ├── InterruptQueuePanel.java
│   │               ├── InterruptVectorTable.java
│   │               ├── Main.java
│   │               ├── PCB.java
│   │               ├── Process.java
│   │               ├── SimulationState.java
│   │               ├── SimulatorGUI.java
│   │               └── StateDiagramPanel.java
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── interruptsimulator/
│
├── target/
│
└── .gitignore
```

> หมายเหตุ: `target/` เป็นโฟลเดอร์ที่ Maven สร้างขึ้นอัตโนมัติ และไม่ควร Commit ขึ้น Git Repository

---

# 8. Technologies

โปรเจกต์นี้ใช้เทคโนโลยีดังต่อไปนี้

| Technology | Purpose |
|---|---|
| Java 17 | Programming Language |
| Java Swing | Graphical User Interface |
| Apache Maven | Build Management |
| PriorityQueue | Interrupt Priority Management |
| OOP | Software Design |
| Timer | GUI Animation |
| Git / GitHub | Version Control |

---



# 9. Build Project

ใช้คำสั่ง

```bash
mvn clean compile
```

หาก Build สำเร็จ จะเห็นข้อความประมาณ

```text
BUILD SUCCESS
```

---

# 10. Run Project

สามารถ Run `Main.java` จาก IDE เช่น IntelliJ IDEA, Eclipse หรือ Visual Studio Code ได้

หรือใช้ Maven หาก `pom.xml` มีการกำหนด Plugin สำหรับการ Run ไว้

```bash
mvn exec:java
```

หากโปรเจกต์ไม่ได้กำหนด `exec-maven-plugin` สามารถ Run ผ่าน IDE ได้โดยตรง

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

ระบบประกอบด้วย CPU Simulation, Process, PCB, Interrupt Queue, Interrupt Controller, Interrupt Vector Table, Interrupt Handler และ GUI Visualization

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
Student ID: `673380607-?`

---

## Interrupt Handling Simulator

```text
CPU
 |
 v
PROCESS
 |
 v
INTERRUPT
 |
 v
INTERRUPT CONTROLLER
 |
 v
PRIORITY QUEUE
 |
 v
INTERRUPT VECTOR TABLE
 |
 v
ISR
 |
 v
RESTORE CONTEXT
 |
 v
PROCESS RESUMED
 |
 v
RUNNING
```

**Operating Systems Final Project**
