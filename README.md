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

# 2. Objectives

โครงการนี้มีวัตถุประสงค์ดังต่อไปนี้

1. เพื่อจำลองกระบวนการจัดการ Interrupt ของระบบปฏิบัติการ
2. เพื่อแสดงการเปลี่ยนแปลงสถานะของ Process
3. เพื่อจำลองการทำงานของ CPU และ Process
4. เพื่อจำลอง Interrupt Queue และการจัดลำดับตาม Priority
5. เพื่อจำลองการบันทึกและกู้คืน CPU Context ด้วย PCB
6. เพื่อจำลอง Interrupt Vector Table
7. เพื่อจำลอง Interrupt Service Routine (ISR)
8. เพื่อแสดงกระบวนการ Context Switching
9. เพื่อแสดงลำดับการทำงานของ Interrupt ผ่าน Animation
10. เพื่อให้ผู้ใช้สามารถติดตามเหตุการณ์ต่าง ๆ ผ่าน Event Timeline และ System Log

---

# 3. Main Concept

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

# 4. Interrupt Handling Flow

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

# 5. Simulation States

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

# 6. Process State

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

# 7. Interrupt Types

ระบบรองรับ Interrupt หลายประเภท ได้แก่

| Interrupt Type | Priority |
|---|---:|
| Timer | 1 |
| Disk | 1 |
| Keyboard | 2 |
| Network | 3 |

ค่าตัวเลข Priority ที่น้อยกว่า หมายถึง Priority ที่สูงกว่า

ตัวอย่างเช่น

```text
Priority 1 = High
Priority 2 = Medium
Priority 3 = Low
```

ดังนั้นหากใน Queue มี

```text
Network   P3
Keyboard  P2
Timer     P1
Disk      P1
```

ระบบจะเลือก Interrupt ที่มี Priority สูงกว่าก่อน

หาก Priority เท่ากัน ระบบจะพิจารณา Interrupt ID เพื่อกำหนดลำดับ

---

# 8. Interrupt Queue

ระบบใช้ `PriorityQueue` ของ Java เพื่อจัดเก็บ Interrupt ที่เข้ามา

```java
PriorityQueue<Interrupt>
```

Queue จะเรียงลำดับโดยใช้

1. Priority
2. Interrupt ID

ตัวอย่าง

```text
Interrupt Queue

+--------------------------+
| Disk      | Priority: 1 |
+--------------------------+
| Timer     | Priority: 1 |
+--------------------------+
| Keyboard  | Priority: 2 |
+--------------------------+
| Network   | Priority: 3 |
+--------------------------+
```

เมื่อระบบต้องจัดการ Interrupt จะเลือก Interrupt ที่มี Priority สูงที่สุดก่อน

---

# 9. Interrupt Controller

`InterruptController` ทำหน้าที่เป็นส่วนกลางในการจัดการ Interrupt

หน้าที่หลัก ได้แก่

- รับ Interrupt
- เพิ่ม Interrupt เข้า Queue
- ตรวจสอบว่ามี Interrupt หรือไม่
- เลือก Interrupt ถัดไป
- เชื่อมต่อกับ Interrupt Vector Table
- เรียก Interrupt Handler

โครงสร้างการทำงาน

```text
Interrupt
    |
    v
InterruptController
    |
    v
InterruptQueue
    |
    v
Highest Priority Interrupt
    |
    v
InterruptVectorTable
    |
    v
InterruptHandler
```

---

# 10. Interrupt Vector Table

ระบบมี `InterruptVectorTable` สำหรับเก็บความสัมพันธ์ระหว่างประเภทของ Interrupt และ Interrupt Handler

ตัวอย่าง

```text
Timer     -> InterruptHandler
Keyboard  -> InterruptHandler
Disk      -> InterruptHandler
Network   -> InterruptHandler
```

เมื่อได้รับ Interrupt ระบบจะตรวจสอบประเภทของ Interrupt แล้วค้นหา Handler ที่เกี่ยวข้อง

ตัวอย่าง

```text
Interrupt Type: Timer

        |
        v

Interrupt Vector Table

Timer -> Timer Handler

        |
        v

Timer ISR
```

---

# 11. Interrupt Service Routine (ISR)

`InterruptHandler` ทำหน้าที่จำลองการทำงานของ Interrupt Service Routine

