package com.hkxy.adbqa;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		AdbRunner adb = new AdbRunner();
		LogcatParser parser = new LogcatParser();
		ArrayList<String> logLines = new ArrayList<>();

		System.out.println("--- ADB Logcat QA Runner ---");

		if (adb.hasConnectedDevice()) {
			String model = adb.getDeviceProperty("ro.product.model");
			String androidVer = adb.getDeviceProperty("ro.build.version.release");
			String sdkVer = adb.getDeviceProperty("ro.build.version.sdk");

			System.out.println("Connected device: " + model + " (Android " + androidVer + ", API " + sdkVer + ")");
			logLines = adb.runAdbCommand(new String[] { "adb", "logcat", "-d", "-t", "300" });
		} else {
			String fallbackFile = "sample-logs/crash_dump.txt";
			if (args.length > 0) {
				fallbackFile = args[0];
			}

			System.out.println("No ADB device detected. Reading local file: " + fallbackFile);
			try {
				logLines = parser.loadLogFile(fallbackFile);
			} catch (IOException e) {
				System.out.println("Failed to read file: " + e.getMessage());
				return;
			}
		}

		ArrayList<CrashEntry> bugs = parser.findCrashes(logLines);

		System.out.println("Scanned " + logLines.size() + " lines. Found " + bugs.size() + " issue(s).");

		if (bugs.isEmpty()) {
			System.out.println("Result: PASS");
		} else {
			System.out.println("Result: FAIL");
			for (int i = 0; i < bugs.size(); i++) {
				System.out.println("  " + bugs.get(i).toString());
			}
		}
	}
}
