/*
 * Copyright (C) 2020-2021 Dipl.-Inform. Kai Hofmann. All rights reserved!
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.powerstat.validation.generator.CodeGenerator;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Code generator tests.
 */
@SuppressFBWarnings({"WEAK_MESSAGE_DIGEST_MD5"})
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
   * @throws InterruptedException Interrupted Exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"string", "int", "long"})
  public void testMain(final String templateType) throws IOException, NoSuchAlgorithmException, InterruptedException
   {
    final Map<String, String> classChecksums = new ConcurrentHashMap<>();
    classChecksums.put("string", "d5077f0662f6b77375b6de1b44790982"); //$NON-NLS-1$ //$NON-NLS-2$
    classChecksums.put("int", "573d5e3864eb49f4f7ac2c4786d917a8"); //$NON-NLS-1$ //$NON-NLS-2$
    classChecksums.put("long", "6904821e6bf360b68c82fce70492472b"); //$NON-NLS-1$ //$NON-NLS-2$
    final Map<String, String> testChecksums = new ConcurrentHashMap<>();
    testChecksums.put("string", "d41d8cd98f00b204e9800998ecf8427e"); //$NON-NLS-1$ //$NON-NLS-2$
    testChecksums.put("int", "d41d8cd98f00b204e9800998ecf8427e"); //$NON-NLS-1$ //$NON-NLS-2$
    testChecksums.put("long", "d41d8cd98f00b204e9800998ecf8427e"); //$NON-NLS-1$ //$NON-NLS-2$

    final String[] args = {"Test3" , templateType}; //$NON-NLS-1$
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
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(classChecksums.get(templateType), String.format("%0" + (msgdigi.getDigestLength() * 2) + "x", new BigInteger(1, msgdigi.digest())), "class hashCodes are not equal " + templateType), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      () -> assertEquals(testChecksums.get(templateType), String.format("%0" + (msgdigi.getDigestLength() * 2) + "x", new BigInteger(1, msgdigi.digest())), "test hashCodes are not equal " + templateType) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    );
   }

 }
