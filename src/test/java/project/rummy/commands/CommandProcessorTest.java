package project.rummy.commands;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import project.rummy.control.ActionHandler;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommandProcessorTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule();

  @Mock
  private ActionHandler handler;

  private CommandProcessor processor;

  @Before
  public void setUp() {
    processor = new CommandProcessor();
  }

  @Test(expected = IllegalStateException.class)
  public void noHandler_shouldThrow() {
    processor.apply(ActionHandler::draw);
  }

  @Test(expected = IllegalStateException.class)
  public void handlerExpired_shouldThrow() {
    when(handler.isExpired()).thenReturn(true);
    processor.setUpHandler(handler);
    processor.apply(ActionHandler::draw);
  }

  @Test()
  public void handlerOkay_shouldSucceed() {
    when(handler.isExpired()).thenReturn(false);
    processor.setUpHandler(handler);
    processor.apply(ActionHandler::draw);

    verify(handler).draw();
  }
}