# DataWearAPP - Mi Band Data Viewer
An Android application designed to display Mi Band fitness data sourced from the Gadgetbridge database. This app provides an intuitive interface to view heart rate patterns through interactive charts and tables. Built with modern Android development tools.

## ✨ About The Project

This project is part of the study and development of an Android application. The objectives are to learn how to retrieve data from an SQLite database, display data using charts and tables, and build a beautiful UI with Jetpack Compose. I hope this project will be beneficial for those interested in future Android application development.

## ⭐ Key Features

- **Database Display**: Reads and displays device data from the Gadgetbridge database.

- **Interactive Heart Rate Charts**: Displays heart rate data over time using zoomable and scrollable line charts.

- **CSV Data Import**: Parses and displays activity data from the sample MI_BAND_ACTIVITY_SAMPLE.csv file.

- **Data Export**: Allows exporting table data in multiple formats, including CSV, XLS, and TXT.

- **Modern UI**: Built entirely with Jetpack Compose for a clean, responsive, and modern user interface.

- **Info & Credits**: A dedicated screen to display data sources and libraries used.

## 🛠️ Technologies & Libraries Used

- **Kotlin**: Primary programming language

- **Jetpack Compose**: For building the entire user interface

- **MPAndroidChart**: For creating beautiful and interactive charts

- **Room Persistence Library**: For database access (schema definition)

- **Material 3**: For UI components and theming

- **Gradle**: For build automation

## 🚀 Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing.

Prerequisites

Android Studio (Latest stable version recommended)

Android SDK

Installation

Clone the repository:

Bash

git clone https://github.com/wit238/androidAPP.git
Open in Android Studio:

Open Android Studio

Select File > Open and navigate to the cloned repository directory.

Let Android Studio sync the project with Gradle.

Run the application:

Select an Emulator or connect a physical device.

Click the Run 'app' (▶️) button in the toolbar.

## 📊 Data Sources

The application is designed to work with a Gadgetbridge.db file.

A sample database is included in app/src/main/assets/Gadgetbridge.db.

Additionally, a sample CSV file is included in app/src/main/assets/MI_BAND_ACTIVITY_SAMPLE.csv for demonstration purposes.

## 📦 Building the APK

You can build a release APK directly from the command line using the Gradle wrapper.

Navigate to the project's root directory.

Run the following command:

On Windows:

Bash

gradlew.bat assembleRelease
On macOS/Linux:

Bash

./gradlew assembleRelease
The unsigned APK will be located in app/build/outputs/apk/release/.

## 🙏 Acknowledgements

Thank you to the Gadgetbridge team for their open-source project.

This project uses the powerful MPAndroidChart library by PhilJay.

==========================================================================================================================================================================================

# DataWearAPP - ตัวแสดงข้อมูล Mi Band

แอปพลิเคชัน Android ที่ออกแบบมาเพื่อแสดงข้อมูลการออกกำลังกายของ Mi Band ที่มาจากฐานข้อมูล Gadgetbridge แอปนี้มีอินเทอร์เฟซที่ใช้งานง่ายเพื่อดูรูปแบบอัตราการเต้นของหัวใจผ่านแผนภูมิและตารางแบบโต้ตอบ สร้างขึ้นด้วยเครื่องมือพัฒนา Android ที่ทันสมัย

## ✨ เกี่ยวกับโปรเจกต์

โปรเจกต์นี้เป็นส่วนหนึ่งของการศึกษาและพัฒนาแอปพลิเคชันบนระบบปฏิบัติการ Android โดยมีวัตถุประสงค์เพื่อเรียนรู้การดึงข้อมูลจากฐานข้อมูล SQLite, การแสดงผลข้อมูลด้วยกราฟและตาราง, และการสร้าง UI ที่สวยงามด้วย Jetpack Compose ครับ ผมหวังว่าโปรเจกต์นี้จะเป็นประโยชน์สำหรับผู้ที่สนใจพัฒนาแอปพลิเคชัน Android ต่อไป

## ⭐ ฟีเจอร์เด่น

