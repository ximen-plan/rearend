package com.ximen.plan.socket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/socket/onlineUser/{uid}")
public class OnlineUserSocket {

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static Set onlineCount = new CopyOnWriteArraySet<>();
    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<OnlineUserSocket> webSocketSet = new CopyOnWriteArraySet<OnlineUserSocket>();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    // 接收sid
    private Integer uid;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("uid") Integer uid) {
        System.out.println("onlineUser ==> link success");
        this.uid = uid;
        this.session = session;
        webSocketSet.add(this); // 加入set中
        addOnlineCount(uid); // 在线数加1
        try {
            // 发送更新在线人数
            sendInfo(getOnlineCount() + "", null);
        } catch (IOException e) {
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this); // 从set中删除
        subOnlineCount(uid); // 在线数减1
        // 发送更新在线人数
        try {
            sendInfo(getOnlineCount() + "", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 收到客户端消息后调用的方法（统计在线人数用不到）
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        this.sendInfoBrock(message, this.uid);
    }

    /**
     * 广播消息
     *
     * @param message
     * @param uid
     */
    private void sendInfoBrock(String message, Integer uid) throws IOException {

        for (OnlineUserSocket item : webSocketSet) {
            if (item.uid == uid) {
                continue;
            }
            item.sendMessage(message);
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message, Integer uid) throws IOException {
        for (OnlineUserSocket item : webSocketSet) {
            try {
                // 这里可以设定只推送给这个uid的，为null则全部推送
                if (uid == null) {
                    item.sendMessage(message);
                } else if (item.uid.equals(uid)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    /**
     * 获取在线人数
     *
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount.size();
    }

    /**
     * 在线人数加1
     */
    public static synchronized void addOnlineCount(Integer uid) {
        OnlineUserSocket.onlineCount.add(uid);
    }

    /**
     * 在线人数减1
     */
    public static synchronized void subOnlineCount(Integer uid) {
        // 存在一个用户再同一个浏览器上开启多个页面 关闭连接时需判断是否还存在另外页面连接
        // 存放需要删除的uid 已存在连接数 如果大于1 不删除
        Integer sameUidNumber = 0;
        Iterator<OnlineUserSocket> iter = OnlineUserSocket.webSocketSet.iterator();
        while (iter.hasNext()) {
            OnlineUserSocket usersSocketServer = iter.next();
            if (usersSocketServer.uid.equals(uid)) {
                sameUidNumber++;
            }
        }
        if (sameUidNumber <= 1) {
            OnlineUserSocket.onlineCount.remove(uid);
        }
    }
}
