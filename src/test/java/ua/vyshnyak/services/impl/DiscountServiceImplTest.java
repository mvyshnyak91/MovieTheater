package ua.vyshnyak.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.vyshnyak.entities.User;
import ua.vyshnyak.services.DiscountService;
import ua.vyshnyak.services.strategies.impl.BirthdayStrategy;
import ua.vyshnyak.services.strategies.impl.EveryNthTicketStrategy;

import java.math.BigDecimal;
import java.util.Arrays;

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

    @Test
    void getDiscountPercent() {
        User user = createUser();
        when(birthdayStrategy.getDiscountPercent(user, dateTime, 2)).thenReturn(BigDecimal.ZERO);
        when(everyNthTicketStrategy.getDiscountPercent(user, dateTime, 2)).thenReturn(BigDecimal.ZERO);

        discountService.getDiscountPercent(user, dateTime, 2);

        verify(birthdayStrategy).getDiscountPercent(user, dateTime, 2);
        verify(everyNthTicketStrategy).getDiscountPercent(user, dateTime, 2);
    }

}