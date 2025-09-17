package com.example.flutter_braintree;

import android.app.Activity;
import android.content.Intent;
import java.util.Map;
import java.util.HashMap;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.ActivityResultListener;
import com.braintreepayments.api.DropInRequest;
import com.braintreepayments.api.PayPalCheckoutRequest;
import com.braintreepayments.api.PayPalVaultRequest;
import com.braintreepayments.api.PayPalRequest;

public class FlutterBraintreePlugin implements FlutterPlugin, ActivityAware, MethodCallHandler, ActivityResultListener {
  private static final int CUSTOM_ACTIVITY_REQUEST_CODE = 0x420; 
  private static final int DROP_IN_REQUEST_CODE_FOR_PAYPAL = 0x421; 

  private Activity activity;
  private Result activeResult;

  private FlutterBraintreeDropIn dropIn;
  private MethodChannel channel;

  @Override
  public void onAttachedToEngine(FlutterPluginBinding binding) {
    channel = new MethodChannel(binding.getBinaryMessenger(), "flutter_braintree.custom");
    channel.setMethodCallHandler(this);

    
    dropIn = new FlutterBraintreeDropIn();
    dropIn.onAttachedToEngine(binding);
  }

  @Override
  public void onDetachedFromEngine(FlutterPluginBinding binding) {
    if (dropIn != null) {
      dropIn.onDetachedFromEngine(binding);
      dropIn = null;
    }
    channel.setMethodCallHandler(null);
    channel = null;
  }

  @Override
  public void onAttachedToActivity(ActivityPluginBinding binding) {
    activity = binding.getActivity();
    binding.addActivityResultListener(this);
    if (dropIn != null) {
      // Ensure FlutterBraintreeDropIn also gets activity context and listener
      dropIn.onAttachedToActivity(binding);
    }
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    activity = null;
    if (dropIn != null) {
      dropIn.onDetachedFromActivity();
    }
  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
    activity = binding.getActivity();
    binding.addActivityResultListener(this);
    if (dropIn != null) {
      dropIn.onReattachedToActivityForConfigChanges(binding);
    }
  }

  @Override
  public void onDetachedFromActivity() {
    activity = null;
    if (dropIn != null) {
      dropIn.onDetachedFromActivity();
    }
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (activeResult != null) {
      result.error("already_running", "Cannot launch another custom activity while one is already running.", null);
      return;
    }
    activeResult = result;

    if (call.method.equals("tokenizeCreditCard")) {
      Intent intent = new Intent(activity, FlutterBraintreeCustom.class);
      intent.putExtra("type", "tokenizeCreditCard");
      intent.putExtra("authorization", (String) call.argument("authorization"));
      assert(call.argument("request") instanceof Map);
      Map request = (Map) call.argument("request");
      intent.putExtra("cardNumber", (String) request.get("cardNumber"));
      intent.putExtra("expirationMonth", (String) request.get("expirationMonth"));
      intent.putExtra("expirationYear", (String) request.get("expirationYear"));
      intent.putExtra("cvv", (String) request.get("cvv"));
      intent.putExtra("cardholderName", (String) request.get("cardholderName"));
      intent.putExtra("amount", (String) request.get("amount"));
      activity.startActivityForResult(intent, CUSTOM_ACTIVITY_REQUEST_CODE);
    } else if (call.method.equals("requestPaypalNonce")) {
    
      String authorization = (String) call.argument("authorization");
      if (authorization == null) {
          activeResult.error("authorization_missing", "Authorization token is required.", null);
          activeResult = null;
          return;
      }

      assert(call.argument("request") instanceof Map);
      Map requestArgs = (Map) call.argument("request");


      DropInRequest dropInRequest = new DropInRequest();
      
     
      dropInRequest.setCardDisabled(true);
      dropInRequest.setGooglePayDisabled(true);
      dropInRequest.setVenmoDisabled(true);
     
      dropInRequest.setPayPalDisabled(false);

      String amount = (String) requestArgs.get("amount");
      PayPalRequest payPalNativeRequest;

      if (amount == null || amount.isEmpty()) {
          android.util.Log.d("BraintreePlugin", "Creating PayPal VAULT request (no amount)");
          PayPalVaultRequest vaultRequest = new PayPalVaultRequest();
          String displayName = (String) requestArgs.get("displayName");
          String billingAgreementDescription = (String) requestArgs.get("billingAgreementDescription");

          if (displayName != null) {
              vaultRequest.setDisplayName(displayName);
          }
          if (billingAgreementDescription != null) {
              vaultRequest.setBillingAgreementDescription(billingAgreementDescription);
          }
         
          
          payPalNativeRequest = vaultRequest;
      } else {
          android.util.Log.d("BraintreePlugin", "Creating PayPal CHECKOUT request with amount: " + amount);
          PayPalCheckoutRequest checkoutRequest = new PayPalCheckoutRequest(amount);
          String currencyCode = (String) requestArgs.get("currencyCode");
          String displayName = (String) requestArgs.get("displayName");
          String payPalPaymentIntent = (String) requestArgs.get("payPalPaymentIntent");
          String payPalPaymentUserAction = (String) requestArgs.get("payPalPaymentUserAction");

          if (currencyCode != null) {
              checkoutRequest.setCurrencyCode(currencyCode);
          }
          if (displayName != null) {
              checkoutRequest.setDisplayName(displayName);
          }
          if (payPalPaymentIntent != null) {
              checkoutRequest.setIntent(payPalPaymentIntent); 
          }
          if (payPalPaymentUserAction != null) {
              checkoutRequest.setUserAction(payPalPaymentUserAction); 
          }
          
          payPalNativeRequest = checkoutRequest;
      }
      
      dropInRequest.setPayPalRequest(payPalNativeRequest); 

      Intent intent = new Intent(activity, DropInActivity.class); 
      intent.putExtra("token", authorization); 
      intent.putExtra("dropInRequest", dropInRequest);
      // Pass the original amount for checkout flows
      if (amount != null && !amount.isEmpty()) {
          intent.putExtra("originalAmount", amount);
      } 
      
      this.activity.startActivityForResult(intent, DROP_IN_REQUEST_CODE_FOR_PAYPAL);
    } else {
      result.notImplemented();
      activeResult = null;
    }
  }

