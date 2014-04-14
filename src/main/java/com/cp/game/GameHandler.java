package com.cp.game;

import com.cp.netty.domain.GameRequest;
import com.cp.netty.domain.GameResponse;

public interface GameHandler {
	void execute(GameRequest request, GameResponse response) throws GameRunTimeException;
}
