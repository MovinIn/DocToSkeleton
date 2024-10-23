import java.util.ArrayList;
import java.util.List;

public final class Block {
  public static final int LINE_LENGTH=80;

  private Block(){}

  public static List<String> wrap(String text) {
    List<String> lines=new ArrayList<>();
    do{
      int end=Math.min(text.length(),LINE_LENGTH);
      if(end!=text.length()&&text.indexOf(" ",end)!=-1){
        end=text.indexOf(" ",end)+1;
      }
      String line=text.substring(0,end);
      lines.add(line);
      text=text.substring(end);
    }
    while(!text.isEmpty());
    return lines;
  }
}
