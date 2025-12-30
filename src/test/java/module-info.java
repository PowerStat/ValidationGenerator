/*
 * Copyright (C) 2020-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */


/**
 * Validation generator module test.
 */
open module de.powerstat.ddd.generator
 {
  exports de.powerstat.ddd.generator;

  requires de.powerstat.phplib.templateengine;
  requires org.apache.logging.log4j;
  requires org.checkerframework.checker.qual;
  requires org.jmolecules.ddd;

  requires com.github.spotbugs.annotations;
  requires org.junit.jupiter.api;
  requires org.junit.jupiter.params;
  requires org.junit.platform.launcher;
  requires org.junit.platform.suite.api;
  // requires nl.jqno.equalsverifier;

 }
