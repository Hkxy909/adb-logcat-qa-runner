package com.hkxy.adbqa;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AdbRunner {

	public ArrayList<String> runAdbCommand(String[] command) {
		ArrayList<String> output = new ArrayList<>();

		try {
			ProcessBuilder builder = new ProcessBuilder(command);
			builder.redirectErrorStream(true);
			Process process = builder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = reader.readLine();
			while (line != null) {
				output.add(line);
				line = reader.readLine();
			}
			reader.close();
			process.waitFor();
		} catch (Exception e) {
			return new ArrayList<>();
		}

		return output;
	}

	public boolean hasConnectedDevice() {
		ArrayList<String> lines = runAdbCommand(new String[] { "adb", "devices" });
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i).trim();
			if (!line.startsWith("List of devices") && line.endsWith("\tdevice")) {
				return true;
			}
		}
		return false;
	}

	public String getDeviceProperty(String propName) {
		ArrayList<String> res = runAdbCommand(new String[] { "adb", "shell", "getprop", propName });
		if (res.isEmpty()) {
			return "Unknown";
		}
		return res.get(0).trim();
	}
}
