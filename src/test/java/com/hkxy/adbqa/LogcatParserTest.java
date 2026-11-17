package com.hkxy.adbqa;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LogcatParserTest {

	private LogcatParser parser;

	@BeforeEach
	public void setUp() {
		parser = new LogcatParser();
	}

	@Test
	public void testFindsFatalCrashAndAnr() {
		ArrayList<String> sample = new ArrayList<>();
		sample.add("11-16 14:00:01 I ActivityManager: Starting service");
		sample.add("11-16 14:00:02 E AndroidRuntime: FATAL EXCEPTION: main");
		sample.add("11-16 14:00:05 E ActivityManager: ANR in com.test.app");

		ArrayList<CrashEntry> results = parser.findCrashes(sample);

		assertEquals(2, results.size());
		assertEquals("FATAL_CRASH", results.get(0).getBugType());
		assertEquals(2, results.get(0).getLineNumber());
		assertEquals("APP_NOT_RESPONDING", results.get(1).getBugType());
	}

	@Test
	public void testDetectsNetworkTimeouts() {
		ArrayList<String> sample = new ArrayList<>();
		sample.add("W System.err: java.net.SocketTimeoutException: connect timed out");

		ArrayList<CrashEntry> results = parser.findCrashes(sample);

		assertEquals(1, results.size());
		assertEquals("NETWORK_DROP", results.get(0).getBugType());
	}

	@Test
	public void testCleanLogsReturnZeroCrashes() {
		ArrayList<String> clean = new ArrayList<>();
		clean.add("I WifiService: Wi-Fi connected");
		clean.add("D SyncAgent: Heartbeat HTTP 200 OK");

		ArrayList<CrashEntry> results = parser.findCrashes(clean);

		assertTrue(results.isEmpty());
	}

	@Test
	public void testEmptyAndNullInputBoundary() {
		ArrayList<String> empty = new ArrayList<>();
		assertEquals(0, parser.findCrashes(empty).size());
		assertEquals(0, parser.findCrashes(null).size());
	}

	@Test
	public void testBatteryThermalThresholdBoundary() {
		assertFalse(parser.isBatteryOverheating(449));
		assertTrue(parser.isBatteryOverheating(450));
	}

	@Test
	public void testNegativeBatteryTempThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> {
			parser.isBatteryOverheating(-10);
		});
	}
}