ระบบรองรับ ISR สำหรับ

- Timer
- Keyboard
- Disk
- Network

ตัวอย่างการทำงาน

```text
Timer Interrupt
       |
       v
Timer ISR
       |
       v
Processing Timer Event
       |
       v
ISR Completed
```

สำหรับ Interrupt แต่ละประเภท ระบบจะแสดงข้อความการทำงานใน Event Timeline และ System Log

---

# 12. PCB (Process Control Block)

PCB หรือ **Process Control Block** ใช้สำหรับเก็บข้อมูลสำคัญของ Process ในขณะที่ Process กำลังทำงาน

ข้อมูลที่จัดเก็บ ได้แก่

```text
Process ID
Program Counter
Register A
Register B
Process State
```

ตัวอย่าง

```text
PID=1
PC=20
A=10
B=4
State=RUNNING
```

เมื่อเกิด Interrupt ระบบจะ Save Context ของ Process ลงใน PCB

```text
Process
   |
   v
Save Context
   |
   v
PCB
```

เมื่อ ISR ทำงานเสร็จ ระบบจะใช้ข้อมูลใน PCB เพื่อ Restore Context

```text
PCB
 |
 v
Restore Context
 |
 v
Process
 |
 v
RUNNING
```

---

# 13. Context Switching

Context Switching เป็นกระบวนการบันทึกและกู้คืนสถานะของ Process

ในระบบจำลองมี 2 ขั้นตอนหลัก

### Save Context

เมื่อเกิด Interrupt

```text
CPU
 |
 v
Current Process
 |
 v
Save Context
 |
 v
PCB
```

ข้อมูลของ Process จะถูกบันทึกไว้ก่อนที่ Process จะถูก Interrupt

### Restore Context

หลังจาก ISR ทำงานเสร็จ

```text
PCB
 |
 v
Restore Context
 |
 v
CPU
 |
 v
Process RUNNING
```

Process จะกลับมาทำงานต่อจาก Context ที่ถูกบันทึกไว้

---

# 14. CPU Simulation

คลาส `CPU` ทำหน้าที่จำลอง CPU

หน้าที่หลัก ได้แก่

- Load Process
- Execute Process
- Save Context
- Restore Context
- เก็บจำนวนการ Execute
- เก็บจำนวน Context Save
- เก็บจำนวน Context Restore

ตัวอย่าง

```text
CPU
 |
 +-- Current Process
 |
 +-- Execute Count
 |
 +-- Context Save Count
 |
 +-- Context Restore Count
```

เมื่อ Execute Process ระบบจะเพิ่มค่าของ Process เช่น

```text
Program Counter
Register A
Register B
```

เพื่อจำลองการเปลี่ยนแปลงของ CPU Context

---

# 15. Interrupt Handling Animation

ระบบมี Animation เพื่อแสดงกระบวนการ Interrupt Handling แบบเป็นขั้นตอน

ลำดับ Animation

```text
1. RUNNING

2. INTERRUPT RECEIVED

3. SAVING CONTEXT

4. INTERRUPTED

5. LOOKUP HANDLER

6. ISR EXECUTING

7. RESTORING CONTEXT

8. RESUMED

9. RUNNING
```

Animation ช่วยให้ผู้ใช้เห็นการเปลี่ยนแปลงของระบบแบบต่อเนื่อง แทนที่จะเห็นเฉพาะผลลัพธ์สุดท้าย

---

# 16. Process State Diagram

ส่วน Process State Diagram แสดงการเปลี่ยนสถานะของ Process

```text
+---------+
|  READY  |
+---------+
     |
     v
+---------+
| RUNNING |
+---------+
     |
     | Interrupt
     v
+-------------+
| INTERRUPTED |
+-------------+
     |
     | Restore Context
     v
+---------+
| RUNNING |
+---------+
```

นอกจากนี้ระบบยังแสดง Interrupt Handling Pipeline แยกออกจาก Process State

```text
RECEIVED
    |
    v
SAVE CONTEXT
    |
    v
LOOKUP HANDLER
    |
    v
ISR
    |
    v
RESTORE CONTEXT
```

การแยกสองส่วนนี้ช่วยให้เห็นความแตกต่างระหว่าง

