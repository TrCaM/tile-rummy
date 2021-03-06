package project.rummy.commands;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import project.rummy.control.ActionHandler;
import project.rummy.game.Game;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommandProcessorTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule();

  @Mock
  private ActionHandler handler;

  @Mock
  private Game game;

  private CommandProcessor processor;

  @Before
  public void setUp() {
    processor = CommandProcessor.getInstance();
    processor.setUpGame(game);
  }

  @Test(expected = IllegalStateException.class)
  public void noHandler_shouldThrow() {
    processor.setUpHandler(null);
    processor.apply(ActionHandler::drawAndEndTurn);
  }

  @Test(expected = IllegalStateException.class)
  public void handlerExpired_shouldThrow() {
    when(handler.isExpired()).thenReturn(true);
    processor.setUpHandler(handler);
    processor.apply(ActionHandler::drawAndEndTurn);
  }

  @Test()
  public void handlerOkay_shouldSucceed() {
    when(handler.isExpired()).thenReturn(false);
    processor.setUpHandler(handler);
    processor.apply(ActionHandler::drawAndEndTurn);

    verify(handler).drawAndEndTurn();
  }
}