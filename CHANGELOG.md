## 5.0.8

### Features
- **Credit Card Type Detection**: Added automatic credit card type detection (Visa, Mastercard, Amex, etc.) with brand icons
- **Card Validation**: Enhanced credit card validation including Luhn algorithm check and card type-specific validation

### Build System Updates
- **Android**: Updated compileSdk to 36 (Android 14) from 34
- **Android**: Improved build configuration for better compatibility with latest Android tooling

## 5.0.7

- Fixed PayPal vault not working on Android #2

## 5.0.6

### Build System Updates
- **Android**: Updated Android Gradle Plugin to 8.1.4 for better compatibility
- **Android**: Updated compileSdk to 34 (Android 14) from 28
- **Android**: Updated Java compatibility from Java 8 to Java 17 (LTS)
- **Android**: Fixed all Gradle deprecation warnings for Gradle 9.0+ compatibility
- **Android**: Replaced deprecated `jcenter()` with `mavenCentral()`
- **Android**: Updated property assignment syntax to modern format
- **Example App**: Added missing AppCompat dependency to fix build issues
- **Example App**: Updated Kotlin configuration to use modern `compilerOptions` DSL

### Improvements
- Enhanced build reliability across different Android development environments
- Future-proofed build configuration for upcoming Gradle versions
- Improved development experience with cleaner build output

## 5.0.5

### Features
- **New Custom UI for Credit Card Tokenization**: Introduced a customizable, full-screen credit card form as a modern replacement for the deprecated Drop-in UI.
- **Navigation Helper**: Added `Braintree.showCreditCardForm()` to easily present the credit card form screen.

### Fixes & Improvements
- **Input Validation**: Added robust validation for card number, expiration date, and CVV fields to prevent submission errors.
- **Card Number Formatting**: The card number field now automatically formats the input into groups of four digits for better readability.

### Breaking Changes
- **Deprecation of Drop-in**: `BraintreeDropInRequest` is now marked as deprecated. Developers should migrate to the new custom UI flow using `Braintree.showCreditCardForm()`.

## 5.0.4

*   **Feature**: Added an optional `amount` field to `BraintreeCreditCardRequest`.
*   **Details**: This `amount` is returned with the `BraintreePaymentMethodNonce` result and can be used for display purposes in the UI (e.g., "You are about to pay $X.XX").
*   **Important**: The `amount` passed on the client-side is for display only. The actual transaction amount must still be specified on the server-side when creating the transaction with the nonce. This maintains PCI compliance by ensuring the server controls the final charge.
*   **Fix**: Resolved Android build error related to missing `Serializable` import.
*   **Fix**: Resolved iOS build error caused by an incorrect force-unwrap on a non-optional dictionary.

## 5.0.3

- **FIX:** Fixed iOS Swift header import path in FlutterBraintreePlugin.m to match package name `braintree_flutter_plus`.

## 5.0.2

- **FIX:** Fixed iOS podspec name mismatch - renamed podspec file and updated s.name to match package name `braintree_flutter_plus`.

## 5.0.1

- **FIX:** Fixed Android package name mismatch in pubspec.yaml that was causing plugin initialization errors.

## 5.0.0

- **BREAKING:** Raised minimum iOS deployment target to 15.0.
- **FIX:** Resolved Android crash during PayPal vault flow.
- **FEAT:** Added `android:exported="true"` to AndroidManifest for Android 12+ compatibility.
- **CHORE:** Updated iOS Braintree SDKs to `BraintreeDropIn 9.14.0` and compatible core SDK versions (`~> 5.27.0`).

## 4.0.0

- Upgrade Drop-In SDK to 6.13.0

## 4.0.0-dev.1

