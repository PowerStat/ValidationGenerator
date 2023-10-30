/*
 * Copyright (C) 2020-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.validation.generator;


import java.io.IOException;
import java.util.Objects;

import de.powerstat.validation.generator.impl.ValidationGenerator;



/**
 * Validation code generator.
 *
 * @author Kai Hofmann
 *
 * TODO outputClassPath outputTestPath parameter
 */
public final class CodeGenerator
 {
  /**
   * Hidden default constructor.
   */
  private CodeGenerator()
   {
    super();
   }


  /**
   * Main.
   *
   * @param args 0: Classname; 1: template type string/int/long/enum
   */
  @SuppressWarnings({"PMD.SystemPrintln", "java:S106"})
  public static void main(final String[] args)
   {
    Objects.requireNonNull(args[0], "arg0"); //$NON-NLS-1$
    Objects.requireNonNull(args[1], "arg1"); //$NON-NLS-1$
    try
     {
      new ValidationGenerator("target/generated-sources", args[0], args[1]).getClasses(); //$NON-NLS-1$
     }
    catch (final IllegalArgumentException | IOException e)
     {
      System.err.println(e.getLocalizedMessage());
     }
   }

 }
