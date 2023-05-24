package com.orekhov.common.messages;

import com.orekhov.common.bean.Board;
import com.orekhov.common.bean.Source;
import com.orekhov.common.bean.Type;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardStateMessage extends Message {

    private Board board;

    public BoardStateMessage() {
        this.source = Source.BOARD;
        this.type = Type.STATE;
    }

    public BoardStateMessage(Board board) {
        this();
        this.board = board;
    }
}
