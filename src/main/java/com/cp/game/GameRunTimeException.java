/**
 * Jan 14, 2009 6:34:50 PM
 */
package com.cp.game;

import com.cp.netty.domain.IGameRequest;
import com.cp.netty.domain.IGameResponse;

public class GameRunTimeException extends RuntimeException {
	private static final long serialVersionUID = 3848407576445825258L;
	private IGameRequest request;
	private IGameResponse response;

	/**
	 * @return the request
	 */
	public IGameRequest getRequest() {
		return request;
	}

	/**
	 * @return the response
	 */
	public IGameResponse getResponse() {
		return response;
	}

	/**
	 * @param e
	 * @param response
	 * @param request
	 */
	public GameRunTimeException(Exception e, IGameRequest request, IGameResponse response) {
		super(e);
		this.request = request;
		this.response = response;
		if (response instanceof IGameResponse) {
			((IGameResponse) this.response).clear();
			this.response.put((byte) -1);
		}

	}

	/**
	 * @param string
	 */
	public GameRunTimeException(String string, IGameRequest request, IGameResponse response) {
		super(string);
		this.request = request;
		this.response = response;
		if (response instanceof IGameResponse) {
			((IGameResponse) this.response).clear();
			this.response.put((byte) -1);
		}

	}
}
