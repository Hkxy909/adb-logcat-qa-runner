# adb-logcat-qa-runner

Small Java command-line tool I built for Android QA testing. It runs `adb` commands to pull device properties and recent `logcat` output from an Android device or emulator, then scans the logs for common crash signatures:

- `FATAL EXCEPTION` crashes
- `ANR in` (Application Not Responding) freezes
- `NullPointerException` & `OutOfMemoryError`
- Network drops (`SocketTimeoutException` / `UnknownHostException`)

If no device is plugged in over ADB, it defaults to reading a saved `.txt` logcat dump from `sample-logs/crash_dump.txt` so regression checks can still run offline.

## Stack
- Java 17
- JUnit 5
- Maven
- Android Debug Bridge (ADB)

## Running it
Run the JUnit 5 test suite:
```bash
mvn test
```

Run the main logcat check:
```bash
mvn compile exec:java -Dexec.mainClass="com.hkxy.adbqa.Main"
```