- Process State
- Interrupt Handling State

ได้ชัดเจนขึ้น

---

# 17. Graphical User Interface

ระบบใช้ Java Swing ในการสร้าง GUI

หน้าจอหลักแบ่งออกเป็นหลายส่วน

```text
+------------------------------------------------------+
|              INTERRUPT HANDLING SIMULATOR            |
+------------------------------------------------------+
| CPU / PROCESS        | INTERRUPT SYSTEM | STATISTICS |
+------------------------------------------------------+
|                                                      |
|              INTERRUPT PIPELINE                     |
|                                                      |
+------------------------------------------------------+
|              PROCESS STATE DIAGRAM                   |
+------------------------------------------------------+
|              INTERRUPT QUEUE                         |
+------------------------------------------------------+
|              EVENT TIMELINE                          |
+------------------------------------------------------+
| Controls                                             |
| [Execute] [Timer] [Keyboard] [Disk] [Network]       |
| [Handle] [Pause] [Auto] [Reset]                     |
+------------------------------------------------------+
```

---

# 18. CPU / Process Monitor

ส่วน CPU / Process Monitor ใช้แสดงข้อมูลของ Process ปัจจุบัน

ข้อมูลที่แสดง ได้แก่

```text
Process ID
Process Name
Process State
Program Counter
Register A
Register B
```

ตัวอย่าง

```text
PROCESS

PID       : 1
Name      : P1
State     : RUNNING
PC        : 20
Register A: 10
Register B: 4
```

---

# 19. Interrupt System Monitor

ส่วน Interrupt System ใช้แสดงข้อมูลเกี่ยวกับ Interrupt

ตัวอย่างข้อมูล

```text
Interrupt Type
Priority
Interrupt Queue Size
Current Interrupt
Interrupt Vector Table
```

ผู้ใช้สามารถสร้าง Interrupt ได้จากปุ่มต่าง ๆ บน GUI

---

# 20. Statistics Dashboard

Statistics Dashboard ใช้แสดงข้อมูลสถิติของระบบ เช่น

```text
Total Executions
Context Saves
Context Restores
Queue Size
```

ตัวอย่าง

```text
STATISTICS

Executions       : 5
Context Saves    : 2
Context Restores : 2
Queue Size       : 3
```

ข้อมูลจะเปลี่ยนแปลงตามการทำงานของ Simulator

---

# 21. Interrupt Queue Panel

Interrupt Queue Panel ใช้แสดง Interrupt ที่กำลังรอการจัดการ

ตัวอย่าง

```text
INTERRUPT QUEUE

+--------------------------+
| TIMER                    |
| Priority: 1              |
| ID: 1                    |
+--------------------------+

+--------------------------+
| KEYBOARD                 |
| Priority: 2              |
| ID: 2                    |
+--------------------------+

+--------------------------+
| NETWORK                  |
| Priority: 3              |
| ID: 3                    |
+--------------------------+
```

เมื่อมี Interrupt จำนวนมาก ระบบจะแสดง Queue ในพื้นที่ที่สามารถเลื่อนดูรายการได้

---

# 22. Event Timeline

Event Timeline ใช้แสดงเหตุการณ์ที่เกิดขึ้นระหว่างการจำลอง

ตัวอย่าง

```text
[INFO] Process P1 is RUNNING

[INFO] STEP 1: Interrupt received
[INFO] Interrupt: Timer

[INFO] STEP 2: Saving CPU context

[INFO] Context saved to PCB:
[INFO] PID=1, PC=20, A=10, B=4

[INFO] STEP 3: Process interrupted

[INFO] STEP 4: Looking up Interrupt Vector Table

[INFO] ISR found: Timer Interrupt Service Routine

[INFO] STEP 5: Executing ISR

[INFO] Timer ISR: processing timer event

[INFO] STEP 6: Restoring CPU context

[INFO] STEP 7: Process P1 resumed

[INFO] Process P1 state: INTERRUPTED -> RUNNING
```

Event Timeline ช่วยให้สามารถตรวจสอบลำดับการทำงานของระบบได้

---

# 23. System Controls

ระบบมีปุ่มควบคุมหลักดังนี้

## Execute Process

ใช้สำหรับให้ CPU Execute Process ปัจจุบัน

ตัวอย่าง