  @Override
  public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    if (activeResult == null)
      return false;


    if (requestCode == CUSTOM_ACTIVITY_REQUEST_CODE) {
        if (resultCode == Activity.RESULT_OK) {
            String type = data.getStringExtra("type");
            if (type != null && type.equals("paymentMethodNonce")) {
                String amount = data.getStringExtra("amount");
                Map<String, Object> nonceMap = (Map<String, Object>) data.getSerializableExtra("paymentMethodNonce");
                if (amount != null) {
                    nonceMap.put("amount", amount);
                }
                activeResult.success(nonceMap);
            } else {
                Exception error = new Exception("Invalid activity result type.");
                activeResult.error("error", error.getMessage(), null);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            activeResult.success(null);
        }  else {
            // Error from FlutterBraintreeCustom
            Exception error = (Exception) data.getSerializableExtra("error");
            activeResult.error("error", (error != null ? error.getMessage() : "Unknown error from custom activity."), null);
        }
        activeResult = null;
        return true;
    } else if (requestCode == DROP_IN_REQUEST_CODE_FOR_PAYPAL) {
        // This part needs to mimic how FlutterBraintreeDropIn.java handles its results
        // but needs to put the result back through the 'activeResult' from this plugin.
        if (resultCode == Activity.RESULT_OK) {
            // DropInResult from your DropInActivity
            com.braintreepayments.api.DropInResult dropInResult = data.getParcelableExtra("dropInResult");
            if (dropInResult != null && dropInResult.getPaymentMethodNonce() != null) {
                com.braintreepayments.api.PaymentMethodNonce paymentMethodNonce = dropInResult.getPaymentMethodNonce();
                HashMap<String, Object> nonceMap = new HashMap<>();
                nonceMap.put("nonce", paymentMethodNonce.getString());
                nonceMap.put("isDefault", paymentMethodNonce.isDefault());
                nonceMap.put("typeLabel", dropInResult.getPaymentMethodType().name()); // Assuming this gives "PAYPAL"
                nonceMap.put("description", dropInResult.getPaymentDescription()); // e.g., PayPal email

                // Retrieve the original amount for checkout flows
                String originalAmount = data.getStringExtra("originalAmount");
                if (originalAmount != null) {
                    nonceMap.put("amount", originalAmount);
                }
                
                activeResult.success(nonceMap);
            } else {
                activeResult.success(null); // No nonce obtained or user canceled from DropIn
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            activeResult.success(null); // User canceled
        } else {
            // Error from DropInActivity
            String error = data.getStringExtra("error"); // Your DropInActivity puts error message
            activeResult.error("braintree_error", error != null ? error : "Unknown error from Drop-in.", null);
        }
        activeResult = null;
        return true;
    }
    return false;
  }
}