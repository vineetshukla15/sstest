package com.vineet.ss.test.dtq;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface ClientProxyProcess<IT, OT> {

	OT read(DataInputStream in);

	void write(DataOutputStream out, IT input);
}