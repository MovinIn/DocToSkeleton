import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;

public class JavadocScraper {
  public static void main(String[]args) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader("package.in"));
    String pkg=reader.readLine();

    Document d=Jsoup.connect(pkg+"package-summary.html").get();
    for(Element e:d.select(".col-first a")){
      String link=pkg+e.attr("href");
      System.out.println("parsing "+link);
      Skeleton.createFileFromDocument(Jsoup.connect(link).get());
    }
  }
}
