package com.cp.game;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cp.netty.domain.GameRequest;
import com.cp.netty.domain.GameResponse;

public abstract class ServerMainHandler implements GameHandler {
	public Logger log = LoggerFactory.getLogger(ServerMainHandler.class);

	public abstract void execute(GameRequest request, GameResponse response, Map<String, Object> model)
			throws GameRunTimeException;

	public void execute(GameRequest request, GameResponse response) throws GameRunTimeException {
		Map<String, Object> model = new HashMap<String, Object>();
		execute(request, response, model);
	}
}
