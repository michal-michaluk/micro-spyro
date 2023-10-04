package intervals;

import intervals.IntervalRules.DeviceIdRule;
import intervals.IntervalRules.ModelRule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

public class IntervalRulesTest {

    IntervalRules subject = new IntervalRules(
            List.of(
                    new DeviceIdRule(600, List.of("EVB-P4562137", "ALF-9571445")),
                    new DeviceIdRule(2700, List.of("t53_8264_019", "EVB-P15079256"))
            ),
            List.of(
                    new ModelRule(120, "ChargeStorm AB", Pattern.compile("Chargestorm Connected"), null),
                    new ModelRule(120, "Alfen BV", Pattern.compile("NG920-5250[6-9]"), null),
                    new ModelRule(99, "Alfen BV", Pattern.compile("NG920-6969"), "56278390")
            ),
            1800
    );

    @Test
    void matchByDeviceId1() {
        DeviceInfo device = givenDevice().deviceId("EVB-P4562137").build();
        Duration interval = subject.calculateInterval(device);
        Assertions.assertThat(interval).hasSeconds(600);
    }

    @Test
    void matchByDeviceId2() {
        DeviceInfo device = givenDevice().deviceId("t53_8264_019").build();
        Duration interval = subject.calculateInterval(device);
        Assertions.assertThat(interval).hasSeconds(2700);
    }

    @Test
    void matchByVendorModelWithModel() {
        DeviceInfo device = givenDevice()
                .vendor("ChargeStorm AB")
                .model("Chargestorm Connected")
                .build();
        Duration interval = subject.calculateInterval(device);
        Assertions.assertThat(interval).hasSeconds(120);
    }

    @Test
    void matchByVendorModelWithRegexp() {
        DeviceInfo device = givenDevice()
                .vendor("Alfen BV")
                .model("NG920-52507")
                .build();
        Duration interval = subject.calculateInterval(device);
        Assertions.assertThat(interval).hasSeconds(120);
    }

    @Test
    void matchByVendorModelWithFirmware() {
        DeviceInfo device = givenDevice()
                .vendor("Alfen BV")
                .model("NG920-6969")
                .firmware("56278390")
                .build();
        Duration interval = subject.calculateInterval(device);
        Assertions.assertThat(interval).hasSeconds(99);
    }

    @Test
    void defaultInterval() {
        DeviceInfo device = givenDevice().build();
        Duration interval = subject.calculateInterval(device);
        Assertions.assertThat(interval).hasSeconds(1800);
    }

    private static DeviceInfo.DeviceInfoBuilder givenDevice() {
        return DeviceInfo.builder()
                .deviceId("EVB-P666777")
                .vendor("ChargeStorm XY")
                .model("Chargestorm NotConnected")
                .firmware("win3.11");
    }
}
