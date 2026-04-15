package javax.mail.search;

public abstract class IntegerComparisonTerm extends ComparisonTerm {
    private static final long serialVersionUID = -6963571240154302484L;
    protected int number;

    protected IntegerComparisonTerm(int comparison, int number) {
        this.comparison = comparison;
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }

    public int getComparison() {
        return this.comparison;
    }

    /* access modifiers changed from: protected */
    public boolean match(int i) {
        switch (this.comparison) {
            case 1:
                if (i > this.number) {
                    return false;
                }
                return true;
            case 2:
                if (i >= this.number) {
                    return false;
                }
                return true;
            case 3:
                if (i != this.number) {
                    return false;
                }
                return true;
            case 4:
                if (i == this.number) {
                    return false;
                }
                return true;
            case 5:
                if (i <= this.number) {
                    return false;
                }
                return true;
            case 6:
                return i >= this.number;
            default:
                return false;
        }
    }

    public boolean equals(Object obj) {
        if ((obj instanceof IntegerComparisonTerm) && ((IntegerComparisonTerm) obj).number == this.number && super.equals(obj)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.number + super.hashCode();
    }
}
