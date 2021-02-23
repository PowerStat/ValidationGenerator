/*
 * Copyright (C) 2020-2021 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.validation.generator.impl;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.powerstat.phplib.templateengine.TemplateEngine;
import de.powerstat.phplib.templateengine.TemplateEngine.HandleUndefined;


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
   * Template types.
   */
  private static final Set<String> TEMPLATE_TYPES = new HashSet<>();

  /**
   * Output path.
   */
  private final String outputPath;

  /**
   * Class name.
   */
  private final String className;

  /**
   * Field name.
   */
  private final String fieldName;

  /**
   * Template type: string, long.
   */
  private final String templateType;


  /**
   * Static initialization.
   */
  static
   {
    ValidationGenerator.TEMPLATE_TYPES.add("string"); //$NON-NLS-1$
    ValidationGenerator.TEMPLATE_TYPES.add("long"); //$NON-NLS-1$
    ValidationGenerator.TEMPLATE_TYPES.add("int"); //$NON-NLS-1$
   }


  /**
   * Constructor.
   *
   * @param outputPath Output path for generated code
   * @param className Class name
   * @param templateType Template type: string, long
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
    if (!className.matches("^[A-Z][a-zA-Z0-9_]*$")) //$NON-NLS-1$
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
   */
  public void getClasses() throws IOException
   {
    final TemplateEngine templClass = new TemplateEngine(HandleUndefined.KEEP);
    templClass.setFile(CLASS, new File(RESOURCES_PATH, this.templateType + "Class.tmpl")); //$NON-NLS-1$
    templClass.setVar("CLASSNAME", this.className); //$NON-NLS-1$
    templClass.setVar("FIELDNAME", this.fieldName); //$NON-NLS-1$
    templClass.parse(CLASS, CLASS);

    final TemplateEngine templTests = new TemplateEngine(HandleUndefined.KEEP);
    templTests.setFile(TESTS, new File(RESOURCES_PATH, this.templateType + "Tests.tmpl")); //$NON-NLS-1$
    templTests.setVar("CLASSNAME", this.className); //$NON-NLS-1$
    templTests.setVar("FIELDNAME", this.fieldName); //$NON-NLS-1$
    templTests.parse(TESTS, TESTS);

    final File dirClass = new File(this.outputPath, PACKAGE_PATH);
    boolean result = dirClass.exists() || dirClass.mkdirs();
    if (result)
     {
      try (PrintWriter out = new PrintWriter(dirClass.getAbsolutePath() + File.separator + this.className + ".java", StandardCharsets.UTF_8.name())) //$NON-NLS-1$
       {
        out.print(templClass.get(CLASS));
        out.flush();
       }
     }
    else
     {
      LOGGER.error("Could not create directory: {}/{}", this.outputPath, PACKAGE_PATH); //$NON-NLS-1$
     }

    final File dirTests = new File(this.outputPath, PACKAGE_PATH + "/test"); //$NON-NLS-1$
    result = dirTests.exists() || dirTests.mkdirs();
    if (result)
     {
      try (PrintWriter out = new PrintWriter(dirTests.getAbsolutePath() + File.separator + this.className + "Tests.java", StandardCharsets.UTF_8.name())) //$NON-NLS-1$
       {
        out.print(templTests.get(TESTS));
        out.flush();
       }
     }
    else
     {
      LOGGER.error("Could not create directory: {}/{}/test", this.outputPath, PACKAGE_PATH); //$NON-NLS-1$
     }

   }

 }
