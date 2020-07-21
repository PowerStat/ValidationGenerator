/*
 * Copyright (C) 2020 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.validation.generator.test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.powerstat.validation.generator.CodeGenerator;

/**
 * Code generator tests.
 */
public class CodeGeneratorTests
 {
  /**
   * Default constructor.
   */
  public CodeGeneratorTests()
   {
    super();
   }


  /**
   * Test main.
   *
   * @param templateType Template type: string, int, long
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException  No such algorithm exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"string", "int", "long"})
  public void testMain(final String templateType) throws IOException, NoSuchAlgorithmException
   {
    final Map<String, String> classChecksums = new HashMap<>();
    classChecksums.put("string", "eb0f3e7d2034ce4fe384b65586387304"); //$NON-NLS-1$ //$NON-NLS-2$
    classChecksums.put("int", "e0989c2cbab51407b24ab6bb6d604e23"); //$NON-NLS-1$ //$NON-NLS-2$
    classChecksums.put("long", "418cd20d988c98b30b84d5a84d946278"); //$NON-NLS-1$ //$NON-NLS-2$
    final Map<String, String> testChecksums = new HashMap<>();
    testChecksums.put("string", "7ede29136809c0bb5ae80ab1d228b993"); //$NON-NLS-1$ //$NON-NLS-2$
    testChecksums.put("int", "76988a104e1090ff9d723881a971e761"); //$NON-NLS-1$ //$NON-NLS-2$
    testChecksums.put("long", "f7daac47f5cd7144a8f8cc41d3add94d"); //$NON-NLS-1$ //$NON-NLS-2$

    final String[] args = {"Test3" , templateType}; //$NON-NLS-1$
    CodeGenerator.main(args);

    final MessageDigest mdClass = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
    try (BufferedInputStream in = new BufferedInputStream((Files.newInputStream(Paths.get("target/generated-sources/de/powerstat/validation/values/Test3.java")))); DigestOutputStream out = new DigestOutputStream(OutputStream.nullOutputStream(), mdClass)) //$NON-NLS-1$
     {
      in.transferTo(out);
     }
    final MessageDigest mdTest = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
    try (BufferedInputStream in = new BufferedInputStream((Files.newInputStream(Paths.get("target/generated-sources/de/powerstat/validation/values/test/Test3Tests.java")))); DigestOutputStream out = new DigestOutputStream(OutputStream.nullOutputStream(), mdTest)) //$NON-NLS-1$
     {
      in.transferTo(out);
     }
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(classChecksums.get(templateType), String.format("%0" + (mdClass.getDigestLength() * 2) + "x", new BigInteger(1, mdClass.digest())), "class hashCodes are not equal"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      () -> assertEquals(testChecksums.get(templateType), String.format("%0" + (mdTest.getDigestLength() * 2) + "x", new BigInteger(1, mdTest.digest())), "test hashCodes are not equal") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    );
   }

 }
