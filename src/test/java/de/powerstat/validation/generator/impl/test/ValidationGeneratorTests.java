/*
 * Copyright (C) 2020 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.validation.generator.impl.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.powerstat.validation.generator.impl.ValidationGenerator;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Validation code generator tests.
 */
@SuppressFBWarnings({"WEAK_MESSAGE_DIGEST_MD5", "SEC_SIDE_EFFECT_CONSTRUCTOR"})
public class ValidationGeneratorTests
 {
  /**
   * Int constant.
   */
  private static final String INT = "int"; //$NON-NLS-1$

  /**
   * Long constant.
   */
  private static final String LONG = "long"; //$NON-NLS-1$

  /**
   * String constant.
   */
  private static final String STRING = "string"; //$NON-NLS-1$

  /**
   * Test 1.
   */
  private static final String TEST1 = "Test1"; //$NON-NLS-1$

  /**
   * Generated sources directory.
   */
  private static final String GENERATED_SOURCES_DIR = "target/generated-sources";


  /**
   * Default constructor.
   */
  public ValidationGeneratorTests()
   {
    super();
   }


  /**
   * Test constructor with template types.
   *
   * @param templateType Template type: string, int, long
   */
  @ParameterizedTest
  @ValueSource(strings = {STRING, INT, LONG})
  public void constructorTypes(final String templateType)
   {
    final ValidationGenerator generator = new ValidationGenerator(GENERATED_SOURCES_DIR, TEST1, templateType);
    assertNotNull(generator, "Generaor creation failed"); //$NON-NLS-1$
   }


  /**
   * Test constructor with Null pointer.
   */
  @Test
  public void constructorNull1()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final ValidationGenerator generator = */ new ValidationGenerator(null, TEST1, STRING);
     }
    );
   }


  /**
   * Test constructor with Null pointer.
   */
  @Test
  public void constructorNull2()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final ValidationGenerator generator = */ new ValidationGenerator(GENERATED_SOURCES_DIR, null, STRING);
     }
    );
   }


  /**
   * Test constructor with Null pointer.
   */
  @Test
  public void constructorNull3()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final ValidationGenerator generator = */ new ValidationGenerator(GENERATED_SOURCES_DIR, TEST1, null);
     }
    );
   }


  /**
   * Test constructor with wrong template type.
   */
  @Test
  public void constructorWrongType()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ValidationGenerator generator = */ new ValidationGenerator(GENERATED_SOURCES_DIR, TEST1, ""); //$NON-NLS-1$
     }
    );
   }


  /**
   * Test getClasses with template types.
   *
   * @param templateType Template type: string, int, long
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException no such algorithm exception
   */
  @ParameterizedTest
  @ValueSource(strings = {STRING, INT, LONG})
  public void getclasses(final String templateType) throws IOException, NoSuchAlgorithmException
   {
    final Map<String, String> classChecksums = new ConcurrentHashMap<>();
    classChecksums.put(STRING, "0e4dc3a82e1204258e1d0799a5553066"); //$NON-NLS-1$
    classChecksums.put(INT, "b64d1b66b8fbdf68794b10488e3affb8"); //$NON-NLS-1$
    classChecksums.put(LONG, "0de115b13234c6029799c4ffa92182e5"); //$NON-NLS-1$
    final Map<String, String> testChecksums = new ConcurrentHashMap<>();
    testChecksums.put(STRING, "d41d8cd98f00b204e9800998ecf8427e"); //$NON-NLS-1$
    testChecksums.put(INT, "d41d8cd98f00b204e9800998ecf8427e"); //$NON-NLS-1$
    testChecksums.put(LONG, "d41d8cd98f00b204e9800998ecf8427e"); //$NON-NLS-1$

    final ValidationGenerator generator = new ValidationGenerator(GENERATED_SOURCES_DIR, "Test2", templateType); //$NON-NLS-1$
    generator.getClasses();

    final MessageDigest msgdigit = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
    try (BufferedInputStream inStream = new BufferedInputStream((Files.newInputStream(Paths.get("target/generated-sources/de/powerstat/validation/values/Test2.java")))); DigestOutputStream out = new DigestOutputStream(OutputStream.nullOutputStream(), msgdigit)) //$NON-NLS-1$
     {
      inStream.transferTo(out);
     }
    try (BufferedInputStream inStream = new BufferedInputStream((Files.newInputStream(Paths.get("target/generated-sources/de/powerstat/validation/values/test/Test2Tests.java")))); DigestOutputStream out = new DigestOutputStream(OutputStream.nullOutputStream(), msgdigit)) //$NON-NLS-1$
     {
      inStream.transferTo(out);
     }
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(classChecksums.get(templateType), String.format("%0" + (msgdigit.getDigestLength() * 2) + "x", new BigInteger(1, msgdigit.digest())), "class hashCodes are not equal " + templateType), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      () -> assertEquals(testChecksums.get(templateType), String.format("%0" + (msgdigit.getDigestLength() * 2) + "x", new BigInteger(1, msgdigit.digest())), "test hashCodes are not equal " + templateType) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    );
   }

 }
