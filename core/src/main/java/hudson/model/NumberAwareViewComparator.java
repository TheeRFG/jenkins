package hudson.model;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberAwareViewComparator implements Comparator<View> {
    public static final NumberAwareViewComparator INSTANCE =
            new NumberAwareViewComparator();

    private final Pattern PATTERN = Pattern.compile("(\\D*)(\\d*)");

    public int compare(View lhs, View rhs) {
        Matcher lhm = PATTERN.matcher(lhs.getViewName());
        Matcher rhm = PATTERN.matcher(rhs.getViewName());

        while (lhm.find() && rhm.find()) {
            int nonDigitCompare = lhm.group(1).compareTo(rhm.group(1));
            if (0 != nonDigitCompare) {
                return nonDigitCompare;
            }

            if (lhm.group(2).isEmpty()) {
                return rhm.group(2).isEmpty() ? 0 : -1;
            } else if (rhm.group(2).isEmpty()) {
                return +1;
            }

            BigInteger n1 = new BigInteger(lhm.group(2));
            BigInteger n2 = new BigInteger(rhm.group(2));
            int numberCompare = n1.compareTo(n2);
            if (0 != numberCompare) {
                return numberCompare;
            }
        }

        return lhm.hitEnd() && rhm.hitEnd() ? 0 :
                lhm.hitEnd() ? -1 : +1;
    }
}
