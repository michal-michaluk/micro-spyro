package intervals;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class IntervalRules {

    private final List<DeviceIdRule> byDeviceId;
    private final List<ModelRule> byModel;
    private final int defaultValue;

    IntervalRules(List<DeviceIdRule> byDeviceId, List<ModelRule> byModel, int defaultValue) {
        this.byDeviceId = byDeviceId;
        this.byModel = byModel;
        this.defaultValue = defaultValue;
    }

    Duration calculateInterval(DeviceInfo device) {
        return Duration.ofSeconds(Stream.of(byDeviceId, byModel)
                .flatMap(Collection::stream)
                .filter(rule -> rule.matches(device))
                .findFirst()
                .map(Rule::interval)
                .orElse(defaultValue));
    }

    private interface Rule {
        boolean matches(DeviceInfo device);

        int interval();
    }

    record DeviceIdRule(int interval, List<String> deviceId) implements Rule {
        @Override
        public boolean matches(DeviceInfo device) {
            return deviceId.contains(device.deviceId());
        }
    }

    record ModelRule(int interval, String vendor, Pattern model, String firmware) implements Rule {

        @Override
        public boolean matches(DeviceInfo device) {
            return vendor.equals(device.vendor())
                   && model.matcher(device.model()).matches()
                   && (firmware == null || firmware.equals(device.firmware()));
        }
    }
}
