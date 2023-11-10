//// SignalingService.java
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class SignalingService {
//
//    private final SimpMessagingTemplate messagingTemplate;
//
//    // 방 참가 처리 로직
//    public void handleJoinRoom(String roomName) {
//        // 방이 기존에 생성되어 있다면
//        if (users.containsKey(roomName)) {
//            // 현재 입장하려는 방에 있는 인원수
//            int currentRoomLength = users.get(roomName).size();
//            if (currentRoomLength == MAXIMUM) {
//                // 인원수가 꽉 찼다면 돌아갑니다.
//                messagingTemplate.convertAndSendToUser(socketId, "/topic/room_full", "Room is full");
//                return;
//            }
//
//            // 여분의 자리가 있다면 해당 방 배열에 추가해줍니다.
//            users.get(roomName).add(socketId);
//        } else {
//            // 방이 존재하지 않다면 값을 생성하고 추가해줍시다.
//            users.put(roomName, new ArrayList<>(Collections.singletonList(socketId)));
//        }
//
//        // 입장
//        // Spring에서는 자동으로 입장 처리됩니다.
//        // socket.join(data.room);
//
//        // 입장하기 전 해당 방의 다른 유저들이 있는지 확인하고
//        // 다른 유저가 있었다면 offer-answer을 위해 알려줍니다.
//        List<String> others = users.get(roomName).stream()
//                .filter(userId -> !userId.equals(socketId))
//                .collect(Collectors.toList());
//
//        if (!others.isEmpty()) {
//            messagingTemplate.convertAndSendToUser(socketId, "/topic/all_users", others);
//        }
//    }
//
//    // Offer 처리 로직
//    public void handleOffer(String sdp, String roomName) {
//        // offer를 전달받고 다른 유저들에게 전달해 줍니다.
//        messagingTemplate.convertAndSendToUser(roomName, "/topic/getOffer", sdp);
//    }
//
//    // Answer 처리 로직
//    public void handleAnswer(String sdp, String roomName) {
//        // answer를 전달받고 방의 다른 유저들에게 전달해 줍니다.
//        messagingTemplate.convertAndSendToUser(roomName, "/topic/getAnswer", sdp);
//    }
//
//    // Candidate 처리 로직
//    public void handleCandidate(String candidate, String roomName) {
//        // candidate를 전달받고 방의 다른 유저들에게 전달해 줍니다.
//        messagingTemplate.convertAndSendToUser(roomName, "/topic/getCandidate", candidate);
//    }
//}
