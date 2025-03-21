package com.anta.vertx_stock_api;

import com.sun.tools.javac.Main;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

    public static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

    public static void main(String[] args) {
        var vertx = Vertx.vertx();
        vertx.exceptionHandler(error -> {
            LOG.error("Unhandled: ", error);
        });
        vertx.deployVerticle(new MainVerticle(), ar -> {
            if(ar.failed()){
                LOG.error("Failed to deploy: ", ar.cause());
                return;
            }
            LOG.info("Deployed: {}", MainVerticle.class.getName());
        });
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        final Router restApi = Router.router(vertx);
        restApi.get("/assets");

        vertx.createHttpServer().requestHandler(req -> {
            req.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from Vert.x!");
        }).listen(8888).onComplete(http -> {
            if (http.succeeded()) {
                startPromise.complete();
                System.out.println("HTTP server started on port 8888");
            } else {
                startPromise.fail(http.cause());
            }
        });
    }
}
