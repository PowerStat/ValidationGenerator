/*
 * Copyright (C) 2020-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.validation.generator.impl.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.FileSystemException;
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
final class ValidationGeneratorTests
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
   * Enum constant.
   */
  private static final String ENUM = "enum"; //$NON-NLS-1$

  /**
   * Test 1.
   */
  private static final String TEST1 = "Test1"; //$NON-NLS-1$

  /**
   * MD5 hash algorithm constant.
   */
  private static final String MD5 = "MD5"; //$NON-NLS-1$

  /**
   * Generated sources directory.
   */
  private static final String GENERATED_SOURCES_DIR = "target/generated-sources"; //$NON-NLS-1$

  /**
   * Target test1 file path constant.
   */
  private static final String TARGET_TEST1 = "target/test1"; //$NON-NLS-1$

  /**
   * Target test2 file path constant.
   */
  private static final String TARGET_TEST2 = "target/test2"; //$NON-NLS-1$

  /**
   * Target test3 file path constant.
   */
  private static final String TARGET_TEST3 = "target/test3"; //$NON-NLS-1$

  /**
   * Null pointer exception expected.
   */
  private static final String NULL_POINTER_EXCEPTION_EXPECTED = "Null pointer exception expected"; //$NON-NLS-1$

  /**
   * Checksum string.
   */
  private static final String CHECKSUM1 = "d41d8cd98f00b204e9800998ecf8427e"; //$NON-NLS-1$

  /**
   * Checksum string.
   */
  private static final String CHECKSUMSTRING = "8c4d2d1d4bcf4b18a70efb5bf8c691c2"; //$NON-NLS-1$

  /**
   * Checksum string.
   */
  private static final String CHECKSUMLONG = "031a93e197489de149b16f42fa95cca0"; //$NON-NLS-1$

  /**
   * Checksum string.
   */
  private static final String CHECKSUMINT = "01a51a44970e95c58ae16dae0372a2e6"; //$NON-NLS-1$

  /**
   * Checksum string.
   */
  private static final String CHECKSUMENUM = "50a95f7a3a59356178228757671cbe5a"; //$NON-NLS-1$

  /**
   * Zero constant.
   */
  private static final String ZERO = "%0"; //$NON-NLS-1$

  /**
   * Test 2 constant.
   */
  private static final String TEST2 = "Test2"; //$NON-NLS-1$

  /**
   * Illegal argument expected constant.
   */
  private static final String ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED = "Illegal argument exception expected"; //$NON-NLS-1$

  /**
   * Test has code constant.
   */
  private static final String TEST_HASH_CODE = "testHashCode"; //$NON-NLS-1$

  /**
   * Class hasCodes are not equal constant.
   */
  private static final String CLASS_HASH_CODES_ARE_NOT_EQUAL = "class hashCodes are not equal "; //$NON-NLS-1$

  /**
   * Test hasCodes are not equal constant.
   */
  private static final String TEST_HASH_CODES_ARE_NOT_EQUAL = "test hashCodes are not equal "; //$NON-NLS-1$

  /**
   * File system exception expected constant.
   */
  private static final String FILE_SYSTEM_EXCEPTION_EXPECTED = "File system exception expected"; //$NON-NLS-1$


  /**
   * Default constructor.
   */
  /* default */ ValidationGeneratorTests()
   {
    super();
   }


  /**
   * Test constructor with template types.
   *
   * @param templateType Template type: string, int, long
   */
  @ParameterizedTest
  @ValueSource(strings = {ValidationGeneratorTests.STRING, ValidationGeneratorTests.INT, ValidationGeneratorTests.LONG, ValidationGeneratorTests.ENUM})
  /* default */ void testConstructorTypes(final String templateType)
   {
    final ValidationGenerator generator = new ValidationGenerator(ValidationGeneratorTests.GENERATED_SOURCES_DIR, ValidationGeneratorTests.TEST1, templateType);
    assertNotNull(generator, "Generaor creation failed"); //$NON-NLS-1$
   }


  /**
   * Test constructor with Null pointer.
   */
  @Test
  /* default */ void testConstructorNull1()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final ValidationGenerator generator = */ new ValidationGenerator(null, ValidationGeneratorTests.TEST1, ValidationGeneratorTests.STRING);
     }, ValidationGeneratorTests.NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test constructor with Null pointer.
   */
  @Test
  /* default */ void testConstructorNull2()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final ValidationGenerator generator = */ new ValidationGenerator(ValidationGeneratorTests.GENERATED_SOURCES_DIR, null, ValidationGeneratorTests.STRING);
     }, ValidationGeneratorTests.NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test constructor with Null pointer.
   */
  @Test
  /* default */ void testConstructorNull3()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final ValidationGenerator generator = */ new ValidationGenerator(ValidationGeneratorTests.GENERATED_SOURCES_DIR, ValidationGeneratorTests.TEST1, null);
     }, ValidationGeneratorTests.NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test constructor with wrong template type.
   */
  @Test
  /* default */ void testConstructorWrongType()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ValidationGenerator generator = */ new ValidationGenerator(ValidationGeneratorTests.GENERATED_SOURCES_DIR, ValidationGeneratorTests.TEST1, ""); //$NON-NLS-1$
     }, ValidationGeneratorTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test constructor with file instead of directory.
   */
  @Test
  /* default */ void testConstructorNoDir()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ValidationGenerator generator = */ new ValidationGenerator(ValidationGeneratorTests.GENERATED_SOURCES_DIR + "/de/powerstat/validation/values/test/Test2Tests.java", ValidationGeneratorTests.TEST1, ValidationGeneratorTests.STRING); //$NON-NLS-1$
     }, ValidationGeneratorTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test constructor with wrong classname.
   */
  @Test
  /* default */ void testConstructorWrongClassname()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ValidationGenerator generator = */ new ValidationGenerator(ValidationGeneratorTests.GENERATED_SOURCES_DIR, "test1", ValidationGeneratorTests.STRING); //$NON-NLS-1$
     }, ValidationGeneratorTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
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
  @ValueSource(strings = {ValidationGeneratorTests.STRING, ValidationGeneratorTests.INT, ValidationGeneratorTests.LONG, ENUM})
  /* default */ void testGetclasses(final String templateType) throws IOException, NoSuchAlgorithmException
   {
    final Map<String, String> classChecksums = new ConcurrentHashMap<>();
    classChecksums.put(ValidationGeneratorTests.STRING, ValidationGeneratorTests.CHECKSUMSTRING);
    classChecksums.put(ValidationGeneratorTests.INT, ValidationGeneratorTests.CHECKSUMINT);
    classChecksums.put(ValidationGeneratorTests.LONG, ValidationGeneratorTests.CHECKSUMLONG);
    classChecksums.put(ValidationGeneratorTests.ENUM, ValidationGeneratorTests.CHECKSUMENUM);
    final Map<String, String> testChecksums = new ConcurrentHashMap<>();
    testChecksums.put(ValidationGeneratorTests.STRING, ValidationGeneratorTests.CHECKSUM1);
    testChecksums.put(ValidationGeneratorTests.INT, ValidationGeneratorTests.CHECKSUM1);
    testChecksums.put(ValidationGeneratorTests.LONG, ValidationGeneratorTests.CHECKSUM1);
    testChecksums.put(ValidationGeneratorTests.ENUM, ValidationGeneratorTests.CHECKSUM1);

    final ValidationGenerator generator = new ValidationGenerator(ValidationGeneratorTests.GENERATED_SOURCES_DIR, ValidationGeneratorTests.TEST2, templateType);
    generator.getClasses();

    final MessageDigest msgdigit = MessageDigest.getInstance(ValidationGeneratorTests.MD5);
    try (BufferedInputStream inStream = new BufferedInputStream((Files.newInputStream(Paths.get("target/generated-sources/de/powerstat/validation/values/Test2.java")))); DigestOutputStream out = new DigestOutputStream(OutputStream.nullOutputStream(), msgdigit)) //$NON-NLS-1$
     {
      inStream.transferTo(out);
     }
    try (BufferedInputStream inStream = new BufferedInputStream((Files.newInputStream(Paths.get("target/generated-sources/de/powerstat/validation/values/test/Test2Tests.java")))); DigestOutputStream out = new DigestOutputStream(OutputStream.nullOutputStream(), msgdigit)) //$NON-NLS-1$
     {
      inStream.transferTo(out);
     }
    assertAll(ValidationGeneratorTests.TEST_HASH_CODE,
      () -> assertEquals(classChecksums.get(templateType), String.format(ValidationGeneratorTests.ZERO + (msgdigit.getDigestLength() * 2) + 'x', new BigInteger(1, msgdigit.digest())), ValidationGeneratorTests.CLASS_HASH_CODES_ARE_NOT_EQUAL + templateType),
      () -> assertEquals(testChecksums.get(templateType), String.format(ValidationGeneratorTests.ZERO + (msgdigit.getDigestLength() * 2) + 'x', new BigInteger(1, msgdigit.digest())), ValidationGeneratorTests.TEST_HASH_CODES_ARE_NOT_EQUAL + templateType)
    );
   }


  /**
   * Test getClasses with incomplete path.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException no such algorithm exception
   */
  @Test
  /* default */ void testGetclassesPathTrue() throws IOException, NoSuchAlgorithmException
   {
    final Map<String, String> classChecksums = new ConcurrentHashMap<>();
    classChecksums.put(ValidationGeneratorTests.STRING, ValidationGeneratorTests.CHECKSUMSTRING);
    classChecksums.put(ValidationGeneratorTests.INT, ValidationGeneratorTests.CHECKSUMINT);
    classChecksums.put(ValidationGeneratorTests.LONG, ValidationGeneratorTests.CHECKSUMLONG);
    classChecksums.put(ValidationGeneratorTests.ENUM, ValidationGeneratorTests.CHECKSUMENUM);
    final Map<String, String> testChecksums = new ConcurrentHashMap<>();
    testChecksums.put(ValidationGeneratorTests.STRING, ValidationGeneratorTests.CHECKSUM1);
    testChecksums.put(ValidationGeneratorTests.INT, ValidationGeneratorTests.CHECKSUMLONG);
    testChecksums.put(ValidationGeneratorTests.LONG, ValidationGeneratorTests.CHECKSUMLONG);
    testChecksums.put(ValidationGeneratorTests.ENUM, ValidationGeneratorTests.CHECKSUM1);

    /* final boolean success = */ new File(ValidationGeneratorTests.TARGET_TEST1).mkdir();
    final ValidationGenerator generator = new ValidationGenerator(ValidationGeneratorTests.TARGET_TEST1, ValidationGeneratorTests.TEST2, ValidationGeneratorTests.STRING);
    generator.getClasses();

    final MessageDigest msgdigit = MessageDigest.getInstance(ValidationGeneratorTests.MD5);
    try (BufferedInputStream inStream = new BufferedInputStream((Files.newInputStream(Paths.get("target/test1/de/powerstat/validation/values/Test2.java")))); DigestOutputStream out = new DigestOutputStream(OutputStream.nullOutputStream(), msgdigit)) //$NON-NLS-1$
     {
      inStream.transferTo(out);
     }
    try (BufferedInputStream inStream = new BufferedInputStream((Files.newInputStream(Paths.get("target/test1/de/powerstat/validation/values/test/Test2Tests.java")))); DigestOutputStream out = new DigestOutputStream(OutputStream.nullOutputStream(), msgdigit)) //$NON-NLS-1$
     {
      inStream.transferTo(out);
     }
    assertAll(ValidationGeneratorTests.TEST_HASH_CODE,
      () -> assertEquals(classChecksums.get(ValidationGeneratorTests.STRING), String.format(ValidationGeneratorTests.ZERO + (msgdigit.getDigestLength() * 2) + 'x', new BigInteger(1, msgdigit.digest())), ValidationGeneratorTests.CLASS_HASH_CODES_ARE_NOT_EQUAL + ValidationGeneratorTests.STRING),
      () -> assertEquals(testChecksums.get(ValidationGeneratorTests.STRING), String.format(ValidationGeneratorTests.ZERO + (msgdigit.getDigestLength() * 2) + 'x', new BigInteger(1, msgdigit.digest())), ValidationGeneratorTests.TEST_HASH_CODES_ARE_NOT_EQUAL + ValidationGeneratorTests.STRING)
    );
   }


  /**
   * Test getClasses with incomplete path.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException no such algorithm exception
   */
  @Test
  /* default */ void testGetclassesPathFalse1() throws IOException, NoSuchAlgorithmException
   {
    assertThrows(FileSystemException.class, () ->
     {
      /*
      final Map<String, String> classChecksums = new ConcurrentHashMap<>();
      classChecksums.put(ValidationGeneratorTests.STRING, ValidationGeneratorTests.CHECKSUMSTRING);
      classChecksums.put(ValidationGeneratorTests.INT, ValidationGeneratorTests.CHECKSUMINT);
      classChecksums.put(ValidationGeneratorTests.LONG, ValidationGeneratorTests.CHECKSUMLONG);
      classChecksums.put(ValidationGeneratorTests.ENUM, ValidationGeneratorTests.CHECKSUMENUM);
      final Map<String, String> testChecksums = new ConcurrentHashMap<>();
      testChecksums.put(ValidationGeneratorTests.STRING, ValidationGeneratorTests.CHECKSUM1);
      testChecksums.put(ValidationGeneratorTests.INT, ValidationGeneratorTests.CHECKSUMLONG);
      testChecksums.put(ValidationGeneratorTests.LONG, ValidationGeneratorTests.CHECKSUMLONG);
      testChecksums.put(ValidationGeneratorTests.ENUM, ValidationGeneratorTests.CHECKSUM1);
      */

      /* final boolean success = */ new File(ValidationGeneratorTests.TARGET_TEST2).mkdir();
      final ValidationGenerator generator = new ValidationGenerator(ValidationGeneratorTests.TARGET_TEST2, ValidationGeneratorTests.TEST2, ValidationGeneratorTests.STRING);
      /* final boolean success = */ new File("target/test2/de/powerstat/validation").mkdirs(); //$NON-NLS-1$
      Files.createFile(Paths.get("target/test2/de/powerstat/validation/values")); //$NON-NLS-1$
      generator.getClasses();
     }, ValidationGeneratorTests.FILE_SYSTEM_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test getClasses with incomplete path.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException no such algorithm exception
   */
  @Test
  /* default */ void testGetclassesPathFalse2() throws IOException, NoSuchAlgorithmException
   {
    assertThrows(FileSystemException.class, () ->
     {
      /*
      final Map<String, String> classChecksums = new ConcurrentHashMap<>();
      classChecksums.put(ValidationGeneratorTests.STRING, ValidationGeneratorTests.CHECKSUMSTRING);
      classChecksums.put(ValidationGeneratorTests.INT, ValidationGeneratorTests.CHECKSUMINT);
      classChecksums.put(ValidationGeneratorTests.LONG, ValidationGeneratorTests.CHECKSUMLONG);
      classChecksums.put(ValidationGeneratorTests.ENUM, ValidationGeneratorTests.CHECKSUMENUM);
      final Map<String, String> testChecksums = new ConcurrentHashMap<>();
      testChecksums.put(ValidationGeneratorTests.STRING, ValidationGeneratorTests.CHECKSUM1);
      testChecksums.put(ValidationGeneratorTests.INT, ValidationGeneratorTests.CHECKSUMLONG);
      testChecksums.put(ValidationGeneratorTests.LONG, ValidationGeneratorTests.CHECKSUMLONG);
      testChecksums.put(ValidationGeneratorTests.ENUM, ValidationGeneratorTests.CHECKSUM1);
      */

      /* final boolean success = */ new File(ValidationGeneratorTests.TARGET_TEST3).mkdir();
      final ValidationGenerator generator = new ValidationGenerator(ValidationGeneratorTests.TARGET_TEST3, ValidationGeneratorTests.TEST2, ValidationGeneratorTests.STRING);
      /* final boolean success = */ new File("target/test3/de/powerstat/validation/values").mkdirs(); //$NON-NLS-1$
      Files.createFile(Paths.get("target/test3/de/powerstat/validation/values/test")); //$NON-NLS-1$
      generator.getClasses();
     }, ValidationGeneratorTests.FILE_SYSTEM_EXCEPTION_EXPECTED
    );
   }

 }
