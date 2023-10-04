package intervals;

import lombok.Builder;

@Builder
public record DeviceInfo(String deviceId, String vendor, String model, String firmware) {
}
