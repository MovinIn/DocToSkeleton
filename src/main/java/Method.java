import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class Method {

  private enum Tag{
    PARAM("@param"),RETURNS("@return"),THROWS("@throws"),OVERRIDES("@Override");
    private String tag;
    Tag(String tag){
      this.tag=tag;
    }
    public static Tag toTag(String text){
      if(text.equals("Parameters:")){
        return PARAM;
      }
      if(text.equals("Returns:")){
        return RETURNS;
      }
      if(text.equals("Throws:")) {
        return THROWS;
      }
      if(text.equals("Overrides:")){
        return OVERRIDES;
      }
      return null;
    }
  }

  private String signature;
  private List<String> block;

  public Method(Element section){
    signature=section.select(".member-signature").text();
    block=Block.wrap(section.select(".block").text());
    Elements notes;
    if(section.select(".notes").isEmpty()){
      notes=new Elements();
    }
    else{
      notes=section.select(".notes").get(0).children();
    }
    Tag t=null;
    for(Element e:notes){
      if(e.is("dt")){
        t=Tag.toTag(e.text());
        if(t==Tag.OVERRIDES){
          signature=t.tag+"\n"+signature;
        }
      }
      else if(e.is("dd")&&t!=Tag.OVERRIDES){
        String text=e.text();
        if(t==Tag.PARAM){
          text=text.replaceFirst(" -","");
        }
        block.addAll(Block.wrap(t.tag+" "+text));
      }
    }
  }
  public String build(){
    String build;
    String javadoc="";
    for(String line:block){
      javadoc+=line+"\n";
    }
    build="/**\n"+javadoc+"*/\n";
    return build+signature+" {\n\n}";
  }
}
