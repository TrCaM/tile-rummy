package project.rummy.game;

import org.junit.Before;
import org.junit.Test;
import project.rummy.control.AutoController;
import project.rummy.control.ManualController;

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
    Game game = new Game(initializer, false);
    initializer.initPlayers(game);

    assertThat(game.getPlayers().length, is(4));
    assertThat(game.getPlayers()[0].getController(), instanceOf(ManualController.class));
    assertThat(game.getPlayers()[1].getController(), instanceOf(AutoController.class));
    assertThat(game.getPlayers()[2].getController(), instanceOf(AutoController.class));
    assertThat(game.getPlayers()[3].getController(), instanceOf(AutoController.class));
  }

}