import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author manishmishra
 * @created 2020-04-17
 **/
public class TestScrappping {

    public static void main(String... arr){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter country name : ");
        String country= sc.next();
        Document document = null;
        try {
            document = Jsoup.connect("https://en.wikipedia.org/wiki/"+country).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements allElements = document.getElementsByClass("infobox geography vcard");
        Elements trs = allElements.get(0).children().get(0).children();
        Iterator<Element> elementIterator = trs.iterator();
        boolean gdpFound = false;
        while(elementIterator.hasNext()){
            Element tr = elementIterator.next();
            if(gdpFound){
                Element td = tr.getElementsByTag("td").get(0);
                Elements elems = td.getElementsMatchingOwnText("(billion|trillion|million)");
                String ownText = elems.get(0).text();
                String t = ownText.contains("billion") ? "billion" :
                        ownText.contains("trillion") ? "trillion" :
                                "million";
                int index = ownText.indexOf(t);
                System.out.println(country+" GDP :"+ownText.substring(0,index)+t);
                break;
            }else {
            Elements tds = tr.getElementsByTag("th");
            for(Element td : tds){
                Elements anchors = td.getElementsByTag("a");
                if(!anchors.isEmpty()){
                    for(Element anchor : anchors){
                        Attributes attributes = anchor.attributes();
                        if(!attributes.isEmpty()){
                            for(Attribute attr : attributes){
                                if("Gross domestic product".equalsIgnoreCase(attr.getValue())){
                                    gdpFound = true;
                                    break;
                                }
                            }
                            if(gdpFound){
                                break;
                            }
                        }
                    }
                    if(gdpFound){
                        break;
                    }
                }
            }
            }
        }

        //System.out.println(nodeList);
    }
}
