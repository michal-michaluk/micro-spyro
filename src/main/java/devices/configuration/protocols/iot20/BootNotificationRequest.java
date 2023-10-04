package devices.configuration.protocols.iot20;

import intervals.DeviceInfo;
import lombok.Value;

@Value
class BootNotificationRequest {
    Device device;
    Reason reason;

    @Value
    static class Device {
        String serialNumber;
        String model;
        Modem modem;
        String vendorName;
        String firmwareVersion;
    }

    @Value
    static class Modem {
        String iccid;
        String imsi;
    }

    enum Reason {
        ApplicationReset,
        FirmwareUpdate,
        LocalReset,
        PowerUp,
        RemoteReset,
        ScheduledReset,
        Triggered,
        Unknown,
        Watchdog
    }

    public DeviceInfo toDeviceInfo(String deviceId) {
        return new DeviceInfo(deviceId, device.vendorName, device.model, device.firmwareVersion);
    }
}
