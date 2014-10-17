package com.rocui.jmsger;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.util.CharsetUtil;

import java.io.File;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static org.jboss.netty.buffer.ChannelBuffers.copiedBuffer;
import static org.jboss.netty.channel.Channels.pipeline;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class comet {

    private static class Connection {

        private final Executor executor;
        private final HttpRequest httpRequest;
        private final ChannelHandlerContext ctx;

        private Connection(Executor executor, HttpRequest httpRequest, ChannelHandlerContext ctx) {
            this.executor = executor;
            this.httpRequest = httpRequest;
            this.ctx = ctx;
        }

        public void send(String message) {
            ctx.getChannel().write(copiedBuffer("data:" + message + "\n\n", CharsetUtil.UTF_8));
        }
    }

    private static class ChannelHandler extends SimpleChannelUpstreamHandler {

        private ExecutorService executor;
        private Connection connection = null;

        public ChannelHandler(ExecutorService executor) {
            this.executor = executor;
        }

        @Override
        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("close");
                        if (connection != null) {
                            pusher.removeConnection(connection);
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void messageReceived(final ChannelHandlerContext ctx, MessageEvent messageEvent) throws Exception {
            final HttpRequest httpRequest = (HttpRequest) messageEvent.getMessage();
            final HttpResponse httpResponse = new DefaultHttpResponse(HTTP_1_1, OK);
            final org.jboss.netty.channel.ChannelHandler channelHandler = this;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(httpRequest.getUri());
                        String path = queryStringDecoder.getPath();
                        if ("/index.html".equals(path)) {
                            File file = new File("com/tianjiaguo/netty/comet");
                            URL resourceURL = getClass().getClassLoader().getResource(new File(file, "index.html").getPath());
                            InputStream stream = resourceURL.openStream();
                            byte[] bytes = new byte[stream.available()];
                            try {
                                stream.read(bytes);
                            } finally {
                                if (stream != null) {
                                    stream.close();
                                }
                            }
                            httpResponse.addHeader("Content-Type", "text/html");
                            httpResponse.addHeader("Content-Length", bytes.length);
                            ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer();
                            channelBuffer.writeBytes(copiedBuffer(bytes));
                            if (httpResponse.getStatus().getCode() != 200) {
                                httpResponse.setContent(copiedBuffer(httpResponse.getStatus().toString(), CharsetUtil.UTF_8));
                                httpResponse.addHeader("Content-Length", httpResponse.getContent().readableBytes());
                            }
                            try {
                                httpResponse.setContent(channelBuffer);
                                ChannelFuture future = ctx.getChannel().write(httpResponse);
                                future.addListener(ChannelFutureListener.CLOSE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            httpResponse.setStatus(HttpResponseStatus.OK);
                            httpResponse.addHeader("Content-Type", "text/event-stream");
                            httpResponse.addHeader("Cache-Control", "no-cache");
                            ctx.getChannel().write(httpResponse);
                            ChannelPipeline p = ctx.getChannel().getPipeline();
                            p.remove("aggregator");
                            p.replace("handler", "sse_handler", channelHandler);

                            connection = new Connection(executor, httpRequest, ctx);
                            pusher.addConnection(connection);
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            });
        }
    }

    public static class Pusher implements Runnable {

        private List<Connection> connections = new ArrayList<Connection>();

        public Pusher(final Executor webThread) {
            Executor pusherThread = Executors.newCachedThreadPool();
            pusherThread.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        webThread.execute(Pusher.this);
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void run() {
            broadcast(String.format("%s %s", connections.size(), new Date().toString()));
        }

        private void broadcast(String message) {
            for (Connection connection : connections) {
                connection.send(message);
            }
        }

        public void addConnection(Connection connection) {
            connections.add(connection);
        }

        public void removeConnection(Connection connection) {
            connections.remove(connection);
        }
    }

    private static final Pusher pusher = new Pusher(newCachedThreadPool());

    public static void main(String[] argv) {
        final ExecutorService executor = newCachedThreadPool();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = pipeline();
                pipeline.addLast("decoder", new HttpRequestDecoder());
                pipeline.addLast("aggregator", new HttpChunkAggregator(65536));
                pipeline.addLast("encoder", new HttpResponseEncoder());
                pipeline.addLast("handler", new ChannelHandler(executor));
                return pipeline;
            }
        });

        bootstrap.setFactory(new NioServerSocketChannelFactory(
                newCachedThreadPool(),
                newCachedThreadPool()));
        Channel channel = bootstrap.bind(new InetSocketAddress(8888));
        if (channel.isConnected()) {
            System.out.println("connected");
        }
    }
}
