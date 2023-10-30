/*
 * Copyright (C) 2020-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.validation.generator.impl;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.powerstat.phplib.templateengine.HandleUndefined;
import de.powerstat.phplib.templateengine.TemplateEngine;


/**
 * Validation base code generator.
 *
 * TODO read templates from inside and outside jar
 */
public final class ValidationGenerator
 {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(ValidationGenerator.class);

  /**
   * Template name constant for implementation class.
   */
  private static final String CLASS = "CLASS"; //$NON-NLS-1$

  /**
   * Template name constant for test class.
   */
  private static final String TESTS = "TESTS"; //$NON-NLS-1$

  /**
   * Package path.
   */
  private static final String PACKAGE_PATH = "de/powerstat/validation/values"; //$NON-NLS-1$

  /**
   * Resources path.
   */
  private static final String RESOURCES_PATH = "src/main/resources"; //$NON-NLS-1$

  /**
   * Test path constant.
   */
  @SuppressWarnings("java:S1075")
  private static final String TEST_PATH = "/test"; //$NON-NLS-1$

  /**
   * Template types.
   */
  private static final Set<String> TEMPLATE_TYPES = new HashSet<>();

  /**
   * Classname regexp.
   */
  private static final Pattern CLASSNAME_REGEXP = Pattern.compile("^[A-Z]\\w*$"); //$NON-NLS-1$

  /**
   * Classname constant.
   */
  private static final String CLASSNAME = "CLASSNAME"; //$NON-NLS-1$

  /**
   * Fieldname constant.
   */
  private static final String FIELDNAME2 = "FIELDNAME"; //$NON-NLS-1$

  /**
   * Could not create directory constant.
   */
  private static final String COULD_NOT_CREATE_DIRECTORY = "Could not create directory: "; //$NON-NLS-1$

  /**
   * Output path.
   */
  private final String outputPath;

  /**
   * Class name.
   */
  @SuppressWarnings("java:S1845")
  private final String className;

  /**
   * Field name.
   */
  private final String fieldName;

  /**
   * Template type: string, long.
   */
  private final String templateType;


  /* *
   * Static initialization.
   */
  static
   {
    ValidationGenerator.TEMPLATE_TYPES.add("string"); //$NON-NLS-1$
    ValidationGenerator.TEMPLATE_TYPES.add("long"); //$NON-NLS-1$
    ValidationGenerator.TEMPLATE_TYPES.add("int"); //$NON-NLS-1$
    ValidationGenerator.TEMPLATE_TYPES.add("enum"); //$NON-NLS-1$
   }


  /**
   * Constructor.
   *
   * @param outputPath Output path for generated code
   * @param className Class name
   * @param templateType Template type: int, long, string, enum
   * @throws IllegalArgumentException Illegal argument
   */
  public ValidationGenerator(final String outputPath, final String className, final String templateType)
   {
    Objects.requireNonNull(outputPath, "outputPath"); //$NON-NLS-1$
    Objects.requireNonNull(className, "className"); //$NON-NLS-1$
    Objects.requireNonNull(templateType, "templateType"); //$NON-NLS-1$
    if (!ValidationGenerator.TEMPLATE_TYPES.contains(templateType))
     {
      throw new IllegalArgumentException("Unknown templateType"); //$NON-NLS-1$
     }
    this.templateType = templateType;
    if (!new File(outputPath).isDirectory())
     {
      throw new IllegalArgumentException("outputPath is not a directory"); //$NON-NLS-1$
     }
    this.outputPath = outputPath;
    if (!ValidationGenerator.CLASSNAME_REGEXP.matcher(className).matches())
     {
      throw new IllegalArgumentException("className does not match [A-Z][a-zA-Z0-9_]*"); //$NON-NLS-1$
     }
    this.className = className;
    this.fieldName = className.substring(0, 1).toLowerCase(Locale.getDefault()) + className.substring(1);
   }


  /**
   * Get classes.
   *
   * @throws IOException IO exception
   * @throws FileSystemException When a directory could not be created
   */
  @SuppressWarnings("PMD.LinguisticNaming")
  public void getClasses() throws IOException
   {
    final var templClass = new TemplateEngine(HandleUndefined.KEEP);
    templClass.setFile(ValidationGenerator.CLASS, new File(ValidationGenerator.RESOURCES_PATH, this.templateType + "Class.tmpl")); //$NON-NLS-1$
    templClass.setVar(ValidationGenerator.CLASSNAME, this.className);
    templClass.setVar(ValidationGenerator.FIELDNAME2, this.fieldName);
    templClass.parse(ValidationGenerator.CLASS, ValidationGenerator.CLASS);

    final var templTests = new TemplateEngine(HandleUndefined.KEEP);
    templTests.setFile(ValidationGenerator.TESTS, new File(ValidationGenerator.RESOURCES_PATH, this.templateType + "Tests.tmpl")); //$NON-NLS-1$
    templTests.setVar(ValidationGenerator.CLASSNAME, this.className);
    templTests.setVar(ValidationGenerator.FIELDNAME2, this.fieldName);
    templTests.parse(ValidationGenerator.TESTS, ValidationGenerator.TESTS);

    final var dirClass = new File(this.outputPath, ValidationGenerator.PACKAGE_PATH);
    if (!dirClass.isDirectory() && !dirClass.mkdirs())
     {
      ValidationGenerator.LOGGER.error("Could not create directory: {}/{}", this.outputPath, ValidationGenerator.PACKAGE_PATH); //$NON-NLS-1$
      throw new FileSystemException(ValidationGenerator.COULD_NOT_CREATE_DIRECTORY + dirClass.getCanonicalPath());
     }
    try (var out = new PrintWriter(dirClass.getAbsolutePath() + File.separator + this.className + ".java", StandardCharsets.UTF_8.name())) //$NON-NLS-1$
     {
      out.print(templClass.get(ValidationGenerator.CLASS));
      out.flush();
     }

    final var dirTests = new File(this.outputPath, ValidationGenerator.PACKAGE_PATH + ValidationGenerator.TEST_PATH);
    if (!dirTests.isDirectory() && !dirTests.mkdirs())
     {
      ValidationGenerator.LOGGER.error("Could not create directory: {}/{}/test", this.outputPath, ValidationGenerator.PACKAGE_PATH + ValidationGenerator.TEST_PATH); //$NON-NLS-1$
      throw new FileSystemException(ValidationGenerator.COULD_NOT_CREATE_DIRECTORY + dirTests.getCanonicalPath());
     }
    try (var out = new PrintWriter(dirTests.getAbsolutePath() + File.separator + this.className + "Tests.java", StandardCharsets.UTF_8.name())) //$NON-NLS-1$
     {
      out.print(templTests.get(ValidationGenerator.TESTS));
      out.flush();
     }
   }

 }
