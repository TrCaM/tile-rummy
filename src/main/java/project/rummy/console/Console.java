package project.rummy.console;

import org.apache.log4j.Logger;

public class Console {
  Logger logger = Logger.getLogger(Console.class);

  public void log(String s) {
    logger.info(s);
  }
}
