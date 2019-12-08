package ua.vyshnyak.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.vyshnyak.entities.User;
import ua.vyshnyak.services.DiscountService;
import ua.vyshnyak.services.strategies.impl.BirthdayStrategy;
import ua.vyshnyak.services.strategies.impl.EveryNthTicketStrategy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.vyshnyak.services.impl.TestUtils.createUser;
import static ua.vyshnyak.services.impl.TestUtils.dateTime;

@ExtendWith(MockitoExtension.class)
class DiscountServiceImplTest {

    private DiscountService discountService;

    @Mock
    private BirthdayStrategy birthdayStrategy;
    @Mock
    private EveryNthTicketStrategy everyNthTicketStrategy;

    @BeforeEach
    void setUp() {
        discountService = new DiscountServiceImpl(Arrays.asList(birthdayStrategy, everyNthTicketStrategy));
    }

    @ParameterizedTest
    @MethodSource("getDiscountPercentArgs")
    void getDiscountPercent(BigDecimal birthdayDiscount, BigDecimal everyNthDiscount, BigDecimal expectedDiscount) {
        User user = createUser();
        when(birthdayStrategy.getDiscountPercent(user, dateTime, 2)).thenReturn(birthdayDiscount);
        when(everyNthTicketStrategy.getDiscountPercent(user, dateTime, 2)).thenReturn(everyNthDiscount);

        BigDecimal discount = discountService.getDiscountPercent(user, dateTime, 2);

        assertThat(discount, is(expectedDiscount));
        verify(birthdayStrategy).getDiscountPercent(user, dateTime, 2);
        verify(everyNthTicketStrategy).getDiscountPercent(user, dateTime, 2);
    }

    static Stream<Arguments> getDiscountPercentArgs() {
        return Stream.of(
                Arguments.of(BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ONE),
                Arguments.of(BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ONE)
        );
    }

}