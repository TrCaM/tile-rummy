package project.rummy.game;

import org.junit.Before;
import org.junit.Test;
import project.rummy.control.AutoController;
import project.rummy.control.ManualController;
import project.rummy.entities.Player;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class DefaultGameInitializerTest {

  private GameInitializer initializer;

  @Before
  public void setUp() {
    initializer = new DefaultGameInitializer();
  }

  @Test
  public void initPlayers_shouldSucceed() {
    Player[] players = initializer.initPlayers();

    assertThat(players.length, is(4));
    assertThat(players[0].getController(), instanceOf(ManualController.class));
    assertThat(players[1].getController(), instanceOf(AutoController.class));
    assertThat(players[2].getController(), instanceOf(AutoController.class));
    assertThat(players[3].getController(), instanceOf(AutoController.class));
  }

}