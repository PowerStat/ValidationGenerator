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
      throw new IllegalArgumentException("Unknown template type");
     }
    this.outputPath = outputPath; // TODO verify
    this.className = className; // TODO verify
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
    templClass.setFile(CLASS, new File("src/main/resources", this.templateType + "Class.tmpl")); //$NON-NLS-1$ //$NON-NLS-2$
    templClass.setVar("CLASSNAME", this.className); //$NON-NLS-1$
    final String fieldname = this.className.substring(0, 1).toLowerCase(Locale.getDefault()) + this.className.substring(1);
    templClass.setVar("FIELDNAME", fieldname); //$NON-NLS-1$
    templClass.parse(CLASS, CLASS);

    final TemplateEngine templTests = new TemplateEngine(HandleUndefined.KEEP);
    templTests.setFile(TESTS, new File("src/main/resources", this.templateType + "Tests.tmpl")); //$NON-NLS-1$ //$NON-NLS-2$
    templTests.setVar("CLASSNAME", this.className); //$NON-NLS-1$
    templTests.setVar("FIELDNAME", fieldname); //$NON-NLS-1$
    templTests.parse(TESTS, TESTS);

    final File dirClass = new File(this.outputPath, "de/powerstat/validation/values"); //$NON-NLS-1$
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
      LOGGER.error("Could not create directory: " + this.outputPath + "/de/powerstat/validation/values"); //$NON-NLS-1$ //$NON-NLS-2$
     }

    final File dirTests = new File(this.outputPath, "de/powerstat/validation/values/test"); //$NON-NLS-1$
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
      LOGGER.error("Could not create directory: " + this.outputPath + "/de/powerstat/validation/values/test"); //$NON-NLS-1$ //$NON-NLS-2$
     }

   }

 }
