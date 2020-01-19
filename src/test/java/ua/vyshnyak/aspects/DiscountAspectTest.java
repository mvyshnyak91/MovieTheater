package ua.vyshnyak.aspects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.vyshnyak.entities.User;
import ua.vyshnyak.services.impl.TestUtils;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DiscountAspectTest {
    private DiscountAspect discountAspect;

    @BeforeEach
    void setUp() {
        discountAspect = new DiscountAspect();
    }

    @ParameterizedTest
    @MethodSource("countGetDiscountPercentGreaterThanZeroArgs")
    void countGetDiscountPercentGreaterThanZero(User user, BigDecimal discountPercent, int callsCount, int expectedCount) {
        Stream.iterate(1, i -> i++)
                .limit(callsCount)
                .forEach(i -> discountAspect.countGetDiscountPercent(user, discountPercent));
        assertThat(discountAspect.getCount(user), is(expectedCount));
    }

    private static Stream<Arguments> countGetDiscountPercentGreaterThanZeroArgs() {
        return Stream.of(
                Arguments.of(TestUtils.createUser(), BigDecimal.ONE, 1, 1),
                Arguments.of(TestUtils.createUser(), BigDecimal.ONE, 2, 2),
                Arguments.of(TestUtils.createUser(), BigDecimal.ONE, 3, 3)
        );
    }

    @Test
    void countGetDiscountPercentZero() {
        User user = TestUtils.createUser();
        discountAspect.countGetDiscountPercent(user, BigDecimal.ZERO);
        assertThat(discountAspect.getCount(user), nullValue());
    }

}