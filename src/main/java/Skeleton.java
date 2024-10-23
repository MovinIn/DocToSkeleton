import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Skeleton {
  private String classHeader;
  private String signature;
  private String className;
  private List<String> fields;
  private List<Method> methods;

  public static void createFileFromDocument(Document doc){
    String classSignature=doc.select(".type-signature").get(0).text();
    String className=doc.select(".type-signature .element-name").get(0).text();
    String classHeader=doc.select(".block").get(0).text();
    List<Method> methods=new ArrayList<>();
    for(Element section:doc.select("#method-detail .detail")){
      methods.add(new Method(section));
    }
    Skeleton skeleton=new Skeleton();
    skeleton.setClassName(className).setSignature(classSignature).setClassHeader(classHeader)
        .setMethods(methods).setFields(skeleton.scrapeFields(doc))
        .createFile();
  }

  private List<String> scrapeFields(Document doc){
    if(signature.contains("enum")){
      String list="";
      for(Element e:doc.select("#enum-constant-summary").get(0)
          .select(".col-first a")){
        list+=e.attr("href").replace("#","")+",";
      }
      return List.of(list);
    }
    List<String> fields=new ArrayList<>();
    Element fieldWrapper=doc.select("#field-detail").get(0);
    for(Element e:fieldWrapper.select(".member-signature")){
      fields.add(e.text()+";");
    }
    return fields;
  }

  public Skeleton setClassName(String className){
    this.className=className;
    return this;
  }

  public Skeleton setClassHeader(String classHeader){
    this.classHeader=classHeader;
    return this;
  }

  public Skeleton setSignature(String signature) {
    this.signature = signature;
    return this;
  }

  public Skeleton setFields(List<String> fields) {
    this.fields = fields;
    return this;
  }

  public Skeleton setMethods(List<Method> methods) {
    this.methods = methods;
    return this;
  }

  public void createFile() {
    try(PrintWriter writer=new PrintWriter(className+".java")) {
      List<String> header=Block.wrap(classHeader);
      writer.println("/**");
      for(String line:header){
        writer.println(line);
      }
      writer.println("*/");
      writer.println(signature+" {");
      for(String field:fields){
        writer.println(field);
      }
      for(Method m:methods){
        writer.println(m.build());
      }
      writer.println("}");
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