```text
PC: 0
   |
Execute
   |
PC: 10
```

Execute อีกครั้ง

```text
PC: 10
   |
Execute
   |
PC: 20
```

---

## Timer Interrupt

สร้าง Timer Interrupt

```text
Type     : Timer
Priority : 1
```

---

## Keyboard Interrupt

สร้าง Keyboard Interrupt

```text
Type     : Keyboard
Priority : 2
```

---

## Disk Interrupt

สร้าง Disk Interrupt

```text
Type     : Disk
Priority : 1
```

---

## Network Interrupt

สร้าง Network Interrupt

```text
Type     : Network
Priority : 3
```

---

## Handle Interrupt

เริ่มกระบวนการจัดการ Interrupt

ระบบจะทำตามขั้นตอน

```text
Interrupt Received
       |
       v
Save Context
       |
       v
Process Interrupted
       |
       v
Lookup Handler
       |
       v
Execute ISR
       |
       v
Restore Context
       |
       v
Process Resumed
```

---

## Pause

หยุด Animation ชั่วคราว

สามารถใช้เพื่อดูแต่ละขั้นตอนของ Interrupt Handling

---

## Auto Simulation

เริ่มการจำลองอัตโนมัติ

ระบบจะสร้าง Interrupt หลายประเภทเข้าสู่ Queue แล้วจัดการตาม Priority

ตัวอย่าง

```text
Timer      P1
Keyboard   P2
Disk       P1
Network    P3
```

จากนั้นระบบจะเลือก Interrupt ตาม Priority

---

## Reset

ใช้สำหรับ Reset Simulator กลับไปยังสถานะเริ่มต้น

เมื่อ Reset ระบบจะ

- สร้าง CPU ใหม่
- สร้าง Process ใหม่
- ล้าง Interrupt Queue
- Reset Interrupt ID
- ล้าง PCB
- ล้าง Event Timeline
- Reset Statistics
- หยุด Animation
- Reset Simulation State

---

# 24. Auto Simulation

Auto Simulation ใช้สำหรับสาธิตการทำงานของระบบแบบอัตโนมัติ

ตัวอย่าง Interrupt ที่ถูกสร้าง

```text
Timer      Priority 1
Keyboard   Priority 2
Disk       Priority 1
Network    Priority 3
```

ระบบจะนำ Interrupt ทั้งหมดเข้า Queue

จากนั้นจะเลือก Interrupt ตาม Priority

ตัวอย่างแนวคิด

```text
Initial Queue

Network   P3
Keyboard  P2
Timer     P1
Disk      P1

       |
       v

Handle Priority 1
       |
       v

Handle Priority 1
       |
       v

Handle Priority 2
       |
       v

Handle Priority 3
```

หาก Priority เท่ากัน ระบบจะใช้ Interrupt ID เพื่อกำหนดลำดับ

---

# 25. Object-Oriented Design

โปรเจกต์ใช้แนวคิด Object-Oriented Programming (OOP)

Class หลักของระบบประกอบด้วย

```text
CPU
Process
PCB
Interrupt
InterruptQueue
InterruptController
InterruptVectorTable
InterruptHandler
SimulationState
SimulatorGUI
StateDiagramPanel
InterruptQueuePanel
Main
```

---

# 26. Class Responsibilities

## CPU.java

รับผิดชอบการจำลอง CPU

```text
- loadProcess()
- execute()
- saveContext()
- restoreContext()
```

---

## Process.java

รับผิดชอบข้อมูลและสถานะของ Process

```text
- Process ID
- Process Name
- Process State
- Program Counter
- Register A
- Register B
```

---

## PCB.java

เก็บ Context ของ Process

```text
- Process ID
- Program Counter
- Register A
- Register B
- State
```

---

## Interrupt.java

เก็บข้อมูลของ Interrupt

```text
- Interrupt ID
- Interrupt Type
- Priority
```

---

## InterruptQueue.java

จัดการ Interrupt Queue โดยใช้ Java PriorityQueue

```text
- Add Interrupt
- Get Next Interrupt
- Peek Next Interrupt
- Check Queue
- Get Queue Size
```

---

## InterruptController.java

ควบคุมการจัดการ Interrupt

```text
- Receive Interrupt
- Get Next Interrupt
- Handle Interrupt
- Access Interrupt Queue
- Access Interrupt Vector Table
```

