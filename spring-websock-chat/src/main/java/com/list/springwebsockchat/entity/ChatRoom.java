package com.list.springwebsockchat.entity;

import com.list.springwebsockchat.dto.ChatDto;
import com.list.springwebsockchat.service.ChatService;
import lombok.Data;

import lombok.Builder;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Data
public class ChatRoom {

    private String roomId;//채팅방 아이디
    private String name;//채팅방 이름
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId,String name){
        this.roomId = roomId;
        this.name = name;
    }
    public void handleAction(WebSocketSession session, ChatDto message, ChatService service){
        //message 에 담긴 타입을 확인한다.
        //이때 message 에서 getType 으로 가져온 내용이
        //chatDto 의 열거형인 MessageType 안에 있는 ENTER 과 동일한 값이라면
        if(message.getType().equals(ChatDto.MessageType.ENTER)){
            //sessions 에 넘어온 session 을 담고,
            sessions.add(session);

            //message 에는 입장하였다는 메시지를 띄워줍니다.
            message.setMessage(message.getSender() + " 님이 입장하였습니다.");
            sendMessage(message,service);
        } else if (message.getType().equals(ChatDto.MessageType.TALK)) {
            message.setMessage(message.getMessage());
            sendMessage(message,service);
        }
    }
    public <T> void sendMessage(T message, ChatService service){
        sessions.parallelStream().forEach(sessions -> service.sendMessage(sessions,message));
    }
}
