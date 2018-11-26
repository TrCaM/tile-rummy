package project.rummy.entities;

import java.io.Serializable;

/**
 * Meld source identifies if a meld is taken from HAND or from the TABLE
 */
public enum MeldSource implements Serializable {
  HAND, TABLE, MANIPULATION
}
