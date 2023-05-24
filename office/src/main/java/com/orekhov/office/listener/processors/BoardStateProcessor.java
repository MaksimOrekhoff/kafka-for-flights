package com.orekhov.office.listener.processors;

import com.orekhov.common.bean.AirPort;
import com.orekhov.common.bean.Board;
import com.orekhov.common.bean.Route;
import com.orekhov.common.messages.AirPortStateMessage;
import com.orekhov.common.messages.BoardStateMessage;
import com.orekhov.common.processor.MessageConverter;
import com.orekhov.common.processor.MessageProcessor;
import com.orekhov.office.provider.AirPortsProvider;
import com.orekhov.office.provider.BoardsProvider;
import com.orekhov.office.service.WaitingRoutesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component("BOARD_STATE")
@RequiredArgsConstructor
public class BoardStateProcessor implements MessageProcessor<BoardStateMessage> {
    private final MessageConverter messageConverter;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final WaitingRoutesService waitingRoutesService;
    private final BoardsProvider boardsProvider;
    private final AirPortsProvider airPortsProvider;

    @Override
    public void process(String jsonMessage) {
        BoardStateMessage message = messageConverter.extractMessage(jsonMessage, BoardStateMessage.class);
        Board board = message.getBoard();
        Optional<Board> previousOpt = boardsProvider.getBoard(board.getName());
        AirPort airPort = airPortsProvider.getAirPort(board.getLocation());

        boardsProvider.addBoard(board);
        if (previousOpt.isPresent() && board.hasRoute() && !previousOpt.get().hasRoute()) {
            Route route = board.getRoute();
            waitingRoutesService.remove(route);
        }

        if (previousOpt.isEmpty() || !board.isBusy() && previousOpt.get().isBusy()) {
            airPort.addBoard(board.getName());
            kafkaTemplate.sendDefault(messageConverter.toJson(new AirPortStateMessage(airPort)));
        }
    }
}
