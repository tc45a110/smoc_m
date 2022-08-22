package com.protocol.access.smpp;


public class SmppConstant {
	public static final int VERSION = 2;
	
	public static final byte COMMAND_NAME_LENGTH=16;
	public static final byte TRANSMITTER = (byte) 0x00;
	public static final byte RECEIVER = (byte) 0x01;
	public static final byte TRANSCEIVER = (byte) 0x02;
	
	public static final int CONNECTION_CLOSED = 0;
	public static final int CONNECTION_OPENED = 1;

	public static final long ACCEPT_TIMEOUT = 60000;
	public static final long RECEIVER_TIMEOUT = 60000;
	public static final long COMMS_TIMEOUT = 60000;
	public static final long QUEUE_TIMEOUT = 60000;
	public static final long RECEIVE_BLOCKING = 0;
	public static final long CONNECTION_RECEIVE_TIMEOUT = 0;
	public static int PDU_HEADER_SIZE = 16;
	
	 /*
     * Command ID constants (CID prefix).
     */
	public static final int CID_BIND_RECEIVER = 0x00000001;
	public static final int CID_BIND_TRANSMITTER = 0x00000002;
	public static final int CID_QUERY_SM = 0x00000003;
	public static final int CID_SUBMIT_SM = 0x00000004;
	public static final int CID_DELIVER_SM = 0x00000005;
	public static final int CID_UNBIND = 0x00000006;
	public static final int CID_REPLACE_SM = 0x00000007;
	public static final int CID_CANCEL_SM = 0x00000008;
	public static final int CID_BIND_TRANSCEIVER = 0x00000009;
	public static final int CID_OUTBIND = 0x0000000B;
	public static final int CID_ENQUIRE_LINK = 0x00000015;
	public static final int CID_SUBMIT_MULTI = 0x00000021;
	public static final int CID_ALERT_NOTIFICATION = 0x00000102;
	public static final int CID_DATA_SM = 0x00000103;
	public static final int CID_BROADCAST_SM = 0x00000111;
	public static final int CID_QUERY_BROADCAST_SM = 0x00000112;
	public static final int CID_CANCEL_BROADCAST_SM = 0x00000113;
	public static final int CID_GENERIC_NACK = 0x80000000;
	public static final int CID_BIND_RECEIVER_RESP = 0x80000001;
	public static final int CID_BIND_TRANSMITTER_RESP = 0x80000002;
	public static final int CID_QUERY_SM_RESP = 0x80000003;
	public static final int CID_SUBMIT_SM_RESP = 0x80000004;
	public static final int CID_DELIVER_SM_RESP = 0x80000005;
	public static final int CID_UNBIND_RESP = 0x80000006;
	public static final int CID_REPLACE_SM_RESP = 0x80000007;
	public static final int CID_CANCEL_SM_RESP = 0x80000008;
	public static final int CID_BIND_TRANSCEIVER_RESP = 0x80000009;
	public static final int CID_ENQUIRE_LINK_RESP = 0x80000015;
	public static final int CID_SUBMIT_MULTI_RESP = 0x80000021;
	public static final int CID_DATA_SM_RESP = 0x80000103;
	public static final int CID_BROADCAST_SM_RESP = 0x80000111;
	public static final int CID_QUERY_BROADCAST_SM_RESP = 0x80000112;
	public static final int CID_CANCEL_BROADCAST_SM_RESP = 0x80000113;

	
	//���ش������
	public static final int ESME_RINVMSGLEN = 0x00000001;
	public static final int ESME_RINVCMDLEN = 0x00000002;
	public static final int ESME_RINVCMDID = 0x00000003;
	public static final int ESME_RINVBNDSTS = 0x00000004;
	public static final int ESME_RSYSERR = 0x00000008;
	public static final int ESME_RINVSRCADR = 0x0000000A;
	public static final int ESME_RINVDSTADR = 0x0000000B;
	public static final int ESME_RINVMSGID = 0x0000000C;
	public static final int ESME_RBINDFAIL = 0x0000000D;
	public static final int ESME_RINVPASWD = 0x0000000E;
	public static final int ESME_RINVSYSID = 0x0000000F;

	public static final int ESME_RCNTCANMSG = 0x00000011;
	public static final int ESME_RMSGQFUL = 0x00000014;
	public static final int ESME_RSERNOTSUP = 0x00000015;
	
