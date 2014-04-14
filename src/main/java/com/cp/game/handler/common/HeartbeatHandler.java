package com.cp.game.handler.common;

import java.util.Map;

import com.cp.game.GameRunTimeException;
import com.cp.game.ServerMainHandler;
import com.cp.netty.domain.GameRequest;
import com.cp.netty.domain.GameResponse;

/**
 * 心跳协议
 * 
 * @author jacarri
 * 
 */
public class HeartbeatHandler extends ServerMainHandler {
	@Override
	public void execute(GameRequest request, GameResponse response, Map<String, Object> model)
			throws GameRunTimeException {

		log.debug("心跳包:" + request.readLong());
		// 返回协议的协议体数据
		response.putLong(System.currentTimeMillis());
	}
}
