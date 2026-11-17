package com.hkxy.adbqa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LogcatParser {

	public ArrayList<String> loadLogFile(String filePath) throws IOException {
		ArrayList<String> lines = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String currentLine = reader.readLine();

		while (currentLine != null) {
			lines.add(currentLine);
			currentLine = reader.readLine();
		}
		reader.close();
		return lines;
	}

	public ArrayList<CrashEntry> findCrashes(ArrayList<String> logLines) {
		ArrayList<CrashEntry> crashes = new ArrayList<>();

		if (logLines == null || logLines.isEmpty()) {
			return crashes;
		}

		for (int i = 0; i < logLines.size(); i++) {
			String line = logLines.get(i);
			if (line == null) {
				continue;
			}

			int lineNum = i + 1;

			if (line.contains("FATAL EXCEPTION")) {
				crashes.add(new CrashEntry(lineNum, "FATAL_CRASH", line.trim()));
			} else if (line.contains("ANR in ")) {
				crashes.add(new CrashEntry(lineNum, "APP_NOT_RESPONDING", line.trim()));
			} else if (line.contains("NullPointerException")) {
				crashes.add(new CrashEntry(lineNum, "NULL_POINTER", line.trim()));
			} else if (line.contains("OutOfMemoryError")) {
				crashes.add(new CrashEntry(lineNum, "OUT_OF_MEMORY", line.trim()));
			} else if (line.contains("UnknownHostException") || line.contains("SocketTimeoutException")) {
				crashes.add(new CrashEntry(lineNum, "NETWORK_DROP", line.trim()));
			}
		}

		return crashes;
	}

	// dumpsys battery prints temp in tenths of a degree C (385 = 38.5C)
	public boolean isBatteryOverheating(int rawAdbTemp) {
		if (rawAdbTemp < 0) {
			throw new IllegalArgumentException("Invalid battery temp: " + rawAdbTemp);
		}
		return rawAdbTemp >= 450;
	}
}
