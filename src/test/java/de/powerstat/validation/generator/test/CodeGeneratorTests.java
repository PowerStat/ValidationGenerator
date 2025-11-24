/*
 * Copyright (C) 2020-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.validation.generator.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.powerstat.validation.generator.CodeGenerator;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Code generator tests.
 */
@SuppressFBWarnings("WEAK_MESSAGE_DIGEST_MD5")
final class CodeGeneratorTests
 {
  /**
   * String constant.
   */
  private static final String STRING = "string"; //$NON-NLS-1$

  /**
   * Int constant.
   */
  private static final String INT = "int"; //$NON-NLS-1$

  /**
   * Long constant.
   */
  private static final String LONG = "long"; //$NON-NLS-1$

  /**
   * Enum constant.
   */
  private static final String ENUM = "enum"; //$NON-NLS-1$

  /**
   * Hex constant.
   */
  private static final String D41D8CD98F00B204E9800998ECF8427E = "d41d8cd98f00b204e9800998ecf8427e"; //$NON-NLS-1$

  /**
   * Zero.
   */
  private static final String ZERO = "%0"; //$NON-NLS-1$

  /**
   * Test3 constant.
   */
  private static final String TEST3 = "Test3"; //$NON-NLS-1$

  /**
   * Original output stream.
   */
  private static final PrintStream ORIGINAL_OUT = System.out;

  /**
   * Original error stream.
   */
  private static final PrintStream ORIGINAL_ERR = System.err;

  /**
   * Out stream.
   */
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

  /**
   * Error stream.
   */
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();


  /**
   * Default constructor.
   */
  /* default */ CodeGeneratorTests()
   {
    super();
   }


  /**
   * Setup stream.
   *
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @BeforeEach
  /* default */ void setUpStreams() throws UnsupportedEncodingException
   {
     System.setOut(new PrintStream(outContent, true, Charset.defaultCharset().name()));
     System.setErr(new PrintStream(errContent, true, Charset.defaultCharset().name()));
   }


  /**
   * Restore streams.
   */
  @AfterEach
  /* default */ void restoreStreams()
   {
    System.setOut(ORIGINAL_OUT);
    System.setErr(ORIGINAL_ERR);
   }


  /**
   * Test main.
   *
   * @param templateType Template type: string, int, long
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException  No such algorithm exception
   * @throws InterruptedException Interrupted Exception
   */
  @ParameterizedTest
  @ValueSource(strings = {CodeGeneratorTests.STRING, CodeGeneratorTests.INT, CodeGeneratorTests.LONG, CodeGeneratorTests.ENUM})
  /* default */ void testMain(final String templateType) throws IOException, NoSuchAlgorithmException, InterruptedException
   {
    final Map<String, String> classChecksums = new ConcurrentHashMap<>();
    classChecksums.put(CodeGeneratorTests.STRING, "4b3db28bedd153fb23ab8fcd1f99f435"); //$NON-NLS-1$
    classChecksums.put(CodeGeneratorTests.INT, "a30e8a5311a5b17d360d051e9ddf8ce9"); //$NON-NLS-1$
    classChecksums.put(CodeGeneratorTests.LONG, "981ccbed5406aed8363cea80cfd613ef"); //$NON-NLS-1$
    classChecksums.put(CodeGeneratorTests.ENUM, "9e8e3b40f38ae3fc2897c1c1354a36be"); //$NON-NLS-1$
    final Map<String, String> testChecksums = new ConcurrentHashMap<>();
    testChecksums.put(CodeGeneratorTests.STRING, CodeGeneratorTests.D41D8CD98F00B204E9800998ECF8427E);
    testChecksums.put(CodeGeneratorTests.INT, CodeGeneratorTests.D41D8CD98F00B204E9800998ECF8427E);
    testChecksums.put(CodeGeneratorTests.LONG, CodeGeneratorTests.D41D8CD98F00B204E9800998ECF8427E);
    testChecksums.put(CodeGeneratorTests.ENUM, CodeGeneratorTests.D41D8CD98F00B204E9800998ECF8427E);

    final String[] args = {CodeGeneratorTests.TEST3, templateType};
    CodeGenerator.main(args);

    final MessageDigest msgdigi = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
    try (BufferedInputStream inStream = new BufferedInputStream((Files.newInputStream(Paths.get("target/generated-sources/de/powerstat/validation/values/Test3.java")))); DigestOutputStream out = new DigestOutputStream(OutputStream.nullOutputStream(), msgdigi)) //$NON-NLS-1$
     {
      inStream.transferTo(out);
     }
    try (BufferedInputStream inStream = new BufferedInputStream((Files.newInputStream(Paths.get("target/generated-sources/de/powerstat/validation/values/test/Test3Tests.java")))); DigestOutputStream out = new DigestOutputStream(OutputStream.nullOutputStream(), msgdigi)) //$NON-NLS-1$
     {
      inStream.transferTo(out);
     }
    final String format = CodeGeneratorTests.ZERO + (msgdigi.getDigestLength() * 2) + 'x';
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(classChecksums.get(templateType), String.format(format, new BigInteger(1, msgdigi.digest())), "class hashCodes are not equal " + templateType), //$NON-NLS-1$
      () -> assertEquals(testChecksums.get(templateType), String.format(format, new BigInteger(1, msgdigi.digest())), "test hashCodes are not equal " + templateType) //$NON-NLS-1$
    );
   }


  /**
   * Test main with exception.
   *
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  @Test
  /* default */ void testMainException() throws UnsupportedEncodingException
   {
    final String[] args = {CodeGeneratorTests.TEST3, "unknown"}; //$NON-NLS-1$
    CodeGenerator.main(args);
    assertEquals("Unknown templateType", errContent.toString(Charset.defaultCharset().name()).replace("\n", "").replace("\r", ""), "System.err not as expected"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
   }

 }
