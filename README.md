# PlatformerGame

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and a main class extending `Game` that sets the first screen.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.

---

## Analysis Tool \- Setup & Installation

This section describes how to set up and use the analysis tool, which includes SonarQube integration, Java-based generation, and a Python script for further analysis. The tool is located in the package “java/com/mygdx/platformer/analysistool”.

### 1\. Prerequisites

- **Java 21**
- **Python 3.10**
- **SonarQube** (server and scanner)
- Access to your project's `.java` files or pre-generated analysis material

### 2\. SonarQube Setup

1. **Install SonarQube Server**

    - Download and install SonarQube from [https://www.sonarqube.org/downloads/](https://www.sonarqube.org/downloads/)
    - Start the SonarQube server (default address: `http://localhost:9000`)



2. **Install SonarScanner**

    - Download SonarScanner from [https://docs.sonarqube.org/latest/analysis/scan/sonarscanner/](https://docs.sonarqube.org/latest/analysis/scan/sonarscanner/)
    - Define the path to SonarScanner in your system's environment variables



3. **API Keys**

    - Generate an API key in your SonarQube user profile (Administration \> My Account \> Security)
    - Save this key for use in the Python script and in the sonar-project.properties environment file.

### 3\. Analysis Tool Setup

Depending on your workflow, choose one of the following:

#### A. Using Your Own Material

- If you already have the required `.java` files and generated material, you can skip the generation step.
- Specify the path to your generated material in the configuration.

#### B. Generating Material with the Tool

- Modify the `generateRCG()` function and the `AttackExporter` class as needed to fit your system and requirements.
- Ensure your `.java` files are available for processing.
- If you do not need the generation functionality, you can comment out the generation functions in the code and only specify the path to the pre-generated material.

### 4\. Data Exporter Setup

- Configure the following in the python script:

    - **Project Name**: The name of your SonarQube project
    - **API Key**: The key generated from your SonarQube account
    - **Server Address**: The address of your SonarQube server (default: `http://localhost:9000`)


- **Requirements:**

    - The target directory must contain the folders `rcg` and `pcg`.
    - If your generated material is structured differently, adjust the regex patterns in the Python script accordingly.

### 5\. Running the Tool

1. **Start SonarQube Server** (if not already running)
2. **Run the Analysis Tool** to generate, process and upload the required files to SonarQube
3. **Run the Python Script** to export the uploaded results from SonarQube into csv-files.

### 6\. Troubleshooting

- Ensure all paths are correctly set in your environment variables and configuration files.
- Check that the SonarQube server is running and accessible.
- Verify that the required folders (`rcg`, `pcg`) exist in your target directory.

### 7\. Additional Notes

- For custom setups or advanced usage, refer to the comments in the code (e.g., `generateRCG()`, `AttackExporter`) for further customization.
- If you encounter issues with the Python script, review and adapt the regex patterns to match your material's structure.

---
