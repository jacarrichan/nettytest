package com.cp.netty.domain;

public interface IGameResponse {

	void put(byte data);

	void putShort(short length);

	void putInt(int data);

	void putFloat(float data);

	void putLong(long data);

	void putString(String data);

	void clear();
}
