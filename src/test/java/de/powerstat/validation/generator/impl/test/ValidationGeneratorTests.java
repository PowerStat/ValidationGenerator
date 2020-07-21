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
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.powerstat.validation.generator.impl.ValidationGenerator;


/**
 * Validation code generator tests.
 */
public class ValidationGeneratorTests
 {
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
  @ValueSource(strings = {"string", "int", "long"})
  public void constructorTypes(final String templateType)
   {
    final ValidationGenerator generator = new ValidationGenerator("target/generated-sources", "Test1", templateType); //$NON-NLS-1$ //$NON-NLS-2$
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
      /* final ValidationGenerator generator = */ new ValidationGenerator(null, "Test1", "string"); //$NON-NLS-1$ //$NON-NLS-2$
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
      /* final ValidationGenerator generator = */ new ValidationGenerator("target/generated-sources", null, "string"); //$NON-NLS-1$ //$NON-NLS-2$
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
      /* final ValidationGenerator generator = */ new ValidationGenerator("target/generated-sources", "Test1", null); //$NON-NLS-1$ //$NON-NLS-2$
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
      /* final ValidationGenerator generator = */ new ValidationGenerator("target/generated-sources", "Test1", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
  @ValueSource(strings = {"string", "int", "long"})
  public void getclasses(final String templateType) throws IOException, NoSuchAlgorithmException
   {
    final Map<String, String> classChecksums = new HashMap<>();
    classChecksums.put("string", "0dff63ca2e13e04583b7ebda640e67be"); //$NON-NLS-1$ //$NON-NLS-2$
    classChecksums.put("int", "4550f57dae31012937f56b53f65460be"); //$NON-NLS-1$ //$NON-NLS-2$
    classChecksums.put("long", "245f4a97f609a3d145b2b57ecab3fc77"); //$NON-NLS-1$ //$NON-NLS-2$
    final Map<String, String> testChecksums = new HashMap<>();
    testChecksums.put("string", "45cf45be8d9661a5464148631ea4782d"); //$NON-NLS-1$ //$NON-NLS-2$
    testChecksums.put("int", "e886f3c2c329da1c950156b40d92dc3d"); //$NON-NLS-1$ //$NON-NLS-2$
    testChecksums.put("long", "47a227b11a611f75704b068cef759a2a"); //$NON-NLS-1$ //$NON-NLS-2$

    final ValidationGenerator generator = new ValidationGenerator("target/generated-sources", "Test2", templateType); //$NON-NLS-1$ //$NON-NLS-2$
    generator.getClasses();

    final MessageDigest mdClass = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
    try (BufferedInputStream in = new BufferedInputStream((Files.newInputStream(Paths.get("target/generated-sources/de/powerstat/validation/values/Test2.java")))); DigestOutputStream out = new DigestOutputStream(OutputStream.nullOutputStream(), mdClass)) //$NON-NLS-1$
     {
      in.transferTo(out);
     }
    final MessageDigest mdTest = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
    try (BufferedInputStream in = new BufferedInputStream((Files.newInputStream(Paths.get("target/generated-sources/de/powerstat/validation/values/test/Test2Tests.java")))); DigestOutputStream out = new DigestOutputStream(OutputStream.nullOutputStream(), mdTest)) //$NON-NLS-1$
     {
      in.transferTo(out);
     }
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(classChecksums.get(templateType), String.format("%0" + (mdClass.getDigestLength() * 2) + "x", new BigInteger(1, mdClass.digest())), "class hashCodes are not equal"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      () -> assertEquals(testChecksums.get(templateType), String.format("%0" + (mdTest.getDigestLength() * 2) + "x", new BigInteger(1, mdTest.digest())), "test hashCodes are not equal") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    );
   }

 }