---

## InterruptVectorTable.java

เก็บความสัมพันธ์ระหว่าง Interrupt Type และ Handler

```text
Timer
Keyboard
Disk
Network
```

---

## InterruptHandler.java

จำลองการทำงานของ ISR

---

## SimulationState.java

เก็บสถานะของ Simulation

```text
READY
RUNNING
INTERRUPT_RECEIVED
SAVING_CONTEXT
INTERRUPTED
LOOKUP_HANDLER
ISR_EXECUTING
RESTORING_CONTEXT
RESUMED
```

---

## SimulatorGUI.java

เป็น Main GUI Controller ของระบบ

รับผิดชอบ

- สร้าง GUI
- รับ User Input
- ควบคุม Simulation
- ควบคุม Animation
- อัปเดตข้อมูลบนหน้าจอ
- แสดง Event Timeline
- ควบคุม Auto Simulation

---

## StateDiagramPanel.java

แสดง Process State และ Interrupt Handling Pipeline แบบ Visualization

---

## InterruptQueuePanel.java

แสดง Interrupt Queue ในรูปแบบ GUI Card

---

## Main.java

เป็นจุดเริ่มต้นของโปรแกรม

---

# 27. Project Structure

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

# 28. Technologies

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

# 29. Requirements

ก่อนใช้งานโปรเจกต์ ต้องติดตั้ง

- Java JDK 17 หรือสูงกว่า
- Apache Maven
- Git

ตรวจสอบ Java

```bash
java -version
```

ตรวจสอบ Maven

```bash
mvn -version
```

---

# 30. Installation

Clone Repository

```bash
git clone https://github.com/suphawat1/Interrupt-Handling-Simulator.git
```

เข้าสู่ Project Directory

```bash
cd Interrupt-Handling-Simulator
```

ตรวจสอบไฟล์

```bash
ls
```

ควรพบ

```text
README.md
pom.xml
src
```

---

# 31. Build Project

ใช้คำสั่ง

```bash
mvn clean compile
```

หาก Build สำเร็จ จะเห็นข้อความประมาณ

```text
BUILD SUCCESS
```

---

# 32. Run Project

สามารถ Run `Main.java` จาก IDE เช่น IntelliJ IDEA, Eclipse หรือ Visual Studio Code ได้

หรือใช้ Maven หาก `pom.xml` มีการกำหนด Plugin สำหรับการ Run ไว้

```bash
mvn exec:java
```

หากโปรเจกต์ไม่ได้กำหนด `exec-maven-plugin` สามารถ Run ผ่าน IDE ได้โดยตรง

---

# 33. Example Usage

ตัวอย่างการใช้งานระบบ

### Step 1: Start Program

เมื่อเปิดโปรแกรม ระบบจะแสดง GUI ของ Interrupt Handling Simulator

---

### Step 2: Execute Process

กด

```text
Execute Process
```

Process จะเริ่มทำงานบน CPU

ตัวอย่าง

```text
Process State : RUNNING
PC             : 10
Register A     : 5
Register B     : 2
```

---

### Step 3: Generate Interrupt

กด

```text
Timer Interrupt
```

ระบบจะสร้าง

```text
Timer
Priority: 1
```

และเพิ่มเข้า Interrupt Queue

---

### Step 4: Handle Interrupt

กด

```text
Handle Interrupt
```

ระบบจะเริ่ม Animation

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

# 34. Example of Context Switching

สมมติ Process มีข้อมูล

```text
PID = 1
PC = 20
A = 10
B = 4
State = RUNNING
```

เมื่อเกิด Interrupt ระบบจะ Save Context

```text
PCB

PID = 1
PC = 20
A = 10
B = 4
State = RUNNING
```

จากนั้น Process จะเข้าสู่สถานะ

```text
INTERRUPTED
```

หลังจาก ISR ทำงานเสร็จ ระบบจะ Restore Context

```text
PC = 20
A = 10
B = 4
```

และ Process จะกลับมา

```text
RUNNING
```

---

# 35. Design Architecture

สถาปัตยกรรมโดยรวมของระบบ

