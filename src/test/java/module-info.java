/*
 * Copyright (C) 2020-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
 
 
/**
 * Validation generator module test.
 */
open module de.powerstat.validation.generator
 {
  exports de.powerstat.validation.generator;

  requires de.powerstat.phplib.templateengine;
  requires org.apache.logging.log4j;

  requires com.github.spotbugs.annotations;
  requires org.junit.jupiter.api;
  requires org.junit.jupiter.params;
  requires org.junit.platform.launcher;
  requires org.junit.platform.suite.api;
  // requires nl.jqno.equalsverifier;

 }
