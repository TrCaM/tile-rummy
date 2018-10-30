package project.rummy.console;

import org.apache.log4j.Logger;

public class Console {
  Logger logger = Logger.getLogger(Console.class);

  // TODO: Create a GUI component that can display the logs.

  public void log(String s) {
    logger.info(s);
  }
}