	public static final int ESME_RINVCUSTID = 0x0000001D;
	public static final int ESME_RINVCUSTIDLEN = 0x0000001E;
	public static final int ESME_RINVCUSTNAMLEN = 0x0000001F;
	public static final int ESME_RINVCUSTADRLEN = 0x00000021;
	public static final int ESME_RCUSTEXIST = 0x00000023;
	public static final int ESME_RCUSTNOTEXIST = 0x00000024;
	public static final int ESME_RINVSRCADDRLEN = 0x00000046;
	public static final int ESME_RINVDSTADDRLEN = 0x00000047;
	public static final int ESME_RINVSYSTYP = 0x00000053;
	
	public static final int ESME_RLIMITEXCEED = 0x00000056;
	public static final int ESME_RINVSCHED = 0x00000061;
	public static final int ESME_RINVEXPIRY = 0x00000062;
	public static final int ESME_RUNKNOWNERR = 0x000000FF;

	//Command_Status Error Codes
	public static final int ESME_ROK = 0x00000000;

	//Interface_Version
	public static final  byte IF_VERSION_00 = 0x00;
	public static final  byte IF_VERSION_33 = 0x33;
	public static final  byte IF_VERSION_34 = 0x34;
	public static final  byte IF_VERSION_50 = 0x50;
	
	//Address_TON
	public static final byte GSM_TON_UNKNOWN = 0x00;
	public static final byte GSM_TON_INTERNATIONAL = 0x01;
	public static final byte GSM_TON_NATIONAL = 0x02;
	public static final byte GSM_TON_NETWORK = 0x03;
	public static final byte GSM_TON_SUBSCRIBER = 0x04;
	public static final byte GSM_TON_ALPHANUMERIC = 0x05;
	public static final byte GSM_TON_ABBREVIATED = 0x06;
	public static final byte GSM_TON_RESERVED_EXTN = 0x07;

	//Address_NPI
	public static final byte GSM_NPI_UNKNOWN = 0x00;
	public static final byte GSM_NPI_E164 = 0x01;
	public static final byte GSM_NPI_ISDN = GSM_NPI_E164;
	public static final byte GSM_NPI_X121 = 0x03;
	public static final byte GSM_NPI_TELEX = 0x04;
	public static final byte GSM_NPI_LAND_MOBILE = 0x06;
	public static final byte GSM_NPI_NATIONAL = 0x08;
	public static final byte GSM_NPI_PRIVATE = 0x09;
	public static final byte GSM_NPI_ERMES = 0x0A;
	public static final byte GSM_NPI_INTERNET = 0x0E;
	public static final byte GSM_NPI_WAP_CLIENT_ID = 0x12;
	public static final byte GSM_NPI_RESERVED_EXTN = 0x0F;

	//Port Value
	public static final int MIN_VALUE_PORT = 1024;
	public static final int MAX_VALUE_PORT = 65535;
	
	//Address Length
	public static final int MIN_LENGTH_ADDRESS = 7;
	
	// list of character encodings
	// see http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html
	// from rt.jar

	// American Standard Code for Information Interchange 
	public static final String ENC_ASCII = "ASCII";
	// Windows Latin-1 
	public static final String ENC_CP1252 = "Cp1252";
	// ISO 8859-1, Latin alphabet No. 1 
	public static final String ENC_ISO8859_1 = "ISO8859_1";
	// Sixteen-bit Unicode Transformation Format, big-endian byte order
	// with byte-order mark
	public static final String ENC_UTF16_BEM = "UnicodeBig";
	// Sixteen-bit Unicode Transformation Format, big-endian byte order 
	public static final String ENC_UTF16_BE = "UnicodeBigUnmarked";
	// Sixteen-bit Unicode Transformation Format, little-endian byte order
	// with byte-order mark
	public static final String ENC_UTF16_LEM = "UnicodeLittle";
	// Sixteen-bit Unicode Transformation Format, little-endian byte order 
	public static final String ENC_UTF16_LE = "UnicodeLittleUnmarked";
	// Eight-bit Unicode Transformation Format 
	public static final String ENC_UTF8 = "UTF8";
	// Sixteen-bit Unicode Transformation Format, byte order specified by
	// a mandatory initial byte-order mark 
	public static final String ENC_UTF16 = "UTF-16";

}

