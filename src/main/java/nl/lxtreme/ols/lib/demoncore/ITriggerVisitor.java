/*
 * OpenBench LogicSniffer / SUMP project 
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 *
 * Copyright (C) 2006-2010 Michael Poppitz, www.sump.org
 * Copyright (C) 2010 J.W. Janssen, www.lxtreme.nl
 */
package nl.lxtreme.ols.lib.demoncore;


import java.io.*;


/**
 * Allows writing to a stream.
 */
public interface ITriggerVisitor
{
  // METHODS

  /**
   * Selects a LUT chain by a given address.
   * 
   * @param aLutChainAddress
   *          the address of the LUT chain to select.
   * @throws IOException
   *           in case of I/O problems.
   */
  void selectChain( int aLutChainAddress ) throws IOException;

  /**
   * Writes to a LUT chain.
   * 
   * @param aLutChainData
   *          the LUT chain data to write.
   * @throws IOException
   *           in case of I/O problems.
   */
  void writeChain( int aLutChainData ) throws IOException;
}