```text
                   +----------------+
                   |      CPU       |
                   +-------+--------+
                           |
                           v
                   +----------------+
                   |    Process     |
                   +-------+--------+
                           |
                    Interrupt Occurs
                           |
                           v
              +-------------------------+
              | Interrupt Controller    |
              +------------+------------+
                           |
                           v
              +-------------------------+
              |    Interrupt Queue      |
              |     PriorityQueue       |
              +------------+------------+
                           |
                           v
              +-------------------------+
              | Interrupt Vector Table  |
              +------------+------------+
                           |
                           v
              +-------------------------+
              |   Interrupt Handler     |
              |          ISR            |
              +------------+------------+
                           |
                           v
              +-------------------------+
              |    Restore Context      |
              +------------+------------+
                           |
                           v
                   +----------------+
                   | Process Resume |
                   +----------------+
```

---

# 36. Separation of Simulation State and Process State

ระบบแยกแนวคิดระหว่าง

### Process State

เป็นสถานะจริงของ Process

```text
READY
RUNNING
INTERRUPTED
```

### Simulation State

เป็นสถานะของกระบวนการจำลอง Interrupt Handling

```text
READY
RUNNING
INTERRUPT_RECEIVED
SAVING_CONTEXT
INTERRUPTED
LOOKUP_HANDLER
ISR_EXECUTING
RESTORING_CONTEXT
RESUMED
```

ตัวอย่าง

```text
Simulation State:
SAVING_CONTEXT

Process State:
RUNNING
```

เมื่อการ Save Context เสร็จแล้ว

```text
Simulation State:
INTERRUPTED

Process State:
INTERRUPTED
```

จากนั้นเมื่อ Restore Context เสร็จ

```text
Simulation State:
RESUMED

Process State:
RUNNING
```

การแยกสองส่วนนี้ทำให้สามารถแสดงรายละเอียดของ Interrupt Handling ได้มากขึ้น โดยไม่ทำให้ Process State มีรายละเอียดมากเกินไป

---

# 37. Error Handling

ระบบมีการตรวจสอบกรณีต่าง ๆ เช่น

- ไม่มี Current Process
- ไม่มี Interrupt ใน Queue
- ไม่มี Interrupt Handler
- ไม่มี PCB สำหรับ Restore
- Queue ว่าง
- Interrupt Type ไม่ตรงกับ Handler

ตัวอย่างกรณีไม่พบ Handler

```text
No handler registered for <Interrupt Type>
```

---

# 38. Future Improvements

ระบบสามารถพัฒนาต่อได้ เช่น

1. รองรับหลาย Process
2. เพิ่ม Interrupt Masking
3. เพิ่ม Nested Interrupt
4. เพิ่ม Interrupt Enable / Disable
5. เพิ่ม CPU Register ที่ละเอียดมากขึ้น
6. เพิ่ม Memory Simulation
7. เพิ่ม Kernel Mode / User Mode
8. เพิ่ม System Call Simulation
9. เพิ่ม Interrupt Statistics ที่ละเอียดขึ้น
10. เพิ่มการบันทึก Simulation History
11. เพิ่มการ Export Event Log
12. เพิ่มการแสดง Interrupt Timeline แบบละเอียด
13. เพิ่มการกำหนด Priority จากผู้ใช้
14. เพิ่มการสร้าง Interrupt แบบ Random
15. เพิ่มการจำลอง Hardware Device
16. เพิ่มการจำลอง Multiple CPU

---

# 39. Educational Purpose

โปรเจกต์นี้จัดทำขึ้นเพื่อใช้เป็นสื่อประกอบการเรียนรู้เกี่ยวกับแนวคิดของระบบปฏิบัติการ โดยเฉพาะ

- Interrupt
- Interrupt Handling
- Interrupt Priority
- Interrupt Queue
- Interrupt Vector Table
- Interrupt Service Routine
- Process State
- Process Control Block
- Context Switching
- CPU Execution

ระบบไม่ได้จำลองการทำงานของ Operating System จริงทั้งหมด แต่เป็น Simulation ที่ออกแบบเพื่อแสดงแนวคิดและลำดับการทำงานให้เข้าใจได้ง่ายผ่าน GUI

---

# 40. Summary

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

# 41. Project Repository

GitHub Repository:

```text
https://github.com/suphawat1/Interrupt-Handling-Simulator
```

---

# 42. Authors

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
