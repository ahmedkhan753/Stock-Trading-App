@echo off
echo ============================================
echo Stock Trading App - Build & Run
echo ============================================
echo.
echo Checking Java version...
java -version 2>&1 | findstr "version" 
echo.
echo NOTE: This app requires Java 21 or later for JavaFX 25 compatibility.
echo Current Java is Java 8. Please:
echo   1. Install Java 21+ (OpenJDK or Oracle JDK)
echo   2. Set JAVA_HOME to Java 21+ installation
echo   3. Or run this project in IntelliJ IDEA (preferred)
echo.
echo RECOMMENDED: Open project in IntelliJ IDEA
echo - Right-click on src/Main.java
echo - Select "Run 'Main.main()'"
echo.
pause
