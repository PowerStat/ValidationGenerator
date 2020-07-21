/*
 * Copyright (C) 2020 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.validation.generator.impl.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
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
    classChecksums.put("string", "60fdea03408aaafd0b4dc6a4817569eb"); //$NON-NLS-1$ //$NON-NLS-2$
    classChecksums.put("int", "63a78c91b56c7cfce37cc523cfed674f"); //$NON-NLS-1$ //$NON-NLS-2$
    classChecksums.put("long", "a7fc481e15d180a32cf46d0aa6e4312c"); //$NON-NLS-1$ //$NON-NLS-2$
    final Map<String, String> testChecksums = new HashMap<>();
    testChecksums.put("string", "53d3d9f9ba53ada70155b2cf9033b20c"); //$NON-NLS-1$ //$NON-NLS-2$
    testChecksums.put("int", "2d880483d3bd96818ee80d6ebf5e4c7c"); //$NON-NLS-1$ //$NON-NLS-2$
    testChecksums.put("long", "290403e5fbc0f4efe6ffcce75fc71268"); //$NON-NLS-1$ //$NON-NLS-2$

    final ValidationGenerator generator = new ValidationGenerator("target/generated-sources", "Test2", templateType); //$NON-NLS-1$ //$NON-NLS-2$
    generator.getClasses();

    final MessageDigest mdClass = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
    try (BufferedInputStream in = new BufferedInputStream((new FileInputStream("target/generated-sources/de/powerstat/validation/values/Test2.java"))); DigestOutputStream out = new DigestOutputStream(OutputStream.nullOutputStream(), mdClass)) //$NON-NLS-1$
     {
      in.transferTo(out);
     }
    final MessageDigest mdTest = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
    try (BufferedInputStream in = new BufferedInputStream((new FileInputStream("target/generated-sources/de/powerstat/validation/values/test/Test2Tests.java"))); DigestOutputStream out = new DigestOutputStream(OutputStream.nullOutputStream(), mdTest)) //$NON-NLS-1$
     {
      in.transferTo(out);
     }
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(classChecksums.get(templateType), String.format("%0" + (mdClass.getDigestLength() * 2) + "x", new BigInteger(1, mdClass.digest())), "class hashCodes are not equal"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      () -> assertEquals(testChecksums.get(templateType), String.format("%0" + (mdTest.getDigestLength() * 2) + "x", new BigInteger(1, mdTest.digest())), "test hashCodes are not equal") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    );
   }

 }
