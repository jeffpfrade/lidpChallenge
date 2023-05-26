import com.lidp.fare.dao.FareRepository;
import com.lidp.fare.domain.Fare;
import com.lidp.fare.domain.FareId;
import com.lidp.fare.service.FareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FareServiceTest {

    @Mock
    private FareRepository fareRepository;

    @InjectMocks
    private FareService fareService;

    List<Fare> globalFareList;

    @BeforeEach
    void setup() {
        globalFareList = new ArrayList<>();
        Fare fare1 = new Fare(new FareId(Instant.now(), 400, 2), 1234);
        Fare fare2 = new Fare(new FareId(Instant.now(), 456, 4), 1234);
        Fare fare3 = new Fare(new FareId(Instant.now(), 678, 10), 1234);
        globalFareList.add(fare1);
        globalFareList.add(fare2);
        globalFareList.add(fare3);
    }

    @Test
    void test_getAllFares(){
        when(fareRepository.findAll()).thenReturn(this.globalFareList);

        List<Fare> result = this.fareService.getFares();

        assertEquals(result, this.globalFareList);
    }

    @Test
    void test_getFareById() {
        FareId fareId = new FareId(Instant.now(), 400, 2);
        Fare fare1 = new Fare(fareId, 1234);

        when(fareRepository.findById(fareId)).thenReturn(Optional.of(fare1));
        Optional<Fare> result = this.fareService.findFareById(fareId);

        assertEquals(result, Optional.of(fare1));
    }

    @Test
    void test_saveFare_when_not_exist(){
        FareId fareId = new FareId(Instant.now(), 400, 9);
        Fare fare1 = new Fare(fareId, 1234);

        Fare result = this.fareService.saveFare(fareId);

        assertNotEquals(fare1, result);
    }

    @Test
    void test_saveFare_when_exist(){
        FareId fareId = new FareId(Instant.now(), 400, 9);
        Fare fare1 = new Fare(fareId, 1234);

        when(fareRepository.findById(fareId)).thenReturn(Optional.of(fare1));
        Fare result = this.fareService.saveFare(fareId);

        assertEquals(fare1, result);
    }

    @Test
    void test_getFare_when_exist(){
        Instant now = Instant.now();
        FareId fareId = new FareId(now, 400, 5);
        Fare fare1 = new Fare(fareId, 1234);

        when(this.fareRepository.findById(fareId)).thenReturn(Optional.of(fare1));

        double result = this.fareService.getFare(now, 400, 5);

        assertEquals(fare1.getCost(), result);
    }

    @Test
    void test_getFare_when_not_exist(){
        Instant now = Instant.now();
        FareId fareId = new FareId(now, 400, 5);
        Fare fare1 = new Fare(fareId, 10);

        double result = this.fareService.getFare(now, 400, 5);

        assertNotEquals(fare1.getCost(), result);
    }
}
