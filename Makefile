# Variables
GRADLEW = ./gradlew
BUILD_DIR = android/build/outputs/apk
UNSIGNED_APK = $(BUILD_DIR)/debug/android-debug.apk
ALIGNED_UNSIGNED_APK = $(BUILD_DIR)/debug/android-debug-unsigned-aligned.apk
SIGNED_APK = $(BUILD_DIR)/debug/android-debug-signed.apk
KEYSTORE = my-release-key.jks
KEY_ALIAS = my-key-alias
KEYSTORE_PASSWORD = StarvingValley
KEY_PASSWORD = $(KEYSTORE_PASSWORD)
ADB = adb

# Default target
all: build-apk align-apk sign-apk 

# Build the APK
build-apk:
	$(GRADLEW) assembleDebug
	@echo "APK built at $(UNSIGNED_APK)"

# Align the APK
align-apk:
	zipalign -v 4 $(UNSIGNED_APK) $(ALIGNED_UNSIGNED_APK)
	@echo "Aligned APK ready for release at $(ALIGNED_UNSIGNED_APK)"

# Sign the APK
sign-apk:
	apksigner sign \
        --ks $(KEYSTORE) \
        --ks-key-alias $(KEY_ALIAS) \
        --ks-pass pass:$(KEYSTORE_PASSWORD) \
        --key-pass pass:$(KEY_PASSWORD) \
        --out $(SIGNED_APK) \
        $(ALIGNED_UNSIGNED_APK)
	@echo "APK signed at $(SIGNED_APK)"

# Install the APK on a connected device
install-apk: sign-apk
	$(ADB) install -r $(SIGNED_APK)
	@echo "APK installed on device"

# TODO: Publish the APK to the Play Store
publish-apk:
	@echo "TODO: Implement APK publishing to the Play Store"
