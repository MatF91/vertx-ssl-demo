package pl.mf.vertx;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;

public class CustomBridgeEvent implements Handler<BridgeEvent> {

	@Override
	public void handle(BridgeEvent be) {
		if (be.type() == BridgeEventType.SOCKET_CREATED) {
			LogUtils.printMessageWithDate("SOCKED CREATED");
			be.complete(true);
		} else if (be.type() == BridgeEventType.SEND) {
			LogUtils.printMessageWithDate("BRIDGE EVENT -> TYPE: " + be.type().toString() + ", MSG: " + (be.getRawMessage() != null ? be.getRawMessage().toString() : "null"));
			if (be.getRawMessage() != null && be.getRawMessage().getString("body") != null) {
				JsonObject jo = new JsonObject(be.getRawMessage().getString("body")).put("bridge_ack", "ok");
				be.getRawMessage().remove("body");
				be.setRawMessage(be.getRawMessage().put("body", jo));
			}
			be.complete(true);
		} else {
			LogUtils.printMessageWithDate("BRIDGE EVENT: " + be.type().toString());
			be.complete(true);
		}
	}

}
