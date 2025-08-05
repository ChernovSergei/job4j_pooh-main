package ru.job4j.pooh;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoohServer {
    private final QueueSchema queueSchema = new QueueSchema();
    private final TopicSchema topicSchema = new TopicSchema();

    private void runSchemas() {
        ExecutorService pool = Executors.newCachedThreadPool();
        pool.execute(queueSchema);
        pool.execute(topicSchema);
    }

    private void runServer() {
        ExecutorService pool = Executors.newCachedThreadPool();
        try (ServerSocket server = new ServerSocket(9000)) {
            while (!server.isClosed()) {
                Socket socket = server.accept();
                pool.execute(() -> {
                    try (OutputStream out = socket.getOutputStream();
                        var input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                        while (true) {
                            var details = input.readLine().split(";");
                            if (details.length != 3) {
                                continue;
                            }
                            if (details[0].equals("intro")) {
                                if (details[1].equals("queue")) {
                                    queueSchema.addReceiver(
                                            new SocketReceiver(details[2], new PrintWriter(out))
                                    );
                                } else if (details[1].equals("topic")) {
                                    topicSchema.addReceiver(
                                            new SocketReceiver(details[2], new PrintWriter(out))
                                    );
                                }
                            }
                            if (details[0].equals("queue")) {
                                queueSchema.publish(new Message(details[1], details[2]));
                            }
                            if (details[0].equals("topic")) {
                                topicSchema.publish(new Message(details[1], details[2]));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        var pooh = new PoohServer();
        pooh.runSchemas();
        pooh.runServer();
    }
}