package com.hkxy.adbqa;

public class CrashEntry {
	private int lineNumber;
	private String bugType;
	private String rawLine;

	public CrashEntry(int lineNumber, String bugType, String rawLine) {
		this.lineNumber = lineNumber;
		this.bugType = bugType;
		this.rawLine = rawLine;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public String getBugType() {
		return bugType;
	}

	public String getRawLine() {
		return rawLine;
	}

	@Override
	public String toString() {
		return "[Line " + lineNumber + "] " + bugType + ": " + rawLine;
	}
}
