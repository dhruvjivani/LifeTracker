package com.conestoga.lifetracker

// ApiIntegrationTest has been moved to src/test/ (JVM unit tests).
// MockWebServer uses cleartext HTTP which Android 9+ blocks on-device,
// causing setUp() to fail before mockWebServer is assigned.
// Running on the JVM has no such restriction.