Credits go to [nicolobozzato](https://github.com/nicolobozzato) once again:

- **Warning:** While the drop-in UI seems to work, the custom PayPal tokenization may be broken, use with caution!
- Add better support for 3D Secure verification using the `BraintreeBillingAddress` class
- Update cardinal SDK version to fix the Google Play submission issues
- Updated README to explain the new browser switch activity

## 3.0.0

- Add supported networks parameter for Apple Pay request (thank you to [dessonchan](https://github.com/dessonchan)!)
- Upgrade Braintree package versions (thank you to [dessonchan](https://github.com/dessonchan)!)
- Fix problems with Facebook login (thank you to [nicolobozzato](https://github.com/nicolobozzato)!)

## 3.0.0-dev.1

- Add support for specifying payment intent and user action in `BraintreePayPalRequest` (thank you to [nabinadhikari](https://github.com/nabinadhikari)!)
- Clean up naming conventions and documentation

## 2.3.1

- Update iOS dependencies (thank you to [jorgefspereira](https://github.com/jorgefspereira)!)

## 2.3.0

- Add option to disable PayPal in the Drop-In UI (thank you to [santhoshvgts](https://github.com/santhoshvgts)!)

## 2.2.1

- Switch to mavenCentral for Android builds (thank you to [asmengistu](https://github.com/asmengistu)!)
- Fix README to account for code changes (thank you to [hrvojecukman](https://github.com/hrvojecukman)!)

## 2.2.0

- Add PayPal Payer ID to result object (thank you to [nabinadhikari](https://github.com/nabinadhikari)!)

## 2.1.0

- Fix PayPal vault flow not working on iOS (thank you to [andrea689](https://github.com/andrea689)!)
- Add support for Apple Pay's `PKPaymentSummaryItem` (thank you to [bkovac](https://github.com/bkovac)!)

## 2.0.0+1

- Fix new build issue on iOS (thank you to [JideGuru](https://github.com/JideGuru)!)

## 2.0.0

- Upgrade several dependencies to fix build issues on Android and iOS (thank you to [bennibau](https://github.com/bennibau), [reverie-ss](https://github.com/reverie-ss), and [andesappal](https://github.com/andesappal)!)

## 2.0.0-nullsafety.0

- Add null-safety support

## 1.2.1

- Fix Android build (hopefully)

## 1.2.0

- Add option for CreditCard CVV number (thank you to [ilicmilan](https://github.com/ilicmilan)!)

## 1.1.0+1

- Fix vulnerability in Braintree plugin (Play Store issue)

## 1.1.0

- Add ApplePay support for the drop-in UI (thank you again to HareshGediya!)

## 1.0.3

- Fix `cardEnabled = false` not working on iOS drop-in (thank you to HareshGediya)

## 1.0.2+2

- Hotfix 3D Secure (thank you to enzobonggio!)
- Hotfix compilation issues

## 1.0.2+1

- Hotfix Braintree versions

## 1.0.2

- Add Podfile change to installation section

## 1.0.1

- Update outdated iOS Braintree dependency

## 1.0.0

- Support custom UI on iOS (thank you to WipeAir)

## 0.6.0

- Make card.io optional on Android to potentially reduce app sizes

## 0.5.3+3

- Fix disabling card not working for drop-in UI

## 0.5.3+2

- Fix incompatibilities with Google and Facebook sign in plugins

## 0.5.3+1

- Temporarily fix crashes (thank you to peternagy1332)

## 0.5.3

- Fix incompatibility with Flutter's new v2 embedding
- Update example project to use v2 embedding
- Use new platforms key in `pubspec.yaml` (requires Flutter 1.10 or newer!)

## 0.5.2

- Fix credit card expiration not being included correctly

## 0.5.1

- Improve README.md

## 0.5.0

- Add basic support for the direct PayPal Checkout and Vault flow Android

## 0.4.0

- Add basic support for direct credit card tokenization on Android

## 0.3.0

- Add iOS support (thank you to Johannes Erschbamer!)
- Improve documentation

## 0.2.0

- Refactor source code
- Set minimum Dart version to 2.2.2

## 0.1.2

- Fix typo in README.md

## 0.1.1

- Throw proper exception when the Drop-in is launched twice
- Improve README.md

## 0.1.0

- Initial release after discontinuing the old plugin
