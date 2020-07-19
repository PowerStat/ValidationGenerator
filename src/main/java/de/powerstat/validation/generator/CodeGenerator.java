/*
 * Copyright (C) 2020 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.validation.generator;


import java.io.IOException;
import java.util.Objects;

import de.powerstat.validation.generator.impl.ValidationGenerator;



/**
 * Validation code generator.
 *
 * @author Kai Hofmann
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
   * @param args 0: Classname; 1: template type string/int/long
   * @throws IOException IO exception
   */
  public static void main(final String[] args) throws IOException
   {
    Objects.requireNonNull(args[0], "arg0"); //$NON-NLS-1$
    Objects.requireNonNull(args[1], "arg1"); //$NON-NLS-1$
    new ValidationGenerator("target/generated-sources", args[0], args[1]).getClasses(); //$NON-NLS-1$
   }

 }
