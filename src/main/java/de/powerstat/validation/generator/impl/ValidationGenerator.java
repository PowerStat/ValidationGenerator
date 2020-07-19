/*
 * Copyright (C) 2020 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.validation.generator.impl;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.powerstat.phplib.templateengine.TemplateEngine;
import de.powerstat.phplib.templateengine.TemplateEngine.HandleUndefined;


/**
 * Validation base code generator.
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
   * Template types.
   */
  private static final List<String> TEMPLATE_TYPES = new ArrayList<>();

  /**
   * Output path.
   */
  private final String outputPath;

  /**
   * Class name.
   */
  private final String className;

  /**
   * Template type: string, long.
   */
  private final String templateType;


  /**
   * Static initialization.
   */
  static
   {
    ValidationGenerator.TEMPLATE_TYPES.add("string");
    ValidationGenerator.TEMPLATE_TYPES.add("long");
    ValidationGenerator.TEMPLATE_TYPES.add("int");
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
      throw new IllegalArgumentException("Unknown template type");
     }
    this.outputPath = outputPath;
    this.className = className;
    this.templateType = templateType;
   }


  /**
   * Get classes.
   *
   * @throws IOException IO exception
   */
  public void getClasses() throws IOException
   {
    final TemplateEngine templClass = new TemplateEngine(HandleUndefined.KEEP);
    templClass.setFile(CLASS, new File("src/main/resources/" + this.templateType + "Class.tmpl")); //$NON-NLS-1$ //$NON-NLS-2$
    templClass.setVar("CLASSNAME", this.className); //$NON-NLS-1$
    templClass.setVar("FIELDNAME", this.className.substring(0, 1).toLowerCase(Locale.getDefault()) + this.className.substring(1)); //$NON-NLS-1$
    templClass.parse(CLASS, CLASS);

    final TemplateEngine templTests = new TemplateEngine(HandleUndefined.KEEP);
    templTests.setFile(TESTS, new File("src/main/resources/" + this.templateType + "Tests.tmpl")); //$NON-NLS-1$ //$NON-NLS-2$
    templTests.setVar("CLASSNAME", this.className); //$NON-NLS-1$
    templTests.setVar("FIELDNAME", this.className.substring(0, 1).toLowerCase(Locale.getDefault()) + this.className.substring(1)); //$NON-NLS-1$
    templTests.parse(TESTS, TESTS);

    final File dirClass = new File(this.outputPath + "/de/powerstat/validation/values"); //$NON-NLS-1$
    dirClass.mkdirs();
    try (PrintWriter out = new PrintWriter(dirClass.getAbsolutePath() + File.separator + this.className + ".java")) //$NON-NLS-1$
     {
      out.println(templClass.get(CLASS));
     }

    final File dirTests = new File(this.outputPath + "/de/powerstat/validation/values/tests"); //$NON-NLS-1$
    dirTests.mkdirs();
    try (PrintWriter out = new PrintWriter(dirTests.getAbsolutePath() + File.separator + this.className + "Tests.java")) //$NON-NLS-1$
     {
      out.println(templTests.get(TESTS));
     }

   }

 }
