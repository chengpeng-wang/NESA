package org.jsoup.safety;

import java.util.Iterator;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public class Cleaner {
    /* access modifiers changed from: private */
    public Whitelist whitelist;

    private static class ElementMeta {
        Element el;
        int numAttribsDiscarded;

        ElementMeta(Element el, int numAttribsDiscarded) {
            this.el = el;
            this.numAttribsDiscarded = numAttribsDiscarded;
        }
    }

    private final class CleaningVisitor implements NodeVisitor {
        private Element destination;
        /* access modifiers changed from: private */
        public int numDiscarded;
        private final Element root;

        private CleaningVisitor(Element root, Element destination) {
            this.numDiscarded = 0;
            this.root = root;
            this.destination = destination;
        }

        public void head(Node source, int depth) {
            if (source instanceof Element) {
                Element sourceEl = (Element) source;
                if (Cleaner.this.whitelist.isSafeTag(sourceEl.tagName())) {
                    ElementMeta meta = Cleaner.this.createSafeElement(sourceEl);
                    Element destChild = meta.el;
                    this.destination.appendChild(destChild);
                    this.numDiscarded += meta.numAttribsDiscarded;
                    this.destination = destChild;
                } else if (source != this.root) {
                    this.numDiscarded++;
                }
            } else if (source instanceof TextNode) {
                this.destination.appendChild(new TextNode(((TextNode) source).getWholeText(), source.baseUri()));
            } else {
                this.numDiscarded++;
            }
        }

        public void tail(Node source, int depth) {
            if ((source instanceof Element) && Cleaner.this.whitelist.isSafeTag(source.nodeName())) {
                this.destination = this.destination.parent();
            }
        }
    }

    public Cleaner(Whitelist whitelist) {
        Validate.notNull(whitelist);
        this.whitelist = whitelist;
    }

    public Document clean(Document dirtyDocument) {
        Validate.notNull(dirtyDocument);
        Document clean = Document.createShell(dirtyDocument.baseUri());
        if (dirtyDocument.body() != null) {
            copySafeNodes(dirtyDocument.body(), clean.body());
        }
        return clean;
    }

    public boolean isValid(Document dirtyDocument) {
        Validate.notNull(dirtyDocument);
        return copySafeNodes(dirtyDocument.body(), Document.createShell(dirtyDocument.baseUri()).body()) == 0;
    }

    private int copySafeNodes(Element source, Element dest) {
        CleaningVisitor cleaningVisitor = new CleaningVisitor(source, dest);
        new NodeTraversor(cleaningVisitor).traverse(source);
        return cleaningVisitor.numDiscarded;
    }

    /* access modifiers changed from: private */
    public ElementMeta createSafeElement(Element sourceEl) {
        String sourceTag = sourceEl.tagName();
        Attributes destAttrs = new Attributes();
        Element dest = new Element(Tag.valueOf(sourceTag), sourceEl.baseUri(), destAttrs);
        int numDiscarded = 0;
        Iterator i$ = sourceEl.attributes().iterator();
        while (i$.hasNext()) {
            Attribute sourceAttr = (Attribute) i$.next();
            if (this.whitelist.isSafeAttribute(sourceTag, sourceEl, sourceAttr)) {
                destAttrs.put(sourceAttr);
            } else {
                numDiscarded++;
            }
        }
        destAttrs.addAll(this.whitelist.getEnforcedAttributes(sourceTag));
        return new ElementMeta(dest, numDiscarded);
    }
}
