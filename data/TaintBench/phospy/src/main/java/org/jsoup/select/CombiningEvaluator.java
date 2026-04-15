package org.jsoup.select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;

abstract class CombiningEvaluator extends Evaluator {
    final List<Evaluator> evaluators;

    static final class And extends CombiningEvaluator {
        And(Collection<Evaluator> evaluators) {
            super(evaluators);
        }

        And(Evaluator... evaluators) {
            this(Arrays.asList(evaluators));
        }

        public boolean matches(Element root, Element node) {
            for (int i = 0; i < this.evaluators.size(); i++) {
                if (!((Evaluator) this.evaluators.get(i)).matches(root, node)) {
                    return false;
                }
            }
            return true;
        }

        public String toString() {
            return StringUtil.join(this.evaluators, " ");
        }
    }

    static final class Or extends CombiningEvaluator {
        Or(Collection<Evaluator> evaluators) {
            if (evaluators.size() > 1) {
                this.evaluators.add(new And((Collection) evaluators));
            } else {
                this.evaluators.addAll(evaluators);
            }
        }

        Or() {
        }

        public void add(Evaluator e) {
            this.evaluators.add(e);
        }

        public boolean matches(Element root, Element node) {
            for (int i = 0; i < this.evaluators.size(); i++) {
                if (((Evaluator) this.evaluators.get(i)).matches(root, node)) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            return String.format(":or%s", new Object[]{this.evaluators});
        }
    }

    CombiningEvaluator() {
        this.evaluators = new ArrayList();
    }

    CombiningEvaluator(Collection<Evaluator> evaluators) {
        this();
        this.evaluators.addAll(evaluators);
    }

    /* access modifiers changed from: 0000 */
    public Evaluator rightMostEvaluator() {
        return this.evaluators.size() > 0 ? (Evaluator) this.evaluators.get(this.evaluators.size() - 1) : null;
    }

    /* access modifiers changed from: 0000 */
    public void replaceRightMostEvaluator(Evaluator replacement) {
        this.evaluators.set(this.evaluators.size() - 1, replacement);
    }
}
