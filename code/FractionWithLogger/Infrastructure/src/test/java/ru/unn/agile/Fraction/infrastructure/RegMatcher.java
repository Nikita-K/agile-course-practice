package ru.unn.agile.Fraction.infrastructure;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class RegMatcher extends BaseMatcher {
    private final String reg;

    public RegMatcher(final String reg) {
        this.reg = reg;
    }

    public boolean matches(final Object o) {
        return ((String) o).matches(reg);
    }

    public void describeTo(final Description description) {
        description.appendText("matches regex = ");
        description.appendText(reg);
    }

    public static Matcher<? super String> matchesPattern(final String reg) {
        RegMatcher matcher = new RegMatcher(reg);
        @SuppressWarnings (value = "unchecked")
        Matcher<? super String> castedMatcher = (Matcher<? super String>)   matcher;
        return castedMatcher;
    }
}
