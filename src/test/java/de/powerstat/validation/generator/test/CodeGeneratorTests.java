/*
 * Copyright (C) 2020 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.validation.generator.test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    classChecksums.put("string", "bb74713bb73112dc99b1eefd4a022cc3"); //$NON-NLS-1$ //$NON-NLS-2$
    classChecksums.put("int", "946ba52207e7653c5cdc13b3d4317f38"); //$NON-NLS-1$ //$NON-NLS-2$
    classChecksums.put("long", "19a5bebaf309b86ade73ef66e94b3551"); //$NON-NLS-1$ //$NON-NLS-2$
    final Map<String, String> testChecksums = new HashMap<>();
    testChecksums.put("string", "ae7cef1da4232f7545887fa40024b7db"); //$NON-NLS-1$ //$NON-NLS-2$
    testChecksums.put("int", "18d194637e03397d5361ad9aba597df6"); //$NON-NLS-1$ //$NON-NLS-2$
    testChecksums.put("long", "930b18b06cfc18206d0431780bacd4d5"); //$NON-NLS-1$ //$NON-NLS-2$

    final String[] args = {"Test3" , templateType}; //$NON-NLS-1$
    CodeGenerator.main(args);

    final MessageDigest mdClass = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
    try (BufferedInputStream in = new BufferedInputStream((new FileInputStream("target/generated-sources/de/powerstat/validation/values/Test3.java"))); DigestOutputStream out = new DigestOutputStream(OutputStream.nullOutputStream(), mdClass)) //$NON-NLS-1$
     {
      in.transferTo(out);
     }
    final MessageDigest mdTest = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
    try (BufferedInputStream in = new BufferedInputStream((new FileInputStream("target/generated-sources/de/powerstat/validation/values/test/Test3Tests.java"))); DigestOutputStream out = new DigestOutputStream(OutputStream.nullOutputStream(), mdTest)) //$NON-NLS-1$
     {
      in.transferTo(out);
     }
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(classChecksums.get(templateType), String.format("%0" + (mdClass.getDigestLength() * 2) + "x", new BigInteger(1, mdClass.digest())), "class hashCodes are not equal"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      () -> assertEquals(testChecksums.get(templateType), String.format("%0" + (mdTest.getDigestLength() * 2) + "x", new BigInteger(1, mdTest.digest())), "test hashCodes are not equal") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    );
   }

 }
