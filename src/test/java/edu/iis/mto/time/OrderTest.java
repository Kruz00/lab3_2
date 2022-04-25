package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;




@ExtendWith(MockitoExtension.class)
class OrderTest {

    @Mock
    private Clock clock;

    private Order order;

    @BeforeEach
    public void setUp() throws Exception {
        lenient().when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        order = new Order(clock);

    }

    @Test
    public void methodShouldThrowAnExceptionWhenOrderIsExpired() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime time = LocalDateTime.now();
        when(clock.instant()).thenReturn(time.atZone(zoneId).toInstant());
        order.submit();


        time = LocalDateTime.now().plusDays(2);
        when(clock.instant()).thenReturn(time.atZone(zoneId).toInstant());


        assertThrows(OrderExpiredException.class, () -> order.confirm());
    }

    @Test
    public void methodTryingToConfirmNotSubmittedOrderShouldThrowException()
    {
        assertThrows(OrderStateException.class,()->order.confirm());
    }

    @Test
    public void methodTryingToRealizeNotSubmittedOrderShouldThrowException()
    {
        assertThrows(OrderStateException.class,()->order.realize());
    }

    @Test
    public void addingItemToConfirmedOrderShouldThrowException(){
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime time = LocalDateTime.now();
        when(clock.instant()).thenReturn(time.atZone(zoneId).toInstant());
        order.submit();
        order.confirm();

        assertThrows(OrderStateException.class,()->order.addItem(new OrderItem()));
    }

    @Test
    public void allStepsShouldEndWithSuccesWhenCalledInOrderAndWithCorrectTime(){
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime time = LocalDateTime.now();
        when(clock.instant()).thenReturn(time.atZone(zoneId).toInstant());

        
        assertDoesNotThrow(() -> order.submit());
        assertDoesNotThrow(() -> order.confirm());
        assertDoesNotThrow(() -> order.realize());
    }

}
