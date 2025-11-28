# Java Guardian: Automated Dependency Manager 🛡️

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Gradle](https://img.shields.io/badge/Gradle-Kotlin_DSL-blue.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)

**Java Guardian** is a command-line interface (CLI) tool designed to automate the maintenance of Java software architectures. It parses project build files, analyzes dependency health against real-time data from Maven Central, and automatically patches outdated libraries to ensure project security and stability.

---

## 🚀 Key Features

* **🔍 Deep Scan:** Parses `pom.xml` files to build a dependency graph of the project.
* **🌐 Real-Time Analysis:** Queries the Maven Central API to fetch the latest stable versions and release metadata.
* **📊 Smart Reporting:** Generates a visually rich, color-coded CLI dashboard summarizing project health.
* **🤖 Auto-Pilot Updates:** Automatically rewrites the build file to upgrade outdated dependencies to their latest stable versions without breaking syntax.

---

## 📸 Demo Output

**Scan Command:**
```text
   ______                     _ _             
  / ____/___  __  ___  ____  (_) | ____  ____ 
 / / __/ __ \/ / / / |/_/ / / / / / __ \/ __ \
/ /_/ / /_/ / /_/ />  </ /_/ / / / /_/ / / / /
\____/\____/\__,_/_/|_|\__,_/_/_/\__,_/_/ /_/ 
                                    v1.0

🔍 Scanning local repository...
---------------------------------------------------------------------------------
| DEPENDENCY                               | CURRENT      | LATEST       | STATUS   |       
---------------------------------------------------------------------------------
| org.junit.jupiter:junit-jupiter-api      | 5.9.1        | 5.10.0       | ⚠ UPDATE |
| org.apache.logging.log4j:log4j-core      | 2.19.0       | 2.20.0       | ⚠ UPDATE |
---------------------------------------------------------------------------------

📊 PROJECT HEALTH SUMMARY:
   Total Dependencies: 2
   Status: 2 Updates Required.
   Action: Run guardian update to patch.



   ---

## 🛠️ Tech Stack

* **Language:** Java 21
* **Build System:** Gradle (Kotlin DSL)
* **CLI Framework:** Picocli
* **Network:** OkHttp (for REST API communication)
* **Data Processing:** Gson (JSON parsing), Maven Model (XML manipulation)

---

## 📦 Installation & Usage

You can download the latest release or build it from source.

### Option 1: Run the Fat JAR
Navigate to the directory containing the JAR file and your `pom.xml`.

```bash
# To scan the project
java -jar JavaGuardian-all.jar scan

# To automatically update dependencies
java -jar JavaGuardian-all.jar update

OPTION 2 Build from Source(Prerequisites: JDK 21+ )

# 1. Clone the repository
git clone [https://github.com/yourusername/JavaGuardian.git](https://github.com/yourusername/JavaGuardian.git)

# 2. Build the project (creates a Fat JAR)
./gradlew shadowJar

# 3. Run the tool

java -jar build/libs/JavaGuardian-all.jar scan


**METHOD 2**
Install Java Guardian as a global command so youcan run "guardian scan" from any folder on your computer

1. CREATE A TOOL DIRECTORY : Create a folder to keep the tool safe (eg: C:/Tools/JavaGuardian)
and move JavaGuardian-all.jar inside it

2. CREATE THE LAUNCHER SCRIPT : Inside the same folder, create a new file named "guardian.bat"
and paste this code

"@echo off
 java -jar  "%~dp0JavaGuardian-all.jar" %* "

3. UPDATE SYSTEM PATH :

i. Search for "Edit the system environment variables" in Windows.
ii. Click "Environment Variables".
iii. Under "System variables", select Path and click Edit
iv. Click New and paste the path to your folder  (eg: C:\Tools\JavaGuardian).
v. Click OK on all windows.

4. VERIFY INSTALLATION :
Open a new terminal window and type :
"guardian --help"

NOW YOU CAN GO TO ANY PROJECT FOLDER ON YOUR COMPUTER AND SIMPLY RUN "guardian scan". 
