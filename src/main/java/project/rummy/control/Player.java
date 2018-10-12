package project.rummy.control;

import project.rummy.entities.Hand;

public class Player {
  Hand hand;
  boolean hasPlayedMeld;

  public Player(Hand hand) {
    this.hand = hand;
    this.hasPlayedMeld = false;
  }
}
