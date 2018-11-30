package project.rummy.rigging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Code adapted from https://stackoverflow.com/questions/3923129/get-a-list-of-resources-from-classpath-directory
 */
public class ResourceReader {

  public List<String> getResourceFiles(String path) throws IOException {
    List<String> filenames = new ArrayList<>();

    try (
        InputStream in = getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
      String resource = br.readLine();

      while (resource != null) {
        filenames.add(resource);
        resource = br.readLine();
      }
    }

    return filenames;
  }

  private InputStream getResourceAsStream(String resource) {
    final InputStream in
        = getContextClassLoader().getResourceAsStream(resource);

    return in == null ? getClass().getResourceAsStream(resource) : in;
  }

  private ClassLoader getContextClassLoader() {
    return Thread.currentThread().getContextClassLoader();
  }
}
