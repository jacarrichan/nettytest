package com.cp.netty.domain;

public interface IGameRequest {

	int getCommandId();

	String readString();

	byte read();

	int readInt();

	int readUnsignedInt();

	short readShort();

	long readLong();

	float readFloat();
}
