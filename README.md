# datawear APP - Mi Band Data Visualizer

An Android application designed to visualize Mi Band fitness data sourced from the Gadgetbridge database. The app provides an intuitive interface to view heart rate patterns through interactive charts and tables, built with modern Android development tools.

## ✨ Features

- **Database Visualization**: Reads and displays device information from the Gadgetbridge database.
- **Interactive Heart Rate Chart**: Visualizes heart rate data over time using a zoomable and scrollable line chart.
- **CSV Data Import**: Parses and displays activity data from a sample `MI_BAND_ACTIVITY_SAMPLE.csv`.
- **Data Export**: Allows exporting table data into multiple formats, including **CSV**, **XLS**, and **TXT**.
- **Modern UI**: Built entirely with Jetpack Compose for a clean, reactive, and modern user interface.
- **Info & Credits**: A dedicated screen acknowledging the data sources and libraries used.

## 🛠️ Tech Stack & Libraries

- **Kotlin**: Primary programming language.
- **Jetpack Compose**: For building the entire user interface.
- **MPAndroidChart**: For creating beautiful and interactive charts.
- **Room Persistence Library**: For database access (schema definition).
- **Material 3**: For UI components and theming.
- **Gradle**: For build automation.

## 🚀 Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (latest stable version recommended)
- Android SDK

### Installation

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/wit238/androidAPP.git
    ```
2.  **Open in Android Studio:**
    - Open Android Studio.
    - Select `File > Open` and navigate to the cloned repository directory.
    - Let Android Studio sync the project with Gradle.

3.  **Run the application:**
    - Select an emulator or connect a physical device.
    - Click the `Run 'app'` button (▶️) in the toolbar.

## 📊 Data Source

The application is designed to work with a `Gadgetbridge.db` file.

- A sample database is included in `app/src/main/assets/Gadgetbridge.db`.
- A sample CSV file is also included in `app/src/main/assets/MI_BAND_ACTIVITY_SAMPLE.csv` for demonstration purposes.

## 📦 Building the APK

You can build a release APK directly from the command line using the Gradle wrapper.

1.  Navigate to the root of the project directory.
2.  Run the following command:

    - On Windows:
      ```sh
      gradlew.bat assembleRelease
      ```
    - On macOS/Linux:
      ```sh
      ./gradlew assembleRelease
      ```

3.  The unsigned APK will be located in `app/build/outputs/apk/release/`.

## 🙏 Acknowledgements

- Special thanks to the **Gadgetbridge** team for their open-source project.
- This project utilizes the powerful **MPAndroidChart** library by PhilJay.
