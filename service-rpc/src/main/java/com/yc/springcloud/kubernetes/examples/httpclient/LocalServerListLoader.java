package com.yc.springcloud.kubernetes.examples.httpclient;

import com.netflix.loadbalancer.Server;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 加载本地服务列表
 *
 * @author yangchuan
 * @version 1.0
 * @createDate 2020/1/7
 */
public class LocalServerListLoader implements DisposableBean, ApplicationListener<WebServerInitializedEvent> {

    /**
     * 定时任务，获取服务列表
     */
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private final Environment environment;

    /**
     * 本地服务注册目录
     */
    private final String LOCAL_DISCOVER_DIR = "LOCAL_DISCOVER_DIR";

    /**
     * serverName&&127.0.0.1&&port
     */
    private Pattern pattern = Pattern.compile("^(.*)&&(.*)&&([\\d]{1,5})$");

    /**
     * 超过10s则认为过期
     */
    private final long ALIVE_DURATION = 10 * 1000L;

    private volatile List<Server> serverList = new LinkedList<>();

    public LocalServerListLoader(Environment environment) {
        super();
        this.environment = environment;
    }

    /**
     * 程序结束后，服务下线
     *
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        List<Runnable> runnables = executor.shutdownNow();
        for (Runnable runnable : runnables) {
            if (runnable instanceof LocalServerLoaderWorker) {
                LocalServerLoaderWorker loader = (LocalServerLoaderWorker) runnable;
                loader.destroy();
            }
        }
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {

        int port = event.getWebServer().getPort();

        String serverName = environment.getProperty("spring.application.name");

        //每隔5s，更新服务列表
        executor.scheduleAtFixedRate(
                new LocalServerLoaderWorker(LOCAL_DISCOVER_DIR, serverName, port, ALIVE_DURATION),
                0,
                5,
                TimeUnit.SECONDS);
    }

    private class LocalServerLoaderWorker implements Runnable {

        private File baseDir;

        private String serverName;

        private int port;

        private long aliveDuration;

        private File serverFile;

        public LocalServerLoaderWorker(String env, String serverName, int port, long aliveDuration) {

            this.baseDir = getBaseDir(env);
            this.serverName = serverName;
            this.port = port;
            this.aliveDuration = aliveDuration;

            run();
        }

        /**
         * 获取服务注册目录
         *
         * @param env 环境变量
         * @return
         */
        private File getBaseDir(String env) {

            Map<String, String> envs = System.getenv();

            if (Objects.isNull(envs) || envs.isEmpty()) {
                throw new UnsupportedOperationException("未获取到任何系统环境变量");
            }

            String value = envs.get(env);

            if (StringUtils.isBlank(value)) {
                throw new UnsupportedOperationException("请配置指定环境变量" + env);
            }

            final File dir = new File(value);

            if (!dir.exists() && !dir.mkdirs()) {
                throw new UnsupportedOperationException("目录创建失败:" + env + " 请检查目录权限");
            }

            if (!dir.isDirectory()) {
                throw new UnsupportedOperationException(env + "不是一个有效的文件目录");
            }
            return dir;
        }

        /**
         * 心跳注册
         */
        private void keepAlive() {
            if (Objects.isNull(this.serverFile)) {
                this.serverFile = new File(baseDir, String.format(
                        "%s&&%s&&%d", serverName, "127.0.0.1", port));
            }

            if (!this.serverFile.exists()) {
                try {
                    if (!this.serverFile.createNewFile()) {
                        throw new UnsupportedOperationException(this.serverFile.getAbsolutePath()
                                + "创建失败");
                    }
                } catch (IOException e) {
                    throw new UnsupportedOperationException(this.serverFile.getAbsolutePath()
                            + "创建失败");
                }

                this.serverFile.deleteOnExit();
            }

            this.serverFile.setLastModified(System.currentTimeMillis());
        }


        /**
         * 定时更新服务列表
         */
        @Override
        public void run() {

            //心跳
            keepAlive();

            //更新服务目录
            serverDiscover();

        }

        private void serverDiscover() {
            List<Server> ss = new LinkedList<>();

            File[] files = baseDir.listFiles();

            if (ArrayUtils.isEmpty(files)) {
                LocalServerListLoader.this.serverList = ss;
                return;
            }

            long now = System.currentTimeMillis();

            for (File file : files) {
                String name = file.getName();

                Matcher matcher = pattern.matcher(name);

                if (!matcher.matches()) {
                    continue;
                }

                long lastModified = file.lastModified();
                long diff = now - lastModified;

                //超时则删除
                if (diff > aliveDuration) {
                    try {
                        Files.delete(file.toPath());
                    } catch (Exception e) {
                        // ignore
                    }
                } else if (diff < aliveDuration) {
                    //添加到服务列表中
                    MatchResult matchResult = matcher.toMatchResult();

                    String iServerName = matchResult.group(1);
                    Server server = new IServer("http", matchResult.group(2), Integer.parseInt(
                            matchResult.group(3)), iServerName);
                    server.setAlive(true);
                    ss.add(server);
                }
            }

            LocalServerListLoader.this.serverList = ss;
        }

        /**
         * 服务下线
         *
         * @throws IOException
         */
        private void destroy() throws IOException {
            Files.delete(this.serverFile.toPath());
        }
    }

    @EqualsAndHashCode(callSuper = true)
    private static class IServer extends Server {

        private String appName;

        public IServer(String scheme, String host, int port, String appName) {
            super(scheme, host, port);
            this.appName = appName;
        }

        private MetaInfo metaInfo = new MetaInfo() {
            @Override
            public String getAppName() {
                return appName;
            }

            @Override
            public String getServerGroup() {
                return null;
            }

            @Override
            public String getServiceIdForDiscovery() {
                return null;
            }

            @Override
            public String getInstanceId() {
                return getId();
            }
        };

        @Override
        public MetaInfo getMetaInfo() {
            return metaInfo;
        }

    }

    public Server getLocalServer(String serverName) {
        for (Server ss : this.serverList) {
            if (ss.getMetaInfo().getAppName().equalsIgnoreCase(serverName) && ss.isAlive()) {
                return ss;
            }
        }
        return null;
    }

    public List<Server> getAllServer() {

        return serverList;
    }
}
