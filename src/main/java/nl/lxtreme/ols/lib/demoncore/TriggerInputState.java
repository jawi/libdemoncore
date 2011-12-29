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


/**
 * Denotes the state of a input term for a trigger.
 */
public enum TriggerInputState
{
  // CONSTANTS

  /** Input is enabled (non-inverted). */
  ENABLED,
  /** Input is enabled (inverted). */
  ENABLED_INVERTED,
  /** Input is disabled. */
  DISABLED;

  // METHODS

  /**
   * @return <code>true</code> if this trigger input is {@link #DISABLED},
   *         <code>false</code> otherwise.
   */
  public boolean isDisabled()
  {
    return !isEnabled();
  }

  /**
   * @return <code>true</code> if this trigger input is either {@link #ENABLED}
   *         or {@link #ENABLED_INVERTED}, <code>false</code> otherwise.
   */
  public boolean isEnabled()
  {
    return ( this == ENABLED ) || ( this == ENABLED_INVERTED );
  }

  /**
   * @return <code>true</code> if this trigger input is
   *         {@link #ENABLED_INVERTED}, <code>false</code> otherwise.
   */
  public boolean isInverted()
  {
    return this == ENABLED_INVERTED;
  }
}
