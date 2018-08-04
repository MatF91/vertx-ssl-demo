package pl.mf.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;

public class SecuredDemoVerticle extends AbstractVerticle {
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new SecuredDemoVerticle());
	}

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		LogUtils.printMessageWithDate("Starting verticle...");
		Router router = Router.router(vertx);
		BridgeOptions options = new BridgeOptions().setPingTimeout(300000).setReplyTimeout(300000);
		options.addOutboundPermitted(new PermittedOptions().setAddressRegex(".*"))
				.addInboundPermitted(new PermittedOptions().setAddressRegex(".*"));
		router.route("/eventbus/*").handler(
				SockJSHandler.create(vertx, new SockJSHandlerOptions()).bridge(options, new CustomBridgeEvent()));
		;

		vertx.eventBus().consumer("demo-address", message -> {
			LogUtils.printMessageWithDate("CONSUMER -> BODY: " + message.body() + ", ADDRESS: " + message.address()
					+ ", REPLY_ADDRESS: " + message.replyAddress());
			JsonObject rep = new JsonObject(message.body().toString()).put("consumer_ack", "ok");
			message.reply(rep);
		});

		vertx.createHttpServer().requestHandler(router::accept).listen(8090);

		vertx.createHttpServer().requestHandler(reqHandler -> {
			LogUtils.printMessageWithDate("PATH " + reqHandler.path());
			HttpServerResponse response = reqHandler.response();
			response.setChunked(true);
			response.write("Verticle response for " + reqHandler.path());
			reqHandler.response().end();
		}).websocketHandler(wsHandler -> {
			LogUtils.printMessageWithDate("WS socket created");
			wsHandler.textMessageHandler(message -> {
				LogUtils.printMessageWithDate("Received something: " + message);
			}).closeHandler(closeHandler -> {
				LogUtils.printMessageWithDate("WS socket closed");
			});
		}).listen(8091);

		LogUtils.printMessageWithDate("Verticle started");
	}
}