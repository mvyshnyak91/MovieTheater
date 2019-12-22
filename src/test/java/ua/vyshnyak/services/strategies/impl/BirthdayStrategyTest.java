package ua.vyshnyak.services.strategies.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.vyshnyak.entities.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ua.vyshnyak.services.impl.TestUtils.createUser;
import static ua.vyshnyak.services.impl.TestUtils.dateTime;
import static ua.vyshnyak.services.strategies.impl.BirthdayStrategy.DAYS;
import static ua.vyshnyak.services.strategies.impl.BirthdayStrategy.DISCOUNT;

class BirthdayStrategyTest {

    private BirthdayStrategy birthdayStrategy;

    @BeforeEach
    void setUp() {
        birthdayStrategy = new BirthdayStrategy();
    }

    @Test
    void getDiscountPercentUserHasNoDateOfBirthday() {
        User user = createUser();
        user.setDateOfBirth(null);
        BigDecimal discountPercent = birthdayStrategy.getDiscountPercent(createUser(), dateTime, 1);
        assertThat(discountPercent, is(BigDecimal.ZERO));
    }

    @ParameterizedTest
    @MethodSource("getDiscountPercentArgs")
    void getDiscountPercent(User user, LocalDateTime airDate, BigDecimal expectedDiscountPercent) {
        BigDecimal discountPercent = birthdayStrategy.getDiscountPercent(user, airDate, 1);
        assertThat(discountPercent, is(expectedDiscountPercent));
    }

    private static Stream<Arguments> getDiscountPercentArgs() {
        return Stream.of(
                Arguments.of(createUser(LocalDate.now().plusDays(1)), LocalDateTime.now(), BigDecimal.valueOf(DISCOUNT)),
                Arguments.of(createUser(LocalDate.now().minusDays(1)), LocalDateTime.now(), BigDecimal.valueOf(DISCOUNT)),
                Arguments.of(createUser(LocalDate.now()), LocalDateTime.now().minusDays(DAYS + 1), BigDecimal.ZERO),
                Arguments.of(createUser(LocalDate.now()), LocalDateTime.now().plusDays(DAYS + 1), BigDecimal.ZERO)
        );
    }

}