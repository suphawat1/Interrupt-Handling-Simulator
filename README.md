# Interrupt Handling Simulator

โปรเจกต์รายวิชา **Operating Systems (OS)**  
หัวข้อ **Interrupt Handling Simulator**

---

## 👥 สมาชิกในกลุ่ม

| ชื่อ | รหัสนักศึกษา |
|---|---|
| นายพิชญพงษ์ ทองแม้น | 673380595-7 |
| นายศุภวัทน์ แสนเรียน | 673380604-2 |
| นายอนุชา ประมาระตา | 673380607-6 |

---

## 📌 เกี่ยวกับโครงงาน

**Interrupt Handling Simulator** เป็นโปรแกรมจำลองการทำงานของ
**Interrupt Handling ในระบบปฏิบัติการ (Operating System)**

โปรแกรมถูกพัฒนาขึ้นเพื่อช่วยให้เข้าใจกระบวนการทำงานของ CPU และ
Operating System เมื่อเกิด Interrupt ขึ้นระหว่างที่ Process กำลังทำงาน

โดยโปรแกรมจะแสดงกระบวนการตั้งแต่การเกิด Interrupt การนำ Interrupt
เข้าสู่ Queue การเลือก Interrupt ตาม Priority การบันทึกสถานะของ CPU
การเรียก Interrupt Service Routine (ISR) และการคืนสถานะของ Process
เพื่อให้ Process สามารถกลับมาทำงานต่อได้

ภาพรวมของกระบวนการ:

```text
Process Running
      ↓
Interrupt Occurs
      ↓
Interrupt Queue
      ↓
Priority Selection
      ↓
Save Context
      ↓
Process Interrupted
      ↓
Interrupt Vector Table
      ↓
Interrupt Service Routine (ISR)
      ↓
Restore Context
      ↓
Process Resume
