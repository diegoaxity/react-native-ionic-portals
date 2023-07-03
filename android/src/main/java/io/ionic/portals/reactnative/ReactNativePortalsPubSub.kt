package io.ionic.portals.reactnative

import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.getcapacitor.JSObject
import io.ionic.portals.PortalsPlugin
import io.ionic.portals.PortalsPubSub
import org.json.JSONObject

internal class PortalsPubSubModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {
    override fun getName() = "IONPortalPubSub"

    @ReactMethod
    fun subscribe(topic: String, promise: Promise) {
        val reference = PortalsPubSub.shared.subscribe(topic) { result ->
            reactApplicationContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit("PortalsSubscription", result.toJSObject().toReactMap())
        }

        promise.resolve(reference)
    }

    @ReactMethod
    fun publish(topic: String, data: ReadableMap) {
        PortalsPubSub.shared.publish(topic, data.toJSObject())
    }

    @ReactMethod
    fun unsubscribe(topic: String, reference: Int) {
        PortalsPubSub.shared.unsubscribe(topic, reference)
    }

    // These are required to be an EventEmitter in javascript

    @ReactMethod
    fun addListener(eventName: String) {
    }

    @ReactMethod
    fun removeListeners(count: Int) {
    }
}

private fun ReadableMap.toJSObject(): JSObject = JSObject.fromJSONObject(JSONObject(toHashMap()))
