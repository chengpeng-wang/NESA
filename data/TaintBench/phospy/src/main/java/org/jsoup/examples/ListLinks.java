package org.jsoup.examples;

import java.io.IOException;
import java.util.Iterator;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ListLinks {
    public static void main(String[] args) throws IOException {
        Element link;
        Validate.isTrue(args.length == 1, "usage: supply url to fetch");
        print("Fetching %s...", args[0]);
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");
        print("\nMedia: (%d)", Integer.valueOf(media.size()));
        Iterator i$ = media.iterator();
        while (i$.hasNext()) {
            if (((Element) i$.next()).tagName().equals("img")) {
                print(" * %s: <%s> %sx%s (%s)", ((Element) i$.next()).tagName(), ((Element) i$.next()).attr("abs:src"), ((Element) i$.next()).attr("width"), ((Element) i$.next()).attr("height"), trim(((Element) i$.next()).attr("alt"), 20));
            } else {
                print(" * %s: <%s>", ((Element) i$.next()).tagName(), ((Element) i$.next()).attr("abs:src"));
            }
        }
        print("\nImports: (%d)", Integer.valueOf(imports.size()));
        i$ = imports.iterator();
        while (i$.hasNext()) {
            link = (Element) i$.next();
            print(" * %s <%s> (%s)", link.tagName(), link.attr("abs:href"), link.attr("rel"));
        }
        print("\nLinks: (%d)", Integer.valueOf(links.size()));
        i$ = links.iterator();
        while (i$.hasNext()) {
            link = (Element) i$.next();
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width) {
            return s.substring(0, width - 1) + ".";
        }
        return s;
    }
}
