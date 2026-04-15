package org.apache.james.mime4j.field.address.parser;

import java.util.Stack;

class JJTAddressListParserState {
    private Stack<Integer> marks = new Stack();
    private int mk = 0;
    private boolean node_created;
    private Stack<Node> nodes = new Stack();
    private int sp = 0;

    JJTAddressListParserState() {
    }

    /* access modifiers changed from: 0000 */
    public boolean nodeCreated() {
        return this.node_created;
    }

    /* access modifiers changed from: 0000 */
    public void reset() {
        this.nodes.removeAllElements();
        this.marks.removeAllElements();
        this.sp = 0;
        this.mk = 0;
    }

    /* access modifiers changed from: 0000 */
    public Node rootNode() {
        return (Node) this.nodes.elementAt(0);
    }

    /* access modifiers changed from: 0000 */
    public void pushNode(Node n) {
        this.nodes.push(n);
        this.sp++;
    }

    /* access modifiers changed from: 0000 */
    public Node popNode() {
        int i = this.sp - 1;
        this.sp = i;
        if (i < this.mk) {
            this.mk = ((Integer) this.marks.pop()).intValue();
        }
        return (Node) this.nodes.pop();
    }

    /* access modifiers changed from: 0000 */
    public Node peekNode() {
        return (Node) this.nodes.peek();
    }

    /* access modifiers changed from: 0000 */
    public int nodeArity() {
        return this.sp - this.mk;
    }

    /* access modifiers changed from: 0000 */
    public void clearNodeScope(Node n) {
        while (this.sp > this.mk) {
            popNode();
        }
        this.mk = ((Integer) this.marks.pop()).intValue();
    }

    /* access modifiers changed from: 0000 */
    public void openNodeScope(Node n) {
        this.marks.push(new Integer(this.mk));
        this.mk = this.sp;
        n.jjtOpen();
    }

    /* access modifiers changed from: 0000 */
    public void closeNodeScope(Node n, int num) {
        this.mk = ((Integer) this.marks.pop()).intValue();
        while (true) {
            int num2 = num;
            num = num2 - 1;
            if (num2 > 0) {
                Node c = popNode();
                c.jjtSetParent(n);
                n.jjtAddChild(c, num);
            } else {
                n.jjtClose();
                pushNode(n);
                this.node_created = true;
                return;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void closeNodeScope(Node n, boolean condition) {
        if (condition) {
            int a = nodeArity();
            this.mk = ((Integer) this.marks.pop()).intValue();
            while (true) {
                int a2 = a;
                a = a2 - 1;
                if (a2 > 0) {
                    Node c = popNode();
                    c.jjtSetParent(n);
                    n.jjtAddChild(c, a);
                } else {
                    n.jjtClose();
                    pushNode(n);
                    this.node_created = true;
                    return;
                }
            }
        }
        this.mk = ((Integer) this.marks.pop()).intValue();
        this.node_created = false;
    }
}