- **การแสดงผลฐานข้อมูล**: อ่านและแสดงข้อมูลอุปกรณ์จากฐานข้อมูล Gadgetbridge
- **แผนภูมิอัตราการเต้นของหัวใจแบบโต้ตอบ**: แสดงข้อมูลอัตราการเต้นของหัวใจเมื่อเวลาผ่านไปโดยใช้แผนภูมิเส้นที่สามารถซูมและเลื่อนได้
- **การนำเข้าข้อมูล CSV**: แยกและแสดงข้อมูลกิจกรรมจากไฟล์ตัวอย่าง `MI_BAND_ACTIVITY_SAMPLE.csv`
- **การส่งออกข้อมูล**: อนุญาตให้ส่งออกข้อมูลตารางในหลายรูปแบบ รวมถึง **CSV**, **XLS** และ **TXT**
- **UI ที่ทันสมัย**: สร้างขึ้นทั้งหมดด้วย Jetpack Compose เพื่ออินเทอร์เฟซผู้ใช้ที่สะอาด ตอบสนอง และทันสมัย
- **ข้อมูลและเครดิต**: หน้าจอเฉพาะสำหรับแสดงที่มาของข้อมูลและไลบรารีที่ใช้

## 🛠️ เทคโนโลยีและไลบรารีที่ใช้

- **Kotlin**: ภาษาโปรแกรมหลัก
- **Jetpack Compose**: สำหรับการสร้างอินเทอร์เฟซผู้ใช้ทั้งหมด
- **MPAndroidChart**: สำหรับการสร้างแผนภูมิที่สวยงามและโต้ตอบได้
- **Room Persistence Library**: สำหรับการเข้าถึงฐานข้อมูล (การกำหนดสคีมา)
- **Material 3**: สำหรับส่วนประกอบ UI และธีม
- **Gradle**: สำหรับการสร้างอัตโนมัติ

## 🚀 การเริ่มต้นใช้งาน

ทำตามคำแนะนำเหล่านี้เพื่อคัดลอกโปรเจกต์และรันบนเครื่องของคุณเพื่อการพัฒนาและทดสอบ

### ข้อกำหนดเบื้องต้น

- [Android Studio](https://developer.android.com/studio) (แนะนำเวอร์ชันเสถียรล่าสุด)
- Android SDK

### การติดตั้ง

1.  **โคลน Repository:**
    ```sh
    git clone https://github.com/wit238/androidAPP.git
    ```
2.  **เปิดใน Android Studio:**
    - เปิด Android Studio
    - เลือก `File > Open` และไปที่ไดเรกทอรีของ Repository ที่โคลนมา
    - ให้ Android Studio ซิงค์โปรเจกต์กับ Gradle

3.  **รันแอปพลิเคชัน:**
    - เลือก Emulator หรือเชื่อมต่ออุปกรณ์จริง
    - คลิกปutton `Run 'app'` (▶️) ในแถบเครื่องมือ

## 📊 แหล่งข้อมูล

แอปพลิเคชันถูกออกแบบมาเพื่อทำงานกับไฟล์ `Gadgetbridge.db`

- มีฐานข้อมูลตัวอย่างรวมอยู่ใน `app/src/main/assets/Gadgetbridge.db`
- นอกจากนี้ยังมีไฟล์ CSV ตัวอย่างรวมอยู่ใน `app/src/main/assets/MI_BAND_ACTIVITY_SAMPLE.csv` เพื่อการสาธิต

## 📦 การสร้าง APK

คุณสามารถสร้าง APK สำหรับ Release ได้โดยตรงจากบรรทัดคำสั่งโดยใช้ Gradle wrapper

1.  ไปที่ไดเรกทอรีรากของโปรเจกต์
2.  รันคำสั่งต่อไปนี้:

    - บน Windows:
      ```sh
      gradlew.bat assembleRelease
      ```
    - บน macOS/Linux:
      ```sh
      ./gradlew assembleRelease
      ```

3.  APK ที่ยังไม่ได้ลงนามจะอยู่ใน `app/build/outputs/apk/release/`

## 🙏 ขอบคุณ

- ขอขอบคุณทีม **Gadgetbridge** สำหรับโปรเจกต์โอเพนซอร์สของพวกเขา
- โปรเจกต์นี้ใช้ไลบรารี **MPAndroidChart** ที่ทรงพลังโดย PhilJay
