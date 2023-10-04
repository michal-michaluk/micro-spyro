package intervals;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IntervalsService {

    IntervalRulesRepository repository;

    public int calculateIntervals(DeviceInfo deviceInfo) {
        IntervalRules rules = repository.get();
        return (int) rules.calculateInterval(deviceInfo).getSeconds();
    }
}
